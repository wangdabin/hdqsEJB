package com.ceb.bank.constants;

import org.apache.hadoop.hbase.util.Pair;

/**
 * 查询条件种类
 */
public enum ChaxzlEnum {
	/**
	 * 账号
	 */
	CHAXZL1(new Pair<String, String>("1", "账号")), //
	/**
	 * 客户账号/一卡通
	 */
	CHAXZL2(new Pair<String, String>("2", "客户账号/一卡通")), //
	/**
	 * 客户号
	 */
	CHAXZL3(new Pair<String, String>("3", "客户号")), //
	/**
	 * 证件
	 */
	CHAXZL4(new Pair<String, String>("4", "证件"));

	private final Pair<String, String> pair;

	private ChaxzlEnum(Pair<String, String> pair) {
		this.pair = pair;
	}

	/**
	 * 查询条件种类与对应中文描述
	 * 
	 * @return
	 */
	public Pair<String, String> getPair() {
		return this.pair;
	}

	public String toString() {
		return this.pair.toString();
	}

	public static final String PREFIX = "CHAXZL";

	public static void main(String[] args) {
		System.out.println(ChaxzlEnum.valueOf(PREFIX + "1").toString());
	}
}