package com.ceb.hdqs.query.entity;

/**
 * 客户账号类型
 *
 *	0-对公帐号
	1-卡
	2-活期一本通
	3-定期一本通
	4-定期存折
	5-存单
	6-国债
	7-外系统帐号
	8-活期存折
	9-内部帐/表外帐
	S-对私内部帐号
	Z-客户号
	A-所有客户账号类型
	B-对公一号通
 */
public enum EnumKhzhlx {
	    LX0(new SkyPair<String,String>("0","对公帐号")),
		LX1(new SkyPair<String,String>("1","卡")),        
		LX2(new SkyPair<String,String>("2","活期一本通")),
		LX3(new SkyPair<String,String>("3","定期一本通")),
		LX4(new SkyPair<String,String>("4","定期存折")), 
		LX5(new SkyPair<String,String>("5","存单")),      
		LX6(new SkyPair<String,String>("6","国债")),      
		LX7(new SkyPair<String,String>("7","外系统帐号")),
		LX8(new SkyPair<String,String>("8","活期存折")),  
		LX9(new SkyPair<String,String>("9","内部帐/表外帐")),
		LXS(new SkyPair<String,String>("S","对私内部帐号")),
		LXZ(new SkyPair<String,String>("Z","客户号")),    
		LXA(new SkyPair<String,String>("A","所有客户账号类")),
		LXB(new SkyPair<String,String>("B","对公一号通"));

	private final SkyPair<String,String> display;

	private EnumKhzhlx(SkyPair<String, String> display) {
		this.display = display;
	}

	public SkyPair<String, String> getDisplay() {
		return this.display;
	}

	public String toString() {
		return this.display.toString();
	}
	public static final String LX="LX";
}
