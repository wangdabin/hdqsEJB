package com.ceb.hdqs.action.synchronize;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;

import com.ceb.hdqs.entity.JgcsEO;
import com.ceb.hdqs.query.utils.QueryConfUtils;

/**
 * 将oracle中的PJGCS同步到HBase中
 * 
 * @author user
 * 
 */
public class PjgcsSynchronizeProcessor {

	public static final Log LOG = LogFactory.getLog(PjgcsSynchronizeProcessor.class);

	/**
	 * 开始同步机构参数到HBase中
	 * 
	 * @param jgcsList
	 *            待同步的机构参数集合
	 * @return
	 */
	public List<Long> doSyn(List<JgcsEO> jgcsList) {
		Configuration conf = QueryConfUtils.getActiveConfig();
		SkyHTable hTable = null;
		try {
			LOG.info("开始同步PJGCS到HBase.");
			hTable = new SkyHTable(conf, "pjgcs");
			PjgcsPutUtils putUtils = new PjgcsPutUtils();
			List<Put> putList = new ArrayList<Put>();
			for (JgcsEO record : jgcsList) {
				List<KeyValue> keyValueList = putUtils.getPutter(Bytes.toBytes(HashCodeBuilder
						.reflectionHashCode(record.getYngyjg()) + "|" + record.getYngyjg()), record);
				Put put = null;
				for (KeyValue keyValue : keyValueList) {
					put = new Put(keyValue.getRow());
					// 设置WAL为true，确保宕机时不丢失数据
					put.setWriteToWAL(true);
					put.add(keyValue);
					putList.add(put);
				}
			}
			hTable.putWithResult(putList);
			LOG.info("同步PJGCS结束，同步数量: " + jgcsList.size());
			// 合并结果
			List<Long> idList = new ArrayList<Long>();
			putList.clear();
			putList = null;
			return idList;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} finally {
			if (hTable != null) {
				try {
					hTable.close();
				} catch (IOException e) {
					LOG.error(e.getMessage(), e);
				}
			}
		}
	}
}
