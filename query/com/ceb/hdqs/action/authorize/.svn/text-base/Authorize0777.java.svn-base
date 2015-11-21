package com.ceb.hdqs.action.authorize;

import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ceb.hdqs.action.query.exception.NoAuthorityException;
import com.ceb.hdqs.constants.HdqsConstants;
import com.ceb.hdqs.entity.PybjyEO;
import com.ceb.hdqs.entity.result.KehhaoParseResult;
import com.ceb.hdqs.entity.result.KehzhParserResult;
import com.ceb.hdqs.entity.result.ParseResult;
import com.ceb.hdqs.entity.result.ParseResultAsyn;
import com.ceb.hdqs.entity.result.ZhangHParseResult;
import com.ceb.hdqs.query.entity.AuthorizeLevel;
import com.ceb.hdqs.query.entity.EnumQueryKind;
import com.ceb.hdqs.query.entity.SkyPair;

/**
 * 0777查询业务授权 YEWUDH│业务代号 =2608 , 输入授权编码1 查询出的YNGYJG│营业机构号！=查的营业机构，输入授权编码1
 * 输入文件查询，输入授权编码5
 * 
 * @author user
 * 
 */
public class Authorize0777 {
	private static final Log LOG = LogFactory.getLog(Authorize0777.class);

	/**
	 * 解析0777查询业务授权 查询aghfh或者agdfh 时查出：YNGYJG│营业机构号
	 * 如果是非客户查询且查询出的YNGYJG│营业机构号！=查的营业机构，输入授权编码1 如果是非客户查询，查询aghfh时查询出YEWUDH│业务代号
	 * =2608 , 输入授权编码1 0777,0778 如果是输入文件查询，输入授权编码5 查询授权级别和授权原因（发起查询时授权）
	 * 
	 * @param parseResultOf0777
	 * @return
	 */
	public AuthorizeLevel grant(ParseResultAsyn parseResultOf0777) {
		AuthorizeLevel level = new AuthorizeLevel();
		Authorize0770 authorize0770 = new Authorize0770();
		for (ParseResult parseResult : parseResultOf0777.getParseResults()) {
			// ParseResult tmpParserResult = parseResult;
			if (parseResult == null) {
				LOG.info("获取的tmpParserResult为空!");
				continue;
			}
			try {
				// 判断解析结果是否为空

				// 对公查询授权不进行客户和非客户区别
				if (!parseResult.isQuery() || !parseResult.getRecord().getNeedAuth().booleanValue()) {
					LOG.info("不需要授权");
					continue;
				}
				PybjyEO rec = parseResult.getRecord();
				switch (EnumQueryKind.valueOf(EnumQueryKind.QK + rec.getChaxzl())) {
				case QK1:// 账号查询
					ParseResult zhangh = parseResult;
					AuthorizeLevel info1 = null;
					try {
						info1 = authorize0770.checkAuthority(zhangh);
					} catch (NoAuthorityException e) {
						parseResult.setTips(e.getMessage());
						continue;
					}
					if (level.setKey(info1.getKey())) {
						level.setValue(info1.getValue());
						parseResult.getRecord().setBeizxx(info1.getValue());
						parseResult.getRecord().setGuiyjb(info1.getKey());
					}
					parseResult.setAuthorityInfo(level);
					break;
				case QK2:// 客户账号查询
					KehzhParserResult khzhParserResult = (KehzhParserResult) parseResult;
					for (Entry<SkyPair<String, EnumQueryKind>, ZhangHParseResult> zhanghResult : khzhParserResult
							.getZhanghParseResult().entrySet()) {

						AuthorizeLevel info2 = null;
						try {
							info2 = authorize0770.checkAuthority(zhanghResult.getValue());
						} catch (NoAuthorityException e) {
							parseResult.setTips(e.getMessage());
							continue;
						}
						if (level.setKey(info2.getKey())) {
							level.setValue(info2.getValue());
							parseResult.getRecord().setBeizxx(info2.getValue());
							parseResult.getRecord().setGuiyjb(info2.getKey());
						}
					}

					khzhParserResult.setAuthorityInfo(level);
					break;
				case QK3:// 客户号查询
					KehhaoParseResult kehhaoParseResult = (KehhaoParseResult) parseResult;
					for (Entry<SkyPair<String, EnumQueryKind>, KehzhParserResult> khzhInfo : kehhaoParseResult
							.getKhzhParseResult().entrySet()) {
						for (Entry<SkyPair<String, EnumQueryKind>, ZhangHParseResult> zhanghInf : khzhInfo.getValue()
								.getZhanghParseResult().entrySet()) {
							AuthorizeLevel info3 = null;
							try {
								info3 = authorize0770.checkAuthority(zhanghInf.getValue());
							} catch (NoAuthorityException e) {
								parseResult.setTips(e.getMessage());
								continue;
							}
							if (level.setKey(info3.getKey())) {
								level.setValue(info3.getValue());
								parseResult.getRecord().setBeizxx(info3.getValue());
								parseResult.getRecord().setGuiyjb(info3.getKey());
							}
						}
					}
					kehhaoParseResult.setAuthorityInfo(level);
					break;
				default:
					break;
				}
			} catch (Exception e) {
				parseResult.getRecord().setRunStatus(HdqsConstants.RUNNING_STATUS_FAILURE);
				LOG.error(e.getMessage(), e);
			}

		}
		return level;
	}
}
