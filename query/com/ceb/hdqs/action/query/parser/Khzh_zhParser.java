package com.ceb.hdqs.action.query.parser;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;

import com.ceb.hdqs.action.query.IconditionProcessor;
import com.ceb.hdqs.action.query.ItemQueryProcessor;
import com.ceb.hdqs.action.query.exception.ConditionNotExistException;
import com.ceb.hdqs.action.query.exception.InvalidRecordParameterException;
import com.ceb.hdqs.action.query0770.Handle0770ItemQuery;
import com.ceb.hdqs.action.query0771.Handle0771ItemQuery;
import com.ceb.hdqs.action.query0772.Handle0772ItemQuery;
import com.ceb.hdqs.entity.PybjyEO;
import com.ceb.hdqs.entity.field.AzhjxField;
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
 * 输入的客户账号是国债、存单、 储蓄存款存折时，这些客户账号就是账号 直接解析AZHJX0772，查询出该账号对应的信息
 * 
 * @author user
 * 
 */
public class Khzh_zhParser implements IconditionProcessor {
	private static Log LOG = LogFactory.getLog(Khzh_zhParser.class);

	private KhzhParser parser;

	public Khzh_zhParser() {
		parser = new KhzhParser(QueryConstants.TABLE_NAME_AZHJX0772) {

			@Override
			public Scan buildParseScanner(PybjyEO record) throws InvalidRecordParameterException {
				// 生成解析解析账号的Scan
				return Khzh_zhParser.this.buildParseScanner(record);
			}

			@Override
			public KehzhParserResult parseCondition(PybjyEO record) throws IOException, InvalidRecordParameterException, Exception {
				KehzhParserResult khzhParseResult = new KehzhParserResult();

				// 由于查询AZHJX0772过程中，输入的条件为账号，所以查询出的结果只有一个
				List<ZhangHParseResult> parseResult = this.scan(buildParseScanner(record));
				if (parseResult == null || parseResult.isEmpty()) {
					return null;
				}
				ZhangHParseResult zhanghParseResult = parseResult.get(0);
				// 获取客户中文名

				CustomerInfo info = QueryMethodUtils.getCustomerChineseName(zhanghParseResult.getKehhao());
				// 如果图形前段回显了客户中文名，但是给客户中文名和解析出的客户中文名不一致，则直接返回空
				if (!StringUtils.isBlank(record.getKhzwm()) && !QueryMethodUtils.checkKehuzwm(record.getKhzwm(), info.getKehzwm())) {
					LOG.info("查询出的客户中文名：" + info.getKehzwm() + "和回显中文名：" + record.getKhzwm());
					return null;
				}

				// 获取账户营业机构名称
				String jgmc = QueryMethodUtils.getJGMC(zhanghParseResult.getZHYYNG());
				zhanghParseResult.setKehzwm(info.getKehzwm());
				zhanghParseResult.setStartDate(record.getStartDate());
				zhanghParseResult.setEndDate(record.getEndDate());
				/**
				 * 该查询日期需要根据输出格式进行格式化输出
				 */
				zhanghParseResult.setQueryDate(record.getJioyrq());
				zhanghParseResult.setRecord(record);
				zhanghParseResult.setJIGOMC(jgmc);
				zhanghParseResult.setZHANGH(record.getKehuzh());
				zhanghParseResult.setKehuzh(record.getKehuzh());
				zhanghParseResult.setZhanghTotal(1);
				LOG.debug("开始设置QueryExectuer!");
				EnumKemucc kemucc = EnumKemucc.valueOf(zhanghParseResult.getKEMUCC());
				LOG.debug("解析出的账号是：" + record.getKehuzh());
				LOG.debug("解析出的kemucc的属性是：" + kemucc.getDisplay().getValue());
				LOG.debug("record.getIsAsyn().booleanValue()" + record.getIsAsyn().booleanValue());
				if (QueryConstants.PRIVATE_BATCH_QUERY_CODE.equals(record.getJiaoym())) {
					switch (kemucc) {
					case S:
						if (record.getJiaoym().equals(QueryConstants.CORPORATE_BATCH_QUERY_CODE)) {
							zhanghParseResult.setQuery(false);
						} else if (record.getJiaoym().equals(QueryConstants.PRIVATE_BATCH_QUERY_CODE) && !"2".equals(record.getZhaoxz())) {
							// 如果是对私活期
							ItemQueryProcessor queryExecuter = new Handle0771ItemQuery();
							zhanghParseResult.setQueryExectuer(queryExecuter);
						} else {
							String errMsg = "账号性质不匹配:" + zhanghParseResult.getZHANGH() + "查询出的账号性质是" + kemucc.getDisplay().getValue()
									+ "需要查询的是" + record.getZhaoxz() + "(0活期,2定期,3全部)";
							record.setErrMsg(errMsg);
							zhanghParseResult.setQuery(false);
							zhanghParseResult.setFinished(QueryConstants.ZHANGH_QUERY_FINISHED);
							int pageCount = zhanghParseResult.getPageTotal();
							zhanghParseResult.setPageTotal(pageCount + 1);
							zhanghParseResult.setTips(errMsg);
						}
						break;
					case C:
						if (record.getJiaoym().equals(QueryConstants.PRIVATE_BATCH_QUERY_CODE)) {
							zhanghParseResult.setQuery(false);
						} else if (record.getJiaoym().equals(QueryConstants.CORPORATE_BATCH_QUERY_CODE)) {
							// 如果是对公活期
							ItemQueryProcessor coQueryExecuter = new Handle0770ItemQuery(QueryConstants.TABLE_NAME_AGHMX);
							zhanghParseResult.setQueryExectuer(coQueryExecuter);
						} else {
							String errMsg = "账号性质不匹配:" + zhanghParseResult.getZHANGH() + "查询出的账号性质是" + kemucc.getDisplay().getValue()
									+ "需要查询的是" + record.getZhaoxz() + "(0活期,2定期,3全部)";
							record.setErrMsg(errMsg);
							zhanghParseResult.setQuery(false);
							zhanghParseResult.setFinished(QueryConstants.ZHANGH_QUERY_FINISHED);
							int pageCount = zhanghParseResult.getPageTotal();
							zhanghParseResult.setPageTotal(pageCount + 1);
							zhanghParseResult.setTips(errMsg);
						}
						break;
					case F:
						if (record.getJiaoym().equals(QueryConstants.CORPORATE_BATCH_QUERY_CODE)) {
							zhanghParseResult.setQuery(false);
						} else if (record.getJiaoym().equals(QueryConstants.PRIVATE_BATCH_QUERY_CODE) && !"0".equals(record.getZhaoxz())) {
							// 如果是对私定期
							zhanghParseResult.setQueryExectuer(new Handle0772ItemQuery());
						} else {
							String errMsg = "账号性质不匹配:" + zhanghParseResult.getZHANGH() + "查询出的账号性质是" + kemucc.getDisplay().getValue()
									+ "需要查询的是" + record.getZhaoxz() + "(0活期,2定期,3全部)";
							record.setErrMsg(errMsg);
							zhanghParseResult.setQuery(false);
							zhanghParseResult.setFinished(QueryConstants.ZHANGH_QUERY_FINISHED);
							int pageCount = zhanghParseResult.getPageTotal();
							zhanghParseResult.setPageTotal(pageCount + 1);
							zhanghParseResult.setTips(errMsg);
						}
						break;
					case E:
						if (record.getJiaoym().equals(QueryConstants.PRIVATE_BATCH_QUERY_CODE)) {
							zhanghParseResult.setQuery(false);
						} else if (record.getJiaoym().equals(QueryConstants.CORPORATE_BATCH_QUERY_CODE)) {
							// 如果是对公定期
							zhanghParseResult.setQueryExectuer(new Handle0770ItemQuery(QueryConstants.TABLE_NAME_AGDMX));
						} else {
							String errMsg = "账号性质不匹配:" + zhanghParseResult.getZHANGH() + "查询出的账号性质是" + kemucc.getDisplay().getValue()
									+ "需要查询的是" + record.getZhaoxz() + "(0活期,2定期,3全部)";
							record.setErrMsg(errMsg);
							zhanghParseResult.setQuery(false);
							zhanghParseResult.setFinished(QueryConstants.ZHANGH_QUERY_FINISHED);
							int pageCount = zhanghParseResult.getPageTotal();
							zhanghParseResult.setPageTotal(pageCount + 1);
							zhanghParseResult.setTips(errMsg);
						}
						break;
					default:
						// 如果查询出气的KEMUCC（主要是空的情况）
						LOG.warn(zhanghParseResult.getZHANGH() + "解析出的KEMUCC是" + kemucc.getDisplay().getValue());
						String msg = "账号" + zhanghParseResult.getZHANGH() + "账号性质是" + kemucc;
						record.setErrMsg(msg);
						zhanghParseResult.setFinished(QueryConstants.ZHANGH_QUERY_FINISHED);
						int pageCount = zhanghParseResult.getPageTotal();
						zhanghParseResult.setPageTotal(pageCount + 1);
						zhanghParseResult.setQuery(false);
						zhanghParseResult.setTips(msg);
					}
				}

				khzhParseResult.setKehhao(zhanghParseResult.getKehhao());
				khzhParseResult.setKehuzh(record.getKehuzh());
				khzhParseResult.setKehzwm(info.getKehzwm());
				khzhParseResult.setHuobdh(zhanghParseResult.getHUOBDH());
				khzhParseResult.setZHYYNG(zhanghParseResult.getZHYYNG());
				khzhParseResult.setJIGOMC(jgmc);
				khzhParseResult.setRecord(record);
				khzhParseResult.setShfobz(info.getShfobz());
				khzhParseResult.setKhzhjb(info.getKhzhjb());

				// 存储解析账号的结果
				HashMap<SkyPair<String, EnumQueryKind>, ZhangHParseResult> res = new HashMap<SkyPair<String, EnumQueryKind>, ZhangHParseResult>();
				EnumQueryKind queryKind = EnumQueryKind.QK1;
				res.put(new SkyPair<String, EnumQueryKind>(record.getKehuzh(), queryKind), zhanghParseResult);
				khzhParseResult.setZhanghParseResult(res);
				return khzhParseResult;
			}

			@Override
			protected ZhangHParseResult parse(Result result) throws IOException {
				ZhangHParseResult zhanghParseResult = new ZhangHParseResult();

				byte[] huobdh = result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AzhjxField.HUOBDH);
				byte[] kehhao = result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AzhjxField.KEHHAO);
				// byte[] kehuzh =
				// result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE,
				// AzhjxField.KEHUZH);
				byte[] kemucc = result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AzhjxField.KEMUCC);
				byte[] yewudh = result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AzhjxField.YEWUDH);
				// byte[] zhangh =
				// result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE,
				// AzhjxField.ZHANGH);
				byte[] zhyyjg = result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AzhjxField.ZHYYJG);

				zhanghParseResult.setHUOBDH(Bytes.toString(huobdh));
				zhanghParseResult.setKehhao(Bytes.toString(kehhao));
				// accRes.setKEHUZH(Bytes.toString(kehuzh));
				zhanghParseResult.setKEMUCC(Bytes.toString(kemucc));
				zhanghParseResult.setYEWUDH(Bytes.toString(yewudh));
				// accRes.setZHANGH(Bytes.toString(zhangh));
				zhanghParseResult.setZHYYNG(Bytes.toString(zhyyjg));

				return zhanghParseResult;
			}
		};
	}

	@Override
	public Scan buildParseScanner(PybjyEO record) throws InvalidRecordParameterException {
		Configuration conf = QueryConfUtils.getActiveConfig();
		String rowKeySplit = conf.get(QueryConstants.ROWKEY_SPLITTER, "|");

		Scan scan = new Scan();
		// 倒置账号（此处是国债、存单、 储蓄存款存折的客户账号就是账号）
		String reAaccount = StringUtils.reverse(record.getKehuzh());

		String row = reAaccount.concat(rowKeySplit);
		scan.setStartRow(Bytes.toBytes(row.concat(QueryConstants.MIN_NUM)));
		scan.setStopRow(Bytes.toBytes(row.concat(QueryConstants.MAX_CHAR)));
		try {
			LOG.debug("Khzh_zhParser的Scan的JSON:" + scan.toJSON());
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		}
		return scan;
	}

	@Override
	public KehzhParserResult parseCondition(PybjyEO record) throws IOException, ConditionNotExistException, Exception {
		KehzhParserResult khzhParseResult = new KehzhParserResult();
		try {
			khzhParseResult = parser.parseCondition(record);
		} catch (Exception e) {
			khzhParseResult.setQuery(false);
			String errMsg = "解析客户账号" + record.getKehuzh() + "异常!";
			khzhParseResult.setTips(errMsg);
			khzhParseResult.setRecord(record);
			record.setErrMsg(errMsg);
		}
		return khzhParseResult;
	}

}