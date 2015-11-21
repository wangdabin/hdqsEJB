package com.ceb.bank.constants;

import org.apache.hadoop.hbase.util.Pair;

/**
 * 客户账号类型
 * 
 */
public enum KhzhlxEnum {
	/**
	 * 对公帐号
	 */
	KHZHLX0(new Pair<String, String>("0", "对公帐号")), //
	/**
	 * 卡
	 */
	KHZHLX1(new Pair<String, String>("1", "卡")), //
	/**
	 * 活期一本通
	 */
	KHZHLX2(new Pair<String, String>("2", "活期一本通")), //
	/**
	 * 定期一本通
	 */
	KHZHLX3(new Pair<String, String>("3", "定期一本通")), //
	/**
	 * 定期存折
	 */
	KHZHLX4(new Pair<String, String>("4", "定期存折")), //
	/**
	 * 存单
	 */
	KHZHLX5(new Pair<String, String>("5", "存单")), //
	/**
	 * 国债
	 */
	KHZHLX6(new Pair<String, String>("6", "国债")), //
	/**
	 * 外系统帐号
	 */
	KHZHLX7(new Pair<String, String>("7", "外系统帐号")), //
	/**
	 * 活期存折
	 */
	KHZHLX8(new Pair<String, String>("8", "活期存折")), //
	/**
	 * 内部帐/表外帐
	 */
	KHZHLX9(new Pair<String, String>("9", "内部帐/表外帐")), //
	/**
	 * 对私内部帐号
	 */
	KHZHLXS(new Pair<String, String>("S", "对私内部帐号")), //
	/**
	 * 客户号
	 */
	KHZHLXZ(new Pair<String, String>("Z", "客户号")), //
	/**
	 * 所有客户账号类
	 */
	KHZHLXA(new Pair<String, String>("A", "所有客户账号类")), //
	/**
	 * 对公一号通
	 */
	KHZHLXB(new Pair<String, String>("B", "对公一号通"));

	private final Pair<String, String> pair;

	private KhzhlxEnum(Pair<String, String> pair) {
		this.pair = pair;
	}

	/**
	 * 客户账号类型与对应中文描述
	 * 
	 * @return
	 */
	public Pair<String, String> getPair() {
		return this.pair;
	}

	public String toString() {
		return this.pair.toString();
	}

	public static final String PREFIX = "KHZHLX";
}