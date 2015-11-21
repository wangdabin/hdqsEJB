package com.ceb.hdqs.wtc;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import weblogic.wtc.jatmi.Reply;
import weblogic.wtc.jatmi.TPServiceInformation;
import weblogic.wtc.jatmi.TypedCArray;

import com.ceb.bank.context.RowkeyContext;
import com.ceb.bank.item.Item0771;
import com.ceb.bank.query.impl.Handle0771Query;
import com.ceb.bank.query.output.Output0771;
import com.ceb.bank.query.scanner.AshmxScanner;
import com.ceb.bank.result.Query0771Result;
import com.ceb.hdqs.action.query.exception.AsynQueryException;
import com.ceb.hdqs.action.query.exception.BalanceBrokedException;
import com.ceb.hdqs.action.query.exception.ConditionNotExistException;
import com.ceb.hdqs.config.ConfigLoader;
import com.ceb.hdqs.constants.HdqsConstants;
import com.ceb.hdqs.entity.PjyjlEO;
import com.ceb.hdqs.entity.PybjyEO;
import com.ceb.hdqs.po.Authorize;
import com.ceb.hdqs.service.cache.C0771RowkeyItem;
import com.ceb.hdqs.service.cache.Handle0771Cache;
import com.ceb.hdqs.sop.SopIntf;
import com.ceb.hdqs.utils.DateTimeFormatUtils;
import com.ceb.hdqs.utils.HdqsUtils;
import com.ceb.hdqs.utils.TimerUtils;
import com.ceb.hdqs.wtc.form.Handle0771Form;

public class Handle0771QueryBean extends AbstractQueryBean {
	public Handle0771QueryBean() {
		setLogger(Logger.getLogger(getClass()));
	}

	public Reply service(TPServiceInformation tpServiceInfor) throws Exception {
		SopIntf recvSop = getSopFromWTC(tpServiceInfor);
		SopIntf sendSop = new SopIntf();
		setSystemHeadForSop(recvSop, sendSop);
		printRecvHeaderPkt(recvSop);

		PybjyEO record = buildAsyQueryRecord(recvSop);
		record.setChaxzl(HdqsConstants.CHAXZL_KEHUZH);
		Handle0771Form form = buildForm(record);
		printRecvFormPkt(form, record);
		try {
			validateField(record);
		} catch (HdqsWtcException e) {
			getLogger().info(getExCode() + " sop invalid: " + e.getMessage());
			if (isFrontAuthorize(record)) {
				return getAuthorizeBuffer(tpServiceInfor, sendSop, Authorize.NO_AUTH_MSG);
			}
			return getExceptionBuffer(tpServiceInfor, sendSop, e.getMessage());
		}
		getLogger().info("Start query:" + getExCode());
		TimerUtils timer = new TimerUtils();
		timer.start();
		PjyjlEO sRecord = saveExchangeRecord(record, form);
		record.setJioyrq(sRecord.getJioyrq());
		String key = buildKey(record);
		isZonghangAuthorize(record);
		try {
			Handle0771Cache cache = Handle0771Cache.getInstance();
			Authorize authorize = new Authorize();
			C0771RowkeyItem rowkeyItem = new C0771RowkeyItem();
			if (!cache.containsKey(key)) {
				Handle0771Query queryAction = new Handle0771Query();
				Query0771Result queryResult = queryAction.load(record);
				Map<Integer, List<RowkeyContext>> qMap = queryResult.getPageCache();
				// 无论记录明细是否存在，都需要进行授权
				if (queryResult != null && queryResult.getAuthorize() != null) {
					authorize = isNeedAuthorize(record, queryResult.getAuthorize());
					rowkeyItem.setAuthorize(authorize);
				}
				if (qMap != null && qMap.size() > 0) {
					rowkeyItem.setBishuu(queryResult.getBishuu());
					rowkeyItem.setKehhao(queryResult.getKehhao());
					rowkeyItem.setKehuzh(record.getKehuzh());
					rowkeyItem.setGerzwm(queryResult.getGerzwm());
					rowkeyItem.getCache().putAll(qMap);
					getLogger().info("total size: " + rowkeyItem.getBishuu());
				}
				// 无论记录明细是否存在，都缓存
				rowkeyItem.setLastModifiedT(System.currentTimeMillis());
				cache.put(key, rowkeyItem);
			}
			List<Item0771> itemList = null;
			if (cache.containsKey(key)) {
				rowkeyItem = cache.get(key);
				if (rowkeyItem.getAuthorize() != null) {
					authorize = isNeedCacheAuthorize(rowkeyItem.getAuthorize(), record);
				}
				if (isFrontAuthorize(record) || authorize.isNeedAuth()) {
					getLogger().info(authorize.getPrintMsg());
					return getAuthorizeBuffer(tpServiceInfor, sendSop, authorize.getPrintMsg());
				}

				Map<Integer, List<RowkeyContext>> itemCache = rowkeyItem.getCache();
				if (itemCache.containsKey(record.getStartNum())) {
					List<RowkeyContext> rowkeyList = itemCache.get(record.getStartNum());
					AshmxScanner itemScanner = new AshmxScanner();
					itemList = itemScanner.query(record, rowkeyList);
					getLogger().info("current query size: " + itemList.size());
				}

				rowkeyItem.setLastModifiedT(System.currentTimeMillis());
				cache.put(key, rowkeyItem);
			}
			if (isFrontAuthorize(record) || authorize.isNeedAuth()) {
				getLogger().info(authorize.getPrintMsg());
				updateExchangeEndTime(sRecord);
				return getAuthorizeBuffer(tpServiceInfor, sendSop, authorize.getPrintMsg());
			}
			if (!cache.containsKey(key) || itemList == null || itemList.isEmpty()) {
				timer.stop();
				getLogger().info("存在[0]条记录, Cost time(ms)=" + timer.getExecutionTime());
				updateExchangeEndTime(sRecord);
				return getExceptionBuffer(tpServiceInfor, sendSop, "存在[0]条记录");
			}
			getTPServiceInfoAsResult(tpServiceInfor, record, sendSop, itemList, rowkeyItem);
		} catch (ConditionNotExistException e) {
			timer.stop();
			getLogger().info(e.getMessage());
			updateExchangeRunErrStatus(sRecord);
			if (isFrontAuthorize(record)) {
				return getAuthorizeBuffer(tpServiceInfor, sendSop, Authorize.NO_AUTH_MSG);
			}
			return getExceptionBuffer(tpServiceInfor, sendSop, e.getMessage());
		} catch (BalanceBrokedException e) {
			timer.stop();
			getLogger().info(e.getMessage());
			updateExchangeRunErrStatus(sRecord);
			if (isFrontAuthorize(record)) {
				return getAuthorizeBuffer(tpServiceInfor, sendSop, Authorize.NO_AUTH_MSG);
			}
			return getExceptionBuffer(tpServiceInfor, sendSop, e.getMessage());
		} catch (AsynQueryException e) {
			timer.stop();
			getLogger().info("进入异步查询");
			updateExchangeEndTime(sRecord);
			Authorize asyAuth = isAsyAuthorize(record);
			if (asyAuth.isNeedAuth()) {
				return getAuthorizeBuffer(tpServiceInfor, sendSop, asyAuth.getPrintMsg());
			}
			if (isFrontAuthorize(record)) {
				return getAuthorizeBuffer(tpServiceInfor, sendSop, Authorize.NO_AUTH_MSG);
			}
			this.getYbjyService().add(record);
			return handleAsynchronized(tpServiceInfor, sendSop, record);
		} catch (Exception e) {
			timer.stop();
			getLogger().error(e.getMessage(), e);
			updateExchangeRunErrStatus(sRecord);
			if (isFrontAuthorize(record)) {
				return getAuthorizeBuffer(tpServiceInfor, sendSop, Authorize.NO_AUTH_MSG);
			}
			return getExceptionBuffer(tpServiceInfor, sendSop, "后台查询异常");
		}
		timer.stop();
		getLogger().info(getExCode() + " query complete, Cost time(ms)=" + timer.getExecutionTime());
		updateExchangeEndTime(sRecord);
		return tpServiceInfor;
	}

	private Handle0771Form buildForm(PybjyEO record) {
		Handle0771Form form = new Handle0771Form();
		form.setChaxlx(record.getChaxlx());
		form.setKhzhlx(record.getKhzhlx());
		form.setKehuzh(record.getKehuzh());
		form.setHuobdh(record.getHuobdh());
		form.setChuibz(record.getChuibz());
		form.setQishrq(record.getStartDate());
		form.setZzhirq(record.getEndDate());
		form.setQishbs(record.getStartNum());
		form.setCxunbs(record.getQueryNum());
		form.setZhhuxz(record.getZhhuxz());
		form.setShfobz(record.getShfobz());
		return form;
	}

	private String buildKey(PybjyEO record) {
		StringBuffer buffer = new StringBuffer(this.getExCode());
		buffer.append(HdqsConstants.SPLIT_CHAR).append(record.getJio1gy());
		buffer.append(HdqsConstants.SPLIT_CHAR).append(record.getStartDate());
		buffer.append(HdqsConstants.SPLIT_CHAR).append(record.getEndDate());
		buffer.append(HdqsConstants.SPLIT_CHAR).append(record.getChaxlx());
		buffer.append(HdqsConstants.SPLIT_CHAR).append(record.getKhzhlx());
		buffer.append(HdqsConstants.SPLIT_CHAR).append(record.getZhhuxz());
		buffer.append(HdqsConstants.SPLIT_CHAR).append(record.getHuobdh());
		buffer.append(HdqsConstants.SPLIT_CHAR).append(record.getChuibz());
		buffer.append(HdqsConstants.SPLIT_CHAR).append(record.getKehuzh());
		return buffer.toString();
	}

	private void validateField(PybjyEO record) throws HdqsWtcException {
		if (HdqsUtils.isBlank(record.getStartDate())) {
			throw new HdqsWtcException("起始日期不能为空");
		}
		if (HdqsUtils.isBlank(record.getEndDate())) {
			throw new HdqsWtcException("终止日期不能为空");
		}
		if (record.getStartDate().compareTo(record.getEndDate()) > 0) {
			throw new HdqsWtcException("开始时间不能大于结束时间");
		}
		ConfigLoader.fitTimeLimit(record.getEndDate(), record.getStartDate());
		if (HdqsUtils.isBlank(record.getChaxlx())) {
			throw new HdqsWtcException("查询类型不能为空");
		}
		if (HdqsUtils.isBlank(record.getKhzhlx())) {
			throw new HdqsWtcException("客户帐号类型不能为空");
		}
		if (HdqsUtils.isBlank(record.getHuobdh())) {
			throw new HdqsWtcException("货币代号不能为空");
		}
		if (HdqsUtils.isBlank(record.getChuibz())) {
			throw new HdqsWtcException("钞汇标志不能为空");
		}
		if (HdqsUtils.isBlank(record.getZhhuxz())) {
			throw new HdqsWtcException("账户性质不能为空");
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
	private Reply getTPServiceInfoAsResult(TPServiceInformation tpServiceInfor, PybjyEO record, SopIntf sendSop, List<Item0771> itemList,
			C0771RowkeyItem item) {
		byte[] sendData = new byte[200 + 148 + 110 + itemList.size() * 396];
		String[] inmap = new String[] { this.getFileSent(), this.getFilePrint() };
		setNormalMessageForSop(sendSop, record, itemList, item);
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
	private void setNormalMessageForSop(SopIntf sendSop, PybjyEO record, List<Item0771> itemList, C0771RowkeyItem item) {
		sendSop.put(null, "JIAOYM", this.getExCode());
		long currentT = System.currentTimeMillis();
		sendSop.put(null, "JIOYRQ", DateTimeFormatUtils.formatDate(currentT, "yyyymmdd"));
		sendSop.put(null, "JIOYSJ", Integer.valueOf(DateTimeFormatUtils.formatDate(currentT, "HHmmss")));
		sendSop.put(null, "GUIYLS", record.getGuiyls());
		Short FDCWJYXH = 0;
		sendSop.put(null, "CWJYXH", FDCWJYXH);
		sendSop.put(null, SopIntf.TPU_RETCODE, SopIntf.SOP_SUCC);
		sendSop.put(getFileSent(), "GERZWM", item.getGerzwm());
		sendSop.put(getFileSent(), "KEHHAO", item.getKehhao());
		sendSop.put(getFileSent(), "BISHUU", item.getBishuu() + "");
		sendSop.put(getFileSent(), "KEHUZH", record.getKehuzh());
		Map<String, List<String>> map = convertResultToMap(itemList);
		sendSop.put(getFileSent(), getFileTable(), "ZHANGH", getStrArrayFromList(map.get("0")));
		sendSop.put(getFileSent(), getFileTable(), "JIOYRQ", getStrArrayFromList(map.get("1")));
		sendSop.put(getFileSent(), getFileTable(), "JIOYSJ", getStrArrayFromList(map.get("2")));
		sendSop.put(getFileSent(), getFileTable(), "JIO1JE", getStrArrayFromList(map.get("3")));
		sendSop.put(getFileSent(), getFileTable(), "ZHHUYE", getStrArrayFromList(map.get("4")));
		sendSop.put(getFileSent(), getFileTable(), "JIAOYM", getStrArrayFromList(map.get("5")));
		sendSop.put(getFileSent(), getFileTable(), "JIEDBZ", getStrArrayFromList(map.get("6")));
		sendSop.put(getFileSent(), getFileTable(), "JIO1GY", getStrArrayFromList(map.get("7")));
		sendSop.put(getFileSent(), getFileTable(), "SHOQGY", getStrArrayFromList(map.get("8")));
		sendSop.put(getFileSent(), getFileTable(), "CHBUBZ", getStrArrayFromList(map.get("9")));
		sendSop.put(getFileSent(), getFileTable(), "GUIYLS", getStrArrayFromList(map.get("10")));
		sendSop.put(getFileSent(), getFileTable(), "ZHYODM", getStrArrayFromList(map.get("11")));
		sendSop.put(getFileSent(), getFileTable(), "YNGYJG", getStrArrayFromList(map.get("12")));
		sendSop.put(getFileSent(), getFileTable(), "KHZHLX", getStrArrayFromList(map.get("13")));
		sendSop.put(getFileSent(), getFileTable(), "PNGZHH", getStrArrayFromList(map.get("14")));
		sendSop.put(getFileSent(), getFileTable(), "DUIFZH", getStrArrayFromList(map.get("15")));
		sendSop.put(getFileSent(), getFileTable(), "DUIFMC", getStrArrayFromList(map.get("16")));
		sendSop.put(getFileSent(), getFileTable(), "DANWMC", getStrArrayFromList(map.get("17")));
		if (!record.getShfobz().equals(HdqsConstants.SHFOBZ_NONE)) {
			String filePath = getFilePathForSyn(record, item);
			setPrintFilePath(sendSop, filePath != null ? filePath : "");
			// setFileDownLoadDir(sendSop);
		}
	}

	protected String asyGenerateFile(PybjyEO record, String filePath) {
		if (StringUtils.isBlank(filePath) || !new File(filePath).exists()) {
			getLogger().info("获取打印文件路径");

			Output0771 outputExcutor = new Output0771();
			try {
				filePath = outputExcutor.execute(record);
			} catch (Exception e) {
				getLogger().error(e.getMessage());
			}
		}

		return filePath;
	}

	/**
	 * 将遍历结果对象转换map对象
	 * 
	 * @param iter
	 * @return
	 */
	private Map<String, List<String>> convertResultToMap(List<Item0771> itemList) {
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		for (int i = 0; i < this.getFieldCount(); i++) {
			map.put("" + i, new ArrayList<String>());
		}

		for (Item0771 item : itemList) {
			map.get("0").add(item.getZhangh());
			map.get("1").add(item.getJioyrq());
			map.get("2").add(item.getJioysj());
			map.get("3").add(formatCurrency(item.getJio1je()));
			map.get("4").add(formatCurrency(item.getZhhuye()));
			map.get("5").add(item.getJiaoym());
			map.get("6").add(item.getJiedbz());
			map.get("7").add(item.getJio1gy());
			map.get("8").add(item.getShoqgy());
			map.get("9").add(item.getChbubz());
			map.get("10").add(item.getGuiyls());
			map.get("11").add(item.getZhyodm());
			map.get("12").add(item.getYngyjg());
			map.get("13").add(item.getKhzhlx());
			map.get("14").add(item.getPngzhh());
			map.get("15").add(item.getDuifzh());
			map.get("16").add(item.getDuifmc());
			map.get("17").add(item.getDanwmc());
		}
		return map;
	}
}