package com.ceb.bank.htable;

import java.io.IOException;
import java.io.Serializable;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.HTablePool;
import org.apache.hadoop.hbase.util.Bytes;

import com.ceb.hdqs.query.utils.QueryConfUtils;

public class HTablePoolHolder implements Serializable {
	private static final long serialVersionUID = 1953968784854850499L;

	private static final int MAX_CONN = 1000;
	private static final ReentrantLock lock = new ReentrantLock();
	private static Map<String, HTablePoolHolder> cache = new HashMap<String, HTablePoolHolder>();

	private Configuration conf;
	private int maxSize;
	private HTablePool htablePool;

	private HTablePoolHolder(Configuration conf, int maxConn) {
		this.setConf(conf);
		this.maxSize = getConf().getInt("com.skycloud.hbase.maxconn", maxConn);
		this.htablePool = new HTablePool(conf, this.maxSize);
	}

	public static HTablePoolHolder getInstance(Configuration conf) {
		return getInstance(conf, MAX_CONN);
	}

	public static HTablePoolHolder getInstance(Configuration conf, int maxConn) {
		if (cache.get(QueryConfUtils.getActiveClusterFlag()) == null) {
			lock.lock();
			try {
				if (cache.get(QueryConfUtils.getActiveClusterFlag()) == null) {
					HTablePoolHolder instance = new HTablePoolHolder(conf, maxConn);
					cache.put(QueryConfUtils.getActiveClusterFlag(), instance);
				}
			} finally {
				lock.unlock();
			}
		}

		return cache.get(QueryConfUtils.getActiveClusterFlag());
	}

	public HTableInterface getHTable(String tableName) {
		if (tableName == null || tableName.trim().length() == 0) {
			throw new InvalidParameterException("TableName must input.");
		}
		return htablePool.getTable(Bytes.toBytes(tableName));
	}

	public void release(HTableInterface htable) {
		if (htable == null) {
			return;
		}
		try {
			htable.close();
		} catch (IOException e) {
		}
	}

	public Configuration getConf() {
		return conf;
	}

	public void setConf(Configuration conf) {
		this.conf = conf;
	}
}