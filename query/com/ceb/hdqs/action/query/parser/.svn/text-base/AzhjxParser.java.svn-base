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

import com.ceb.bank.htable.AbstractQuery;
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
import com.ceb.hdqs.entity.field.AzhjxField;
import com.ceb.hdqs.entity.result.KehhaoParseResult;
import com.ceb.hdqs.entity.result.KehzhParserResult;
import com.ceb.hdqs.entity.result.ZhangHParseResult;
import com.ceb.hdqs.query.entity.CustomerInfo;
import com.ceb.hdqs.query.entity.EIdentityType;
import com.ceb.hdqs.query.entity.EnumKemucc;
import com.ceb.hdqs.query.entity.EnumQueryKind;
import com.ceb.hdqs.query.entity.SkyPair;
import com.ceb.hdqs.query.utils.QueryConstants;
import com.ceb.hdqs.query.utils.QueryMethodUtils;

/**
 * <p>
 * 客户号解析
 * <p>
 * 客户号解析 查询AZHJX表，如果查询是0777查询，则只过滤账号是对公活期和对公定期的；如果查询时0778，则需要过滤kemucc和账户性质，
 * 过滤出具体的对私活期和对私定期，活期全部对私明细
 * 
 * @author user
 * 
 */
public class AzhjxParser extends AbstractQuery<ZhangHParseResult> implements IconditionProcessor {
	private static final Log log = LogFactory.getLog(AzhjxParser.class);

	private boolean hasRecord = false;
	private Akhzh0773Parser chuibzParser = new Akhzh0773Parser();
	private KehhaoParseResult kehhaoParseResult = null;
	private Map<SkyPair<String, EnumQueryKind>, KehzhParserResult> khzhParseResultMap = null;

	public AzhjxParser() {
		super(QueryConstants.TABLE_NAME_AZHJX);
	}

	@Override
	protected ZhangHParseResult parse(Result result) throws IOException {

		// 记录客户号是否具有记录
		if (!hasRecord) {
			hasRecord = true;
		}
		String zhangh = Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AzhjxField.ZHANGH));
		String huobdh = Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AzhjxField.HUOBDH));
		String zhyyjg = Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AzhjxField.ZHYYJG));
		String kemucc = Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AzhjxField.KEMUCC));
		String yewudh = Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AzhjxField.YEWUDH));
		String kehuzh = Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AzhjxField.KEHUZH));
		String khzhlx = Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AzhjxField.KHZHLX));

		log.debug("key:" + Bytes.toString(result.getRow()) + " zhangh:" + zhangh + "  kemucc:" + kemucc + "  khzh:" + kehuzh);

		if (StringUtils.isBlank(zhangh) || StringUtils.isBlank(kemucc)) {
			log.info("发现账号为空或者KEMUCC为空的记录!");
			return null;
		}
		// 如果查询出的khzh为空，则查询AKHZH表查询出该账号对应的账户营业机构
		if (StringUtils.isBlank(kehuzh) && StringUtils.isNotBlank(zhangh)) {
			log.info("账号 " + zhangh + "对应的客户账号为空，开始查询AKHZH表");
			KhzhGetter getter = new KhzhGetter(QueryConstants.TABLE_NAME_AKHZH_0773);
			kehuzh = getter.getKhzh(zhangh);
			log.info("查询AKHZH得到的客户账号为: " + kehuzh);
		}		
		ZhangHParseResult tempParseResult = new ZhangHParseResult(zhangh, kemucc, zhyyjg, yewudh);
		tempParseResult.setKehuzh(kehuzh);
		tempParseResult.setKHZHLX(khzhlx);
		tempParseResult.setHUOBDH(huobdh);
		String jigomc = "";
		if (HdqsConstants.KHZHLX_CARD.equals(khzhlx)) {
			VyktdParser cardParser = new VyktdParser();

			try {
				KehzhParserResult tmpKhzhInfo = cardParser.parseCondition(kehuzh);
				if (tmpKhzhInfo == null) {
					// 如果在VYKTD中没有查询出对应的账户营业机构，则利用账号的 "ynyejg" 进行查询
					jigomc = QueryMethodUtils.getJGMC(zhyyjg);
					log.warn("解析卡对象的发卡机构出现数据不一致，客户账号 在AZHJX中为空!");
				} else {
					jigomc = tmpKhzhInfo.getJIGOMC();
					zhyyjg = tmpKhzhInfo.getZHYYNG();
				}
			} catch (ConditionNotExistException e) {
				log.error(e.getMessage());
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		} else {
			jigomc = QueryMethodUtils.getJGMC(zhyyjg);
		}

		
		tempParseResult.setJIGOMC(jigomc);
		tempParseResult.setZHYYNG(zhyyjg);
		SkyPair<String, EnumQueryKind> khzhKey = new SkyPair<String, EnumQueryKind>(kehuzh, EnumQueryKind.QK2);
		SkyPair<String, EnumQueryKind> zhKey = new SkyPair<String, EnumQueryKind>(zhangh, EnumQueryKind.QK1);
		if (khzhParseResultMap.containsKey(khzhKey)) {
			KehzhParserResult existResult=khzhParseResultMap.get(khzhKey);
			existResult.setKehuzh(kehuzh);
			existResult.getZhanghParseResult().put(zhKey, tempParseResult);
			existResult.setJIGOMC(jigomc);
			existResult.setZHYYNG(zhyyjg);
		} else {
			KehzhParserResult newKhzhResult = new KehzhParserResult();
			newKhzhResult.setKehuzh(kehuzh);
			newKhzhResult.getZhanghParseResult().put(zhKey, tempParseResult);
			newKhzhResult.setJIGOMC(jigomc);
			newKhzhResult.setZHYYNG(zhyyjg);
			khzhParseResultMap.put(new SkyPair<String, EnumQueryKind>(kehuzh, EnumQueryKind.QK2), newKhzhResult);
		}

		return null;
	}

	@Override
	public KehhaoParseResult parseCondition(PybjyEO record) throws Exception {
		kehhaoParseResult = new KehhaoParseResult();
		kehhaoParseResult.setRecord(record);
		khzhParseResultMap = kehhaoParseResult.getKhzhParseResult();
		log.info("开始解析客户号: " + record.getKehuhao());
		// 查询客户信息
		try {
			CustomerInfo customerInfo = QueryMethodUtils.getCustomerChineseName(record.getKehuhao());
			// 如果图形前段回显了客户中文名，但是给客户中文名和解析出的客户中文名不一致，则直接返回空
			if (StringUtils.isNotBlank(record.getKhzwm()) && !QueryMethodUtils.checkKehuzwm(record.getKhzwm(), customerInfo.getKehzwm())) {
				log.info("查询出的客户中文名：" + customerInfo.getKehzwm() + "和回显中文名：" + record.getKhzwm());
				return null;
			}
			this.scan(buildParseScanner(record));

			// 如果不存在客户号
			if (kehhaoParseResult.getKhzhParseResult().isEmpty() && !hasRecord) {
				log.info("不存在客户号或者客户号没有对应的客户账号.");
				return null;
			} else if (!kehhaoParseResult.getKhzhParseResult().isEmpty()) {// 存在客户号且存在账号
				// 查询客户中文名
				kehhaoParseResult.setShfbz(customerInfo.getShfobz());
				kehhaoParseResult.setKehhao(record.getKehuhao());
				kehhaoParseResult.setKehzwm(StringUtils.isBlank(customerInfo.getKehzwm()) ? "" : customerInfo.getKehzwm());
				kehhaoParseResult.setKhzhjb(customerInfo.getKhzhjb());

				// 计算解析出的账号的个数
				int zhanghNum = 0;
				for (Entry<SkyPair<String, EnumQueryKind>, KehzhParserResult> khzhInfo : kehhaoParseResult.getKhzhParseResult().entrySet()) {
					log.debug("客户账号:" + khzhInfo.getValue().getKehuzh() + "的账户数是: " + khzhInfo.getValue().getZhanghParseResult().size());
					zhanghNum += khzhInfo.getValue().getZhanghParseResult().size();

					for (Entry<SkyPair<String, EnumQueryKind>, ZhangHParseResult> entry : khzhInfo.getValue().getZhanghParseResult()
							.entrySet()) {
						log.debug("---------账号:" + entry.getValue().getZHANGH());
					}
				}
				// 在客户好解析结果中记录该客户号解析出的账号的个数
				kehhaoParseResult.setZhanghTotal(zhanghNum);
				log.info("解析出的客户账号的个数是: " + kehhaoParseResult.getKhzhParseResult().size());

				for (Entry<SkyPair<String, EnumQueryKind>, KehzhParserResult> khzhInfo : kehhaoParseResult.getKhzhParseResult().entrySet()) {
					khzhInfo.getValue().setKehzwm(customerInfo.getKehzwm());
					khzhInfo.getValue().setKehhao(record.getKehuhao());
					khzhInfo.getValue().setKhzhjb(customerInfo.getKhzhjb());
					khzhInfo.getValue().setRecord(record);
					for (Entry<SkyPair<String, EnumQueryKind>, ZhangHParseResult> zhanghInfo : khzhInfo.getValue().getZhanghParseResult()
							.entrySet()) {
						// 由于账号解析表中不存在钞汇标志，所以需要根据账号查询AKHZH表查询出钞汇标志
						zhanghInfo.getValue().setZhanghTotal(zhanghNum);
						String chuibz = chuibzParser.getChuibz(zhanghInfo.getValue().getZHANGH());
						zhanghInfo.getValue().setCHUIBZ(chuibz);
						zhanghInfo.getValue().setKehzwm(customerInfo.getKehzwm());
						zhanghInfo.getValue().setKehhao(record.getKehuhao());
						zhanghInfo.getValue().setRecord(record);
						EnumKemucc kemucc = null;
						try {
							kemucc = EnumKemucc.valueOf(zhanghInfo.getValue().getKEMUCC());
						} catch (Exception e) {

							log.debug("账号" + zhanghInfo.getValue().getZHANGH() + "的kemucc是" + zhanghInfo.getValue().getKEMUCC()
									+ ",查询放弃该账号!");
							continue;
						}

						log.debug("解析出的账号是：" + zhanghInfo.getValue().getZHANGH());
						log.debug("解析出的kemucc的属性是：" + kemucc.getDisplay().getValue());
						switch (kemucc) {
						case S:
							if (record.getJiaoym().equals(QueryConstants.CORPORATE_BATCH_QUERY_CODE)) {
								zhanghInfo.getValue().setQuery(false);
								continue;
							} else if (record.getJiaoym().equals(QueryConstants.PRIVATE_BATCH_QUERY_CODE)
									&& !"2".equals(record.getZhaoxz())) {
								// 如果是对私活期
								ItemQueryProcessor queryExecuter = new Handle0771ItemQuery();
								zhanghInfo.getValue().setQueryExectuer(queryExecuter);
							} else {
								String errMsg = "账号性质不匹配:" + zhanghInfo.getValue().getZHANGH() + "查询出的账号性质是"
										+ kemucc.getDisplay().getValue() + "需要查询的是" + record.getZhaoxz() + "(0活期,2定期,3全部)";
								record.setErrMsg(errMsg);
								zhanghInfo.getValue().setQuery(false);
								zhanghInfo.getValue().setFinished(QueryConstants.ZHANGH_QUERY_FINISHED);
								int pageCount = zhanghInfo.getValue().getPageTotal();
								zhanghInfo.getValue().setPageTotal(pageCount + 1);
								zhanghInfo.getValue().setTips(errMsg);
							}
							break;
						case C:
							if (record.getJiaoym().equals(QueryConstants.PRIVATE_BATCH_QUERY_CODE)) {
								zhanghInfo.getValue().setQuery(false);
								continue;
							} else if (record.getJiaoym().equals(QueryConstants.CORPORATE_BATCH_QUERY_CODE)) {
								// 如果是对公活期
								ItemQueryProcessor coQueryExecuter = new Handle0770ItemQuery(QueryConstants.TABLE_NAME_AGHMX);
								zhanghInfo.getValue().setQueryExectuer(coQueryExecuter);
							} else {
								String errMsg = "账号性质不匹配:" + zhanghInfo.getValue().getZHANGH() + "查询出的账号性质是"
										+ kemucc.getDisplay().getValue() + "需要查询的是" + record.getZhaoxz() + "(0活期,2定期,3全部)";
								record.setErrMsg(errMsg);
								zhanghInfo.getValue().setQuery(false);
								zhanghInfo.getValue().setFinished(QueryConstants.ZHANGH_QUERY_FINISHED);
								int pageCount = zhanghInfo.getValue().getPageTotal();
								zhanghInfo.getValue().setPageTotal(pageCount + 1);
								zhanghInfo.getValue().setTips(errMsg);
							}
							break;
						case F:
							if (record.getJiaoym().equals(QueryConstants.CORPORATE_BATCH_QUERY_CODE)) {
								zhanghInfo.getValue().setQuery(false);
								continue;
							} else if (record.getJiaoym().equals(QueryConstants.PRIVATE_BATCH_QUERY_CODE)
									&& !"0".equals(record.getZhaoxz())) {
								// 如果是对私定期
								zhanghInfo.getValue().setQueryExectuer(new Handle0772ItemQuery());
							} else {
								String errMsg = "账号性质不匹配:" + zhanghInfo.getValue().getZHANGH() + "查询出的账号性质是"
										+ kemucc.getDisplay().getValue() + "需要查询的是" + record.getZhaoxz() + "(0活期,2定期,3全部)";
								record.setErrMsg(errMsg);
								zhanghInfo.getValue().setQuery(false);
								zhanghInfo.getValue().setFinished(QueryConstants.ZHANGH_QUERY_FINISHED);
								int pageCount = zhanghInfo.getValue().getPageTotal();
								zhanghInfo.getValue().setPageTotal(pageCount + 1);
								zhanghInfo.getValue().setTips(errMsg);
							}
							break;
						case E:
							if (record.getJiaoym().equals(QueryConstants.PRIVATE_BATCH_QUERY_CODE)) {
								zhanghInfo.getValue().setQuery(false);
								continue;
							} else if (record.getJiaoym().equals(QueryConstants.CORPORATE_BATCH_QUERY_CODE)) {
								// 如果是对公定期
								zhanghInfo.getValue().setQueryExectuer(new Handle0770ItemQuery(QueryConstants.TABLE_NAME_AGDMX));
							} else {
								String errMsg = "账号性质不匹配:" + zhanghInfo.getValue().getZHANGH() + "查询出的账号性质是"
										+ kemucc.getDisplay().getValue() + "需要查询的是" + record.getZhaoxz() + "(0活期,2定期,3全部)";
								record.setErrMsg(errMsg);
								zhanghInfo.getValue().setQuery(false);
								zhanghInfo.getValue().setFinished(QueryConstants.ZHANGH_QUERY_FINISHED);
								int pageCount = zhanghInfo.getValue().getPageTotal();
								zhanghInfo.getValue().setPageTotal(pageCount + 1);
								zhanghInfo.getValue().setTips(errMsg);
							}
							break;
						default:
							// 如果查询出气的KEMUCC（主要是空的情况）
							log.warn(zhanghInfo.getValue().getZHANGH() + "解析出的KEMUCC是" + kemucc.getDisplay().getValue());
							String msg = "账号" + zhanghInfo.getValue().getZHANGH() + "账号性质是" + kemucc;
							record.setErrMsg(msg);
							zhanghInfo.getValue().setQuery(false);
							zhanghInfo.getValue().setFinished(QueryConstants.ZHANGH_QUERY_FINISHED);
							int pageCount = zhanghInfo.getValue().getPageTotal();
							zhanghInfo.getValue().setPageTotal(pageCount + 1);							
						}
					}
				}
			} else {// 存在客户号且不存在账号的情况
				kehhaoParseResult.setQuery(false);
				String zjTip = "";
				if (HdqsConstants.CHAXZL_ZHJNZL.equals(record.getChaxzl())) {
					String idType = EIdentityType.ID + record.getZhjnzl();
					String idName = EIdentityType.valueOf(idType).getDisplay();
					zjTip = idName + record.getZhjhao() + ",";
				}
				String tips = zjTip + "客户号" + record.getKehuhao() + "在我行存在客户信息,无账号信息.";
				kehhaoParseResult.setTips(tips);
				kehhaoParseResult.setRecord(record);
				return kehhaoParseResult;
			}
		} catch (Exception e) {
			if (record.getChaxzl().equals(HdqsConstants.CHAXZL_ZHJNZL)) {
				throw e;// 如果是证件查询调用了本次的客户号查询，则需要将异常抛出到证件查询的函数中进行异常处理
			} else {
				log.error(e.getMessage(), e);
				String errMsg = "解析客户号" + record.getKehuhao() + "异常!";
				record.setErrMsg(errMsg);
				record.setRunStatus(HdqsConstants.RUNNING_STATUS_FAILURE);
				kehhaoParseResult.setQuery(false);
				kehhaoParseResult.setTips(errMsg);
				kehhaoParseResult.setRecord(record);
			}
		}

		return kehhaoParseResult;
	}

	@Override
	public Scan buildParseScanner(PybjyEO record) throws InvalidRecordParameterException {
		Scan scan = new Scan();
		scan.addFamily(QueryConstants.HBASE_TABLE_FAMILY_BYTE);

		scan.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AzhjxField.ZHANGH);
		scan.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AzhjxField.HUOBDH);
		scan.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AzhjxField.ZHYYJG);
		scan.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AzhjxField.KEMUCC);
		scan.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AzhjxField.YEWUDH);
		scan.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AzhjxField.KEHUZH);
		scan.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AzhjxField.KHZHLX);

		FilterList list = new FilterList(Operator.MUST_PASS_ALL);
		// 如果是0777查询则直接解析客户号
		byte[] startRow = buildStartRowByKHUH(record.getKehuhao());
		byte[] stopRow = buildStopRowByKHUH(record.getKehuhao());
		scan.setStartRow(startRow);
		scan.setStopRow(stopRow);
		if (record.getJiaoym().equals(QueryConstants.CORPORATE_BATCH_QUERY_CODE)) {
			// 设置账号性质(账号性质 0活期,2定期,3全部)
			FilterList orList = new FilterList(Operator.MUST_PASS_ONE);
			SingleColumnValueFilter dgdqFilter = new SingleColumnValueFilter(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AzhjxField.KEMUCC,
					CompareOp.EQUAL, Bytes.toBytes(QueryConstants.KEMUCC_DGDQ));
			SingleColumnValueFilter dghqFilter = new SingleColumnValueFilter(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AzhjxField.KEMUCC,
					CompareOp.EQUAL, Bytes.toBytes(QueryConstants.KEMUCC_DGHQ));
			orList.addFilter(dgdqFilter);
			orList.addFilter(dghqFilter);
			list.addFilter(orList);
			// 如果是0778查询，则根据0778输入的货币种类、账号性质进行查询
		} else if (record.getJiaoym().equals(QueryConstants.PRIVATE_BATCH_QUERY_CODE)) {
			// 设置账号性质(活期、定期、全部)
			if (record.getZhaoxz().equals(QueryConstants.ZHAOHXZ_DINGQI)) {
				SingleColumnValueFilter dsdqFilter = new SingleColumnValueFilter(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AzhjxField.KEMUCC,
						CompareOp.EQUAL, Bytes.toBytes(QueryConstants.KEMUCC_DSDQ));
				list.addFilter(dsdqFilter);
			} else if (record.getZhaoxz().equals(QueryConstants.ZHAOHXZ_HUOQI)) {
				SingleColumnValueFilter dshqFilter = new SingleColumnValueFilter(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AzhjxField.KEMUCC,
						CompareOp.EQUAL, Bytes.toBytes(QueryConstants.KEMUCC_DSHQ));
				list.addFilter(dshqFilter);
			} else {
				FilterList orList = new FilterList(Operator.MUST_PASS_ONE);
				SingleColumnValueFilter dsdqFilter = new SingleColumnValueFilter(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AzhjxField.KEMUCC,
						CompareOp.EQUAL, Bytes.toBytes(QueryConstants.KEMUCC_DSDQ));
				SingleColumnValueFilter dshqFilter = new SingleColumnValueFilter(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AzhjxField.KEMUCC,
						CompareOp.EQUAL, Bytes.toBytes(QueryConstants.KEMUCC_DSHQ));
				orList.addFilter(dsdqFilter);
				orList.addFilter(dshqFilter);
				list.addFilter(orList);
			}
			// 设置货币代号的过滤条件
			if (!"00".equals(record.getHuobdh())) {
				log.info("需要查询的货币代号是：" + record.getHuobdh());
				SingleColumnValueFilter huobiFilter = new SingleColumnValueFilter(QueryConstants.HBASE_TABLE_FAMILY_BYTE,
						Bytes.toBytes("HUOBDH"), CompareOp.EQUAL, Bytes.toBytes(record.getHuobdh()));
				list.addFilter(huobiFilter);
			}

		} else {
			throw new InvalidRecordParameterException("客户号解析中传入的查询码错误：" + record.getJiaoym());
		}

		scan.setFilter(list);
		try {
			log.debug(scan.toJSON());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return scan;
	}

	/**
	 * 生成查询客户号的结束rowkey
	 * 
	 * @param kehhao
	 * @return
	 */
	private byte[] buildStopRowByKHUH(String kehhao) {
		StringBuilder stopRow = new StringBuilder();
		stopRow.append(StringUtils.reverse(kehhao)).append("|").append(QueryConstants.MAX_CHAR);
		return Bytes.toBytes(stopRow.toString());
	}

	/**
	 * 生成查询客户号的开始rowkey
	 * 
	 * @param kehhao
	 * @return
	 */
	private byte[] buildStartRowByKHUH(String kehhao) {
		StringBuilder startRow = new StringBuilder();
		startRow.append(StringUtils.reverse(kehhao)).append("|").append(QueryConstants.MIN_NUM);
		return Bytes.toBytes(startRow.toString());
	}

	/**
	 * 在AZHJX查询khzh为空的时候，通过该方法查询akhzh查询对应的khzh的值
	 * 
	 * @author user
	 * 
	 */
	class KhzhGetter extends AbstractQuery<String> {

		public KhzhGetter(String tableName) {
			super(QueryConstants.TABLE_NAME_AKHZH_0773);
		}

		/**
		 * 查询AKHZH查询出 对应的Khzh
		 * 
		 * @param zhangh
		 *            账号
		 * @return
		 * @throws IOException
		 */
		public String getKhzh(String zhangh) throws IOException {
			Scan scan = new Scan();
			String startRow = StringUtils.reverse(zhangh) + "|#";
			String endRow = StringUtils.reverse(zhangh) + "|~";
			scan.setStartRow(Bytes.toBytes(startRow));
			scan.setStopRow(Bytes.toBytes(endRow));
			scan.addFamily(QueryConstants.HBASE_TABLE_FAMILY_BYTE);
			scan.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AkhzhField.KEHUZH);
			List<String> results = this.scan(scan);
			// 过滤出最后一个，由于已经在HBase中按照时间戳进行了排序，因此可以直接取出List的最后一个即可
			int size = results.size();
			return size <= 0 ? null : results.get(size - 1);

		}

		@Override
		protected String parse(Result result) throws IOException {
			byte[] khzh = result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AkhzhField.KEHUZH);
			return khzh == null ? "" : Bytes.toString(khzh);
		}
	}

	public static void main(String[] args) {
		List<String> list = new ArrayList<String>();
		System.out.println(list.get(0));
	}
}