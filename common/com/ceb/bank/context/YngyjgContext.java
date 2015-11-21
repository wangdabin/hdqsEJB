package com.ceb.bank.context;

import java.io.Serializable;

public class YngyjgContext implements Serializable {
	private static final long serialVersionUID = -1117138739249645936L;

	private String yngyjg;// 营业机构号,如果是卡时,取VYKTD中的值,否则使用AZHJX中的值
	private String jigomc;// 机构中文名称

	/**
	 * 营业机构号
	 * 
	 * @return
	 */
	public String getYngyjg() {
		return yngyjg;
	}

	/**
	 * 营业机构号
	 * 
	 * @param yngyjg
	 */
	public void setYngyjg(String yngyjg) {
		this.yngyjg = yngyjg;
	}

	/**
	 * 机构中文名称
	 * 
	 * @return
	 */
	public String getJigomc() {
		return jigomc;
	}

	/**
	 * 机构中文名称
	 * 
	 * @param jigomc
	 */
	public void setJigomc(String jigomc) {
		this.jigomc = jigomc;
	}
}