package com.ceb.hdqs.wtc;

import org.apache.log4j.Logger;

import com.ceb.hdqs.constants.HdqsConstants;
import com.ceb.hdqs.entity.PjyjlEO;
import com.ceb.hdqs.utils.HdqsUtils;
import com.ceb.hdqs.wtc.form.Handle0779Form;

public class Handle0779QueryBean extends AbstractFetchBean {
	public Handle0779QueryBean() {
		setLogger(Logger.getLogger(getClass()));
	}

	protected String buildKey(Handle0779Form form, PjyjlEO record) {
		StringBuffer buffer = new StringBuffer(getExCode());
		buffer.append(HdqsConstants.SPLIT_CHAR).append(record.getJio1gy());
		buffer.append(HdqsConstants.SPLIT_CHAR).append(form.getSlbhao());
		buffer.append(HdqsConstants.SPLIT_CHAR).append(form.getFileType());
		return buffer.toString();
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
		if (HdqsUtils.isBlank(form.getFileType())) {
			throw new HdqsWtcException("文件类型不能为空");
		}
		int length = form.getSlbhao().length();
		if (length != 18) {
			throw new HdqsWtcException("受理编码长度不符合要求");
		}
		if (!HdqsUtils.isNumeric(form.getFileType())) {
			throw new HdqsWtcException("文件类型必须为数字");
		}
	}
}