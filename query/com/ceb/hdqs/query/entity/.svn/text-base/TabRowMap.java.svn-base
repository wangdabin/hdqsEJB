package com.ceb.hdqs.query.entity;

import java.io.Serializable;

import com.ceb.hdqs.entity.result.ZhangHParseResult;

/**
 * 分页逻辑中每个RowKey需要查询的表
 * 
 * @author user
 * 
 */
public class TabRowMap extends SkyPair<byte[], String> implements Serializable {
	private static final long serialVersionUID = 593280456167141703L;
	/*
	 * 当前页面信息来源的账号，以及账号的属性信息
	 */
	private ZhangHParseResult zhangHParseResult;

	public TabRowMap(byte[] rowkey, String tabName) {
		this.setKey(rowkey);
		this.setValue(tabName);
	}

	public byte[] getRowKey() {
		return key;
	}

	public void setRowKey(byte[] rowkey) {
		this.key = rowkey;
	}

	public String getTableName() {
		return value;
	}

	public void setTableName(String tableName) {
		this.value = tableName;
	}

	public ZhangHParseResult getZhangHParseResult() {
		return zhangHParseResult;
	}

	public void setZhangHParseResult(ZhangHParseResult zhangHParseResult) {
		this.zhangHParseResult = zhangHParseResult;
	}
}