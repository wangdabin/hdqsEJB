package com.ceb.hdqs.action.query.parser;

import java.io.IOException;
import java.util.List;
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
import com.ceb.hdqs.action.query0770.Handle0770ItemQuery;
import com.ceb.hdqs.action.query0771.Handle0771ItemQuery;
import com.ceb.hdqs.action.query0772.Handle0772ItemQuery;
import com.ceb.hdqs.constants.HdqsConstants;
import com.ceb.hdqs.entity.PybjyEO;
import com.ceb.hdqs.entity.field.AkhzhField;
import com.ceb.hdqs.entity.result.KehzhParserResult;
import com.ceb.hdqs.entity.result.ZhangHParseResult;
import com.ceb.hdqs.query.entity.CustomerInfo;
import com.ceb.hdqs.query.entity.EnumKemucc;
import com.ceb.hdqs.query.entity.EnumQueryKind;
import com.ceb.hdqs.query.entity.SkyPair;
import com.ceb.hdqs.query.utils.QueryConstants;
import com.ceb.hdqs.query.utils.QueryMethodUtils;

/**
 * 解析AKHZH，解析出 <账号> 后需要解析AZHJX0772,解析出对应账号的KEMUCC和账户营业机构号
 * 
 * @author user
 * 
 */
public abstract class KhzhParserAsyn implements IconditionProcessor {
	private static final Log LOG = LogFactory.getLog(KhzhParserAsyn.class);

	private ZhKMJGParser zhjx = new ZhKMJGParser();
	private KhzhParser khzhParser;

	public KhzhParserAsyn() {
		khzhParser = new KhzhParser(QueryConstants.TABLE_NAME_AKHZH) {

			@Override
			public KehzhParserResult parseCondition(PybjyEO record) throws ConditionNotExistException, Exception {
				List<ZhangHParseResult> accounts = this.scan(buildParseScanner(record));
				if (accounts == null || accounts.isEmpty()) {
					return null;
				}
				// 查询客户中文名
				LOG.debug("Kehhao  :" + accounts.get(0).getKehhao());

				String kehhao = accounts.get(0).getKehhao();
				CustomerInfo customerInfo = QueryMethodUtils.getCustomerChineseName(kehhao);
				LOG.debug("查询出的客户信息：" + customerInfo.toString());
				// 如果前段回显了客户中文名，则进行验证客户中文名是否相同
				if (!StringUtils.isBlank(record.getKhzwm()) && !QueryMethodUtils.checkKehuzwm(record.getKhzwm(), customerInfo.getKehzwm())) {
					LOG.debug("查询出的客户中文名：" + customerInfo.getKehzwm() + "和回显中文名：" + record.getKhzwm());
					return null;
				}

				// 封装查询结果
				// 到AZHJX0772中查询每个账户的kemucc，zhyyjg
				KehzhParserResult vyktdKHZHResult = null;
				if (HdqsConstants.KHZHLX_CARD.equals(record.getKhzhlx())) {
					// 如果客户账号类型是卡，则发卡机构名称和发卡机构代号需要查询VYKTD
					VyktdParser vyktdParser = new VyktdParser();
					vyktdKHZHResult = vyktdParser.parseCondition(record);
				}

				// 开始组装解析结果
				KehzhParserResult khzhParseResult = null;
				for (ZhangHParseResult zhangHParseResult : accounts) {
					zhjx.parseZhjx(zhangHParseResult);//填充KEMUCC、ZHYYNG、JIGOMC、YEWUDH、HUOBDH
					zhangHParseResult.setKehzwm(customerInfo.getKehzwm());
					zhangHParseResult.setRecord(record);
					zhangHParseResult.setStartDate(record.getStartDate());
					zhangHParseResult.setEndDate(record.getEndDate());
					zhangHParseResult.setZhanghTotal(accounts.size());
					if (HdqsConstants.KHZHLX_CARD.equals(record.getKhzhlx())) {//如果是卡查询，覆盖ZHYYNG、JIGOMC
						zhangHParseResult.setZHYYNG(vyktdKHZHResult.getZHYYNG());
						zhangHParseResult.setJIGOMC(vyktdKHZHResult.getJIGOMC());
					}
					if (zhangHParseResult.getKEMUCC() == null) {
						LOG.warn(zhangHParseResult.getZHANGH() + "解析出的KEMUCC是空");
						String errMsg = "账号" + zhangHParseResult.getZHANGH() + "解析的科目存储(KEMUCC)为空，无法判断账号的活定期属性!";
						record.setErrMsg(errMsg);
						zhangHParseResult.setQuery(false);
						continue;
					}
					// 对0778交易进行账号性质的过滤，过滤出符合条件的账号，封装成zhanghParseResult
					if (record.getJiaoym().equals(QueryConstants.PRIVATE_BATCH_QUERY_CODE)) {
						// 过滤定期
						if (record.getZhaoxz().equals(QueryConstants.ZHAOHXZ_DINGQI)) {
							if (zhangHParseResult.getKEMUCC().equals(QueryConstants.KEMUCC_DGHQ)
									|| zhangHParseResult.getKEMUCC().equals(QueryConstants.KEMUCC_DSHQ)) {
								continue;
							}
							// 过滤活期
						} else if (record.getZhaoxz().equals(QueryConstants.ZHAOHXZ_HUOQI)) {
							if (zhangHParseResult.getKEMUCC().equals(QueryConstants.KEMUCC_DGDQ)
									|| zhangHParseResult.getKEMUCC().equals(QueryConstants.KEMUCC_DSDQ)) {
								continue;
							}
						}
					}
					// 实例化结果，开始封装
					if (khzhParseResult == null) {
						khzhParseResult = new KehzhParserResult();
					}
					EnumKemucc kemucc = EnumKemucc.valueOf(zhangHParseResult.getKEMUCC());
					LOG.debug("解析出的账号是：" + zhangHParseResult.getZHANGH());
					LOG.debug("解析出的kemucc的属性是：" + kemucc.getDisplay().getValue());
					switch (kemucc) {
					case S:
						if (QueryConstants.CORPORATE_BATCH_QUERY_CODE.equals(record.getJiaoym())) {
							zhangHParseResult.setQuery(false);
							continue;
						} else if (QueryConstants.PRIVATE_BATCH_QUERY_CODE.equals(record.getJiaoym()) && !"2".equals(record.getZhaoxz())) {
							// 如果是对私活期
							ItemQueryProcessor queryExecuter = new Handle0771ItemQuery();
							zhangHParseResult.setQueryExectuer(queryExecuter);
						} else {
							String errMsg = "账号性质不匹配:" + zhangHParseResult.getZHANGH() + "查询出的账号性质是" + kemucc.getDisplay().getValue()
									+ "需要查询的是" + record.getZhaoxz() + "(0活期,2定期,3全部)";
							record.setErrMsg(errMsg);
							zhangHParseResult.setQuery(false);
							zhangHParseResult.setFinished(QueryConstants.ZHANGH_QUERY_FINISHED);
							int pageCount = zhangHParseResult.getPageTotal();
							zhangHParseResult.setPageTotal(pageCount + 1);
							zhangHParseResult.setTips(errMsg);
						}
						break;
					case C:
						if (QueryConstants.PRIVATE_BATCH_QUERY_CODE.equals(record.getJiaoym())) {
							zhangHParseResult.setQuery(false);
							continue;
						} else if (QueryConstants.CORPORATE_BATCH_QUERY_CODE.equals(record.getJiaoym())) {
							// 如果是对公活期
							ItemQueryProcessor coQueryExecuter = new Handle0770ItemQuery(QueryConstants.TABLE_NAME_AGHMX);
							zhangHParseResult.setQueryExectuer(coQueryExecuter);
						} else {
							String errMsg = "账号性质不匹配:" + zhangHParseResult.getZHANGH() + "查询出的账号性质是" + kemucc.getDisplay().getValue()
									+ "需要查询的是" + record.getZhaoxz() + "(0活期,2定期,3全部)";
							record.setErrMsg(errMsg);
							zhangHParseResult.setQuery(false);
							zhangHParseResult.setFinished(QueryConstants.ZHANGH_QUERY_FINISHED);
							int pageCount = zhangHParseResult.getPageTotal();
							zhangHParseResult.setPageTotal(pageCount + 1);
							zhangHParseResult.setTips(errMsg);
						}
						break;
					case F:
						if (QueryConstants.CORPORATE_BATCH_QUERY_CODE.equals(record.getJiaoym())) {
							zhangHParseResult.setQuery(false);
							continue;
						} else if (QueryConstants.PRIVATE_BATCH_QUERY_CODE.equals(record.getJiaoym()) && !"0".equals(record.getZhaoxz())) {
							// 如果是对私定期
							zhangHParseResult.setQueryExectuer(new Handle0772ItemQuery());
						} else {
							String errMsg = "账号性质不匹配:" + zhangHParseResult.getZHANGH() + "查询出的账号性质是" + kemucc.getDisplay().getValue()
									+ "需要查询的是" + record.getZhaoxz() + "(0活期,2定期,3全部)";
							record.setErrMsg(errMsg);
							zhangHParseResult.setQuery(false);
							zhangHParseResult.setFinished(QueryConstants.ZHANGH_QUERY_FINISHED);
							int pageCount = zhangHParseResult.getPageTotal();
							zhangHParseResult.setPageTotal(pageCount + 1);
							zhangHParseResult.setTips(errMsg);
						}
						break;
					case E:
						if (QueryConstants.PRIVATE_BATCH_QUERY_CODE.equals(record.getJiaoym())) {
							zhangHParseResult.setQuery(false);
							continue;
						} else if (QueryConstants.CORPORATE_BATCH_QUERY_CODE.equals(record.getJiaoym())) {
							// 如果是对公定期
							zhangHParseResult.setQueryExectuer(new Handle0770ItemQuery(QueryConstants.TABLE_NAME_AGDMX));
						} else {
							String errMsg = "账号性质不匹配:" + zhangHParseResult.getZHANGH() + "查询出的账号性质是" + kemucc.getDisplay().getValue()
									+ "需要查询的是" + record.getZhaoxz() + "(0活期,2定期,3全部)";
							record.setErrMsg(errMsg);
							zhangHParseResult.setQuery(false);
							zhangHParseResult.setFinished(QueryConstants.ZHANGH_QUERY_FINISHED);
							int pageCount = zhangHParseResult.getPageTotal();
							zhangHParseResult.setPageTotal(pageCount + 1);
							zhangHParseResult.setTips(errMsg);
						}
						break;
					default:
						// 如果查询出气的KEMUCC（主要是空的情况）
						LOG.warn(zhangHParseResult.getZHANGH() + "解析出的KEMUCC是" + kemucc.getDisplay().getValue());
						String msg = "账号" + zhangHParseResult.getZHANGH() + "科目存储是" + kemucc;
						record.setErrMsg(msg);
						zhangHParseResult.setQuery(false);
						zhangHParseResult.setFinished(QueryConstants.ZHANGH_QUERY_FINISHED);
						int pageCount = zhangHParseResult.getPageTotal();
						zhangHParseResult.setPageTotal(pageCount + 1);
						zhangHParseResult.setTips(msg);
					}

					SkyPair<String, EnumQueryKind> pair = new SkyPair<String, EnumQueryKind>(zhangHParseResult.getZHANGH(),
							EnumQueryKind.QK1);
					khzhParseResult.getZhanghParseResult().put(pair, zhangHParseResult);
				}

				if (khzhParseResult == null) {
					return null;
				}
				// 填充 khzhParseResult
				khzhParseResult.setHuobdh(accounts.get(0).getHUOBDH());
				khzhParseResult.setKehhao(kehhao);
				khzhParseResult.setKhzhlx(accounts.get(0).getKHZHLX());
				khzhParseResult.setKehuzh(accounts.get(0).getKehuzh());
				khzhParseResult.setKehzwm(customerInfo.getKehzwm());
				khzhParseResult.setShfobz(customerInfo.getShfobz());
				khzhParseResult.setKhzhjb(customerInfo.getKhzhjb());
				if (HdqsConstants.KHZHLX_CARD.equals(record.getChaxzl())) {
					khzhParseResult.setZHYYNG(vyktdKHZHResult.getZHYYNG());
					khzhParseResult.setJIGOMC(vyktdKHZHResult.getJIGOMC());
				} else {
					khzhParseResult.setZHYYNG(accounts.get(0).getZHYYNG());
					khzhParseResult.setJIGOMC(accounts.get(0).getJIGOMC());
				}

				khzhParseResult.setRecord(record);

				return khzhParseResult;
			}

			@Override
			protected ZhangHParseResult parse(Result result) throws IOException {
				ZhangHParseResult zhjxResult = new ZhangHParseResult();
				String KEHHAO = Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AkhzhField.KEHHAO));
				String ZHANGH = Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AkhzhField.ZHUZZH));
				String kehuzh = Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AkhzhField.KEHUZH));
				String HUOBDH = Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AkhzhField.HUOBDH));
				String KHZHLX = Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AkhzhField.KHZHLX));
				String CHUIBZ = Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AkhzhField.CHUIBZ));
				zhjxResult.setKehhao(KEHHAO);
				zhjxResult.setZHANGH(ZHANGH);
				zhjxResult.setKehuzh(kehuzh);
				zhjxResult.setHUOBDH(HUOBDH);
				zhjxResult.setKHZHLX(KHZHLX);
				zhjxResult.setCHUIBZ(CHUIBZ);
				return zhjxResult;
			}

			@Override
			public Scan buildParseScanner(PybjyEO record) throws InvalidRecordParameterException {
				// TODO Auto-generated method stub
				return KhzhParserAsyn.this.buildParseScanner(record);
			}
		};
	}

	public abstract Scan buildParseScanner(PybjyEO record) throws InvalidRecordParameterException;

	@Override
	public KehzhParserResult parseCondition(PybjyEO record) throws ConditionNotExistException {
		KehzhParserResult khzhParseResult = new KehzhParserResult();
		try {
			khzhParseResult = khzhParser.parseCondition(record);
			if (khzhParseResult != null) {
				int zhanghTotal = khzhParseResult.getZhanghParseResult().size();
				for (Entry<SkyPair<String, EnumQueryKind>, ZhangHParseResult> zhanghInfo : khzhParseResult.getZhanghParseResult()
						.entrySet()) {
					zhanghInfo.getValue().setZhanghTotal(zhanghTotal);
				}
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			khzhParseResult.setQuery(false);
			String errMsg = "解析客户账号" + record.getKehuzh() + "异常!";
			record.setErrMsg(errMsg);
			record.setRunStatus(HdqsConstants.RUNNING_STATUS_FAILURE);
			khzhParseResult.setRecord(record);
		}
		return khzhParseResult;
	}

	public static void main(String[] args) {
		for (int i = 1; i < 5; i++) {
			switch (i) {
			case 1:
				System.out.println(i);
				if (i == 1) {
					continue;
				}
				break;
			case 2:
				System.out.println(i);
				break;
			case 3:
				System.out.println(i);
				break;

			}

			System.out.println("end");
		}
	}
}
