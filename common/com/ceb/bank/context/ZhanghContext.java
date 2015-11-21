package com.ceb.bank.context;

import java.io.Serializable;

/**
 * 账号上下文,查询出客户账号对应的多个账号信息
 */
public class ZhanghContext implements Serializable {
	private static final long serialVersionUID = -2623966261834919842L;

	private String kehhao;
	private String kehzwm = "";// 账号主文件中的字段
	private String kehuzh;// 客户号、证件查询时,连续性校验时需要
	private String khzhlx;// 客户号、证件查询时,连续性校验时需要
	private String zhangh;
	private String yewudh;// 账号主文件中的字段,AZHJX0772表中查询字段
	private String huobdh;// AKHZH表中查询字段,AZHJX0772表中查询字段,打印需要
	private String chuibz;// AKHZH表中查询字段,打印需要
	private String yngyjg = "";// AZHJX0772表中查询字段
	private String jigomc = "";

	// 以下字段在生成PDF时使用,用来区分是否是第一次查询
	private boolean firstQuery = true;// 本账号开始查询标识
	private byte[] lastRowkey;// 本账户每次多取一条记录,记录下一页第一条RowKey

	/**
	 * 从AZHJX表中查询,用来区分活期或定期
	 */
	private String kemucc;
	private String tableName;// 要查询的明细表名称

	public String getKehhao() {
		return kehhao;
	}

	public void setKehhao(String kehhao) {
		this.kehhao = kehhao;
	}

	public String getKehzwm() {
		return kehzwm;
	}

	public void setKehzwm(String kehzwm) {
		this.kehzwm = kehzwm;
	}

	public String getKehuzh() {
		return kehuzh;
	}

	public void setKehuzh(String kehuzh) {
		this.kehuzh = kehuzh;
	}

	public String getKhzhlx() {
		return khzhlx;
	}

	public void setKhzhlx(String khzhlx) {
		this.khzhlx = khzhlx;
	}

	public String getYewudh() {
		return yewudh;
	}

	public void setYewudh(String yewudh) {
		this.yewudh = yewudh;
	}

	public String getZhangh() {
		return zhangh;
	}

	public void setZhangh(String zhangh) {
		this.zhangh = zhangh;
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

	public String getYngyjg() {
		return yngyjg;
	}

	public void setYngyjg(String yngyjg) {
		this.yngyjg = yngyjg;
	}

	public String getJigomc() {
		return jigomc;
	}

	public void setJigomc(String jigomc) {
		this.jigomc = jigomc;
	}

	/**
	 * 本账号开始查询标识
	 * 
	 * @return
	 */
	public boolean isFirstQuery() {
		return firstQuery;
	}

	/**
	 * 本账号开始查询标识
	 * 
	 * @param firstQuery
	 */
	public void setFirstQuery(boolean firstQuery) {
		this.firstQuery = firstQuery;
	}

	/**
	 * 本账户每次多取一条记录,记录下一页第一条RowKey
	 * 
	 * @return
	 */
	public byte[] getLastRowkey() {
		return lastRowkey;
	}

	/**
	 * 本账户每次多取一条记录,记录下一页第一条RowKey
	 * 
	 * @param lastRowkey
	 */
	public void setLastRowkey(byte[] lastRowkey) {
		this.lastRowkey = lastRowkey;
	}

	public String getKemucc() {
		return kemucc;
	}

	public void setKemucc(String kemucc) {
		this.kemucc = kemucc;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
}