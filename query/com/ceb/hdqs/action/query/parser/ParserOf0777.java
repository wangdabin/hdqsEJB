package com.ceb.hdqs.action.query.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ceb.hdqs.action.query.exception.ConditionNotExistException;
import com.ceb.hdqs.constants.HdqsConstants;
import com.ceb.hdqs.entity.PybjyEO;
import com.ceb.hdqs.entity.result.KehhaoParseResult;
import com.ceb.hdqs.entity.result.KehzhParserResult;
import com.ceb.hdqs.entity.result.ParseResult;
import com.ceb.hdqs.entity.result.ParseResultAsyn;
import com.ceb.hdqs.entity.result.ZhangHParseResult;
import com.ceb.hdqs.query.entity.EnumKhzhlx;
import com.ceb.hdqs.query.entity.EnumQueryKind;

/**
 * 对公查询解析过程
 * 
 * @author user
 * 
 */
public class ParserOf0777 {
	private static final Log LOG = LogFactory.getLog(ParserOf0777.class);

	private ParserOf0770 zhanghParser;
	private KhzhParserof0777 khzhParser;
	private AzhjxParser kehhaoParser;
	private List<PybjyEO> recordList;
	private List<ParseResult> parseResults = new ArrayList<ParseResult>();// 存储解析成功的输入条件

	private ParseResultAsyn _ParseResult;

	public ParserOf0777(List<PybjyEO> recordList) {
		this.recordList = recordList;
		LOG.info("传入的recordList的大小是：" + recordList.size());
		zhanghParser = new ParserOf0770();
		khzhParser = new KhzhParserof0777();
		kehhaoParser = new AzhjxParser();
		_ParseResult = new ParseResultAsyn();
	}

	public ParseResultAsyn parse() throws IOException, ConditionNotExistException, Exception {
		for (PybjyEO record : recordList) {
			parseCondition(record);
		}
		_ParseResult.setParseResults(parseResults);
		return _ParseResult;
	}

	public void parseCondition(PybjyEO record) throws IOException, ConditionNotExistException, Exception {
		LOG.info(record.toString());
		String chaxzl = record.getChaxzl();
		try {
			switch (EnumQueryKind.valueOf(EnumQueryKind.QK + chaxzl)) {
			case QK3:// 客户号
				LOG.debug("Kehuhao: " + record.getKehuhao());
				KehhaoParseResult kehhaoParseResult = kehhaoParser.parseCondition(record);
				if (kehhaoParseResult != null) {
					parseResults.add(kehhaoParseResult);
				} else {
					String zwmTip = StringUtils.isBlank(record.getKhzwm()) ? "" : ",中文名:" + record.getKhzwm();
					String tips = "客户号:" + record.getKehuhao() + zwmTip + ",在我行无开户记录.";
					kehhaoParseResult = new KehhaoParseResult();
					kehhaoParseResult.setQuery(false);
					kehhaoParseResult.setRecord(record);

					kehhaoParseResult.setTips(tips);
					parseResults.add(kehhaoParseResult);
				}
				break;
			case QK2:// 对公一号通(客户账号)
				LOG.debug("Kehuzh " + record.getKehuzh());
				KehzhParserResult kehzhParserResult = khzhParser.parseCondition(record);
				if (kehzhParserResult != null) {
					parseResults.add(kehzhParserResult);
				} else {
					String khzhlx = EnumKhzhlx.LX + record.getKhzhlx();
					String khzhName = EnumKhzhlx.valueOf(khzhlx).getDisplay().getValue();
					String tipText = khzhName + "," + record.getKehuzh() + ",在我行无开户记录";
					kehzhParserResult = new KehzhParserResult();
					kehzhParserResult.setQuery(false);
					kehzhParserResult.setRecord(record);

					kehzhParserResult.setTips(tipText);
					parseResults.add(kehzhParserResult);
				}
				break;
			case QK1:// 账号
				ZhangHParseResult zhangHParseResult = zhanghParser.parseCondition(record);
				if (zhangHParseResult != null) {
					parseResults.add(zhangHParseResult);
				} else {
					String tipText = "账号" + record.getZhangh() + "在我行无开户记录.";
					zhangHParseResult = new ZhangHParseResult();
					zhangHParseResult.setQuery(false);
					zhangHParseResult.setTips(tipText);
					zhangHParseResult.setRecord(record);

					zhangHParseResult.setZhanghTotal(1);
					zhangHParseResult.setQueriedZhNum(1);
					parseResults.add(zhangHParseResult);
				}
				break;
			default:
				record.setRunStatus(HdqsConstants.RUNNING_STATUS_FAILURE);
				record.setErrMsg("查询条件类型" + record.getChaxzl() + "是0777查询不支持的条件类型");
			}

		} catch (Exception e) {
			throw e;
		}
	}

	public static void main(String[] args) {
		System.out.println(EnumQueryKind.valueOf("5"));
	}
}