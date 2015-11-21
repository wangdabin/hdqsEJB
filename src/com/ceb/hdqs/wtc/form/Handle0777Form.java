package com.ceb.hdqs.wtc.form;

public class Handle0777Form extends AbstractForm {
	private static final long serialVersionUID = -8764105096988174100L;
	private String chaxlx;
	private String chaxzl;
	private String zhangh;
	private String kehuzh;
	private String kehuhao;
	private String qishrq;
	private String zzhirq;

	public String getChaxlx() {
		return chaxlx;
	}

	public void setChaxlx(String chaxlx) {
		this.chaxlx = chaxlx;
	}

	public String getChaxzl() {
		return chaxzl;
	}

	public void setChaxzl(String chaxzl) {
		this.chaxzl = chaxzl;
	}

	public String getZhangh() {
		return zhangh;
	}

	public void setZhangh(String zhangh) {
		this.zhangh = zhangh;
	}

	public String getKehuzh() {
		return kehuzh;
	}

	public void setKehuzh(String kehuzh) {
		this.kehuzh = kehuzh;
	}

	public String getKehuhao() {
		return kehuhao;
	}

	public void setKehuhao(String kehuhao) {
		this.kehuhao = kehuhao;
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

	public String toLog() {
		StringBuilder buffer = new StringBuilder("**********0777***********\r\n");
		buffer.append("起始日期:" + getQishrq());
		buffer.append(",终止日期:" + getZzhirq());
		buffer.append(",查询类型:" + getChaxlx());
		buffer.append(",查询条件种类:" + getChaxzl());
		buffer.append(",帐号:" + getZhangh());
		buffer.append(",一号通号:" + getKehuzh());
		buffer.append(",客户号:" + getKehuhao());

		return buffer.toString();
	}
}
