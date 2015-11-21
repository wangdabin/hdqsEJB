package com.ceb.hdqs.action.query.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;

import com.ceb.bank.htable.AbstractQuery;
import com.ceb.hdqs.action.query.IconditionProcessor;
import com.ceb.hdqs.constants.HdqsConstants;
import com.ceb.hdqs.entity.PybjyEO;
import com.ceb.hdqs.entity.result.IdentityParseResult;
import com.ceb.hdqs.entity.result.KehhaoParseResult;
import com.ceb.hdqs.entity.result.KehzhParserResult;
import com.ceb.hdqs.entity.result.ZhangHParseResult;
import com.ceb.hdqs.query.entity.EIdentityType;
import com.ceb.hdqs.query.entity.EnumQueryKind;
import com.ceb.hdqs.query.entity.SkyPair;
import com.ceb.hdqs.query.utils.QueryConfUtils;
import com.ceb.hdqs.query.utils.QueryConstants;

/**
 * 输入条件为证件的据解析
 * 
 * @author user
 * 
 */
public class BkhzjParser extends AbstractQuery<IdentityParseResult> implements IconditionProcessor {
	private static final Log LOG = LogFactory.getLog(BkhzjParser.class);

	private IdentityParseResult identityParseResult;

	public BkhzjParser() {
		super(QueryConstants.TABLE_NAME_BKHZJ);
	}

	public Set<String> getkehuhaoResults(PybjyEO record) {
		identityParseResult = new IdentityParseResult();
		Set<String> kehuhaoSet = new HashSet<String>();
		try {
			identityParseResult.setRecord(record);
			String idType = record.getZhjnzl();
			try {
				EIdentityType identityType = EIdentityType.valueOf(EIdentityType.ID + idType);
				identityParseResult.setIdentityType(identityType);
			} catch (Exception e) {
				LOG.warn("未识别的证件类型：" + idType);
				identityParseResult.setIdentityType(EIdentityType.IDux);
			}

			LOG.info("开始解析客户证件" + record.getZhjhao() + "信息！");

			String zhjhao = record.getZhjhao();
			String zhjnzl = record.getZhjnzl();
			/**
			 * 初始化一个ArrayList，完成对客户证件查询时，18 位的身份证装换成15位的身份证号码的转换
			 */
			List<String> identifyNumList = new ArrayList<String>();
			identifyNumList.add(zhjhao);
			if ("1".equals(zhjnzl) && zhjhao.length() == 18) {// 如果是身份证，则进行对应的转换
				identifyNumList.add(get15IdentifyNum(zhjhao));
			}
			for (String num : identifyNumList) {
				PybjyEO tmprecord=new PybjyEO();
				BeanUtils.copyProperties(tmprecord, record);
				tmprecord.setZhjhao(num);
				Scan scan = buildParseScanner(tmprecord);
				this.scan(scan);// 可以优化查询
				LOG.info("身份证" + num + "解析出的客户号个数是：" + identityParseResult.getIdentityParseResult().size());
			}
			for (Map.Entry<String, KehhaoParseResult> entry : identityParseResult.getIdentityParseResult().entrySet()) {
				kehuhaoSet.add(entry.getValue().getKehhao());
			}
		} catch (Exception e) {
			LOG.debug("解析客户号时出现错误");

		}
		return kehuhaoSet;

	}

	@Override
	public IdentityParseResult parseCondition(PybjyEO record) {
		identityParseResult = new IdentityParseResult();
		try {
			identityParseResult.setRecord(record);
			String idType = record.getZhjnzl();
			try {
				EIdentityType identityType = EIdentityType.valueOf(EIdentityType.ID + idType);
				identityParseResult.setIdentityType(identityType);
			} catch (Exception e) {
				LOG.warn("未识别的证件类型：" + idType);
				identityParseResult.setIdentityType(EIdentityType.IDux);
			}

			if (!StringUtils.isBlank(record.getKehuhao())) {
				AzhjxParser khhaoPaser = new AzhjxParser();
				String khhao = record.getKehuhao();
				record.setKehuhao(khhao);
				KehhaoParseResult khhaoParseResult = khhaoPaser.parseCondition(record);
				String newName = "";
				if (khhaoParseResult == null) {
					khhaoParseResult = new KehhaoParseResult();
					khhaoParseResult.setRecord(record);
					khhaoParseResult.setQuery(false);
					// **证件类型**证件号码**户名，在我行无开户记录
					String eidType = EIdentityType.ID + record.getZhjnzl();
					String idName = EIdentityType.valueOf(eidType).getDisplay();
					String zwmTip = StringUtils.isBlank(record.getKhzwm()) ? "" : ",中文名:" + record.getKhzwm();
					String tips = idName + "," + record.getZhjhao() + " 客户号:" + record.getKehuhao() + zwmTip + ",在我行无开户记录.";
					khhaoParseResult.setTips(tips);
					identityParseResult.getIdentityParseResult().put(khhao, khhaoParseResult);
				} else {
					newName = khhaoParseResult.getKehzwm();
					LOG.debug("通过解析出的客户号查询出的客户中文名是：" + newName);
					if (StringUtils.isNotBlank(newName)) {
						identityParseResult.setKehzwm(newName);
					} else {
						identityParseResult.setKehzwm("");
					}
					identityParseResult.getIdentityParseResult().put(khhao, khhaoParseResult);
				}

				int zhanghTotal = 0;
				for (Entry<String, KehhaoParseResult> idResult : identityParseResult.getIdentityParseResult().entrySet()) {
					zhanghTotal += idResult.getValue().getZhanghTotal();
				}
				// 对每个账号的解析结果记录改证件号码所解析出的账号的个数
				for (Entry<String, KehhaoParseResult> idResult : identityParseResult.getIdentityParseResult().entrySet()) {
					for (Entry<SkyPair<String, EnumQueryKind>, KehzhParserResult> khzhInfo : idResult.getValue().getKhzhParseResult().entrySet()) {
						for (Entry<SkyPair<String, EnumQueryKind>, ZhangHParseResult> zhanghInfo : khzhInfo.getValue().getZhanghParseResult().entrySet()) {
							zhanghInfo.getValue().setZhanghTotal(zhanghTotal);
						}
					}
				}

				// 如果存在证件号码，但是无法查询出客户号，则需要将identityParseResult 设置为无法查询
				if (zhanghTotal == 0) {
					identityParseResult.setQuery(false);
					// identityParseResult.setTips("**证件**" + record.getZhjhao()
					// + "**户名**" + record.getKhzwm()
					// + "在我行无开户记录,请联系总行查证.");
					String type = EIdentityType.ID + record.getZhjnzl();
					String idName = EIdentityType.valueOf(type).getDisplay();
					String suffixText = StringUtils.isBlank(record.getKhzwm()) ? ",在我行存在客户("+record.getKehuhao()+")信息,无账号信息." : "**" + record.getKhzwm() + ",在我行存在客户("+record.getKehuhao()+")信息,无账号信息.";
					String TipsText = "**" + idName + "**" + record.getZhjhao() + suffixText;
					identityParseResult.setTips(TipsText);
				}
				return identityParseResult;
			} else {
				return null;
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			String errMsg = "解析证件" + record.getZhjhao() + "异常!";
			record.setErrMsg(errMsg);
			record.setRunStatus(HdqsConstants.RUNNING_STATUS_FAILURE);
			identityParseResult.setQuery(false);
			identityParseResult.setTips(errMsg);
			identityParseResult.setRecord(record);
			return identityParseResult;
		}

	}

	@Override
	protected IdentityParseResult parse(Result result) throws IOException {
		String kehhao = Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, Bytes.toBytes("KEHHAO")));
		LOG.debug("解析出的客户号是:" + kehhao);

		Map<String, KehhaoParseResult> khhParseResult = identityParseResult.getIdentityParseResult();
		if (!khhParseResult.containsKey(kehhao)) {
			KehhaoParseResult khhResult = new KehhaoParseResult();
			khhResult.setKehhao(kehhao);
			khhParseResult.put(kehhao, khhResult);
		}
		return identityParseResult;
	}

	@Override
	public Scan buildParseScanner(PybjyEO record) {
		Scan scan = new Scan();
		byte[] startRow = buildStartRowByIdentify(record.getZhjhao(), record.getZhjnzl());
		byte[] stopRow = buildStopRowByIdentify(record.getZhjhao(), record.getZhjnzl());
		scan.setStartRow(startRow);
		scan.setStopRow(stopRow);
		return scan;
	}

	/**
	 * 身份证由18位到15位
	 * 
	 * @param identifyNum
	 * @return
	 */
	private String get15IdentifyNum(String identifyNum) {
		StringBuilder newIdentifyNum = new StringBuilder();
		newIdentifyNum.append(identifyNum.substring(0, 6)).append(identifyNum.substring(8, 17));
		LOG.debug("原来的18位的身份证的号码是" + identifyNum);
		LOG.debug("生成的15位的身份证的号码是：" + newIdentifyNum.toString());
		return newIdentifyNum.toString();
	}

	/**
	 * 生成查询客户证件表的结束key
	 * 
	 * @param identifyNum
	 * @param identifyType
	 * @return
	 */
	private byte[] buildStopRowByIdentify(String identifyNum, String identifyType) {
		StringBuilder stopRow = new StringBuilder();
		String rowKeySplit = QueryConfUtils.getActiveConfig().get(QueryConstants.ROWKEY_SPLITTER, "|");
		stopRow.append(StringUtils.reverse(identifyNum)).append(rowKeySplit).append(identifyType).append(rowKeySplit).append(QueryConstants.MAX_CHAR);
		return Bytes.toBytes(stopRow.toString());
	}

	/**
	 * 生成查询客户证件表的开始key
	 * 
	 * @param identifyNum
	 * @param identifyType
	 * @return
	 */
	private byte[] buildStartRowByIdentify(String identifyNum, String identifyType) {
		StringBuilder startRow = new StringBuilder();
		String rowKeySplit = QueryConfUtils.getActiveConfig().get(QueryConstants.ROWKEY_SPLITTER, "|");
		startRow.append(StringUtils.reverse(identifyNum)).append(rowKeySplit).append(identifyType).append(rowKeySplit).append(QueryConstants.MIN_NUM);
		return Bytes.toBytes(startRow.toString());
	}
}
