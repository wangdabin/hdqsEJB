package com.ceb.hdqs.wtc.form;

public class Handle0775Form extends AbstractForm {
	private static final long serialVersionUID = 5794105879410858730L;
	/**
	 * 查询范围,0:指定机构
	 */
	public static final Integer QYRAGE_SPECIAL = 0;
	/**
	 * 查询范围,1:辖内
	 */
	public static final Integer QYRAGE_MANAGER = 1;

	private String qishrq;// 起始日期
	private String zzhirq;// 终止日期
	private String jio1gy;// 表单柜员
	private Integer qyrage;// 查询范围，0:指定机构，1:辖内
	private String yngyjg;// 表单机构
	private String jiaoym;// 交易码
	private String guiyls;// 柜员流水

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

	public String getJio1gy() {
		return jio1gy;
	}

	public void setJio1gy(String jio1gy) {
		this.jio1gy = jio1gy;
	}

	public Integer getQyrage() {
		return qyrage;
	}

	public void setQyrage(Integer qyrage) {
		this.qyrage = qyrage;
	}

	public String getYngyjg() {
		return yngyjg;
	}

	public void setYngyjg(String yngyjg) {
		this.yngyjg = yngyjg;
	}

	public String getJiaoym() {
		return jiaoym;
	}

	public void setJiaoym(String jiaoym) {
		this.jiaoym = jiaoym;
	}

	public String getGuiyls() {
		return guiyls;
	}

	public void setGuiyls(String guiyls) {
		this.guiyls = guiyls;
	}

	public String toLog() {
		StringBuilder buffer = new StringBuilder("**********0775***********\r\n");
		buffer.append("起始日期:" + getQishrq());
		buffer.append(",终止日期:" + getZzhirq());
		buffer.append(",表单柜员:" + getJio1gy());
		buffer.append(",查询范围(0:指定机构,1:辖内):" + getQyrage());
		buffer.append(",表单机构:" + getYngyjg());
		buffer.append(",交易码:" + getJiaoym());
		buffer.append(",柜员流水:" + getGuiyls());
		buffer.append(",起始笔数:" + getQishbs());
		buffer.append(",查询笔数:" + getCxunbs());

		return buffer.toString();
	}
}