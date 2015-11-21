package com.ceb.hdqs.action.synchronize;

/**
 * 从oracle数据库中同步交易记录到HBase中
 * @author user
 *
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;

import com.ceb.hdqs.entity.PjyjlEO;
import com.ceb.hdqs.query.utils.QueryConfUtils;
import com.ceb.hdqs.query.utils.QueryConstants;

/**
 * 从oracle数据库中同步交易记录到HBase中
 * 
 * 
 * @author user
 */
public class PjyjlSynchronizeProcessor {
	private static final Log log = LogFactory.getLog(PjyjlSynchronizeProcessor.class);
	private static final byte[] Q = Bytes.toBytes("Q");

	/**
	 * 同步到active集群
	 * 
	 * @param list
	 * @return
	 * @throws Exception
	 */
	public List<Long> synchronizeToActive(List<PjyjlEO> list) throws Exception {
		Configuration activeConf = QueryConfUtils.getActiveConfig();
		if (activeConf == null) {
			throw new RuntimeException("无法获取主集群，请查询主备集群切换服务状态!");
		}
		log.info("开始同步ExchangeRecord （交易记录）到" + QueryConfUtils.getActiveClusterFlag() + "集群...");
		List<Long> idList = new ArrayList<Long>();

		SkyHTable hTable = null;
		try {
			hTable = new SkyHTable(activeConf, QueryConstants.HBASE_TABLE_NAME_EXCHANGE_RECORD);
		} catch (IOException e) {
			log.error("打开表" + QueryConstants.HBASE_TABLE_NAME_EXCHANGE_RECORD + "失败");
			throw e;
		}

		try {
			idList = doSynchronize(hTable, list);
			if (idList.size() > 0)
				log.info("主表同步失败个数：" + idList.size());
		} finally {
			closeTable(hTable);
		}
		log.info("同步ExchangeRecord（交易记录） 到" + QueryConfUtils.getActiveClusterFlag() + "集群结束  " + "Success: "
				+ (list.size() - idList.size()) + "failed：" + idList.size());
		return idList;
	}

	/**
	 * 同步信息到Active集群
	 * 
	 * @param list
	 * @return
	 * @throws Exception
	 */
	public List<Long> synchronizeToStandby(List<PjyjlEO> list) throws Exception {
		Configuration standbyConf = QueryConfUtils.getStandbyConfig();
		if (standbyConf == null) {
			throw new RuntimeException("无法获取备集群，请查询主备集群切换服务状态!");
		}
		log.info("开始同步ExchangeRecord 到（交易记录）" + QueryConfUtils.getStandbyClusterFlag() + "集群...");
		List<Long> idList = new ArrayList<Long>();
		SkyHTable hTable = null;
		try {
			hTable = new SkyHTable(QueryConfUtils.getStandbyConfig(), QueryConstants.HBASE_TABLE_NAME_EXCHANGE_RECORD);
		} catch (IOException e) {
			log.error("打开表" + QueryConstants.HBASE_TABLE_NAME_EXCHANGE_RECORD + "失败");
			throw e;
		}
		try {
			idList = doSynchronize(hTable, list);
			if (idList.size() > 0)
				log.info("主表同步失败个数：" + idList.size());
		} finally {
			closeTable(hTable);
		}
		log.info("同步ExchangeRecord（交易记录） 到" + QueryConfUtils.getStandbyClusterFlag() + "集群结束  " + "Success: "
				+ (list.size() - idList.size()) + "failed：" + idList.size());
		return idList;
	}

	/**
	 * 同步每次异步查询中的多个任务 在异步查询中一个输入条件可以产生多个查询任务（每个条件对应一个任务）
	 * 
	 * @param list
	 * @return
	 * @throws Exception
	 */
	public List<Long> doSynchronize(SkyHTable hTable, List<PjyjlEO> list) throws Exception {
		String SPLITTER = hTable.getConfiguration().get(QueryConstants.ROWKEY_SPLITTER, "|");

		PjyjlPutUtils putUtils = new PjyjlPutUtils();
		List<Put> putList = new ArrayList<Put>();
		for (PjyjlEO record : list) {
			List<KeyValue> keyValueList = putUtils.getPutter(buildRowKey(record, SPLITTER), record);
			Put put = null;
			for (KeyValue keyValue : keyValueList) {
				put = new Put(keyValue.getRow());
				// 设置WAL为true，确保宕机时不丢失数据
				put.setWriteToWAL(true);
				put.add(keyValue);
				putList.add(put);
			}
		}
		List<Put> failList = hTable.putWithResult(putList);
		// 同步索引表
		log.debug("Begin to create index.");
		Set<Long> failSet = synIndexTable(hTable.getConfiguration(), list, SPLITTER);
		log.debug("Create index Finished.");
		// 合并结果
		List<Long> idList = new ArrayList<Long>();
		addFailIdToSet(failList, failSet);
		StringBuilder failBuilder = new StringBuilder();
		for (Long id : failSet) {
			idList.add(id);
			failBuilder.append(id + ",");
		}

		log.info("Failed to Synchronized record is : "
				+ (StringUtils.isBlank(failBuilder.toString()) ? "No Record Failed " : failBuilder.toString()));
		putList.clear();
		putList = null;
		return idList;
	}

	/**
	 * 建立HBase的索引表 索引主要有：
	 * 交易柜员|交易机构|日期、交易柜员|交易码|日期、交易机构|交易码|日期、交易柜员|日期、交易机构|日期、交易码、日期
	 * 
	 * @param puts
	 * @param activeTable
	 * @throws IOException
	 */
	public Set<Long> synIndexTable(Configuration conf, List<PjyjlEO> list, String splitter) throws IOException {
		Set<Long> failSet = new HashSet<Long>();
		addFailIdToSet(writeJio1gyIndex(conf, list, splitter), failSet);
		addFailIdToSet(writeYngyjgIndex(conf, list, splitter), failSet);
		addFailIdToSet(writeJiaoymIndex(conf, list, splitter), failSet);
		addFailIdToSet(writeGuiyuanAndJGIndex(conf, list, splitter), failSet);
		addFailIdToSet(writeGuiyuanAndCodeIndex(conf, list, splitter), failSet);
		addFailIdToSet(writeJGAndCodeIndex(conf, list, splitter), failSet);
		addFailIdToSet(writeHandleNoIndex(conf, list, splitter), failSet);
		addFailIdToSet(writeGuiyuanJGAndCodeIndex(conf, list, splitter), failSet);
		addFailIdToSet(writeJioysjIndex(conf, list, splitter), failSet);

		// List<Long> failList = new ArrayList<Long>(failSet.size());
		// for (Long id : failSet) {
		// failList.add(id);
		// }
		return failSet;
	}

	private void addFailIdToSet(List<Put> failList, Set<Long> set) {
		for (Put put : failList) {
			set.add((Long) put.toMap().get("id"));
		}
	}

	private List<Put> writeJio1gyIndex(Configuration conf, List<PjyjlEO> list, String splitter)
			throws IOException {
		SkyHTable hTable = null;
		try {
			hTable = new SkyHTable(conf, QueryConstants.HBASE_TABLE_NAME_INDEX_GY);
		} catch (IOException e) {
			log.error("打开表" + QueryConstants.HBASE_TABLE_NAME_INDEX_GY + "失败");
			throw e;
		}
		try {
			List<Put> putList = new ArrayList<Put>();
			for (PjyjlEO record : list) {
				byte[] rowKey = buildRowKey(record, splitter);
				Put put = new Put(buildJio1gyKey(record, splitter));
				put.setWriteToWAL(true);
				put.add(QueryConstants.HBASE_TABLE_FAMILY_BYTE, Q, rowKey);
				putList.add(put);
			}
			List<Put> failList = hTable.putWithResult(putList);
			if (failList.size() > 0)
				log.info(Bytes.toString(hTable.getTableName()) + "同步失败个数：" + failList.size());
			putList.clear();
			putList = null;
			return failList;
		} finally {
			if (hTable != null) {
				hTable.close();
			}
		}
	}

	private List<Put> writeYngyjgIndex(Configuration conf, List<PjyjlEO> list, String splitter)
			throws IOException {
		// SkyHTable hTable = new SkyHTable(conf,
		// QueryConstants.HBASE_TABLE_NAME_INDEX_JG);
		SkyHTable hTable = null;
		try {
			hTable = new SkyHTable(conf, QueryConstants.HBASE_TABLE_NAME_INDEX_JG);
		} catch (IOException e) {
			log.error("打开表" + QueryConstants.HBASE_TABLE_NAME_INDEX_JG + "失败");
			throw e;
		}
		try {
			List<Put> putList = new ArrayList<Put>();
			for (PjyjlEO record : list) {
				byte[] rowKey = buildRowKey(record, splitter);
				Put put = new Put(buildYngyjgKey(record, splitter));
				put.setWriteToWAL(true);
				put.add(QueryConstants.HBASE_TABLE_FAMILY_BYTE, Q, rowKey);
				putList.add(put);
			}
			List<Put> failList = hTable.putWithResult(putList);
			if (failList.size() > 0)
				log.info(Bytes.toString(hTable.getTableName()) + "同步失败个数：" + failList.size());
			putList.clear();
			putList = null;
			return failList;
		} finally {
			if (hTable != null) {
				hTable.close();
			}
		}
	}

	private List<Put> writeJiaoymIndex(Configuration conf, List<PjyjlEO> list, String splitter)
			throws IOException {
		// SkyHTable hTable = new SkyHTable(conf,
		// QueryConstants.HBASE_TABLE_NAME_INDEX_CODE);
		SkyHTable hTable = null;
		try {
			hTable = new SkyHTable(conf, QueryConstants.HBASE_TABLE_NAME_INDEX_CODE);
		} catch (IOException e) {
			log.error("打开表" + QueryConstants.HBASE_TABLE_NAME_INDEX_CODE + "失败");
			throw e;
		}
		try {
			List<Put> putList = new ArrayList<Put>();
			for (PjyjlEO record : list) {
				byte[] rowKey = buildRowKey(record, splitter);
				Put put = new Put(buildJiaoymKey(record, splitter));
				put.setWriteToWAL(true);
				put.add(QueryConstants.HBASE_TABLE_FAMILY_BYTE, Q, rowKey);
				putList.add(put);
			}
			List<Put> failList = hTable.putWithResult(putList);
			if (failList.size() > 0)
				log.info(Bytes.toString(hTable.getTableName()) + "同步失败个数：" + failList.size());
			putList.clear();
			putList = null;
			return failList;
		} finally {
			if (hTable != null) {
				hTable.close();
			}
		}
	}

	private List<Put> writeGuiyuanAndJGIndex(Configuration conf, List<PjyjlEO> list, String splitter)
			throws IOException {
		// SkyHTable hTable = new SkyHTable(conf,
		// QueryConstants.HBASE_TABLE_NAME_INDEX_GYJG);
		SkyHTable hTable = null;
		try {
			hTable = new SkyHTable(conf, QueryConstants.HBASE_TABLE_NAME_INDEX_GYJG);
		} catch (IOException e) {
			log.error("打开表" + QueryConstants.HBASE_TABLE_NAME_INDEX_GYJG + "失败");
			throw e;
		}
		try {
			List<Put> putList = new ArrayList<Put>();
			for (PjyjlEO record : list) {
				byte[] rowKey = buildRowKey(record, splitter);
				Put put = new Put(buildGuiyuanAndJGKey(record, splitter));
				put.setWriteToWAL(true);
				put.add(QueryConstants.HBASE_TABLE_FAMILY_BYTE, Q, rowKey);
				putList.add(put);
			}
			List<Put> failList = hTable.putWithResult(putList);
			if (failList.size() > 0)
				log.info(Bytes.toString(hTable.getTableName()) + "同步失败个数：" + failList.size());
			putList.clear();
			putList = null;
			return failList;
		} finally {
			if (hTable != null) {
				hTable.close();
			}
		}
	}

	private List<Put> writeGuiyuanAndCodeIndex(Configuration conf, List<PjyjlEO> list, String splitter)
			throws IOException {
		// SkyHTable hTable = new SkyHTable(conf,
		// QueryConstants.HBASE_TABLE_NAME_INDEX_GYCODE);
		SkyHTable hTable = null;
		try {
			hTable = new SkyHTable(conf, QueryConstants.HBASE_TABLE_NAME_INDEX_GYCODE);
		} catch (IOException e) {
			log.error("打开表" + QueryConstants.HBASE_TABLE_NAME_INDEX_GYCODE + "失败");
			throw e;
		}
		try {
			List<Put> putList = new ArrayList<Put>();
			for (PjyjlEO record : list) {
				byte[] rowKey = buildRowKey(record, splitter);
				Put put = new Put(buildGuiyuanAndCodeKey(record, splitter));
				put.setWriteToWAL(true);
				put.add(QueryConstants.HBASE_TABLE_FAMILY_BYTE, Q, rowKey);
				putList.add(put);
			}
			List<Put> failList = hTable.putWithResult(putList);
			if (failList.size() > 0)
				log.info(Bytes.toString(hTable.getTableName()) + "同步失败个数：" + failList.size());
			putList.clear();
			putList = null;
			return failList;
		} finally {
			if (hTable != null) {
				hTable.close();
			}
		}
	}

	private List<Put> writeJGAndCodeIndex(Configuration conf, List<PjyjlEO> list, String splitter)
			throws IOException {
		// SkyHTable hTable = new SkyHTable(conf,
		// QueryConstants.HBASE_TABLE_NAME_INDEX_JGCODE);
		SkyHTable hTable = null;
		try {
			hTable = new SkyHTable(conf, QueryConstants.HBASE_TABLE_NAME_INDEX_JGCODE);
		} catch (IOException e) {
			log.error("打开表" + QueryConstants.HBASE_TABLE_NAME_INDEX_JGCODE + "失败");
			throw e;
		}
		try {
			List<Put> putList = new ArrayList<Put>();
			for (PjyjlEO record : list) {
				byte[] rowKey = buildRowKey(record, splitter);
				Put put = new Put(buildJGAndCodeKey(record, splitter));
				put.setWriteToWAL(true);
				put.add(QueryConstants.HBASE_TABLE_FAMILY_BYTE, Q, rowKey);
				putList.add(put);
			}
			List<Put> failList = hTable.putWithResult(putList);
			if (failList.size() > 0)
				log.info(Bytes.toString(hTable.getTableName()) + "同步失败个数：" + failList.size());
			putList.clear();
			putList = null;
			return failList;
		} finally {
			if (hTable != null) {
				hTable.close();
			}
		}
	}

	private List<Put> writeHandleNoIndex(Configuration conf, List<PjyjlEO> list, String splitter)
			throws IOException {
		// SkyHTable hTable = new SkyHTable(conf,
		// QueryConstants.HBASE_TABLE_NAME_INDEX_HANDLENO);
		SkyHTable hTable = null;
		try {
			hTable = new SkyHTable(conf, QueryConstants.HBASE_TABLE_NAME_INDEX_HANDLENO);
		} catch (IOException e) {
			log.error("打开表" + QueryConstants.HBASE_TABLE_NAME_INDEX_HANDLENO + "失败");
			throw e;
		}
		try {
			List<Put> putList = new ArrayList<Put>();
			for (PjyjlEO record : list) {
				byte[] rowKey = buildRowKey(record, splitter);
				Put put = new Put(buildHandleNoKey(record, splitter));
				put.setWriteToWAL(true);
				put.add(QueryConstants.HBASE_TABLE_FAMILY_BYTE, Q, rowKey);
				putList.add(put);
			}
			List<Put> failList = hTable.putWithResult(putList);
			if (failList.size() > 0)
				log.info(Bytes.toString(hTable.getTableName()) + "同步失败个数：" + failList.size());
			putList.clear();
			putList = null;
			return failList;
		} finally {
			if (hTable != null) {
				hTable.close();
			}
		}
	}

	private List<Put> writeGuiyuanJGAndCodeIndex(Configuration conf, List<PjyjlEO> list, String splitter)
			throws IOException {
		// SkyHTable hTable = new SkyHTable(conf,
		// QueryConstants.HBASE_TABLE_NAME_INDEX_GYJGCODE);
		SkyHTable hTable = null;
		try {
			hTable = new SkyHTable(conf, QueryConstants.HBASE_TABLE_NAME_INDEX_GYJGCODE);
		} catch (IOException e) {
			log.error("打开表" + QueryConstants.HBASE_TABLE_NAME_INDEX_GYJGCODE + "失败");
			throw e;
		}
		try {
			List<Put> putList = new ArrayList<Put>();
			for (PjyjlEO record : list) {
				byte[] rowKey = buildRowKey(record, splitter);
				Put put = new Put(buildGuiyuanJGAndCodeKey(record, splitter));
				put.setWriteToWAL(true);
				put.add(QueryConstants.HBASE_TABLE_FAMILY_BYTE, Q, rowKey);
				putList.add(put);
			}
			List<Put> failList = hTable.putWithResult(putList);
			if (failList.size() > 0)
				log.info(Bytes.toString(hTable.getTableName()) + "同步失败个数：" + failList.size());
			putList.clear();
			putList = null;
			return failList;
		} finally {
			if (hTable != null) {
				hTable.close();
			}
		}
	}

	private List<Put> writeJioysjIndex(Configuration conf, List<PjyjlEO> list, String splitter)
			throws IOException {
		// SkyHTable hTable = new SkyHTable(conf,
		// QueryConstants.HBASE_TABLE_NAME_INDEX_DATE);
		SkyHTable hTable = null;
		try {
			hTable = new SkyHTable(conf, QueryConstants.HBASE_TABLE_NAME_INDEX_DATE);
		} catch (IOException e) {
			log.error("打开表" + QueryConstants.HBASE_TABLE_NAME_INDEX_DATE + "失败");
			throw e;
		}
		try {
			List<Put> putList = new ArrayList<Put>();
			for (PjyjlEO record : list) {
				byte[] rowKey = buildRowKey(record, splitter);
				Put put = new Put(buildJioysjKey(record, splitter));
				put.setWriteToWAL(true);
				put.add(QueryConstants.HBASE_TABLE_FAMILY_BYTE, Q, rowKey);
				putList.add(put);
			}
			List<Put> failList = hTable.putWithResult(putList);
			if (failList.size() > 0)
				log.info(Bytes.toString(hTable.getTableName()) + "同步失败个数：" + failList.size());
			putList.clear();
			putList = null;
			return failList;
		} finally {
			if (hTable != null) {
				hTable.close();
			}
		}
	}

	/**
	 * 创建主表的RowKey
	 * 
	 * @param record
	 * @param splitter
	 * @return
	 */
	private byte[] buildRowKey(PjyjlEO record, String splitter) {
		return Bytes.toBytes(record.getGuiyls() + splitter + record.getStartTime() + splitter + record.getId());
	}

	/**
	 * 创建索引表的rowkey
	 * 
	 * @param record
	 * @param splitter
	 * @return
	 */
	private byte[] buildJio1gyKey(PjyjlEO record, String splitter) {
		return Bytes.toBytes(record.getJio1gy() + splitter + record.getJioyrq() + splitter + record.getGuiyls()
				+ splitter + record.getStartTime() + splitter + record.getJiaoym());
	}

	private byte[] buildJiaoymKey(PjyjlEO record, String splitter) {
		return Bytes.toBytes(record.getJiaoym() + splitter + record.getJioyrq() + splitter + record.getGuiyls()
				+ splitter + record.getStartTime() + splitter + record.getJiaoym());
	}

	private byte[] buildYngyjgKey(PjyjlEO record, String splitter) {
		return Bytes.toBytes(record.getYngyjg() + splitter + record.getJioyrq() + splitter + record.getGuiyls()
				+ splitter + record.getStartTime() + splitter + record.getJiaoym());
	}

	private byte[] buildGuiyuanAndCodeKey(PjyjlEO record, String splitter) {
		return Bytes.toBytes(record.getJio1gy() + splitter + record.getJiaoym() + splitter + record.getJioyrq()
				+ splitter + record.getGuiyls() + splitter + record.getStartTime() + splitter + record.getJiaoym());
	}

	private byte[] buildGuiyuanAndJGKey(PjyjlEO record, String splitter) {
		return Bytes.toBytes(record.getJio1gy() + splitter + record.getYngyjg() + splitter + record.getJioyrq()
				+ splitter + record.getGuiyls() + splitter + record.getStartTime() + splitter + record.getJiaoym());
	}

	private byte[] buildJGAndCodeKey(PjyjlEO record, String splitter) {
		return Bytes.toBytes(record.getYngyjg() + splitter + record.getJiaoym() + splitter + record.getJioyrq()
				+ splitter + record.getGuiyls() + splitter + record.getStartTime() + splitter + record.getJiaoym());
	}

	private byte[] buildHandleNoKey(PjyjlEO record, String splitter) {
		return Bytes.toBytes(record.getSlbhao() + splitter + record.getJioyrq() + splitter + record.getStartTime()
				+ splitter + record.getJiaoym());
	}

	private byte[] buildGuiyuanJGAndCodeKey(PjyjlEO record, String splitter) {
		return Bytes.toBytes(record.getJio1gy() + splitter + record.getYngyjg() + splitter + record.getJiaoym()
				+ splitter + record.getJioyrq() + splitter + record.getGuiyls() + splitter + record.getStartTime()
				+ splitter + record.getJiaoym());
	}

	private byte[] buildJioysjKey(PjyjlEO record, String splitter) {
		return Bytes.toBytes(record.getJioyrq() + splitter + record.getGuiyls() + splitter + record.getStartTime()
				+ splitter + record.getJiaoym());
	}

	/**
	 * 释放资源
	 * 
	 * @param hTable
	 */
	private void closeTable(SkyHTable hTable) {
		if (hTable == null) {
			return;
		}
		try {
			hTable.close();
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
	}
}
