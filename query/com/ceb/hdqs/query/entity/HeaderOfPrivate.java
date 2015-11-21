package com.ceb.hdqs.query.entity;

import java.io.Serializable;

/**
 * 0771/0772/0778 的客户对账单查询输出模板
 * @author user
 *
 */
@Deprecated
public class HeaderOfPrivate extends PageHeader implements Serializable{
//	姓名：
//	发卡/折机构：机构号  机构名称                   对账日期：
//	客户账号：                                      打印日期：YYYYMMDD TIME
//	币种：  钞汇标志：

	
	private static final long serialVersionUID = -5557820248967791637L;
	
	private  String mingch;//姓名
	private  String fkjg;//发卡机构
	private String dzrq;//对账日期
	private String khzh;//客户账号
	private String bizh;//币种
	private String chbz;//炒汇标志
	private String jigh;
	
	public String getJigh() {
		return jigh;
	}
	public void setJigh(String jigh) {
		this.jigh = jigh;
	}
	public String getMingch() {
		return mingch;
	}
	public void setMingch(String mingch) {
		this.mingch = mingch;
	}
	public String getFkjg() {
		return fkjg;
	}
	public void setFkjg(String fkjg) {
		this.fkjg = fkjg;
	}
	public String getDzrq() {
		return dzrq;
	}
	public void setDzrq(String dzrq) {
		this.dzrq = dzrq;
	}
	public String getKhzh() {
		return khzh;
	}
	public void setKhzh(String khzh) {
		this.khzh = khzh;
	}
	public String getBizh() {
		return bizh;
	}
	public void setBizh(String bizh) {
		this.bizh = bizh;
	}
	public String getChbz() {
		return chbz;
	}
	public void setChbz(String chbz) {
		this.chbz = chbz;
	}
}
