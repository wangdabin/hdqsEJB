package com.ceb.hdqs.wtc;

import org.apache.log4j.Logger;

import com.ceb.hdqs.constants.HdqsConstants;
import com.ceb.hdqs.entity.PjyjlEO;
import com.ceb.hdqs.utils.HdqsUtils;
import com.ceb.hdqs.wtc.form.Handle0779Form;

public class Handle0776QueryBean extends AbstractFetchBean {
	public Handle0776QueryBean() {
		setLogger(Logger.getLogger(getClass()));
	}

	protected String buildKey(Handle0779Form form, PjyjlEO record) {
		StringBuffer buffer = new StringBuffer(getExCode());
		buffer.append(HdqsConstants.SPLIT_CHAR).append(record.getJio1gy());
		buffer.append(HdqsConstants.SPLIT_CHAR).append(form.getSlbhao());
		return buffer.toString();
	}

	/**
	 * 记录输入条件
	 * 
	 * @param record
	 */
	protected void printRecvFormPkt(Handle0779Form form, PjyjlEO record) {
		StringBuffer buffer = new StringBuffer("\r\n");
		buffer.append("受理编号:" + form.getSlbhao());
		buffer.append(",起始笔数:" + form.getQishbs());
		buffer.append(",查询笔数:" + form.getCxunbs());
		buffer.append(",校验标识:" + record.getJioybz());
		buffer.append(",授权柜员:" + record.getShoqgy());
		buffer.append(",交易柜员:" + record.getJio1gy());
		buffer.append(",交易机构:" + record.getYngyjg());
		buffer.append(",渠道:" + record.getQudaoo());
		getLogger().info(buffer.toString());
	}

	/**
	 * 对输入值进行验证
	 * 
	 * @param sendSop
	 * @param form
	 * @return
	 */
	protected void validateField(Handle0779Form form) throws HdqsWtcException {
		if (HdqsUtils.isBlank(form.getSlbhao())) {
			throw new HdqsWtcException("受理编号不能为空");
		}
		int length = form.getSlbhao().length();
		if (length != 18) {
			throw new HdqsWtcException("受理编码长度不符合要求");
		}

	}
}