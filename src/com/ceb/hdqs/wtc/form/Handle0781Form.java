package com.ceb.hdqs.wtc.form;

public class Handle0781Form extends AbstractForm {
	private static final long serialVersionUID = -6144068241000526988L;
	private String chaxlx;// 1 S 0 0 0 0 NULL 0 //查询类型：0-客户；1-非客户
	private String shfobz;// 1 S 0 0 0 0 NULL 0 //是否打印：0-两者都不；1-文件打印；2-文件导出
	private String kaaadx;// 1 S 0 0 0 0 NULL 0 //卡对象类型 1-单位卡
	private String kehuzh;// 21 S 0 0 0 0 NULL 0 //客户账号
	private String zhaoxz;// 1 S 0 0 0 0 NULL 0 //账号性质 0活期,2定期,3全部
	private String huobdh;// 2 S 0 0 0 0 NULL 0 //货币代号
	private String chuibz;// 1 S 0 0 0 0 NULL 0 //钞汇标志：0-钞户；1-汇户；2-钞户和汇户
	private String shunxh;// 4 S 0 0 0 0 NULL 0 //客户帐号子序号
	private String qishrq;// 8 S 0 0 0 0 NULL 0 //起始日期
	private String zzhirq;// 8 S 0 0 0 0 NULL 0 //终止日期

	public String getChaxlx() {
		return chaxlx;
	}

	public void setChaxlx(String chaxlx) {
		this.chaxlx = chaxlx;
	}

	public String getShfobz() {
		return shfobz;
	}

	public void setShfobz(String shfobz) {
		this.shfobz = shfobz;
	}

	public String getKaaadx() {
		return kaaadx;
	}

	public void setKaaadx(String kaaadx) {
		this.kaaadx = kaaadx;
	}

	public String getKehuzh() {
		return kehuzh;
	}

	public void setKehuzh(String kehuzh) {
		this.kehuzh = kehuzh;
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

	public String getChuibz() {
		return chuibz;
	}

	public void setChuibz(String chuibz) {
		this.chuibz = chuibz;
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

	@Override
	public String toLog() {
		StringBuilder buffer = new StringBuilder("**********0781***********\r\n");
		buffer.append("查询类型:" + this.getChaxlx());
		buffer.append(",是否打印:" + this.getShfobz());
		buffer.append(",卡对象类型:" + this.getKaaadx());
		buffer.append(",客户账号:" + this.getKehuzh());
		buffer.append(",账号性质:" + this.getZhaoxz());
		buffer.append(",货币代号:" + this.getHuobdh());
		buffer.append(",钞汇标志:" + this.getChuibz());
		buffer.append(",客户帐号子序号:" + this.getShunxh());
		buffer.append(",起始日期:" + this.getQishrq());
		buffer.append(",终止日期:" + this.getZzhirq());

		return buffer.toString();
	}
}