package com.ceb.hdqs.action.query.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.FilterList.Operator;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;

import com.ceb.hdqs.action.query.IconditionProcessor;
import com.ceb.hdqs.action.query.ItemQueryProcessor;
import com.ceb.hdqs.action.query.exception.ConditionNotExistException;
import com.ceb.hdqs.action.query.exception.InvalidRecordParameterException;
import com.ceb.hdqs.action.query0773.Handle0773ItemQuery;
import com.ceb.hdqs.entity.PybjyEO;
import com.ceb.hdqs.entity.field.AkhzhField;
import com.ceb.hdqs.entity.result.KehzhParserResult;
import com.ceb.hdqs.entity.result.ReplacementCardItemResult;
import com.ceb.hdqs.entity.result.ReplacementCardParseResult;
import com.ceb.hdqs.entity.result.ZhangHParseResult;
import com.ceb.hdqs.query.entity.CustomerInfo;
import com.ceb.hdqs.query.entity.EnumQueryKind;
import com.ceb.hdqs.query.entity.Page;
import com.ceb.hdqs.query.entity.SkyPair;
import com.ceb.hdqs.query.utils.QueryConfUtils;
import com.ceb.hdqs.query.utils.QueryConstants;
import com.ceb.hdqs.query.utils.QueryMethodUtils;

/**
 * 换卡查询条件（客户账号）解析 首先根据{@link InputKHZHParser} 解析出客户号和主账号
 * <P>
 * 然后根据客户号查询出客户中文名
 * <p>
 * 根据主账号查询该卡的换卡记录（此时查询出来的换卡记录中，每条记录的字段信息不全面，需要通过
 * 
 * 
 * @author user
 * 
 */

public class ParserOf0773 implements IconditionProcessor {
	private static final Log LOG = LogFactory.getLog(ParserOf0773.class);

	// private ReplacementCardParseResult<ReplacementCardItemResult>
	// cardParseResult;

	public ParserOf0773() {
	}

	@Override
	public ReplacementCardParseResult<ReplacementCardItemResult> parseCondition(PybjyEO record) throws ConditionNotExistException,
			Exception {

		InputKHZHParser inputParser = new InputKHZHParser(QueryConstants.TABLE_NAME_AKHZH);

		KehzhParserResult inputParseResult = inputParser.parseCondition(record);
		if (inputParseResult == null) {
			throw new ConditionNotExistException("客户账号" + record.getKehuzh() + "不存在");
		}
		if (inputParseResult.getZhanghParseResult().size() > 1) {
			throw new RuntimeException("客户账号" + record.getKehuzh() + "对应多个主账号");
		}
		Entry<SkyPair<String, EnumQueryKind>, ZhangHParseResult> zhanghInfo = inputParseResult.getZhanghParseResult().entrySet().iterator()
				.next();

		/*
		 * 根据客户号获取客户信息
		 */
		ReplacementCardParseResult<ReplacementCardItemResult> cardParseResult = new ReplacementCardParseResult<ReplacementCardItemResult>();
		CustomerInfo customerInfo = QueryMethodUtils.getCustomerChineseName(inputParseResult.getKehhao());
		cardParseResult.setKehhao(customerInfo.getKehhao());
		cardParseResult.setKehzwm(customerInfo.getKehzwm());
		cardParseResult.setKehuzh(record.getKehuzh());
//		cardParseResult.setRecord(record);
		// 配置授权验证需要的信息
		cardParseResult.setShifbz(customerInfo.getShfobz());
//		cardParseResult.setZHYYNG(inputParseResult.getZHYYNG());

		/*
		 * 开始查询AKHZH_0773表，查询出该客户账号的换卡记录
		 * 
		 * 假设，如果在上面的AKHZH表中已经查询出该客户账号对应的信息，则在AKHZH_0773中至少对应一条该客户账号的信息（没有换卡记录的的时候
		 * ）
		 */
		ZhangHParseResult zhanghParseResult = zhanghInfo.getValue();
//		zhanghParseResult.setKehhao(customerInfo.getKehhao());
		zhanghParseResult.setKehzwm(customerInfo.getKehzwm());
		LOG.info("客户账号" + record.getKehuzh() + "解析出的主账号是：" + zhanghParseResult.getZHANGH());
		Handle0773ItemQuery itemQuery = (Handle0773ItemQuery) inputParseResult.getQueryExectuer();
		Page<ReplacementCardItemResult> itemParseResult = itemQuery.nextPage(zhanghParseResult, null);
		if (itemParseResult == null) {
			// 如果在AKHZH_0773中没有查询出具体的明细，则产生了数据不一致问题(AKHZHhe
			// AKHZH_0773的数据不一致)，则直接返回输入文件的基本信息

			LOG.error("客户账号" + record.getKehuzh() + "在AKHZH0773中不存在，需要检查数据一致性！！");
			List<ReplacementCardItemResult> items = new ArrayList<ReplacementCardItemResult>();
			ReplacementCardItemResult itemRes = new ReplacementCardItemResult();
			itemRes.setKahaoo(record.getKehuzh());
			itemRes.setKehhao(customerInfo.getKehhao());
			itemRes.setJiluzt("0");
			items.add(itemRes);
			cardParseResult.setCardParseResult(items);

		} else {
			cardParseResult.setCardParseResult(itemParseResult.getPageItem());
		}

		cardParseResult.setRecord(record);
		return cardParseResult;
	}

	/**
	 * 解析输入的客户账号，解析出该客户账号对应的主账号和客户号 过滤条件：“HUOBDH=‘01’（人民币） 、
	 * ZHHUXZ=‘0001’（普通活期）、 CHUIBZ=‘0’ （钞户）、KHZHLX=‘1’（卡）”
	 * 
	 * @author user
	 * 
	 */

	class InputKHZHParser extends KhzhParser {
		// private KehzhParserResult kehzhParseResult;

		public InputKHZHParser(String tableName) {
			super(tableName);
		}

		@Override
		protected ZhangHParseResult parse(Result result) throws IOException {

			ZhangHParseResult zhjxResult = new ZhangHParseResult();
			String KEHHAO = Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AkhzhField.KEHHAO));
			String ZHANGH = Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AkhzhField.ZHUZZH));
			LOG.debug("解析出的账号是：" + ZHANGH);
//			String kehuzh = Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AkhzhField.KEHUZH));
//			String HUOBDH = Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AkhzhField.HUOBDH));
//			String KHZHLX = Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AkhzhField.KHZHLX));

			zhjxResult.setKehhao(KEHHAO);
			zhjxResult.setZHANGH(ZHANGH);
//			zhjxResult.setKehuzh(kehuzh);
//			zhjxResult.setHUOBDH(HUOBDH);
//			zhjxResult.setKHZHLX(KHZHLX);
			return zhjxResult;

		}

		@Override
		public KehzhParserResult parseCondition(PybjyEO record) throws IOException, InvalidRecordParameterException, Exception {
			ItemQueryProcessor queryEx = new Handle0773ItemQuery();
			List<ZhangHParseResult> parseResult = this.scan(buildParseScanner(record));
			if (parseResult == null || parseResult.isEmpty()) {
				return null;
			}
			// 初始化KehzhParseResult，保存解析出的客户账号的信息
			KehzhParserResult kehzhParseResult = new KehzhParserResult();
			for (ZhangHParseResult zhanghParseResult : parseResult) {
				zhanghParseResult.setStartDate(record.getStartDate());
				zhanghParseResult.setEndDate(record.getEndDate());
				/*
				 * 该查询日期需要根据输出格式进行格式化输出
				 */
				zhanghParseResult.setQueryDate(record.getJioyrq());
				zhanghParseResult.setRecord(record);
				zhanghParseResult.setQueryExectuer(queryEx);
				kehzhParseResult.getZhanghParseResult().put(
						new SkyPair<String, EnumQueryKind>(zhanghParseResult.getZHANGH(), EnumQueryKind.QK1), zhanghParseResult);
			}

			kehzhParseResult.setKehhao(parseResult.get(0).getKehhao());
			kehzhParseResult.setKehuzh(record.getKehuzh());
			// kehzhParseResult.setKehzwm(parseResult.get(0).getKehzwm());
			kehzhParseResult.setKhzhlx("1");
			kehzhParseResult.setQueryExectuer(queryEx);

			return kehzhParseResult;

		}

		@Override
		public Scan buildParseScanner(PybjyEO record) {
			return ParserOf0773.this.buildParseScanner(record);
		}
	}

	@Override
	public Scan buildParseScanner(PybjyEO record) {

		String rowKeySplit = QueryConfUtils.getActiveConfig().get(QueryConstants.ROWKEY_SPLITTER, "|");
		String rowKey = buildRowKey(record);

		Scan scan = new Scan();
		// 货币类型:人民币
		String HUOBDH = "01";
		// 活期账号

		// 设置货币代号过滤器
		SingleColumnValueFilter huobiFileter = new SingleColumnValueFilter(QueryConstants.HBASE_TABLE_FAMILY_BYTE, Bytes.toBytes("HUOBDH"),
				CompareOp.EQUAL, Bytes.toBytes(HUOBDH));
		// 设置记录状态过滤器

		// ZHHUXZ=‘0001’（普通活期）、
		String ZHHUXZ = "0001";
		SingleColumnValueFilter zhhuxzFileter = new SingleColumnValueFilter(QueryConstants.HBASE_TABLE_FAMILY_BYTE,
				Bytes.toBytes("ZHHUXZ"), CompareOp.EQUAL, Bytes.toBytes(ZHHUXZ));
		// CHUIBZ=‘0’ （钞户）、KHZHLX=‘1’（卡）”
		String CHUIBZ = "0";
		SingleColumnValueFilter chuibzFileter = new SingleColumnValueFilter(QueryConstants.HBASE_TABLE_FAMILY_BYTE,
				Bytes.toBytes("CHUIBZ"), CompareOp.EQUAL, Bytes.toBytes(CHUIBZ));

		// KHZHLX=‘1’（卡）”
		String KHZHLX = "1";
		SingleColumnValueFilter khzhlxFileter = new SingleColumnValueFilter(QueryConstants.HBASE_TABLE_FAMILY_BYTE,
				Bytes.toBytes("KHZHLX"), CompareOp.EQUAL, Bytes.toBytes(KHZHLX));

		scan.addFamily(QueryConstants.HBASE_TABLE_FAMILY_BYTE);
		scan.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AkhzhField.ZHUZZH);
		scan.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AkhzhField.KEHHAO);
		scan.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AkhzhField.HUOBDH);
		scan.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AkhzhField.ZHHUXZ);
		scan.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AkhzhField.CHUIBZ);
		scan.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AkhzhField.KHZHLX);
		// scan.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE,
		// KhzhField.ZHYYJG);

		scan.setStartRow(Bytes.toBytes(rowKey.concat(rowKeySplit).concat(QueryConstants.MIN_NUM)));
		scan.setStopRow(Bytes.toBytes(rowKey.concat(rowKeySplit).concat(QueryConstants.MAX_CHAR)));

		FilterList list = new FilterList(Operator.MUST_PASS_ALL);
		list.addFilter(huobiFileter);
		list.addFilter(zhhuxzFileter);
		list.addFilter(chuibzFileter);
		list.addFilter(khzhlxFileter);
		scan.setFilter(list);

		try {
			LOG.info(scan.toJSON());
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		}
		return scan;
	}

	private String buildRowKey(PybjyEO condition) {
		String rowReg = QueryConfUtils.getActiveConfig().get(QueryConstants.ROWKEY_SPLITTER, "|");
		// 查询账号解析表AKHZH,rowKey为：客户账号、货币代码、科目存储
		String account = StringUtils.reverse(condition.getKehuzh());// 将客户账号进行翻转
		String KHZHLX = "1";
		StringBuilder row = new StringBuilder();
		row.append(account).append(rowReg).append(KHZHLX);
		return row.toString();
	}
}
