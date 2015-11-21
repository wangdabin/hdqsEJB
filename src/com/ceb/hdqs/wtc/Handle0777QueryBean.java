package com.ceb.hdqs.wtc;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import weblogic.wtc.jatmi.Reply;
import weblogic.wtc.jatmi.TPException;
import weblogic.wtc.jatmi.TPServiceInformation;
import weblogic.wtc.jatmi.TypedCArray;

import com.ceb.hdqs.action.asyn.AsynQuery0777;
import com.ceb.hdqs.action.query.exception.ConditionNotExistException;
import com.ceb.hdqs.action.query.exception.NoAuthorityException;
import com.ceb.hdqs.config.ConfigLoader;
import com.ceb.hdqs.constants.HdqsConstants;
import com.ceb.hdqs.entity.PjyjlEO;
import com.ceb.hdqs.entity.PybjyEO;
import com.ceb.hdqs.po.Authorize;
import com.ceb.hdqs.sop.SopIntf;
import com.ceb.hdqs.utils.HdqsUtils;
import com.ceb.hdqs.utils.TimerUtils;
import com.ceb.hdqs.wtc.file.AsynCorporateQuery;
import com.ceb.hdqs.wtc.form.Handle0777Form;

public class Handle0777QueryBean extends AbstractQueryBean {

	public Handle0777QueryBean() {
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

		byte[] sendData = new byte[1024];
		String[] inmap = new String[] { this.getFileSent() };

		PybjyEO aRecord = buildAsyQueryRecord(recvSop);
		PjyjlEO eRecord = buildExchangeRecord(aRecord);

		isZonghangAuthorize(aRecord);
		Authorize authorize = isNeedAsynAuth(aRecord);
		if (isFrontAuthorize(aRecord) || authorize.isNeedAuth()) {
			getLogger().info(authorize.getPrintMsg());
			return getAuthorizeBuffer(tpServiceInfor, sendSop, authorize.getPrintMsg());
		}
		List<PybjyEO> list = new ArrayList<PybjyEO>();
		if (!aRecord.getFileQuery()) { 
			try {
				validateField(aRecord);
			} catch (HdqsWtcException e) {
				getLogger().info(this.getExCode() + " sop invalid: " + e.getMessage());
				return getExceptionBuffer(tpServiceInfor, sendSop, e.getMessage());
			}

			Handle0777Form form = buildForm(aRecord);
			printRecvFormPkt(form, aRecord);
			eRecord.setQueryStr(form.toString());
			splitRecord(list, aRecord);
			AsynQuery0777 aQuery = new AsynQuery0777(list);
			try {
				aQuery.checkAuthorize();
			} catch (NoAuthorityException e) {
				getLogger().info(e.getMessage());
				return getExceptionBuffer(tpServiceInfor, sendSop, e.getMessage());
			}catch (ConditionNotExistException e) {
				getLogger().info(e.getMessage());
				return getExceptionBuffer(tpServiceInfor, sendSop, e.getMessage());
			}
			
		} else {
			File recvFile = getFilePath(aRecord.getRecvFile());
			if (!recvFile.exists()) {
				getLogger().info(recvFile.getAbsolutePath() + "文件不存在");
				return getExceptionBuffer(tpServiceInfor, sendSop, "查询文件不存在");
			}

			AsynCorporateQuery cQuery = new AsynCorporateQuery();
			try {
				list = cQuery.getRecordList(recvFile, aRecord);
			} catch (HdqsWtcException e) {
				getLogger().info("查询文件无效:" + e.getMessage());
				return getExceptionBuffer(tpServiceInfor, sendSop, e.getMessage());
			}

			if (list.isEmpty()) {
				TypedCArray replyBuffer = getTypedCArray(sendData, inmap, sendSop);
				tpServiceInfor.setReplyBuffer(replyBuffer);
				getLogger().info(getExCode() + " query condition is empty.");
				return tpServiceInfor;
			}
			eRecord.setQueryStr(recvFile.getAbsolutePath());
			eRecord.setFileQuery(Boolean.TRUE);
		}
		try {
			saveExchangeRecord(eRecord);
			this.getYbjyService().batchAddRecords(list);
		} catch (Exception e) {
			getLogger().info("保存记录异常:", e);
			return getExceptionBuffer(tpServiceInfor, sendSop, "保存记录异常");
		}
		getLogger().info("Start query:" + getExCode());
		TimerUtils timer = new TimerUtils();
		timer.start();

		getAsynTPServiceInfoAsResult(sendSop, list.get(0));

		TypedCArray replyBuffer = getTypedCArray(sendData, inmap, sendSop);
		tpServiceInfor.setReplyBuffer(replyBuffer);
		timer.stop();
		getLogger().info(getExCode() + " query complete, Cost time(ms)=" + timer.getExecutionTime());
		return tpServiceInfor;
	}

	private Handle0777Form buildForm(PybjyEO record) {
		Handle0777Form form = new Handle0777Form();
		form.setChaxlx(record.getChaxlx());
		form.setChaxzl(record.getChaxzl());
		form.setZhangh(record.getZhangh());
		form.setKehuzh(record.getKehuzh());
		form.setKehuhao(record.getKehuhao());
		form.setQishrq(record.getStartDate());
		form.setZzhirq(record.getEndDate());
		return form;
	}

	/**
	 * 对输入值进行验证
	 * 
	 * @param record
	 * @return
	 */
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
		if (HdqsUtils.isBlank(record.getChaxzl())) {
			throw new HdqsWtcException("查询条件种类不能为空");
		}
		if (record.getChaxzl().equals(HdqsConstants.CHAXZL_ZHANGH)) {
			if (HdqsUtils.isBlank(record.getZhangh())) {
				throw new HdqsWtcException("账号不能为空");
			}
			int length = record.getZhangh().length();
			if (length < 16 || length > 21) {
				throw new HdqsWtcException("账号超出长度限制");
			}
		} else if (record.getChaxzl().equals(HdqsConstants.CHAXZL_KEHUZH)) {
			if (HdqsUtils.isBlank(record.getKehuzh())) {
				throw new HdqsWtcException("一号通号不能为空");
			}
			int length = record.getKehuzh().length();
			if (length < 16 || length > 21) {
				throw new HdqsWtcException("一号通号超出长度限制");
			}
		} else if (record.getChaxzl().equals(HdqsConstants.CHAXZL_KEHUHAO)) {
			if (HdqsUtils.isBlank(record.getKehuhao())) {
				throw new HdqsWtcException("客户号不能为空");
			}
			int length = record.getKehuhao().length();
			if (length > 10) {
				throw new HdqsWtcException("客户号超出长度限制");
			}
		}
	}

	public static void main(String[] args) {
		File recvFile = new File("D:\\src\\1.txt");
		File bakDir = new File("D:\\bak1");
		try {
			FileUtils.moveFileToDirectory(recvFile, bakDir, true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}