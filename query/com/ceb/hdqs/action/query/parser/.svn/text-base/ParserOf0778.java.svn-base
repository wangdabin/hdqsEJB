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
import com.ceb.hdqs.entity.result.IdentityParseResult;
import com.ceb.hdqs.entity.result.KehhaoParseResult;
import com.ceb.hdqs.entity.result.KehzhParserResult;
import com.ceb.hdqs.entity.result.ParseResult;
import com.ceb.hdqs.entity.result.ParseResultAsyn;
import com.ceb.hdqs.query.entity.EIdentityType;
import com.ceb.hdqs.query.entity.EnumKhzhlx;
import com.ceb.hdqs.query.entity.EnumQueryKind;

/**
 * 0778输入条件解析 0778输入条件有客户账号{@link KhzhParserOf0778}、客户号{@link AzhjxParser}、证件
 * {@link BkhzjParser}
 * 
 * @author user
 * 
 */
public class ParserOf0778 {
	private static final Log log = LogFactory.getLog(ParserOf0778.class);

	private KhzhParserOf0778 khzhParser;
	private AzhjxParser kehhaoParser;
	private BkhzjParser identityParser;
	private List<PybjyEO> recordList;
	private List<ParseResult> parseResults = new ArrayList<ParseResult>();// 存储解析成功的输入条件

	private ParseResultAsyn _ParseResult;

	public ParserOf0778() {
	}

	public ParserOf0778(List<PybjyEO> recordList) {
		this.recordList = recordList;
		log.info("查询条件结合recordList的大小是：" + recordList.size());
		kehhaoParser = new AzhjxParser();
		identityParser = new BkhzjParser();
		_ParseResult = new ParseResultAsyn();
		khzhParser = new KhzhParserOf0778();
		// khzhParser = new KhzhParserNew();
	}

	public ParseResultAsyn parse() throws IOException, ConditionNotExistException, Exception {
		for (PybjyEO record : recordList) {
			parseCondition(record);
		}
		_ParseResult.setParseResults(parseResults);
		return _ParseResult;
	}

	public void parseCondition(PybjyEO record) throws IOException, ConditionNotExistException, Exception {
		log.debug(record.toString());
		String chaxzl = record.getChaxzl();
		log.debug("查询输入条件种类：" + chaxzl);
		try {
			switch (EnumQueryKind.valueOf(EnumQueryKind.QK + chaxzl)) {
			case QK4:// 证件类型
				IdentityParseResult identityParserResult = identityParser.parseCondition(record);
				if (identityParserResult != null) {
					parseResults.add(identityParserResult);
				} else {
					// **证件类型**证件号码**户名，在我行无开户记录
					String idType = EIdentityType.ID + record.getZhjnzl();
					String idName = EIdentityType.valueOf(idType).getDisplay();
					String suffixText = StringUtils.isBlank(record.getKhzwm()) ? ",在我行无开户记录" : "**" + record.getKhzwm() + ",在我行无开户记录";
					String TipsText = "**" + idName + "**" + record.getZhjhao() + suffixText;
					identityParserResult = new IdentityParseResult();
					identityParserResult.setQuery(false);
					identityParserResult.setRecord(record);

					identityParserResult.setTips(TipsText);
					identityParserResult.setKehzwm(record.getKhzwm());
					parseResults.add(identityParserResult);
				}
				break;
			case QK3:// 客户号
				log.info("po.getCustomerHao()" + record.getKehuhao());
				KehhaoParseResult kehhaoParseResult = kehhaoParser.parseCondition(record);
				if (kehhaoParseResult != null) {
					log.info("开始处理解析结果！");
					parseResults.add(kehhaoParseResult);
				} else {
					String zwmTip = StringUtils.isBlank(record.getKhzwm()) ? "" : ",中文名:" + record.getKhzwm();
					String TipText = "客户号:" + record.getKehuhao() + zwmTip + ",在我行无开户记录.";
					kehhaoParseResult = new KehhaoParseResult();
					kehhaoParseResult.setQuery(false);
					kehhaoParseResult.setTips(TipText);
					kehhaoParseResult.setRecord(record);
					parseResults.add(kehhaoParseResult);
				}
				break;
			case QK2:// 客户账号
				log.info("po.getAccount" + record.getKehuzh());
				KehzhParserResult kehzhParserResult = khzhParser.parseCondition(record);
				if (kehzhParserResult != null) {
					parseResults.add(kehzhParserResult);
				} else {
					String khzhlx = EnumKhzhlx.LX + record.getKhzhlx();
					String khzhName = EnumKhzhlx.valueOf(khzhlx).getDisplay().getValue();
					String tipText = khzhName + "," + record.getKehuzh() + ",在我行无开户记录";
					kehzhParserResult = new KehzhParserResult();
					kehzhParserResult.setQuery(false);

					kehzhParserResult.setTips(tipText);
					kehzhParserResult.setRecord(record);
					parseResults.add(kehzhParserResult);
				}
				break;
			default:
				record.setRunStatus(HdqsConstants.RUNNING_STATUS_FAILURE);
				record.setErrMsg("查询条件类型" + record.getChaxzl() + " 是0778查询不支持的条件类型");
			}
		} catch (Exception e) {
			throw e;
		}
	}
}