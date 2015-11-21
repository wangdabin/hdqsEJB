package com.ceb.hdqs.wtc.form;

import java.io.Serializable;

import com.ceb.hdqs.entity.PybjyEO;
import com.ceb.hdqs.entity.PjyjlEO;
import com.ceb.hdqs.utils.GsonUtils;

public abstract class AbstractForm implements Serializable {
	private static final long serialVersionUID = -4376886459527345886L;
	private int qishbs = 1;// 起始笔数
	private int cxunbs = 20;// 查询笔数

	public int getQishbs() {
		return qishbs;
	}

	public void setQishbs(int qishbs) {
		this.qishbs = qishbs;
	}

	public int getCxunbs() {
		return cxunbs;
	}

	public void setCxunbs(int cxunbs) {
		this.cxunbs = cxunbs;
	}

	public abstract String toLog();

	public String toLog(PybjyEO record) {
		StringBuilder buffer = new StringBuilder(toLog());
		buffer.append(",校验标识:" + record.getJioybz());
		buffer.append(",授权柜员:" + record.getShoqgy());
		buffer.append(",交易柜员:" + record.getJio1gy());
		buffer.append(",交易机构:" + record.getYngyjg());
		buffer.append(",渠道:" + record.getQudaoo());

		return buffer.toString();
	}

	public String toLog(PjyjlEO record) {
		StringBuilder buffer = new StringBuilder(toLog());
		buffer.append(",校验标识:" + record.getJioybz());
		buffer.append(",授权柜员:" + record.getShoqgy());
		buffer.append(",交易柜员:" + record.getJio1gy());
		buffer.append(",交易机构:" + record.getYngyjg());
		buffer.append(",渠道:" + record.getQudaoo());

		return buffer.toString();
	}

	public String toString() {
		return GsonUtils.fromJson(this);
	}
}