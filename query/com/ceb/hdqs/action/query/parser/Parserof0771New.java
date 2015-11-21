package com.ceb.hdqs.action.query.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
import com.ceb.hdqs.action.query0771.Handle0771ItemQuery;
import com.ceb.hdqs.constants.HdqsConstants;
import com.ceb.hdqs.entity.PybjyEO;
import com.ceb.hdqs.entity.field.AkhzhField;
import com.ceb.hdqs.entity.result.KehzhParserResult;
import com.ceb.hdqs.entity.result.ZhangHParseResult;
import com.ceb.hdqs.query.entity.CustomerInfo;
import com.ceb.hdqs.query.entity.EnumQueryKind;
import com.ceb.hdqs.query.entity.SkyPair;
import com.ceb.hdqs.query.utils.QueryConfUtils;
import com.ceb.hdqs.query.utils.QueryConstants;
import com.ceb.hdqs.query.utils.QueryMethodUtils;

/**
 * 解析客户账号 0771,0772查询过程中需要解析出AKHZH，AZHJX，如果是卡，则需要解析VYKTD查询出对应的数据
 * 
 * @author user
 */
public class Parserof0771New implements IconditionProcessor {
	private static final Log LOG = LogFactory.getLog(Parserof0771New.class);

	private KhzhParser khzhParser;
	private VyktdParser vyktdParser;

	public Parserof0771New() {
		vyktdParser = new VyktdParser();
		khzhParser = new KhzhParser(QueryConstants.TABLE_NAME_AKHZH) {
			@Override
			public Scan buildParseScanner(PybjyEO record) throws InvalidRecordParameterException {
				return Parserof0771New.this.buildParseScanner(record);
			}

			@Override
			protected ZhangHParseResult parse(Result result) throws IOException {
				ZhangHParseResult zhjxResult = new ZhangHParseResult();
				String KEHHAO = Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AkhzhField.KEHHAO));
				String HUOBDH = Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AkhzhField.HUOBDH));
				String CHUIBZ = Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AkhzhField.CHUIBZ));
				String ZHANGH = Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AkhzhField.ZHUZZH));
				zhjxResult.setKehhao(KEHHAO);
				String KHZHLX = Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AkhzhField.KHZHLX));
				zhjxResult.setHUOBDH(HUOBDH);
				zhjxResult.setCHUIBZ(CHUIBZ);
				zhjxResult.setZHANGH(ZHANGH);
				zhjxResult.setKHZHLX(KHZHLX);
				return zhjxResult;

			}

			@Override
			public KehzhParserResult parseCondition(PybjyEO record) throws IOException, InvalidRecordParameterException, Exception {
				List<ZhangHParseResult> list = this.scan(buildParseScanner(record));
				if (list == null || list.isEmpty()) {
					return null;
				}

				// 查询客户号的信息，该客户的姓名和是否标志
				String kehhao = list.get(0).getKehhao();
				CustomerInfo info = QueryMethodUtils.getCustomerChineseName(kehhao);

				// 初始化KehzhParseResult，保存解析出的客户账号的信息
				KehzhParserResult kehzhParseResult = new KehzhParserResult();
				for (ZhangHParseResult zhanghParseResult : list) {
					zhanghParseResult.setKehzwm(info.getKehzwm());
					zhanghParseResult.setKehuzh(record.getKehuzh());
					zhanghParseResult.setStartDate(record.getStartDate());
					zhanghParseResult.setEndDate(record.getEndDate());
					/*
					 * 该查询日期需要根据输出格式进行格式化输出
					 */
					zhanghParseResult.setQueryDate(record.getJioyrq());
					// zhanghParseResult.setQueryTpye(Integer.parseInt(record.getChaxlx()));
					zhanghParseResult.setRecord(record);

					ItemQueryProcessor queryProcessor = new Handle0771ItemQuery();
					zhanghParseResult.setQueryExectuer(queryProcessor);
					kehzhParseResult.getZhanghParseResult().put(
							new SkyPair<String, EnumQueryKind>(zhanghParseResult.getZHANGH(), EnumQueryKind.QK1), zhanghParseResult);
				}

				if (!HdqsConstants.KHZHLX_CARD.equals(record.getKhzhlx())) {
					Azhjx0772Parser parser = new Azhjx0772Parser();
					parser.addKhzhParseInfo(kehzhParseResult);// 设置ZHYYJG和JIGOMC
				}
				kehzhParseResult.setRecord(record);
				kehzhParseResult.setKehhao(list.get(0).getKehhao());
				kehzhParseResult.setKehzwm(info.getKehzwm());
				kehzhParseResult.setShfobz(info.getShfobz());
				kehzhParseResult.setKhzhjb(info.getKhzhjb());
				kehzhParseResult.setKhzhlx(record.getKhzhlx());
				kehzhParseResult.setKehuzh(record.getKehuzh());
				kehzhParseResult.setHuobdh(list.get(0).getHUOBDH());
				kehzhParseResult.setChohbz(list.get(0).getCHUIBZ());
				return kehzhParseResult;
			}

		};
	}

	/**
	 * 生成解析客户账号的Scan
	 * 
	 * @param condation
	 * @return
	 */
	@Override
	public Scan buildParseScanner(PybjyEO record) {
		String rowKeySplit = QueryConfUtils.getActiveConfig().get(QueryConstants.ROWKEY_SPLITTER, "|");
		String rowkey = buildKhzhParserRowKey(record);
		Scan scanner = new Scan();
		scanner.setStartRow(Bytes.toBytes(rowkey + rowKeySplit + QueryConstants.MIN_NUM));
		scanner.setStopRow(Bytes.toBytes(rowkey + rowKeySplit + QueryConstants.MAX_CHAR));

		SingleColumnValueFilter khzhlxFilter = new SingleColumnValueFilter(QueryConstants.HBASE_TABLE_FAMILY_BYTE, Bytes.toBytes("KHZHLX"),
				CompareOp.EQUAL, Bytes.toBytes(record.getKhzhlx()));
		SingleColumnValueFilter zhhuxzFilter = new SingleColumnValueFilter(QueryConstants.HBASE_TABLE_FAMILY_BYTE, Bytes.toBytes("ZHHUXZ"),
				CompareOp.EQUAL, Bytes.toBytes(record.getZhhuxz()));
		SingleColumnValueFilter huobiFilter = new SingleColumnValueFilter(QueryConstants.HBASE_TABLE_FAMILY_BYTE, Bytes.toBytes("HUOBDH"),
				CompareOp.EQUAL, Bytes.toBytes(record.getHuobdh()));

		SingleColumnValueFilter chaohuiFilter = new SingleColumnValueFilter(QueryConstants.HBASE_TABLE_FAMILY_BYTE,
				Bytes.toBytes("CHUIBZ"), CompareOp.EQUAL, Bytes.toBytes(record.getChuibz()));

		FilterList list = new FilterList(Operator.MUST_PASS_ALL);
		list.addFilter(khzhlxFilter);
		list.addFilter(zhhuxzFilter);
		list.addFilter(huobiFilter);
		LOG.debug("输入的钞汇标志是：" + record.getChuibz());
		if (record.getChuibz().equals("0") || record.getChuibz().equals("1")) {
			list.addFilter(chaohuiFilter);
		}

		// 输出Debug Scan的信息
		scanner.setFilter(list);
		try {
			LOG.debug("scan：" + scanner.toJSON());
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		}
		return scanner;
	}

	private String buildKhzhParserRowKey(PybjyEO record) {
		return StringUtils.reverse(record.getKehuzh());
	}

	@Override
	public KehzhParserResult parseCondition(PybjyEO record) throws IOException, ConditionNotExistException, Exception {

		KhzhParserManager manager = new KhzhParserManager();
		List<KhzhParser> parser = new ArrayList<KhzhParser>();
		parser.add(khzhParser);
		if (record.getKhzhlx().equals(HdqsConstants.KHZHLX_CARD)) {
			parser.add(vyktdParser);
		}
		KhzhParseTraceQueue queue = new KhzhParseTraceQueue();
		manager.process(record, parser, queue);

		Map<String, KehzhParserResult> parseRsultMap = queue.loopSwitch();

		// 合并从两个表中解析出的内容
		// 首先判断是否为空，如果没有查询出具体的值，则直接返回
		if (parseRsultMap == null || parseRsultMap.get(QueryConstants.TABLE_NAME_AKHZH) == null) {
			throw new ConditionNotExistException("客户账号" + record.getKehuzh() + "不存在");
		}

		KehzhParserResult parseResult = parseRsultMap.get(QueryConstants.TABLE_NAME_AKHZH);
		KehzhParserResult vyktdResult = null;
		if (HdqsConstants.KHZHLX_CARD.equals(record.getKhzhlx())) {
			vyktdResult = parseRsultMap.get(QueryConstants.TABLE_NAME_VYKTD);
		}

		LOG.info("0771查询AKHZH查询出的账号的个数是：" + parseRsultMap.size());
		// 如果是卡，则需要将VYKTD查询出的结果赋值到 AKHZH查询出的结果的对应地方
		if (record.getKhzhlx().equals(HdqsConstants.KHZHLX_CARD) && vyktdResult != null) {
			parseResult.setZHYYNG(vyktdResult.getZHYYNG());
			// 需要改造成查询名称
			parseResult.setJIGOMC(vyktdResult.getJIGOMC());
			for (Entry<SkyPair<String, EnumQueryKind>, ZhangHParseResult> zhanghInfo : parseResult.getZhanghParseResult().entrySet()) {
				LOG.debug("客户账号类为卡，设置账户营业机构在卡一层设置");
				ZhangHParseResult zhanghResult = zhanghInfo.getValue();
				zhanghResult.setZHYYNG(vyktdResult.getZHYYNG());
				zhanghResult.setJIGOMC(vyktdResult.getJIGOMC());
			}
		}
		ItemQueryProcessor queryExecutor = new Handle0771ItemQuery();
		if (parseResult != null) {
			parseResult.setQueryExectuer(queryExecutor);
		}

		for (Entry<SkyPair<String, EnumQueryKind>, ZhangHParseResult> zhanghInfo : parseResult.getZhanghParseResult().entrySet()) {
			zhanghInfo.getValue().setZhanghTotal(parseResult.getZhanghParseResult().size());
		}

		return parseResult;
	}

}
