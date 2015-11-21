package com.ceb.hdqs.action.synchronize;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;

import com.ceb.hdqs.entity.PybjyEO;
import com.ceb.hdqs.query.utils.QueryConfUtils;
import com.ceb.hdqs.query.utils.QueryConstants;

/**
 * 异步查询交易记录同步
 * 
 * @author user
 * 
 */
public class PybjySynchronizeProcessor {
	private static final Log LOG = LogFactory.getLog(PybjySynchronizeProcessor.class);

	/**
	 * 同步Active集群的数据库
	 * 
	 * @param list
	 * @return
	 * @throws Exception
	 */
	public List<Long> synchronizeToActive(List<PybjyEO> list) throws Exception {
		LOG.info("开始同步AsyQueryRecord(异步查询交易记录)到HBase集群" + QueryConfUtils.getActiveClusterFlag() + "...");
		List<Long> idList = new ArrayList<Long>();
		SkyHTable hTable = new SkyHTable(QueryConfUtils.getActiveConfig(), QueryConstants.HBASE_TABLE_NAME_ASYNQUERY);
		try {
			idList = doSynchronize(hTable, list);
			if (idList.size() > 0) {
				LOG.info("同步失败个数：" + idList.size());
			}
		} finally {
			closeTable(hTable);
		}

		LOG.info("同步AsyQueryRecord(异步查询交易记录)到HBase集群" + QueryConfUtils.getActiveClusterFlag() + "集群结束. " + "Success: "
				+ (list.size() - idList.size()) + "failed：" + idList.size());
		return idList;
	}

	/**
	 * 同步Standby集群的数据库
	 * 
	 * @param list
	 *            待同步的交易记录
	 * @return List<Long> 同步失败记录的id 的集合
	 * @throws Exception
	 *             同步异常
	 */
	public List<Long> synchronizeToStandby(List<PybjyEO> list) throws Exception {
		LOG.info("开始同步AsyQueryRecord(异步查询交易记录)到HBase集群" + QueryConfUtils.getStandbyClusterFlag() + "集群....");
		List<Long> idList = new ArrayList<Long>();
		SkyHTable hTable = new SkyHTable(QueryConfUtils.getStandbyConfig(), QueryConstants.HBASE_TABLE_NAME_ASYNQUERY);
		try {
			idList = doSynchronize(hTable, list);
			if (idList.size() > 0) {
				LOG.info("同步失败个数：" + idList.size());
			}
		} finally {
			closeTable(hTable);
		}

		LOG.info("同步AsyQueryRecord(异步查询交易记录)到HBase集群" + QueryConfUtils.getStandbyClusterFlag() + "集群结束   "
				+ "Success: " + (list.size() - idList.size()) + "failed：" + idList.size());
		return idList;
	}

	/**
	 * 同步异步查询的记录 来源：oracle 目标：HBase
	 * 
	 * @param list
	 *            待同步的交易记录
	 * @return List<Long> 同步失败记录的id 的集合
	 * @throws Exception
	 */
	public List<Long> doSynchronize(SkyHTable hTable, List<PybjyEO> list) throws Exception {
		PybjyPutUtils putUtils = new PybjyPutUtils();

		List<Put> putList = new ArrayList<Put>();
		for (PybjyEO record : list) {
			List<KeyValue> keyValueList = putUtils.getPutter(buildRowKey(record, hTable.getConfiguration()), record);

			if (keyValueList == null || keyValueList.isEmpty()) {
				LOG.warn("同步解析字段为空: " + record);
				continue;
			}
			Put put = null;
			for (KeyValue keyValue : keyValueList) {
				put = new Put(keyValue.getRow());
				// 设置WAL为true，确保宕机时不丢失数据。
				put.setWriteToWAL(true);
				put.add(keyValue);
				putList.add(put);
			}
		}
		List<Put> failList = hTable.putWithResult(putList);
		List<Long> idList = new ArrayList<Long>();
		if (failList.size() > 0) {
			for (Put put : failList) {
				idList.add((Long) put.toMap().get("id"));
			}

			failList.clear();
			failList = null;
		}
		putList.clear();
		putList = null;
		return idList;
	}

	private byte[] buildRowKey(PybjyEO record, Configuration conf) {
		String SPLITTER = conf.get(QueryConstants.ROWKEY_SPLITTER, "|");
		return Bytes.toBytes(record.getSlbhao() + SPLITTER + record.getStartTime() + SPLITTER + record.getId());
	}

	/**
	 * 关闭同步AsyQueryRecord的两个表
	 */
	private void closeTable(SkyHTable hTable) {
		if (hTable == null) {
			return;
		}
		try {
			hTable.close();
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		}
	}
}