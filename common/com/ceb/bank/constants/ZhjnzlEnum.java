package com.ceb.bank.constants;

/**
 * 证件种类
 * 
 */
public enum ZhjnzlEnum {
	/**
	 * 身份证
	 */
	ZHJNZL1("身份证"), //
	/**
	 * 护照
	 */
	ZHJNZL2("护照"), //
	/**
	 * 军人证
	 */
	ZHJNZL3("军人证"), //
	/**
	 * 武警证
	 */
	ZHJNZL4("武警证"), //
	/**
	 * 港澳居民来往内地通行证
	 */
	ZHJNZL5("港澳居民来往内地通行证"), //
	/**
	 * 户口簿
	 */
	ZHJNZL6("户口簿"), //
	/**
	 * 其他
	 */
	ZHJNZL8("其他"), //
	/**
	 * 台湾居民来往大陆通行证
	 */
	ZHJNZL9("台湾居民来往大陆通行证"), //
	/**
	 * 外国人永久居留证
	 */
	ZHJNZLa("外国人永久居留证"), //
	/**
	 * 未知
	 */
	ZHJNZLux("未知");

	private final String display;

	private ZhjnzlEnum(String display) {
		this.display = display;
	}

	/**
	 * 获取证件种类中文描述
	 * 
	 * @return
	 */
	public String getDisplay() {
		return this.display;
	}

	public String toString() {
		return this.display;
	}

	public static final String PREFIX = "ZHJNZL";
}