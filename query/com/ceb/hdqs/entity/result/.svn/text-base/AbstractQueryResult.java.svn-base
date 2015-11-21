package com.ceb.hdqs.entity.result;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.ceb.hdqs.query.entity.AuthorizeLevel;
import com.ceb.hdqs.query.entity.QueryPageInfo;

/**
 * 查询结果抽象对象;所有的查询结果都继承自该对象
 * 
 * @author user
 * 
 */
public abstract class AbstractQueryResult {
	/*
	 * 存储查询任务的授权级别和说明
	 */
	private AuthorizeLevel authorityInfo;
	private byte[] rowKey;
	// 分页映射信息
	private Map<Integer, QueryPageInfo> pageMap;
	private String queryTableName;// 查询结果来源表

	public AuthorizeLevel getAuthorityInfo() {
		return authorityInfo;
	}

	public void setAuthorityInfo(AuthorizeLevel authorityInfo) {
		this.authorityInfo = authorityInfo;
	}

	public byte[] getRowKey() {
		return rowKey;
	}

	public void setRowKey(byte[] rowKey) {
		this.rowKey = rowKey;
	}

	public Map<Integer, QueryPageInfo> getPageMap() {
		return pageMap;
	}

	public void setPageMap(Map<Integer, QueryPageInfo> pageMap) {
		this.pageMap = pageMap;
	}

	public String getQueryTableName() {
		return queryTableName;
	}

	public void setQueryTableName(String queryTableName) {
		this.queryTableName = queryTableName;
	}

	public String[] toArray(String printStyle, String huobdh) {
		return null;
	}

	

	/**
	 * 将对象解析称符合输出规则的字符串
	 * 
	 * @return
	 */
	public abstract String parseToString(String huobdh);
}
