package com.ceb.bank.item;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * 提供账号明细字段,0770交易显示字段
 */
public class Item0770 extends AbstractItem {
	private static final long serialVersionUID = 2511075032919468054L;

	private String jiaoym;
	private String yngyjg;
	private String zhyodm;
	private String pngzhh;
	private String pngzlx;
	private String kehuzh;
	private String jio1gy;
	private String shoqgy;
	private String jioysj;
	private String chbubz;// 冲补标志
	private String zhujrq;//
	private String guiyls;
	private String duifzh;
	private String duifmc;
	private String xnzhbz;// 现转标志

	public String getJiaoym() {
		return jiaoym;
	}

	public void setJiaoym(String jiaoym) {
		this.jiaoym = jiaoym;
	}

	public String getYngyjg() {
		return yngyjg;
	}

	public void setYngyjg(String yngyjg) {
		this.yngyjg = yngyjg;
	}

	public String getZhyodm() {
		return zhyodm;
	}

	public void setZhyodm(String zhyodm) {
		this.zhyodm = zhyodm;
	}

	public String getPngzhh() {
		return pngzhh;
	}

	public void setPngzhh(String pngzhh) {
		this.pngzhh = pngzhh;
		if (StringUtils.isNotBlank(pngzhh) && pngzhh.length() >= 3) {
			setPngzlx(pngzhh.substring(0, 3));
		} else {
			setPngzlx("");
		}
	}

	public String getPngzlx() {
		return pngzlx;
	}

	public void setPngzlx(String pngzlx) {
		this.pngzlx = pngzlx;
	}

	public String getKehuzh() {
		return kehuzh;
	}

	public void setKehuzh(String kehuzh) {
		this.kehuzh = kehuzh;
	}

	public String getJio1gy() {
		return jio1gy;
	}

	public void setJio1gy(String jio1gy) {
		this.jio1gy = jio1gy;
	}

	public String getShoqgy() {
		return shoqgy;
	}

	public void setShoqgy(String shoqgy) {
		this.shoqgy = shoqgy;
	}

	public String getJioysj() {
		return jioysj;
	}

	public void setJioysj(String jioysj) {
		this.jioysj = jioysj;
	}

	/**
	 * 冲补标志
	 * 
	 * @return
	 */
	public String getChbubz() {
		return chbubz;
	}

	/**
	 * 冲补标志
	 * 
	 * @param chbubz
	 */
	public void setChbubz(String chbubz) {
		this.chbubz = chbubz;
	}

	public String getZhujrq() {
		return zhujrq;
	}

	public void setZhujrq(String zhujrq) {
		this.zhujrq = zhujrq;
	}

	public String getGuiyls() {
		return guiyls;
	}

	public void setGuiyls(String guiyls) {
		this.guiyls = guiyls;
	}

	public String getDuifzh() {
		return duifzh;
	}

	public void setDuifzh(String duifzh) {
		this.duifzh = duifzh;
	}

	public String getDuifmc() {
		return duifmc;
	}

	public void setDuifmc(String duifmc) {
		this.duifmc = duifmc;
	}

	/**
	 * 现转标志
	 * 
	 * @return
	 */
	public String getXnzhbz() {
		return xnzhbz;
	}

	/**
	 * 现转标志
	 * 
	 * @param xnzhbz
	 */
	public void setXnzhbz(String xnzhbz) {
		this.xnzhbz = xnzhbz;
	}
}