package com.ceb.bank.context;

/**
 * 从AZHJX表中查询账号的KEMUCC
 */
public class KemuccContext extends YngyjgContext {
	private static final long serialVersionUID = 5239020021730930291L;

	private String yewudh;
	private String huobdh;
	private String kemucc;

	public String getYewudh() {
		return yewudh;
	}

	public void setYewudh(String yewudh) {
		this.yewudh = yewudh;
	}

	public String getHuobdh() {
		return huobdh;
	}

	public void setHuobdh(String huobdh) {
		this.huobdh = huobdh;
	}

	public String getKemucc() {
		return kemucc;
	}

	public void setKemucc(String kemucc) {
		this.kemucc = kemucc;
	}
}