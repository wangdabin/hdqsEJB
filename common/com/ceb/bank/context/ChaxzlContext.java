package com.ceb.bank.context;

public class ChaxzlContext {
	/**
	 * 查询种类
	 */
	private String key;
	/**
	 * 根据查询种类对应的查询条件值
	 */
	private String value;
	/**
	 * 中文描述
	 */
	private String display;

	public ChaxzlContext() {

	}

	/**
	 * 查询种类
	 */
	public String getKey() {
		return key;
	}

	/**
	 * 查询种类
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * 根据查询种类对应的查询条件值
	 */
	public String getValue() {
		return value;
	}

	/**
	 * 根据查询种类对应的查询条件值
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * 中文描述
	 */
	public String getDisplay() {
		return display;
	}

	/**
	 * 中文描述
	 */
	public void setDisplay(String display) {
		this.display = display;
	}
}