package com.ceb.hdqs.action.query.parser;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.FilterList.Operator;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;

import com.ceb.hdqs.action.query.AbstractPrivateItemQuery;
import com.ceb.hdqs.action.query.IconditionProcessor;
import com.ceb.hdqs.action.query.ItemQueryProcessor;
import com.ceb.hdqs.action.query.exception.ConditionNotExistException;
import com.ceb.hdqs.action.query.exception.InvalidRecordParameterException;
import com.ceb.hdqs.action.query0770.Handle0770ItemQuery;
import com.ceb.hdqs.entity.PybjyEO;
import com.ceb.hdqs.entity.field.AkhzhField;
import com.ceb.hdqs.entity.result.KehzhParserResult;
import com.ceb.hdqs.entity.result.ZhangHParseResult;
import com.ceb.hdqs.query.entity.CustomerInfo;
import com.ceb.hdqs.query.entity.EnumKemucc;
import com.ceb.hdqs.query.entity.EnumQueryKind;
import com.ceb.hdqs.query.entity.SkyPair;
import com.ceb.hdqs.query.utils.QueryConfUtils;
import com.ceb.hdqs.query.utils.QueryConstants;
import com.ceb.hdqs.query.utils.QueryMethodUtils;

/**
 * 查询AKHZH解析输入的单位卡
 * <p>
 * 输出： KEHHAO│客户号 KHZHLX│客户帐号类型 ZHHUXZ│帐户性质 HUOBDH│货币代号 CHUIBZ│钞汇标志 ZHUZZH│主帐号
 * 
 * @author user
 * 
 */
public class ParserOf0781 extends AbstractPrivateItemQuery<ZhangHParseResult> implements IconditionProcessor {
	private static final Log LOG = LogFactory.getLog(ParserOf0781.class);

	private static final String KHZHLX_CARD = "1";
	private VyktdChecker checker = new VyktdChecker();
	private ZhKMJGParser zhjx = new ZhKMJGParser();

	public ParserOf0781() {
		super(QueryConstants.TABLE_NAME_AKHZH);
	}

	@Override
	public KehzhParserResult parseCondition(PybjyEO record) throws Exception {
		// 判断是否为单位卡
		KehzhParserResult kehzhParseResult = checker.validCard(record.getKehuzh());

		List<ZhangHParseResult> zhanghList = this.scan(buildParseScanner(record));
		if (zhanghList == null || zhanghList.size() == 0) {
			throw new ConditionNotExistException("查询条件" + record.getKehuzh() + "不存在");
		}
		// 查询客户号的信息，该客户的姓名和是否标志,客户评估级别
		String kehuhao = zhanghList.get(0).getKehhao();
		CustomerInfo kehuInfo = QueryMethodUtils.getCustomerChineseName(kehuhao);

		for (ZhangHParseResult zhanghParseResult : zhanghList) {
			// 查询客户AZHJX表查询出每个账号的KEMUCC属性以确定查询那个明细表
			zhjx.parseZhjx(zhanghParseResult);
			zhanghParseResult.setKehzwm(kehuInfo.getKehzwm());
			zhanghParseResult.setStartDate(record.getStartDate());
			zhanghParseResult.setEndDate(record.getEndDate());
			zhanghParseResult.setKehuzh(record.getKehuzh());
			zhanghParseResult.setJIGOMC(kehzhParseResult.getJIGOMC());
			zhanghParseResult.setZHYYNG(kehzhParseResult.getZHYYNG());
			/*
			 * 该查询日期需要根据输出格式进行格式化输出
			 */
			zhanghParseResult.setQueryDate(record.getJioyrq());
			zhanghParseResult.setRecord(record);

			zhanghParseResult.setZhanghTotal(zhanghList.size());
			// 确定查询AGHMX或者ASHMX
			EnumKemucc kemucc = null;
			try {
				kemucc = EnumKemucc.valueOf(zhanghParseResult.getKEMUCC());
			} catch (Exception e) {
				LOG.debug("账号" + zhanghParseResult.getZHANGH() + "的kemucc是" + zhanghParseResult.getKEMUCC() + ",查询放弃该账号!");
				continue;
			}

			LOG.debug("解析出的账号是：" + zhanghParseResult.getZHANGH());
			LOG.debug("解析出的kemucc的属性是：" + kemucc.getDisplay().getValue());

			switch (kemucc) {
			case C:
				// 如果是对公活期
				if (!"2".equals(record.getZhaoxz())) {
					ItemQueryProcessor coQueryExecuter = new Handle0770ItemQuery(QueryConstants.TABLE_NAME_AGHMX);
					zhanghParseResult.setQueryExectuer(coQueryExecuter);
					zhanghParseResult.setQueryTableName(QueryConstants.TABLE_NAME_AGHMX);
					kehzhParseResult.getZhanghParseResult().put(
							new SkyPair<String, EnumQueryKind>(zhanghParseResult.getZHANGH(), EnumQueryKind.QK1), zhanghParseResult);
				}
				break;
			case E:
				// 如果是对公定期
				if (!"0".equals(record.getZhaoxz())) {
					zhanghParseResult.setQueryExectuer(new Handle0770ItemQuery(QueryConstants.TABLE_NAME_AGDMX));
					zhanghParseResult.setQueryTableName(QueryConstants.TABLE_NAME_AGDMX);
					kehzhParseResult.getZhanghParseResult().put(
							new SkyPair<String, EnumQueryKind>(zhanghParseResult.getZHANGH(), EnumQueryKind.QK1), zhanghParseResult);
				}
				break;
			default:
			}
		}

		if (kehzhParseResult.getZhanghParseResult().size() == 0) {
			throw new ConditionNotExistException("查询条件" + record.getKehuzh() + "不存在");
		}
		kehzhParseResult.setRecord(record);
		kehzhParseResult.setKehhao(zhanghList.get(0).getKehhao());
		kehzhParseResult.setKehuzh(record.getKehuzh());
		kehzhParseResult.setKehzwm(kehuInfo.getKehzwm());
		kehzhParseResult.setShfobz(kehuInfo.getShfobz());
		kehzhParseResult.setKhzhjb(kehuInfo.getKhzhjb());
		kehzhParseResult.setHuobdh(zhanghList.get(0).getHUOBDH());
		kehzhParseResult.setKhzhlx(zhanghList.get(0).getKHZHLX());
		kehzhParseResult.setChohbz(zhanghList.get(0).getCHUIBZ());
		// 为了启动检测异步的过程
		kehzhParseResult.setQueryExectuer(new Handle0770ItemQuery(QueryConstants.TABLE_NAME_AGDMX));
		return kehzhParseResult;
	}

	@Override
	public Scan buildParseScanner(PybjyEO record) throws InvalidRecordParameterException {
		String fieldSpit = QueryConfUtils.getActiveConfig().get(QueryConstants.ROWKEY_SPLITTER, "|");
		Scan scan = null;
		if (StringUtils.isNotBlank(record.getShunxh())) {
			String rowkey = StringUtils.reverse(record.getKehuzh()) + fieldSpit + KHZHLX_CARD + fieldSpit + record.getShunxh();
			Get get = new Get(Bytes.toBytes(rowkey));
			scan = new Scan(get);
		} else {
			scan = new Scan();
			scan.setStartRow(Bytes.toBytes(StringUtils.reverse(record.getKehuzh()) + fieldSpit + KHZHLX_CARD + fieldSpit
					+ QueryConstants.MIN_NUM));
			scan.setStopRow(Bytes.toBytes(StringUtils.reverse(record.getKehuzh()) + fieldSpit + KHZHLX_CARD + fieldSpit
					+ QueryConstants.MAX_CHAR));
		}

		FilterList list = new FilterList(Operator.MUST_PASS_ALL);
		if (!QueryConstants.CHUIBZ_ALL.equals(record.getChuibz())) {
			SingleColumnValueFilter chuibzFilter = new SingleColumnValueFilter(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AkhzhField.CHUIBZ,
					CompareOp.EQUAL, Bytes.toBytes(record.getChuibz()));
			list.addFilter(chuibzFilter);
		}
		if (!QueryConstants.HUOBDH_ALL.equals(record.getHuobdh())) {
			SingleColumnValueFilter huobdhFilter = new SingleColumnValueFilter(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AkhzhField.HUOBDH,
					CompareOp.EQUAL, Bytes.toBytes(record.getHuobdh()));
			list.addFilter(huobdhFilter);
		}
		scan.setFilter(list);

		scan.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AkhzhField.KEHHAO);
		scan.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AkhzhField.KHZHLX);
		scan.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AkhzhField.HUOBDH);
		scan.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AkhzhField.CHUIBZ);
		scan.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AkhzhField.ZHUZZH);

		return scan;
	}

	@Override
	protected ZhangHParseResult parse(Result result) throws IOException {
		ZhangHParseResult zhjxResult = new ZhangHParseResult();
		zhjxResult.setKehhao(Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AkhzhField.KEHHAO)));
		zhjxResult.setKHZHLX(Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AkhzhField.KHZHLX)));
		zhjxResult.setHUOBDH(Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AkhzhField.HUOBDH)));
		zhjxResult.setCHUIBZ(Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AkhzhField.CHUIBZ)));
		zhjxResult.setZHANGH(Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AkhzhField.ZHUZZH)));
		return zhjxResult;
	}
}
