package com.ceb.hdqs.wtc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import weblogic.wtc.jatmi.Reply;
import weblogic.wtc.jatmi.TPServiceInformation;
import weblogic.wtc.jatmi.TypedCArray;

import com.ceb.hdqs.action.query.exception.ConditionNotExistException;
import com.ceb.hdqs.action.query0775.HandleQuery0775;
import com.ceb.hdqs.config.ConfigLoader;
import com.ceb.hdqs.config.RegisterTable;
import com.ceb.hdqs.constants.ExchangeCode;
import com.ceb.hdqs.constants.HdqsConstants;
import com.ceb.hdqs.entity.GycsEO;
import com.ceb.hdqs.entity.PjyjlEO;
import com.ceb.hdqs.service.JyjlComparator;
import com.ceb.hdqs.service.cache.C0775RowkeyItem;
import com.ceb.hdqs.service.cache.Handle0775Cache;
import com.ceb.hdqs.sop.SopIntf;
import com.ceb.hdqs.utils.DateTimeFormatUtils;
import com.ceb.hdqs.utils.HdqsUtils;
import com.ceb.hdqs.utils.TimerUtils;
import com.ceb.hdqs.wtc.form.Handle0775Form;

public class Handle0775QueryBean extends AbstractQueryBean {
	public Handle0775QueryBean() {
		setLogger(Logger.getLogger(getClass()));
	}

	public Reply service(TPServiceInformation tpServiceInfor) throws Exception {
		SopIntf recvSop = getSopFromWTC(tpServiceInfor);
		SopIntf sendSop = new SopIntf();
		setSystemHeadForSop(recvSop, sendSop);
		printRecvHeaderPkt(recvSop);

		Handle0775Form form = buildForm(recvSop);
		PjyjlEO record = buildExchangeRecord(recvSop, form);
		printRecvFormPkt(form, record);
		try {
			validateField(form);
		} catch (HdqsWtcException e) {
			getLogger().info(this.getExCode() + " sop invalid: " + e.getMessage());
			return getExceptionBuffer(tpServiceInfor, sendSop, e.getMessage());
		}
		getLogger().info("Start query:" + getExCode());
		GycsEO gycsEO = this.getGycsService().findByGuiydh(record.getJio1gy());
		if (gycsEO == null) {
			getLogger().info("操作柜员在柜员参数表中不存在.");
			return getExceptionBuffer(tpServiceInfor, sendSop, "操作柜员在柜员参数表中不存在");
		}

		String guiyjb = gycsEO.getGuiyjb();
		if (StringUtils.isNotBlank(guiyjb)) {
			getLogger().info("Guiyjb: " + guiyjb);
		} else {
			getLogger().info("Guiyjb is empty.");
		}
		if (StringUtils.isBlank(guiyjb) || guiyjb.equals(GycsEO.GUIYJB_ONE) || guiyjb.equals(GycsEO.GUIYJB_TWO)) {
			// 一级、二级柜员只能查询本人
			if (StringUtils.isNotBlank(form.getJio1gy()) && !form.getJio1gy().equals(record.getJio1gy())) {
				return getExceptionBuffer(tpServiceInfor, sendSop, "三级以下级别柜员只能查询本人");
			}
			if (StringUtils.isNotBlank(form.getYngyjg()) && !form.getYngyjg().equals(record.getYngyjg())) {
				return getExceptionBuffer(tpServiceInfor, sendSop, "三级以下级别柜员只能查询本人");
			}
			if (StringUtils.isNotBlank(form.getGuiyls()) && !form.getGuiyls().startsWith(record.getJio1gy())) {
				return getExceptionBuffer(tpServiceInfor, sendSop, "三级以下级别柜员只能查询本人");
			}
			form.setJio1gy(record.getJio1gy());
			form.setYngyjg(record.getYngyjg());
		} else {
			if (StringUtils.isBlank(form.getYngyjg())) {
				// 本机构只能查本机构。
				if (StringUtils.isNotBlank(form.getJio1gy()) && !form.getJio1gy().equals(record.getJio1gy())) {
					GycsEO jio1GycsEO = this.getGycsService().findByGuiydh(form.getJio1gy());
					if (jio1GycsEO == null) {
						return getExceptionBuffer(tpServiceInfor, sendSop, "查询柜员在柜员参数表中不存在");
					}
					if (getJgcsService().isParentQuery(record.getYngyjg(), jio1GycsEO.getYngyjg())) {
						form.setYngyjg(jio1GycsEO.getYngyjg());
					} else {
						return getExceptionBuffer(tpServiceInfor, sendSop, "非上级机构,无权查询");
					}
				} else {
					form.setYngyjg(record.getYngyjg());
				}
			} else {
				// 上级机构可以查询下级机构
				if (!getJgcsService().isParentQuery(record.getYngyjg(), form.getYngyjg())) {
					return getExceptionBuffer(tpServiceInfor, sendSop, "非上级机构,无权查询");
				}
			}
		}

		return doService(tpServiceInfor, recvSop, sendSop, form, record);
	}

	private Reply doService(TPServiceInformation tpServiceInfor, SopIntf recvSop, SopIntf sendSop, Handle0775Form form, PjyjlEO record) {
		TimerUtils timer = new TimerUtils();
		timer.start();
		PjyjlEO sRecord = saveExchangeRecord(record);
		String key = buildKey(form, sRecord);
		try {
			C0775RowkeyItem rowkeyItem = null;
			Handle0775Cache cache = Handle0775Cache.getInstance();
			List<PjyjlEO> result = new ArrayList<PjyjlEO>();
			File resultFile = null;
			if (cache.containsKey(key)) {
				getLogger().debug("Load data from cache,key " + key);
				rowkeyItem = cache.get(key);
				Map<Integer, List<PjyjlEO>> itemCache = rowkeyItem.getCache();
				Map<Integer, File> fileCache = rowkeyItem.getfCache();
				if (itemCache.containsKey(Integer.valueOf(form.getQishbs()))) {
					result = itemCache.get(Integer.valueOf(form.getQishbs()));
					if (fileCache.containsKey(Integer.valueOf(form.getQishbs()))) {
						resultFile = fileCache.get(Integer.valueOf(form.getQishbs()));
					} else {
						resultFile = initFileCache(fileCache, result, sRecord, form);
					}
				}
			} else {
				// first query
				int ctrlThreshold = Integer.parseInt(ConfigLoader.getInstance().getProperty(
						RegisterTable.QUERY_0775_RECORD_COUNT_THRESHOLD, "1000"));
				HandleQuery0775 logQuery = new HandleQuery0775();
				List<PjyjlEO> list = new ArrayList<PjyjlEO>();
				// 指定机构
				if (form.getQyrage() == Handle0775Form.QYRAGE_SPECIAL) {
					// 查询总记录数，判断是否超过设定阀值，如果超过，则进行提示
					TimerUtils qtimer = new TimerUtils();
					qtimer.start();
					int count = this.getJyjlService().getCounts(form, this.getExCode());
					qtimer.stop();
					getLogger().debug("Oracle查询总数" + count + ", Cost time(ms)=" + qtimer.getExecutionTime());
					qtimer.start();
					List<String> rowkeyList = logQuery.getCounts(form, this.getExCode());
					qtimer.stop();
					getLogger().debug("Hbase查询总数完成" + rowkeyList.size() + ", Cost time(ms)=" + qtimer.getExecutionTime());
					count += rowkeyList.size();

					if (count >= ctrlThreshold) {
						timer.stop();
						getLogger().info("总记录数为" + count + ",查询记录数阀值为" + ctrlThreshold + ",超过查询记录数阀值");
						updateExchangeEndTime(sRecord);
						return getExceptionBuffer(tpServiceInfor, sendSop, "总记录数为" + count + ",查询记录数阀值为" + ctrlThreshold + ",请缩小查询范围");
					}
					qtimer.start();
					list.addAll(this.getJyjlService().findByProperty(form, this.getExCode()));
					qtimer.stop();
					getLogger().debug("Oracle查询记录完成, Cost time(ms)=" + qtimer.getExecutionTime());
					qtimer.start();
					List<PjyjlEO> hbList = logQuery.queryAll(rowkeyList);
					if (hbList != null) {
						list.addAll(hbList);
					}
					qtimer.stop();
					getLogger().debug("Hbase查询记录完成, Cost time(ms)=" + qtimer.getExecutionTime());
				} else {// 辖内
					// 查询总记录数，判断是否超过设定阀值，如果超过，则进行提示
					TimerUtils qtimer = new TimerUtils();
					qtimer.start();
					List<String> childList = getJgcsService().findChildren(form.getYngyjg());
					StringBuffer buffer = new StringBuffer();
					for (String yngyjg : childList) {
						buffer.append(yngyjg + ",");
					}
					qtimer.stop();
					getLogger().debug("查询辖内机构完成, Cost time(ms)=" + qtimer.getExecutionTime());
					buffer.deleteCharAt(buffer.length() - 1);
					getLogger().debug(buffer.toString());
					qtimer.start();
					int count = this.getJyjlService().getCounts(form, this.getExCode(), childList);
					qtimer.stop();
					getLogger().debug("Oracle查询总数" + count + ", Cost time(ms)=" + qtimer.getExecutionTime());
					qtimer.start();
					List<String> rowkeyList = logQuery.getCounts(form, childList, this.getExCode());
					qtimer.stop();
					getLogger().debug("Hbase查询总数" + rowkeyList.size() + ", Cost time(ms)=" + qtimer.getExecutionTime());
					count += rowkeyList.size();
					if (count >= ctrlThreshold) {
						timer.stop();
						getLogger().info(
								"总记录数为" + count + ",查询记录数阀值为" + ctrlThreshold + ",超过查询记录数阀值, Cost time(ms)=" + timer.getExecutionTime());
						updateExchangeEndTime(sRecord);
						return getExceptionBuffer(tpServiceInfor, sendSop, "总记录数为" + count + ",查询记录数阀值为" + ctrlThreshold + ",请缩小查询范围");
					}
					qtimer.start();
					list.addAll(this.getJyjlService().findByProperty(form, this.getExCode(), childList));
					qtimer.stop();
					getLogger().debug("Oracle查询记录完成, Cost time(ms)=" + qtimer.getExecutionTime());
					qtimer.start();
					List<PjyjlEO> hbList = logQuery.queryAll(rowkeyList);
					if (hbList != null) {
						list.addAll(hbList);
					}
					qtimer.stop();
					getLogger().debug("Hbase查询记录完成, Cost time(ms)=" + qtimer.getExecutionTime());
				}
				rowkeyItem = new C0775RowkeyItem();
				rowkeyItem.setSize(list.size());
				Collections.sort(list, new JyjlComparator());
				Map<Integer, List<PjyjlEO>> itemCache = rowkeyItem.getCache();
				Map<Integer, File> fileCache = rowkeyItem.getfCache();
				initCache(itemCache, list, form);
				if (itemCache.containsKey(Integer.valueOf(form.getQishbs()))) {
					result = itemCache.get(Integer.valueOf(form.getQishbs()));
					if (fileCache.containsKey(Integer.valueOf(form.getQishbs()))) {
						resultFile = fileCache.get(Integer.valueOf(form.getQishbs()));
					} else {
						resultFile = initFileCache(fileCache, result, sRecord, form);
					}
				}
			}

			rowkeyItem.setLastModifiedT(System.currentTimeMillis());
			cache.put(key, rowkeyItem);
			if (result == null || result.isEmpty()) {
				timer.stop();
				getLogger().info("存在[0]条记录, Cost time(ms)=" + timer.getExecutionTime());
				updateExchangeEndTime(sRecord);
				return getExceptionBuffer(tpServiceInfor, sendSop, "存在[0]条记录");
			} else {
				getTPServiceInfoAsResult(tpServiceInfor, sendSop, rowkeyItem, sRecord, result, resultFile);
			}
		} catch (ConditionNotExistException e) {
			timer.stop();
			getLogger().error(e.getMessage(), e);
			updateExchangeRunErrStatus(sRecord);
			return getExceptionBuffer(tpServiceInfor, sendSop, "存在[0]条记录");
		} catch (Exception e) {
			timer.stop();
			getLogger().error(e.getMessage(), e);
			updateExchangeRunErrStatus(sRecord);
			return getExceptionBuffer(tpServiceInfor, sendSop, "后台查询异常");
		}
		timer.stop();
		getLogger().info(getExCode() + " query complete, Cost time(ms)=" + timer.getExecutionTime());
		updateExchangeEndTime(sRecord);
		return tpServiceInfor;
	}

	private void initCache(Map<Integer, List<PjyjlEO>> itemCache, List<PjyjlEO> list, Handle0775Form form) {
		if (list == null) {
			return;
		}
		int size = list.size();
		int page = size / form.getCxunbs();
		int mod = size % form.getCxunbs();
		if (mod != 0) {
			page += 1;
		}
		for (int i = 0; i < page; i++) {
			List<PjyjlEO> tmpList = new ArrayList<PjyjlEO>();
			int fromIndex = i * form.getCxunbs();
			int toIndex = (i + 1) * form.getCxunbs();
			tmpList.addAll(list.subList(fromIndex, Math.min(toIndex, size)));
			itemCache.put(Integer.valueOf(fromIndex + 1), tmpList);
		}
		list.clear();
		list = null;
	}

	private File initFileCache(Map<Integer, File> fileCache, List<PjyjlEO> list, PjyjlEO record, Handle0775Form form) {
		if (list == null) {
			return null;
		}

		List<PjyjlEO> fileList = new ArrayList<PjyjlEO>();
		for (PjyjlEO r : list) {
			if (r.getFileQuery()) {
				fileList.add(r);
			}
		}
		if (fileList.isEmpty()) {
			return null;
		}
		File resultFile = buildResultFile(fileList, record);
		if (resultFile != null) {
			fileCache.put(Integer.valueOf(form.getQishbs()), resultFile);
		}

		return resultFile;
	}

	private String buildKey(Handle0775Form form, PjyjlEO record) {
		StringBuffer buffer = new StringBuffer(getExCode());
		buffer.append(HdqsConstants.SPLIT_CHAR).append(record.getJio1gy());
		buffer.append(HdqsConstants.SPLIT_CHAR).append(form.getQishrq());
		buffer.append(HdqsConstants.SPLIT_CHAR).append(form.getZzhirq());
		buffer.append(HdqsConstants.SPLIT_CHAR).append(form.getJio1gy());
		buffer.append(HdqsConstants.SPLIT_CHAR).append(form.getQyrage());
		buffer.append(HdqsConstants.SPLIT_CHAR).append(form.getYngyjg());
		buffer.append(HdqsConstants.SPLIT_CHAR).append(form.getJiaoym());
		buffer.append(HdqsConstants.SPLIT_CHAR).append(form.getGuiyls());
		return buffer.toString();
	}

	private Handle0775Form buildForm(SopIntf recvSop) {
		Handle0775Form form = new Handle0775Form();
		form.setQishrq(recvSop.getStr(getFileRecv(), "QISHRQ").trim());
		form.setZzhirq(recvSop.getStr(getFileRecv(), "ZZHIRQ").trim());
		form.setJio1gy(recvSop.getStr(getFileRecv(), "JIO1GY").trim());
		String qyrage = recvSop.getStr(getFileRecv(), "QYRAGE").trim();
		if (HdqsUtils.isNotBlank(qyrage)) {
			form.setQyrage(Integer.valueOf(qyrage));
		}
		form.setYngyjg(recvSop.getStr(getFileRecv(), "YNGYJG").trim());
		form.setJiaoym(recvSop.getStr(getFileRecv(), "JIAOYM").trim());
		form.setGuiyls(recvSop.getStr(getFileRecv(), "GUIYLS").trim());
		String startNum = recvSop.getStr(getFileRecv(), "QISHBS").trim();
		if (HdqsUtils.isNotBlank(startNum)) {
			form.setQishbs(Integer.parseInt(startNum));
		}
		String queryNum = recvSop.getStr(getFileRecv(), "CXUNBS").trim();
		if (HdqsUtils.isNotBlank(queryNum)) {
			form.setCxunbs(Integer.parseInt(queryNum));
		}

		return form;
	}

	/**
	 * 对输入值进行验证
	 * 
	 * @param form
	 * @return
	 */
	private void validateField(Handle0775Form form) throws HdqsWtcException {
		if (form.getJiaoym().equals(getExCode())) {
			throw new HdqsWtcException("不能查询" + getExCode() + "交易");
		}
		if (HdqsUtils.isBlank(form.getQishrq())) {
			throw new HdqsWtcException("起始日期不能为空");
		}
		if (HdqsUtils.isBlank(form.getZzhirq())) {
			throw new HdqsWtcException("终止日期不能为空");
		}
		if (form.getQishrq().compareTo(form.getZzhirq()) > 0) {
			throw new HdqsWtcException("起始时间不能大于终止时间");
		}
		ConfigLoader.overflowMonthLimit(form.getQishrq(), form.getZzhirq());
		if (HdqsUtils.isNotBlank(form.getJio1gy())) {
			int length = form.getJio1gy().length();
			if (length != 6) {
				throw new HdqsWtcException("交易柜员长度不符合要求");
			}
		}
		if (form.getQyrage() == null) {
			throw new HdqsWtcException("查询范围不能为空");
		}
		if (HdqsUtils.isNotBlank(form.getYngyjg())) {
			int length = form.getYngyjg().length();
			if (length != 4) {
				throw new HdqsWtcException("营业机构长度不符合要求");
			}
		}
		if (HdqsUtils.isNotBlank(form.getGuiyls())) {
			int length = form.getGuiyls().length();
			if (length != 10) {
				throw new HdqsWtcException("柜员流水长度不符合要求");
			}
		}
	}

	/**
	 * 根据查询结果获取
	 * 
	 */
	private Reply getTPServiceInfoAsResult(TPServiceInformation tpServiceInfor, SopIntf sendSop, C0775RowkeyItem item, PjyjlEO record,
			List<PjyjlEO> list, File file) {
		String[] inmap = new String[] { getFileSent() };
		byte[] sendData = new byte[200 + 148 + list.size() * 578];
		setNormalMessageForSop(sendSop, record, list, item, file);
		TypedCArray replyBuffer = getTypedCArray(sendData, inmap, sendSop);
		tpServiceInfor.setReplyBuffer(replyBuffer);
		return tpServiceInfor;
	}

	private void setNormalMessageForSop(SopIntf sendSop, PjyjlEO record, List<PjyjlEO> list, C0775RowkeyItem item, File resultFile) {
		sendSop.put(null, "JIAOYM", getExCode());
		long currentT = System.currentTimeMillis();
		sendSop.put(null, "JIOYRQ", DateTimeFormatUtils.formatDate(currentT, "yyyymmdd"));
		sendSop.put(null, "JIOYSJ", Integer.valueOf(DateTimeFormatUtils.formatDate(currentT, "HHmmss")));
		sendSop.put(null, "GUIYLS", record.getGuiyls());
		Short FDCWJYXH = 0;
		sendSop.put(null, "CWJYXH", FDCWJYXH);
		sendSop.put(null, SopIntf.TPU_RETCODE, SopIntf.SOP_SUCC);
		sendSop.put(getFileSent(), "BISHUU", String.valueOf(item.getSize()));
		if (resultFile != null) {
			String filePath = getRelativePath(resultFile.getAbsolutePath());
			sendSop.put(getFileSent(), "FILEPATH", filePath);
		}

		Map<String, List<String>> map = convertResultToMap(list);
		sendSop.put(getFileSent(), getFileTable(), "JIOYRQ", getStrArrayFromList(map.get("0")));
		sendSop.put(getFileSent(), getFileTable(), "JIO1GY", getStrArrayFromList(map.get("1")));
		sendSop.put(getFileSent(), getFileTable(), "YNGYJG", getStrArrayFromList(map.get("2")));
		sendSop.put(getFileSent(), getFileTable(), "JIAOYM", getStrArrayFromList(map.get("3")));
		sendSop.put(getFileSent(), getFileTable(), "JIONAM", getStrArrayFromList(map.get("4")));
		sendSop.put(getFileSent(), getFileTable(), "GUIYLS", getStrArrayFromList(map.get("5")));
		sendSop.put(getFileSent(), getFileTable(), "QYCOND", getStrArrayFromList(map.get("6")));
		sendSop.put(getFileSent(), getFileTable(), "QYFILE", getStrArrayFromList(map.get("7")));
		sendSop.put(getFileSent(), getFileTable(), "SUCCFG", getStrArrayFromList(map.get("8")));

	}

	/**
	 * 将遍历结果对象转换map对象
	 * 
	 * @param iter
	 * @return
	 */
	private Map<String, List<String>> convertResultToMap(List<PjyjlEO> list) {
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		for (int i = 0; i < getFieldCount(); i++) {
			map.put("" + i, new ArrayList<String>());
		}
		for (PjyjlEO item : list) {
			map.get("0").add(item.getJioyrq() == null ? "" : item.getJioyrq());
			map.get("1").add(item.getJio1gy() == null ? "" : item.getJio1gy());
			map.get("2").add(item.getYngyjg() == null ? "" : item.getYngyjg());
			map.get("3").add(item.getJiaoym() == null ? "" : item.getJiaoym());
			String exCode = item.getJiaoym();
			if (HdqsUtils.isNotBlank(exCode)) {
				String exName = ExchangeCode.valueOf("F" + exCode).getDisplay();
				map.get("4").add(exName == null ? "" : exName);
			}

			map.get("5").add(item.getGuiyls() == null ? "" : item.getGuiyls());
			if (item.getFileQuery()) {
				map.get("6").add("");
				map.get("7").add(item.getQueryStr() == null ? "" : item.getQueryStr());
			} else {
				map.get("6").add(item.getQueryStr() == null ? "" : item.getQueryStr());
				map.get("7").add("");
			}
			if (item.getRunSucc()) {
				map.get("8").add("1");
			} else {
				map.get("8").add("0");
			}
		}
		return map;
	}

	private void printStart(BufferedWriter writer, String title) throws IOException {
		for (int i = 0; i < 80; i++) {
			writer.write("*");
		}
		// writer.newLine();//记事本换行有问题
		writer.write("\r\n");
		writer.write(title);
		writer.write("\r\n");
		for (int i = 0; i < 80; i++) {
			writer.write("*");
		}
		writer.write("\r\n");
	}

	private File buildResultFile(List<PjyjlEO> list, PjyjlEO record) {
		ConfigLoader instance = ConfigLoader.getInstance();
		String queryPath = instance.getProperty(RegisterTable.FTP_DOWNLOAD_PATH);
		String fileName = record.getSlbhao() + ".txt";
		File fileDir = new File(queryPath);
		fileDir.mkdirs();
		BufferedWriter writer = null;
		File destFile = new File(queryPath, fileName);
		try {
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(destFile),
					instance.getProperty(RegisterTable.ASY_HANDLE_FILE_CHARSET)));
			for (PjyjlEO tmp : list) {
				BufferedReader reader = null;
				try {
					File tmpFile = new File(tmp.getQueryStr());
					this.getLogger().info(tmp.getQueryStr());
					if (!tmpFile.exists()) {
						continue;
					}
					printStart(writer, tmpFile.getAbsolutePath());
					reader = new BufferedReader(new InputStreamReader(new FileInputStream(tmpFile),
							instance.getProperty(RegisterTable.ASY_HANDLE_FILE_CHARSET)));
					String line = null;
					while ((line = reader.readLine()) != null) {
						writer.write(line);
						// writer.newLine();//记事本换行有问题
						writer.write("\r\n");
					}
				} catch (Exception e) {
					getLogger().error(e.getMessage(), e);
				} finally {
					if (reader != null) {
						try {
							reader.close();
						} catch (IOException e) {
						}
					}
				}
			}
		} catch (Exception e) {
			getLogger().error(e.getMessage(), e);
		} finally {
			if (writer != null) {
				try {
					writer.flush();
					writer.close();
				} catch (IOException e) {

				}
			}
		}
		if (destFile.length() == 0) {
			destFile.delete();
			return null;
		}
		return destFile;
	}

	public static void main(String[] args) throws Exception {
		Handle0775QueryBean bean = new Handle0775QueryBean();
		List<PjyjlEO> list = new ArrayList<PjyjlEO>();
		for (int i = 0; i < 10; i++) {
			PjyjlEO record = new PjyjlEO();
			record.setJiaoym("0770");
			record.setQueryStr("D:/tools/jndi.properties");
			list.add(record);
		}
		PjyjlEO tmp = new PjyjlEO();
		tmp.setSlbhao("111111111111111111");
		File result = bean.buildResultFile(list, tmp);
		System.out.println(result.getAbsolutePath());
		System.out.println(result.getName());
	}
}