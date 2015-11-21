package com.ceb.bank.context;

/**
 * 客户回单上下文,用于
 */
public class BkhhdConext {
	private String tableName;// 要查询表名,(主表or索引表1or索引表2)
	private boolean isIndexTable = false;
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
}
