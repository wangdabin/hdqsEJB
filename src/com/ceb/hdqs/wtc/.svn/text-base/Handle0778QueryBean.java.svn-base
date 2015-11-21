package com.ceb.hdqs.wtc;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import weblogic.wtc.jatmi.Reply;
import weblogic.wtc.jatmi.TPException;
import weblogic.wtc.jatmi.TPServiceInformation;
import weblogic.wtc.jatmi.TypedCArray;

import com.ceb.hdqs.config.ConfigLoader;
import com.ceb.hdqs.constants.HdqsConstants;
import com.ceb.hdqs.entity.PjyjlEO;
import com.ceb.hdqs.entity.PybjyEO;
import com.ceb.hdqs.po.Authorize;
import com.ceb.hdqs.sop.SopIntf;
import com.ceb.hdqs.utils.HdqsUtils;
import com.ceb.hdqs.utils.TimerUtils;
import com.ceb.hdqs.wtc.file.AsynPrivateQuery;
import com.ceb.hdqs.wtc.form.Handle0778Form;

public class Handle0778QueryBean extends AbstractQueryBean {
	public Handle0778QueryBean() {
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

			Handle0778Form form = buildForm(aRecord);
			printRecvFormPkt(form, aRecord);
			splitRecord(list, aRecord);

		} else {
			File recvFile = getFilePath(aRecord.getRecvFile());
			if (!recvFile.exists()) {
				getLogger().info(recvFile.getAbsolutePath() + "文件不存在");
				return getExceptionBuffer(tpServiceInfor, sendSop, "查询文件不存在");
			}

			AsynPrivateQuery cQuery = new AsynPrivateQuery();
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

	private Handle0778Form buildForm(PybjyEO record) {
		Handle0778Form form = new Handle0778Form();
		form.setChaxlx(record.getChaxlx());
		form.setChaxzl(record.getChaxzl());
		form.setKhzhlx(record.getKhzhlx());
		form.setKehuzh(record.getKehuzh());
		form.setKehuhao(record.getKehuhao());
		form.setZhjnzl(record.getZhjnzl());
		form.setZhjhao(record.getZhjhao());
		form.setKhzwm(record.getKhzwm());
		form.setZhaoxz(record.getZhaoxz());
		form.setHuobdh(record.getHuobdh());
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
		if (record.getChaxzl().equals(HdqsConstants.CHAXZL_KEHUZH)) {
			if (HdqsUtils.isBlank(record.getKhzhlx()) || HdqsUtils.isBlank(record.getKehuzh())) {
				throw new HdqsWtcException("客户账号类型或客户账号不能为空");
			}
			int length = record.getKehuzh().length();
			if (length < 16 || length > 21) {
				throw new HdqsWtcException("客户账号超出长度限制");
			}
		} else if (record.getChaxzl().equals(HdqsConstants.CHAXZL_KEHUHAO)) {
			if (HdqsUtils.isBlank(record.getKehuhao())) {
				throw new HdqsWtcException("客户号不能为空");
			}
			int length = record.getKehuhao().length();
			if (length > 10) {
				throw new HdqsWtcException("客户号超出长度限制");
			}
		} else if (record.getChaxzl().equals(HdqsConstants.CHAXZL_ZHJNZL)) {
			if ((HdqsUtils.isBlank(record.getZhjnzl()) || HdqsUtils.isBlank(record.getZhjhao()))) {
				throw new HdqsWtcException("证件种类或证件号码不能为空");
			}

		}
		if (HdqsUtils.isBlank(record.getZhaoxz())) {
			throw new HdqsWtcException("账号性质不能为空");
		}
		if (HdqsUtils.isBlank(record.getHuobdh())) {
			throw new HdqsWtcException("查询币种不能为空");
		}
	}
}