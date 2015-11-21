package com.ceb.bank.context;

import java.io.Serializable;

/**
 * 账号关联关系,客户号或证件查询时,保存客户账号和客户账号类型等属性信息
 */
public class RZhanghContext implements Serializable {
	private static final long serialVersionUID = 7951361090460803698L;

	private String zhangh;// 帐号
	private String khzhlx;// 客户帐号类型
	private String kehuzh;// 客户帐号

	/**
	 * 帐号
	 * 
	 * @return
	 */
	public String getZhangh() {
		return zhangh;
	}

	/**
	 * 帐号
	 * 
	 * @param zhangh
	 */
	public void setZhangh(String zhangh) {
		this.zhangh = zhangh;
	}

	/**
	 * 客户帐号类型
	 * 
	 * @return
	 */
	public String getKhzhlx() {
		return khzhlx;
	}

	/**
	 * 客户帐号类型
	 * 
	 * @param khzhlx
	 */
	public void setKhzhlx(String khzhlx) {
		this.khzhlx = khzhlx;
	}

	/**
	 * 客户帐号
	 * 
	 * @return
	 */
	public String getKehuzh() {
		return kehuzh;
	}

	/**
	 * 客户帐号
	 * 
	 * @param kehuzh
	 */
	public void setKehuzh(String kehuzh) {
		this.kehuzh = kehuzh;
	}
}