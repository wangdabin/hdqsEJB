package com.ceb.hdqs.query.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 每次发起查询的时候，输入的基本信息 当前页面信息中，包含了当前页中的每个rowkey和每个rowkey的查询条数
 * 
 * @author user
 * 
 */
public class QueryPageInfo implements Serializable {
	private static final long serialVersionUID = -6006818879302917855L;

	

	/*
	 * 每页所包含的的key的个数的每个key需要查询的条数，map中的value的和是每页的queryNum
	 */
	private Map<TabRowMap, Integer> pageInfo = new HashMap<TabRowMap, Integer>();

	public Map<TabRowMap, Integer> getPageInfo() {
		return pageInfo;
	}

	public void setPageInfo(Map<TabRowMap, Integer> pageInfo) {
		this.pageInfo = pageInfo;
	}


}
