package com.ceb.hdqs.wtc.form;

public class Handle0772Form extends AbstractForm {
	private static final long serialVersionUID = -2179410402483237423L;
	private String chaxlx;
	private String khzhlx;
	private String kehuzh;
	private String shunxh;
	private String qishrq;
	private String zzhirq;
	private String shfobz;

	public String getChaxlx() {
		return chaxlx;
	}

	public void setChaxlx(String chaxlx) {
		this.chaxlx = chaxlx;
	}

	public String getKhzhlx() {
		return khzhlx;
	}

	public void setKhzhlx(String khzhlx) {
		this.khzhlx = khzhlx;
	}

	public String getKehuzh() {
		return kehuzh;
	}

	public void setKehuzh(String kehuzh) {
		this.kehuzh = kehuzh;
	}

	public String getShunxh() {
		return shunxh;
	}

	public void setShunxh(String shunxh) {
		this.shunxh = shunxh;
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
		StringBuilder buffer = new StringBuilder("**********0772***********\r\n");
		buffer.append("起始日期:" + getQishrq());
		buffer.append(",终止日期:" + getZzhirq());
		buffer.append(",查询类型:" + getChaxlx());
		buffer.append(",客户帐号类型:" + getKhzhlx());
		buffer.append(",客户帐号:" + getKehuzh());
		buffer.append(",顺序号:" + getShunxh());
		buffer.append(",起始笔数:" + getQishbs());
		buffer.append(",查询笔数:" + getCxunbs());
		buffer.append(",是否标志:" + getShfobz());

		return buffer.toString();
	}
}
