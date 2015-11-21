package com.ceb.bank.constants;

import org.apache.hadoop.hbase.util.Pair;

/**
 * KEMUCC枚举值
 * 
 * <PRE>
 * S-对私活期 
 * C-对公活期 
 * F-对私定期 
 * E-对公定期
 * </PRE>
 * 
 */
public enum KemuccEnum {
	/**
	 * S-对私活期
	 */
	S(new Pair<String, String>("S", "对私活期")), //

	/**
	 * C-对公活期
	 */
	C(new Pair<String, String>("C", "对公活期")), //

	/**
	 * F-对私定期
	 */
	F(new Pair<String, String>("F", "对私定期")), //

	/**
	 * E-对公定期
	 */
	E(new Pair<String, String>("E", "对公定期"));

	private final Pair<String, String> pair;

	private KemuccEnum(Pair<String, String> pair) {
		this.pair = pair;
	}

	public Pair<String, String> getPair() {
		return this.pair;
	}

	public String toString() {
		return this.pair.toString();
	}

	public static final String KEMUCC_DSHQ = "S";
	public static final String KEMUCC_DGHQ = "C";
	public static final String KEMUCC_DSDQ = "F";
	public static final String KEMUCC_DGDQ = "E";
}