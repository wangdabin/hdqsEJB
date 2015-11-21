package com.ceb.bank.context;

/**
 * 从VYKTD表中获取KAAADX(卡对象)
 */
public class KaaadxContext extends YngyjgContext {
	private static final long serialVersionUID = 1212430470355677662L;

	/**
	 * 卡对象
	 */
	private String kaaadx;

	public String getKaaadx() {
		return kaaadx;
	}

	public void setKaaadx(String kaaadx) {
		this.kaaadx = kaaadx;
	}
}
