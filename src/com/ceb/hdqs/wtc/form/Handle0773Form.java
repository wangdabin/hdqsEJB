package com.ceb.hdqs.wtc.form;

public class Handle0773Form extends AbstractForm {
	private static final long serialVersionUID = 5821745069337727023L;
	private String chaxlx;
	private String kehuzh;
	private String shfobz;

	public String getChaxlx() {
		return chaxlx;
	}

	public void setChaxlx(String chaxlx) {
		this.chaxlx = chaxlx;
	}

	public String getKehuzh() {
		return kehuzh;
	}

	public void setKehuzh(String kehuzh) {
		this.kehuzh = kehuzh;
	}

	public String getShfobz() {
		return shfobz;
	}

	public void setShfobz(String shfobz) {
		this.shfobz = shfobz;
	}

	public String toLog() {
		StringBuilder buffer = new StringBuilder("**********0773***********\r\n");
		buffer.append("查询类型:" + getChaxlx());
		buffer.append(",客户帐号:" + getKehuzh());
		buffer.append(",起始笔数:" + getQishbs());
		buffer.append(",查询笔数:" + getCxunbs());
		buffer.append(",是否标志:" + getShfobz());

		return buffer.toString();
	}
}
