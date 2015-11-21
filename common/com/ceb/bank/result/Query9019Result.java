package com.ceb.bank.result;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ceb.bank.context.RowkeyContext;

public class Query9019Result {

	private Map<Integer, List<RowkeyContext>> pageCache = new HashMap<Integer, List<RowkeyContext>>();// 分页映射信息
	private Long bishuu = 0L;
	private String tableName;// 要查询表名,(主表or索引表1or索引表2)
	private boolean isIndexTable;//是否为索引表
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public boolean isIndexTable() {
		return isIndexTable;
	}
	public void setIndexTable(boolean isIndexTable) {
		this.isIndexTable = isIndexTable;
	}
	public Map<Integer, List<RowkeyContext>> getPageCache() {
		return pageCache;
	}
	public void setPageCache(Map<Integer, List<RowkeyContext>> pageCache) {
		this.pageCache = pageCache;
	}
	public Long getBishuu() {
		return bishuu;
	}
	public void setBishuu(Long bishuu) {
		this.bishuu = bishuu;
	}
}
