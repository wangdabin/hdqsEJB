package com.ceb.hdqs.action.query0775;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Row;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.KeyOnlyFilter;
import org.apache.hadoop.hbase.util.Bytes;

import com.ceb.bank.htable.AbstractQuery;
import com.ceb.hdqs.entity.PjyjlEO;
import com.ceb.hdqs.entity.field.PjyjlField;
import com.ceb.hdqs.query.utils.QueryConstants;

/**
 * 根据IndexQuery查询出的rowkey进行明细查询 单词查询的最大值是 MAX_INTEGER
 * 
 * @author user
 * 
 */
public class Handle0775ItemQuery extends AbstractQuery<PjyjlEO> {

	private static final Log LOG = LogFactory.getLog(Handle0775ItemQuery.class);
	private String tableName;

	public Handle0775ItemQuery(String tableName) {
		super(tableName);
		this.tableName = tableName;
	}

	/**
	 * 查询HBase表，查询出当前需要查询的页面内容
	 * 
	 * @param queryList
	 *            需要查询的明细的rowkey
	 * @return
	 * @throws IOException
	 */
	public List<PjyjlEO> queryItem(List<String> queryList) throws IOException {
		List<Row> tmpGets = new ArrayList<Row>();
		for (String key : queryList) {
			Row get = new Get(Bytes.toBytes(key));
			tmpGets.add(get);
		}

		List<PjyjlEO> items = scan(tmpGets);
		return items;

	}

	/**
	 * 获取交易明细的总数量
	 * 
	 * @param scan
	 *            查询总数量的scan
	 * @return
	 * @throws IOException
	 */
	public List<String> getCounts(Scan scan) throws IOException {
		ItemCounter counter = new ItemCounter(tableName);
		return counter.getCounts(scan);
	}

	/**
	 * 查询具体的交易明细
	 * 
	 * @param gets
	 *            每个明细对应的rowkey
	 * @return
	 * @throws IOException
	 */
	public List<PjyjlEO> scan(List<Row> gets) throws IOException {
		List<PjyjlEO> results = new ArrayList<PjyjlEO>();
		HTableInterface table = null;
		try {
			table = this.getHTable();
			Object[] res = table.batch(gets);
			for (Object result : res) {
				PjyjlEO v = parse((Result) result);
				if (v != null) {
					results.add(v);
				}
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		} finally {
			if (table != null)
				table.close();
		}
		return results;
	}

	@Override
	public PjyjlEO parse(Result result) {
		PjyjlEO record = new PjyjlEO();
		record.setJioyrq(Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, PjyjlField.JIOYRQ)));
		record.setJiaoym(Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, PjyjlField.JIAOYM)));
		record.setYngyjg(Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, PjyjlField.YNGYJG)));
		record.setJio1gy(Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, PjyjlField.JIO1GY)));
		record.setGuiyls(Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, PjyjlField.GUIYLS)));
		record.setSlbhao(Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, PjyjlField.SLBHAO)));
		record.setJioybz(Bytes.toBoolean(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, PjyjlField.JIOYBZ)));
		record.setShoqgy(Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, PjyjlField.SHOQGY)));
		record.setQudaoo(Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, PjyjlField.QUDAOO)));
		record.setFileQuery(Bytes.toBoolean(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, PjyjlField.FILEQUERY)));

		record.setQueryStr(Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, PjyjlField.QUERYSTR)));
		record.setRunSucc(Bytes.toBoolean(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, PjyjlField.RUNSUCC)));
		record.setStartTime(Bytes.toLong(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, PjyjlField.STARTTIME)));
		record.setEndTime(Bytes.toLong(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, PjyjlField.ENDTIME)));
		return record;
	}

	/**
	 * 第一次查询获取总记录数
	 * 
	 * @author user
	 * 
	 */
	class ItemCounter extends AbstractQuery<String> {
		public ItemCounter(String tableName) {
			super(tableName);
		}

		/**
		 * 获取交易明细的总数量
		 * 
		 * @param scan
		 *            查询总数量的scan
		 * @return
		 * @throws IOException
		 */
		public List<String> getCounts(Scan scan) throws IOException {
			scan.setFilter(new KeyOnlyFilter());
			return this.scan(scan);
		}

		@Override
		protected String parse(Result result) throws IOException {
			String key = Bytes.toString(result.getRow());
			return key;
		}
	}
}