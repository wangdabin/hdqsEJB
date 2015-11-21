package com.ceb.hdqs.action.query0775;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;

import com.ceb.bank.htable.AbstractQuery;
import com.ceb.hdqs.action.query.Enum0775Index;
import com.ceb.hdqs.action.query.exception.ConditionNotExistException;
import com.ceb.hdqs.action.query.exception.InvalidRecordParameterException;
import com.ceb.hdqs.entity.PjyjlEO;
import com.ceb.hdqs.entity.result.Handle0775QueryResult;
import com.ceb.hdqs.query.utils.QueryConfUtils;
import com.ceb.hdqs.query.utils.QueryConstants;
import com.ceb.hdqs.utils.DateFormatUtils;
import com.ceb.hdqs.utils.TimerUtils;
import com.ceb.hdqs.wtc.form.Handle0775Form;

/**
 * 查询日志查询，每次发起的查询交易都会记录到数据库中、该类完成对用户查询记录的查询
 * <p>
 * 查询的输入条件： 交易柜员|交易机构|日期、 交易柜员|交易码|日期、 交易机构|交易码|日期、 交易柜员|日期、 交易机构|日期、 交易码、日期
 * 
 * @author user
 * 
 */
public class HandleQuery0775 {

	private static final Log LOG = LogFactory.getLog(HandleQuery0775.class);
	private String indexTableName;
	private byte[] indexStartRowKey;
	private byte[] indexEndRowKey;
	private final long DAY_MILLS = 24 * 3600 * 1000;

	// 交易柜员
	// 交易机构
	// 交易码（不包含0775）
	// 交易流水号

	/**
	 * 生成查询的rowkey
	 * 
	 * @param from
	 * @return
	 */
	private void getRowKey(Handle0775Form form) {
		StringBuilder inputField = new StringBuilder();
		if (StringUtils.isNotEmpty(form.getJio1gy()))
			inputField.append(Enum0775Index.jio1gy.getValue());
		if (StringUtils.isNotEmpty(form.getYngyjg()))
			inputField.append(Enum0775Index.jio1jg.getValue());
		if (StringUtils.isNotEmpty(form.getJiaoym()))
			inputField.append(Enum0775Index.jio1code.getValue());
		if (StringUtils.isNotEmpty(form.getGuiyls()))
			inputField.append(Enum0775Index.jio1seq.getValue());
		String queryStr = inputField.toString();
		LOG.debug("queryStr是：" + queryStr);

		// 根据输入的字段结果进行生成rowkey

		StringBuilder startIndexKey = new StringBuilder();
		StringBuilder endIndexKey = new StringBuilder();
		String rowKeySplit = QueryConfUtils.getActiveConfig().get(QueryConstants.ROWKEY_SPLITTER, "|");

		if (queryStr.contains("4")) {
			LOG.debug("输入的柜员流水号，直接查询日志表！");
			long startDate = 0;
			long endDate = 0;
			try {
				startDate = DateFormatUtils.parseDate(form.getQishrq()).getTime();
				endDate = DateFormatUtils.parseDate(form.getZzhirq()).getTime() + DAY_MILLS;

				LOG.debug("起始时间：" + startDate + "结束时间：" + endDate);
			} catch (ParseException e) {
				LOG.error(e.getMessage(), e);
			}

			indexStartRowKey = Bytes.toBytes(startIndexKey.append(form.getGuiyls()).append(rowKeySplit).append(startDate).append(rowKeySplit).append(QueryConstants.MIN_NUM)
					.toString());
			indexEndRowKey = Bytes.toBytes(endIndexKey.append(form.getGuiyls()).append(rowKeySplit).append(endDate).append(rowKeySplit).append(QueryConstants.MAX_CHAR).toString());
			indexTableName = QueryConstants.HBASE_TABLE_NAME_EXCHANGE_RECORD;
			return;
		}
		int intPut = Integer.parseInt(queryStr);
		switch (intPut) {
		// case 0:
		// 和case2
		// case 1:// 只输入柜员的时候
		// 和case12
		case 2:// 只输入营业机构
			LOG.debug("输入的查询条件是营业机构");
			indexStartRowKey = Bytes.toBytes(startIndexKey.append(form.getYngyjg()).append(rowKeySplit).append(form.getQishrq()).append(rowKeySplit).append(QueryConstants.MIN_NUM)
					.toString());
			indexEndRowKey = Bytes.toBytes(endIndexKey.append(form.getYngyjg()).append(rowKeySplit).append(form.getZzhirq()).append(rowKeySplit).append(QueryConstants.MAX_CHAR)
					.toString());
			indexTableName = QueryConstants.HBASE_TABLE_NAME_INDEX_JG;
			break;
		// case 3:// 只输入交易码
		// 和case23
		case 12:// 输入柜员和营业机构
			LOG.debug("输入的查询条件是柜员和营业机构");
			indexStartRowKey = Bytes.toBytes(startIndexKey.append(form.getJio1gy()).append(rowKeySplit).append(form.getYngyjg()).append(rowKeySplit).append(form.getQishrq())
					.append(rowKeySplit).append(QueryConstants.MIN_NUM).toString());
			indexEndRowKey = Bytes.toBytes(endIndexKey.append(form.getJio1gy()).append(rowKeySplit).append(form.getYngyjg()).append(rowKeySplit).append(form.getZzhirq())
					.append(rowKeySplit).append(QueryConstants.MAX_CHAR).toString());
			indexTableName = QueryConstants.HBASE_TABLE_NAME_INDEX_GYJG;
			break;
		// case 13:// 输入柜员和交易码
		// 和case123
		case 23:// 输入营业机构和交易码
			LOG.debug("输入的查询条件是营业机构和交易码");
			indexStartRowKey = Bytes.toBytes(startIndexKey.append(form.getYngyjg()).append(rowKeySplit).append(form.getJiaoym()).append(rowKeySplit).append(form.getQishrq())
					.append(rowKeySplit).append(QueryConstants.MIN_NUM).toString());
			indexEndRowKey = Bytes.toBytes(endIndexKey.append(form.getYngyjg()).append(rowKeySplit).append(form.getJiaoym()).append(rowKeySplit).append(form.getZzhirq())
					.append(rowKeySplit).append(QueryConstants.MAX_CHAR).toString());
			indexTableName = QueryConstants.HBASE_TABLE_NAME_INDEX_JGCODE;
			break;
		case 123:
			LOG.debug("输入的查询条件是柜员、营业机构和交易码");
			indexStartRowKey = Bytes.toBytes(startIndexKey.append(form.getJio1gy()).append(rowKeySplit).append(form.getYngyjg()).append(rowKeySplit).append(form.getJiaoym())
					.append(rowKeySplit).append(form.getQishrq()).append(rowKeySplit).append(QueryConstants.MIN_NUM).toString());
			indexEndRowKey = Bytes.toBytes(endIndexKey.append(form.getJio1gy()).append(rowKeySplit).append(form.getYngyjg()).append(rowKeySplit).append(form.getJiaoym())
					.append(rowKeySplit).append(form.getZzhirq()).append(rowKeySplit).append(QueryConstants.MAX_CHAR).toString());
			indexTableName = QueryConstants.HBASE_TABLE_NAME_INDEX_GYJGCODE;
			break;
		}

		LOG.debug("indexStartRowKey :　" + Bytes.toString(indexStartRowKey));
		LOG.debug("indexEndRowKey : " + Bytes.toString(indexEndRowKey));
		LOG.debug("需要查询的索引表：" + indexTableName);

	}

	/**
	 * 日志查询的查询类
	 * 
	 * @return
	 * @throws IOException
	 * @throws InvalidRecordParameterException
	 */
	@Deprecated
	public Handle0775QueryResult query(Handle0775Form form, String excludeCode) throws IOException, ConditionNotExistException {
		return null;
	}

	/**
	 * 查询日志文件
	 * 
	 * @param form
	 *            输入查询的条件
	 * @param excludeCode
	 *            排除查询的受理编码
	 * @param start
	 *            开始条数
	 * @param limit
	 *            查询的条数
	 * @return
	 * @throws IOException
	 * @throws ConditionNotExistException
	 */
	public List<PjyjlEO> nextPage(List<String> queryList) throws IOException, ConditionNotExistException {
		Handle0775ItemQuery itemQuery = new Handle0775ItemQuery(QueryConstants.HBASE_TABLE_NAME_EXCHANGE_RECORD);
		List<PjyjlEO> itemResult = itemQuery.queryItem(queryList);
		return itemResult;
	}

	/**
	 * 带辖内的查询过程，直接返回所有的数据
	 * 
	 * @param form
	 * @param yngyjgList
	 * @return
	 * @throws ConditionNotExistException
	 * @throws IOException
	 */
	public List<PjyjlEO> queryAll(List<String> queryList) throws IOException, ConditionNotExistException {
		return nextPage(queryList);
	}

	/**
	 * 计算符合form条件（exCode排除excludeCode）的总记录数
	 * 
	 * @param form
	 * @param excludeCode
	 * @return
	 * @throws IOException
	 * @throws ConditionNotExistException
	 */
	public List<String> getCounts(Handle0775Form form, String excludeCode) throws IOException, ConditionNotExistException {
		// 根据输入条件解析出要查询的索引表
		TimerUtils timer = new TimerUtils();
		timer.start();
		getRowKey(form);
		IndexQuery indexQuery = new IndexQuery(indexTableName, excludeCode);
		List<String> keys = indexQuery.getItemRowkey();

		if (keys == null || keys.isEmpty()) {
			LOG.debug("Condition Not Exist " + form.getYngyjg());
			keys = Collections.emptyList();
		}
		timer.stop();
		LOG.info("Get item keys and num Cost time(ms) = " + timer.getExecutionTime());
		return keys;
	}

	/**
	 * 选择辖内条件下， 计算符合form条件（exCode排除excludeCode）的总记录数
	 * 
	 * @param form
	 * @param yngyjgList
	 * @param excludeCode
	 * @return
	 * @throws IOException
	 * @throws ConditionNotExistException
	 */
	public List<String> getCounts(Handle0775Form form, List<String> yngyjgList, String excludeCode) throws IOException, ConditionNotExistException {
		TimerUtils timer = new TimerUtils();
		timer.start();
		// 拿到所有机构的rowkey
		List<String> keyMap = new ArrayList<String>();

		LogQueryTraceQueue queue = new LogQueryTraceQueue();
		LogQueryQueue queryQueue = new LogQueryQueue();
		queryQueue.process(form, yngyjgList, queue, excludeCode);
		keyMap = queue.loopSwitch();
		timer.stop();
		LOG.debug("Get item keys and num Cost time(ms) = " + timer.getExecutionTime());

		LOG.info("总数是：" + keyMap.size());
		return keyMap;
	}

	/**
	 * 查询索引，找出LogItemQuery需要的rowkey
	 * 
	 * @author user
	 * 
	 */
	class IndexQuery extends AbstractQuery<String> {
		private String excludeCode;

		public IndexQuery(String tableName, String excludeCode) {
			super(tableName);
			this.excludeCode = excludeCode;
		}

		public List<String> getItemRowkey() throws IOException {
			Scan scanner = new Scan();
			scanner.addFamily(QueryConstants.HBASE_TABLE_FAMILY_BYTE);
			scanner.setStartRow(indexStartRowKey);
			scanner.setStopRow(indexEndRowKey);
			scanner.setCaching(1000);
			List<String> itemRowkey = new ArrayList<String>();
			if (QueryConstants.HBASE_TABLE_NAME_EXCHANGE_RECORD.equals(indexTableName)) {

				Handle0775ItemQuery query = new Handle0775ItemQuery(indexTableName);
				itemRowkey = query.getCounts(scanner);
			} else {
				itemRowkey = scan(scanner);
			}
			return itemRowkey;
		}

		@Override
		public String parse(Result result) {
			String row = Bytes.toString(result.getRow());
			String jiaoym = row.substring(row.lastIndexOf("|") + 1);
			if (excludeCode.equals(jiaoym)) {
				return null;
			}
			byte[] itemRow = result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, Bytes.toBytes("Q"));

			if (itemRow == null) {
				return null;
			}
			String rowKey = Bytes.toString(itemRow);
			return rowKey;
		}
	}

	public static void main(String[] args) {
		Configuration conf = new Configuration(false);
		conf.addResource("cebank-site-201.xml");
	}
}