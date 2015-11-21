package com.ceb.bank.context;

/**
 * 0771输出文件时使用的字段
 */
public class Output0771Context extends OutPdfContext {
	private static final long serialVersionUID = -5965898621850152567L;

	private KehuzhContext header = new KehuzhContext();

	public KehuzhContext getHeader() {
		return header;
	}

	public void setHeader(KehuzhContext header) {
		this.header = header;
	}
}