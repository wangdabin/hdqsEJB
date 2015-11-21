package com.ceb.hdqs.wtc.form;

public class Handle0778Form extends AbstractForm {
	private static final long serialVersionUID = -7389664624864270676L;
	private String chaxlx;
	private String chaxzl;
	private String khzhlx;
	private String kehuzh;
	private String kehuhao;
	private String zhjnzl;
	private String zhjhao;
	private String khzwm;
	private String zhaoxz;
	private String huobdh;
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

	public String getKehuhao() {
		return kehuhao;
	}

	public void setKehuhao(String kehuhao) {
		this.kehuhao = kehuhao;
	}

	public String getZhjnzl() {
		return zhjnzl;
	}

	public void setZhjnzl(String zhjnzl) {
		this.zhjnzl = zhjnzl;
	}

	public String getZhjhao() {
		return zhjhao;
	}

	public void setZhjhao(String zhjhao) {
		this.zhjhao = zhjhao;
	}

	public String getKhzwm() {
		return khzwm;
	}

	public void setKhzwm(String khzwm) {
		this.khzwm = khzwm;
	}

	public String getZhaoxz() {
		return zhaoxz;
	}

	public void setZhaoxz(String zhaoxz) {
		this.zhaoxz = zhaoxz;
	}

	public String getHuobdh() {
		return huobdh;
	}

	public void setHuobdh(String huobdh) {
		this.huobdh = huobdh;
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
		StringBuilder buffer = new StringBuilder("**********0778***********\r\n");
		buffer.append("起始日期:" + getQishrq());
		buffer.append(",终止日期:" + getZzhirq());
		buffer.append(",查询类型:" + getChaxlx());
		buffer.append(",查询条件种类:" + getChaxzl());
		buffer.append(",客户账号类型:" + getKhzhlx());
		buffer.append(",客户账号:" + getKehuzh());
		buffer.append(",客户号:" + getKehuhao());
		buffer.append(",证件类型:" + getZhjnzl());
		buffer.append(",证件号码:" + getZhjhao());
		buffer.append(",客户中文名:" + getKhzwm());
		buffer.append(",账号性质:" + getZhaoxz());
		buffer.append(",查询币种:" + getHuobdh());

		return buffer.toString();
	}
}
