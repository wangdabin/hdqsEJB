package com.ceb.bank.context;

/**
 * 0770输出文件时使用的字段
 */
public class Output0770Context extends OutPdfContext {
	private static final long serialVersionUID = -9146251739583342194L;

	private ZhanghContext header = new ZhanghContext();
	private String zhangh;// 保存查询条件中的ZHANGH,NRA的一些特殊条件

	public ZhanghContext getHeader() {
		return header;
	}

	public void setHeader(ZhanghContext header) {
		this.header = header;
	}

	public String getZhangh() {
		return zhangh;
	}

	public void setZhangh(String zhangh) {
		this.zhangh = zhangh;
	}
}