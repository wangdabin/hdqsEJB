package com.ceb.hdqs.wtc;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import weblogic.wtc.jatmi.Reply;
import weblogic.wtc.jatmi.TPException;
import weblogic.wtc.jatmi.TPServiceInformation;
import weblogic.wtc.jatmi.TypedCArray;

import com.ceb.hdqs.action.query.exception.ConditionNotExistException;
import com.ceb.hdqs.action.query0773.HandleQuery0773;
import com.ceb.hdqs.constants.HdqsConstants;
import com.ceb.hdqs.entity.PybjyEO;
import com.ceb.hdqs.entity.PjyjlEO;
import com.ceb.hdqs.entity.result.Handle0773QueryResult;
import com.ceb.hdqs.entity.result.ReplacementCardItemResult;
import com.ceb.hdqs.service.cache.C0773RowkeyItem;
import com.ceb.hdqs.service.cache.Handle0773Cache;
import com.ceb.hdqs.sop.SopIntf;
import com.ceb.hdqs.utils.DateTimeFormatUtils;
import com.ceb.hdqs.utils.HdqsUtils;
import com.ceb.hdqs.utils.TimerUtils;
import com.ceb.hdqs.wtc.form.Handle0773Form;

public class Handle0773QueryBean extends AbstractQueryBean {

	public Handle0773QueryBean() {
		setLogger(Logger.getLogger(getClass()));
	}

	/**
	 * 对图前报文进行解析，处理业务请求
	 * 
	 * @param tpServiceInfor
	 * @return
	 * @throws TPException
	 * @throws InterruptedException
	 */
	public Reply service(TPServiceInformation tpServiceInfor) throws Exception {
		SopIntf recvSop = getSopFromWTC(tpServiceInfor);
		SopIntf sendSop = new SopIntf();
		setSystemHeadForSop(recvSop, sendSop);
		printRecvHeaderPkt(recvSop);

		PybjyEO record = buildAsyQueryRecord(recvSop);
		Handle0773Form form = buildForm(record);
		printRecvFormPkt(form, record);
		try {
			validateField(record);
		} catch (HdqsWtcException e) {
			getLogger().info(this.getExCode() + " sop invalid: " + e.getMessage());
			return getExceptionBuffer(tpServiceInfor, sendSop, e.getMessage());
		}
		getLogger().info("Start query:" + getExCode());
		TimerUtils timer = new TimerUtils();
		timer.start();
		PjyjlEO sRecord = saveExchangeRecord(record, form);
		record.setJioyrq(sRecord.getJioyrq());
		String key = buildKey(record);
		try {
			C0773RowkeyItem item = null;
			Handle0773Cache queryCache = Handle0773Cache.getInstance();

			if (queryCache.containsKey(key)) {
				getLogger().info("Load data from cache: " + key);
				item = queryCache.get(key);
			} else {
				HandleQuery0773 queryAction = new HandleQuery0773();
				Handle0773QueryResult queryResult = queryAction.query(record);
				List<ReplacementCardItemResult> list = queryResult.getReplacementCardResult();
				item = new C0773RowkeyItem();
				if (list == null || list.isEmpty()) {
					item.getAscList().addAll(new ArrayList<ReplacementCardItemResult>());
				} else {
					item.setResult(queryResult);
					item.getAscList().addAll(list);
					list.clear();
					queryResult.setReplacementCardResult(null);
				}
			}
			item.setLastModifiedT(System.currentTimeMillis());
			queryCache.put(key, item);
			if (item.getAscList().isEmpty()) {
				timer.stop();
				getLogger().info("存在[0]条记录, Cost time(ms)=" + timer.getExecutionTime());
				updateExchangeEndTime(sRecord);
				return getExceptionBuffer(tpServiceInfor, sendSop, "存在[0]条记录");
			} else {
				getTPServiceInfoAsResult(tpServiceInfor, record, sendSop, item);
			}
		} catch (ConditionNotExistException e) {
			timer.stop();
			getLogger().info("账号在主文件中不存在, Cost time(ms)=" + timer.getExecutionTime());
			updateExchangeRunErrStatus(sRecord);
			return getExceptionBuffer(tpServiceInfor, sendSop, e.getMessage());
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

	private Handle0773Form buildForm(PybjyEO record) {
		Handle0773Form form = new Handle0773Form();
		form.setChaxlx(record.getChaxlx());
		form.setKehuzh(record.getKehuzh());
		form.setQishbs(record.getStartNum());
		form.setCxunbs(record.getQueryNum());
		form.setShfobz(record.getShfobz());
		return form;
	}

	/**
	 * 获取key
	 * 
	 * @param record
	 * @return
	 */
	private String buildKey(PybjyEO record) {
		StringBuffer buffer = new StringBuffer(getExCode());
		buffer.append(HdqsConstants.SPLIT_CHAR).append(record.getJio1gy());
		buffer.append(HdqsConstants.SPLIT_CHAR).append(record.getChaxlx());
		buffer.append(HdqsConstants.SPLIT_CHAR).append(record.getKehuzh());
		return buffer.toString();
	}

	private void validateField(PybjyEO record) throws HdqsWtcException {
		if (HdqsUtils.isBlank(record.getChaxlx())) {
			throw new HdqsWtcException("查询类型不能为空");
		}
		if (HdqsUtils.isBlank(record.getKehuzh())) {
			throw new HdqsWtcException("客户帐号不能为空");
		}
		int length = record.getKehuzh().length();
		if (length < 16 || length > 21) {
			throw new HdqsWtcException("客户帐号超出长度限制");
		}
	}

	/**
	 * 根据查询结果获取
	 * 
	 * @param tpServiceInfor
	 * @param sendData
	 * @param inmap
	 * @param record
	 * @param key
	 * @param sendSop
	 * @param queryResult
	 * @param queryCache
	 * @return
	 */
	private Reply getTPServiceInfoAsResult(TPServiceInformation tpServiceInfor, PybjyEO record, SopIntf sendSop, C0773RowkeyItem item) {
		List<ReplacementCardItemResult> itemList = item.getAscList();
		int totalCount = itemList.size();
		if (Integer.valueOf(record.getStartNum()) > totalCount) {
			getLogger().info("起始笔数大于总记录数");
			return getExceptionBuffer(tpServiceInfor, sendSop, "起始笔数大于总记录数");
		}
		byte[] sendData = new byte[200 + 148 + 110 + totalCount * 32];
		String[] inmap = new String[] { this.getFileSent(), this.getFilePrint() };
		setNormalMessageForSop(sendSop, record, item);
		TypedCArray replyBuffer = getTypedCArray(sendData, inmap, sendSop);
		tpServiceInfor.setReplyBuffer(replyBuffer);
		return tpServiceInfor;
	}

	/**
	 * 将查询结果转换sop对象
	 * 
	 * @param sendSop
	 * @param startNum
	 * @param result
	 * @param corporateQueryItemList
	 * @param queryNumber
	 */
	private void setNormalMessageForSop(SopIntf sendSop, PybjyEO record, C0773RowkeyItem item) {
		sendSop.put(null, "JIAOYM", getExCode());
		long currentT = System.currentTimeMillis();
		sendSop.put(null, "JIOYRQ", DateTimeFormatUtils.formatDate(currentT, "yyyymmdd"));
		sendSop.put(null, "JIOYSJ", Integer.valueOf(DateTimeFormatUtils.formatDate(currentT, "HHmmss")));
		sendSop.put(null, "GUIYLS", record.getGuiyls());
		Short FDCWJYXH = 0;
		sendSop.put(null, "CWJYXH", FDCWJYXH);
		sendSop.put(null, SopIntf.TPU_RETCODE, SopIntf.SOP_SUCC);
		Handle0773QueryResult result = item.getResult();
		sendSop.put(getFileSent(), "GERZWM", result.getGERZWM());
		sendSop.put(getFileSent(), "KEHHAO", result.getKEHUZH());
		sendSop.put(getFileSent(), "BEIZXX", result.getRemark());
		int formIndex = record.getStartNum() - 1;
		List<ReplacementCardItemResult> list = item.getAscList();
		int toIndex = formIndex + record.getQueryNum();
		if (toIndex > list.size())
			toIndex = list.size();
		getLogger().info("form:" + formIndex + ",to:" + toIndex + ",total:" + list.size());
		List<ReplacementCardItemResult> subList = list.subList(formIndex, toIndex);
		getLogger().info("size:" + subList.size());
		Map<String, List<String>> map = convertResultToMap(subList);

		sendSop.put(getFileSent(), getFileTable(), "KEHUZH", getStrArrayFromList(map.get("0")));
		sendSop.put(getFileSent(), getFileTable(), "JILUZT", getStrArrayFromList(map.get("1")));
		sendSop.put(getFileSent(), getFileTable(), "JDJKBZ", getStrArrayFromList(map.get("2")));
		sendSop.put(getFileSent(), getFileTable(), "KAAAZL", getStrArrayFromList(map.get("3")));
		sendSop.put(getFileSent(), getFileTable(), "FAKARQ", getStrArrayFromList(map.get("4")));
		if (!record.getShfobz().equals(HdqsConstants.SHFOBZ_NONE)) {
			String filePath = getFilePathFor0773(record, item);
			setPrintFilePath(sendSop, filePath != null ? filePath : "");
			// setFileDownLoadDir(sendSop);
		}
	}

	private String getFilePathFor0773(PybjyEO record, C0773RowkeyItem item) {
		String filePath = record.getShfobz().equals(HdqsConstants.SHFOBZ_PRINT) ? item.getCommFilePath() : item.getPdfFilePath();

		filePath = asyGenerateFile(record, filePath, item);
		if (filePath != null) {
			if (record.getShfobz().equals(HdqsConstants.SHFOBZ_PRINT)) {
				item.setCommFilePath(filePath);
			} else {
				item.setPdfFilePath(filePath);
			}
			getLogger().info("路径: " + filePath);
			filePath = getRelativePath(filePath);
			getLogger().info("返回前端相对路径: " + filePath);
		}

		return filePath;
	}

	private String asyGenerateFile(PybjyEO record, String filePath, C0773RowkeyItem item) {
		if (StringUtils.isBlank(filePath) || !new File(filePath).exists()) {
			getLogger().info("获取打印文件路径");
			try {
				HandleQuery0773 queryAction = new HandleQuery0773();
				filePath = queryAction.generateFile(record, item.getResult(), item.getAscList());
			} catch (Exception e) {
				getLogger().error(e.getMessage(), e);
			}
		}

		return filePath;
	}

	/**
	 * 将遍历结果对象转换map对象
	 * 
	 * @param iterator
	 * @return
	 */
	private Map<String, List<String>> convertResultToMap(List<ReplacementCardItemResult> list) {
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		for (int i = 0; i < this.getFieldCount(); i++) {
			map.put("" + i, new ArrayList<String>());
		}
		for (ReplacementCardItemResult item : list) {
			map.get("0").add(item.getKahaoo() == null ? "" : item.getKahaoo().trim());
			map.get("1").add(item.getJiluzt() == null ? "" : item.getJiluzt().trim());
			map.get("2").add(item.getJdjkbz() == null ? "" : item.getJdjkbz().trim());
			map.get("3").add(item.getKaaazl() == null ? "" : item.getKaaazl().trim());
			map.get("4").add(item.getFakarq() == null ? "" : item.getFakarq().trim());
		}
		return map;
	}

	void printSendData(byte[] sendData, SopIntf sendSop, int res) {
		sendSop.clear();
		sendSop.convertSopToPool(sendData, res);
		// 输出数据
		String[] userAccount = sendSop.getStrs("O07732", "F07732", "KEHUZH");
		getLogger().info("result size" + userAccount.length);
		getLogger().info("个人中文名:" + sendSop.getStr("O07732", "GERZWM"));
		getLogger().info("客户号:" + sendSop.getStr("O07732", "KEHHAO"));
		getLogger().info("备注信息:" + sendSop.getStr("O07732", "BEIZXX"));
		for (int i = 0; i < userAccount.length; i++) {
			StringBuffer buf = new StringBuffer();
			buf.append(sendSop.getStrs("O07732", "F07732", "KEHUZH")[i] + "|");
			buf.append(sendSop.getStrs("O07732", "F07732", "JILUZT")[i] + "|");
			buf.append(sendSop.getStrs("O07732", "F07732", "JDJKBZ")[i] + "|");
			buf.append(sendSop.getStrs("O07732", "F07732", "KAAAZL")[i] + "|");
			buf.append(sendSop.getStrs("O07732", "F07732", "FAKARQ")[i] + "|");
			getLogger().info(buf.toString());
		}
	}
}