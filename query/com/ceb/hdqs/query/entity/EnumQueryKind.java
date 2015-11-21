package com.ceb.hdqs.query.entity;

/**
 * 输入条件的类型QueryKind
 * @author user
 *
 */
public enum EnumQueryKind {
	
	/**
	 * 账号
	 */
	QK1( new SkyPair<Integer,String>(1,"账号")), //
	/**
	 * 客户账号
	 */
	QK2(new SkyPair<Integer,String>(2,"客户账号/一卡通")),//
	/**
	 * 客户号
	 */
	QK3(new SkyPair<Integer,String>(3,"客户号")), //
	/**
	 * 证件
	 */
	QK4(new SkyPair<Integer,String>(4,"证件"));
	
	private final SkyPair<Integer,String> display;

	private EnumQueryKind(SkyPair<Integer, String> display) {
		this.display = display;
	}

	public SkyPair<Integer, String> getDisplay() {
		return this.display;
	}

	public String toString() {
		return this.display.toString();
	}
	
	public static final String QK="QK";
}
