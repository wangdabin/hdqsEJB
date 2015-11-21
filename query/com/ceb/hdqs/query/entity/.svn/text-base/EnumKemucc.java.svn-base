package com.ceb.hdqs.query.entity;

/**
 * kemucc枚举值 S-对私活期 C-对公活期 F-对私定期 E-对公定期
 * 
 * @author user
 * 
 */
public enum EnumKemucc {

	/**
	 * S-对私活期
	 */
	S(new SkyPair<String, String>("S", "对私活期")), //
	
	/**
	 * C-对公活期
	 */
	C(new SkyPair<String, String>("C", "对公活期")), //
	
	/**
	 * F-对私定期
	 */
	F(new SkyPair<String, String>("F", "对私定期")), //
	
	/**
	 * E-对公定期
	 */
	E(new SkyPair<String, String>("E", "对公定期"));

	private final SkyPair<String, String> display;

	private EnumKemucc(SkyPair<String, String> display) {
		this.display = display;
	}

	public SkyPair<String, String> getDisplay() {
		return this.display;
	}

	public String toString() {
		return this.display.toString();
	}

	public static final String KM = "KM";
}
