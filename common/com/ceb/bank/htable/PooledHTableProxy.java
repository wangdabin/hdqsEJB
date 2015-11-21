package com.ceb.bank.htable;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.hbase.client.HTableInterface;

public abstract class PooledHTableProxy extends Configured {

	private String tableName;

	/**
	 * 调用getHTable()方式前,必须调用setTableName(String tableName)
	 * 
	 * @param conf
	 */
	public PooledHTableProxy(Configuration conf) {
		super(conf);
	}

	public PooledHTableProxy(Configuration conf, String tableName) {
		super(conf);
		this.tableName = tableName;
	}

	public HTableInterface getHTable() {
		return HTablePoolHolder.getInstance(this.getConf()).getHTable(tableName);
	}

	public void release(HTableInterface htable) {
		HTablePoolHolder.getInstance(this.getConf()).release(htable);
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
}