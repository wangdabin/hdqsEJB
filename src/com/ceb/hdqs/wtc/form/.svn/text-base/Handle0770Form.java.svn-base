package com.ceb.hdqs.wtc.form;

public class Handle0770Form extends AbstractForm {
	private static final long serialVersionUID = 7079438801582796734L;
	private String zhangh;
	private String qishrq;
	private String zzhirq;

	private String shfobz;

	public String getZhangh() {
		return zhangh;
	}

	public void setZhangh(String zhangh) {
		this.zhangh = zhangh;
	}

	public String getQishrq() {
		return qishrq;
	}

	public void setQishrq(String qishrq) {
		this.qishrq = qishrq;
	}

	public String getZzhirq() {
		return zzhirq;
	}

	public void setZzhirq(String zzhirq) {
		this.zzhirq = zzhirq;
	}

	public String getShfobz() {
		return shfobz;
	}

	public void setShfobz(String shfobz) {
		this.shfobz = shfobz;
	}

	public String toLog() {
		StringBuilder buffer = new StringBuilder("**********0770***********\r\n");
		buffer.append("起始日期:" + getQishrq());
		buffer.append(",终止日期:" + getZzhirq());
		buffer.append(",帐号:" + getZhangh());
		buffer.append(",起始笔数:" + getQishbs());
		buffer.append(",查询笔数:" + getCxunbs());
		buffer.append(",是否标志:" + getShfobz());

		return buffer.toString();
	}
}
