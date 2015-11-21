package com.ceb.hdqs.service.cache;

import com.ceb.hdqs.po.Authorize;

public abstract class AbstractRowkeyItem {

	/**
	 * 缓存最后使用时间,这个需要在每次调用后更新
	 */
	private long lastModifiedT;
	private Authorize authorize = new Authorize();
	private String pdfFilePath;
	private String commFilePath;

	public long getLastModifiedT() {
		return lastModifiedT;
	}

	public void setLastModifiedT(long lastModifiedT) {
		this.lastModifiedT = lastModifiedT;
	}

	public Authorize getAuthorize() {
		return authorize;
	}

	public void setAuthorize(Authorize authorize) {
		this.authorize = authorize;
	}

	public String getPdfFilePath() {
		return pdfFilePath;
	}

	public void setPdfFilePath(String pdfFilePath) {
		this.pdfFilePath = pdfFilePath;
	}

	public String getCommFilePath() {
		return commFilePath;
	}

	public void setCommFilePath(String commFilePath) {
		this.commFilePath = commFilePath;
	}
}