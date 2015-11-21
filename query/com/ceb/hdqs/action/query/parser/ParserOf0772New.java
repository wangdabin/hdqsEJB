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
import org.apache.hadoop.hbase.util.Bytes;

import com.ceb.hdqs.action.query.IconditionProcessor;
import com.ceb.hdqs.action.query.ItemQueryProcessor;
import com.ceb.hdqs.action.query.exception.ConditionNotExistException;
import com.ceb.hdqs.action.query.exception.InvalidRecordParameterException;
import com.ceb.hdqs.action.query0772.Handle0772ItemQuery;
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

public class ParserOf0772New implements IconditionProcessor {
	private static final Log LOG = LogFactory.getLog(ParserOf0772New.class);

	private KhzhParser khzhParser;
	private VyktdParser vyktdParser;

	public ParserOf0772New() {
		vyktdParser = new VyktdParser();
		khzhParser = new KhzhParser(QueryConstants.TABLE_NAME_AKHZH) {
			@Override
			public Scan buildParseScanner(PybjyEO record) throws InvalidRecordParameterException {
				return ParserOf0772New.this.buildParseScanner(record);
			}

			@Override
			protected ZhangHParseResult parse(Result result) throws IOException {
				ZhangHParseResult zhjxResult = new ZhangHParseResult();
				String KEHHAO = Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AkhzhField.KEHHAO));
				String HUOBDH = Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AkhzhField.HUOBDH));
				String CHUIBZ = Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AkhzhField.CHUIBZ));
				String ZHANGH = Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AkhzhField.ZHUZZH));
				String KHZHLX = Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AkhzhField.KHZHLX));
				zhjxResult.setKHZHLX(KHZHLX);
				zhjxResult.setKehhao(KEHHAO);
				zhjxResult.setHUOBDH(HUOBDH);
				zhjxResult.setCHUIBZ(CHUIBZ);
				zhjxResult.setZHANGH(ZHANGH);
				return zhjxResult;
			}

			@Override
			public KehzhParserResult parseCondition(PybjyEO condition) throws IOException, InvalidRecordParameterException, Exception {

				if (QueryConstants._0772_KHZHLX_CUNDAN.equals(condition.getKhzhlx())
						|| QueryConstants._0772_KHZHLX_CXCKCZ.equals(condition.getKhzhlx())
						|| QueryConstants._0772_KHZHLX_GUOZHAI.equals(condition.getKhzhlx())) {
					// 如果输入的客户账号类型是存单、国债、储蓄存款存折则查询表AZHJX_0772进行解析
					// doSmothing to Parse zhangh
					Khzh_zhParser parser = new Khzh_zhParser();
					return parser.parseCondition(condition);
				} else {
					List<ZhangHParseResult> parseResult = this.scan(buildParseScanner(condition));

					if (parseResult == null || parseResult.isEmpty()) {
						return null;
					}
					// 查询客户号的信息，该客户的姓名和是否标志

					String khh = parseResult.get(0).getKehhao();
					CustomerInfo info = QueryMethodUtils.getCustomerChineseName(khh);
					// 初始化KehzhParseResult，保存解析出的客户账号的信息
					KehzhParserResult kehzhParseResult = new KehzhParserResult();

					for (ZhangHParseResult zhanghParseResult : parseResult) {
						zhanghParseResult.setKehzwm(info.getKehzwm());
						zhanghParseResult.setKehuzh(condition.getKehuzh());
						zhanghParseResult.setStartDate(condition.getStartDate());
						zhanghParseResult.setEndDate(condition.getEndDate());
						/*
						 * 该查询日期需要根据输出格式进行格式化输出
						 */
						zhanghParseResult.setQueryDate(condition.getJioyrq());
						zhanghParseResult.setRecord(condition);

						ItemQueryProcessor queryProcessor = new Handle0772ItemQuery();
						zhanghParseResult.setQueryExectuer(queryProcessor);

						kehzhParseResult.getZhanghParseResult().put(
								new SkyPair<String, EnumQueryKind>(zhanghParseResult.getZHANGH(), EnumQueryKind.QK1), zhanghParseResult);
					}
					// 赋值发卡/折机构 及名称
					if (!HdqsConstants.KHZHLX_CARD.equals(condition.getKhzhlx())) {
						LOG.debug("客户账号类型是：" + condition.getKhzhlx());
						Azhjx0772Parser parser = new Azhjx0772Parser();
						parser.addKhzhParseInfo(kehzhParseResult);
					}
					kehzhParseResult.setRecord(condition);
					kehzhParseResult.setKehhao(khh);
					kehzhParseResult.setKehuzh(condition.getKehuzh());
					kehzhParseResult.setKehzwm(info.getKehzwm());
					kehzhParseResult.setShfobz(info.getShfobz());
					kehzhParseResult.setKhzhjb(info.getKhzhjb());
					kehzhParseResult.setKhzhlx(condition.getKhzhlx());
					kehzhParseResult.setHuobdh(parseResult.get(0).getHUOBDH());
					kehzhParseResult.setChohbz(parseResult.get(0).getCHUIBZ());
					return kehzhParseResult;
				}
			}
		};
	}

	@Override
	public Scan buildParseScanner(PybjyEO record) {
		StringBuilder rowkey = buildKhzhParseRowKey(record);
		String rowSplitReg = QueryConfUtils.getActiveConfig().get(QueryConstants.ROWKEY_SPLITTER, "|");
		Scan scan = null;
		if (StringUtils.isBlank(record.getShunxh())||record.getShunxh().equals("0000")) {
			LOG.debug("顺序号为空的查询...");
			scan = new Scan();
			scan.setStartRow(Bytes.toBytes(rowkey + rowSplitReg + QueryConstants.MIN_NUM));
			scan.setStopRow(Bytes.toBytes(rowkey + rowSplitReg + QueryConstants.MAX_CHAR));
		} else {
			LOG.debug("顺序号不为空的查询...");
			rowkey.append(rowSplitReg).append(record.getShunxh());
			scan = new Scan();
			scan.setStartRow(Bytes.toBytes(rowkey.toString()));
			scan.setStopRow(Bytes.toBytes(rowkey.toString()));
		}
		try {
			LOG.debug(record.getSlbhao() + "的查询条件:" + scan.toJSON());
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		}
		return scan;
	}

	private StringBuilder buildKhzhParseRowKey(PybjyEO record) {
		String rowReg = QueryConfUtils.getActiveConfig().get(QueryConstants.ROWKEY_SPLITTER, "|");
		String account = StringUtils.reverse(record.getKehuzh());
		String accountType = record.getKhzhlx();
		StringBuilder row = new StringBuilder();
		row.append(account.trim()).append(rowReg).append(accountType);

		return row;
	}

	@Override
	public KehzhParserResult parseCondition(PybjyEO record) throws ConditionNotExistException, IOException,
			InvalidRecordParameterException, Exception {

		KhzhParserManager manager = new KhzhParserManager();
		List<KhzhParser> parser = new ArrayList<KhzhParser>();
		parser.add(khzhParser);
		if (record.getKhzhlx().equals(HdqsConstants.KHZHLX_CARD)) {
			parser.add(vyktdParser);
		}
		KhzhParseTraceQueue queue = new KhzhParseTraceQueue();
		manager.process(record, parser, queue);

		Map<String, KehzhParserResult> parseRsultMap = queue.loopSwitch();

		// 首先判断是否为空，如果没有查询出具体的值，则直接返回
		if (parseRsultMap == null || parseRsultMap.get(QueryConstants.TABLE_NAME_AKHZH) == null) {
			throw new ConditionNotExistException("客户账号" + record.getKehuzh() + "不存在");
		}

		KehzhParserResult parseResult = parseRsultMap.get(QueryConstants.TABLE_NAME_AKHZH);
		KehzhParserResult vyktdResult = null;
		if (record.getKhzhlx().equals(HdqsConstants.KHZHLX_CARD)) {
			vyktdResult = parseRsultMap.get(QueryConstants.TABLE_NAME_VYKTD);
		}

		LOG.info("查询AKHZH查询出的账号的个数是：" + parseRsultMap.size());
		// 如果是卡，则需要将VYKTD查询出的结果赋值到 AKHZH查询出的结果的对应地方
		if (record.getKhzhlx().equals(HdqsConstants.KHZHLX_CARD) && vyktdResult != null) {
			parseResult.setZHYYNG(vyktdResult.getZHYYNG());
			// 需要改造成查询名称
			parseResult.setJIGOMC(vyktdResult.getJIGOMC());
			for (Entry<SkyPair<String, EnumQueryKind>, ZhangHParseResult> zhanghInfo : parseResult.getZhanghParseResult().entrySet()) {
				LOG.debug("客户账号类为卡，设置账户营业机构在卡一层设置");
				zhanghInfo.getValue().setZHYYNG(vyktdResult.getZHYYNG());
				zhanghInfo.getValue().setJIGOMC(vyktdResult.getJIGOMC());
			}
		}
		ItemQueryProcessor queryExecutor = new Handle0772ItemQuery();
		for (Entry<SkyPair<String, EnumQueryKind>, ZhangHParseResult> zhanghInfo : parseResult.getZhanghParseResult().entrySet()) {
			zhanghInfo.getValue().setZhanghTotal(parseResult.getZhanghParseResult().size());
			zhanghInfo.getValue().setQueryExectuer(queryExecutor);
		}

		if (parseResult != null) {
			parseResult.setQueryExectuer(queryExecutor);
		}
		return parseResult;
	}

}
