package com.ceb.hdqs.action.authorize;

import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ceb.hdqs.action.authorize.parse.EmployeeAuthorize;
import com.ceb.hdqs.action.authorize.parse.KhzhjbAuthorize;
import com.ceb.hdqs.constants.HdqsConstants;
import com.ceb.hdqs.entity.result.IdentityParseResult;
import com.ceb.hdqs.entity.result.KehhaoParseResult;
import com.ceb.hdqs.entity.result.KehzhParserResult;
import com.ceb.hdqs.entity.result.ParseResult;
import com.ceb.hdqs.entity.result.ParseResultAsyn;
import com.ceb.hdqs.query.entity.AuthorizeLevel;
import com.ceb.hdqs.query.entity.EnumQueryKind;
import com.ceb.hdqs.query.utils.QueryConstants;

/**
 * 0778授权业务 客户号、客户账号和账号查询azhjx查询出的YNGYJG│营业机构号！=查的营业机构，输入授权编码1 如果是非客户查询，查BDSKH表
 * 查出 SHFOBZ=1输入授权编码3 0777,0778 如果是输入文件查询，输入授权编码5 查询授权级别和授权原因(发起查询时授权)
 * 
 * @author user
 * 
 */
public class Authorize0778 {

	private static final Log LOG = LogFactory.getLog(Authorize0778.class);

	public AuthorizeLevel grant(ParseResultAsyn parseResultOf0778) throws Exception {
		AuthorizeLevel level = new AuthorizeLevel();
		EmployeeAuthorize employeeAuthorize = new EmployeeAuthorize();
		KhzhjbAuthorize khzhjbAuthorize = new KhzhjbAuthorize();

		for (ParseResult parseResult : parseResultOf0778.getParseResults()) {
			// ParseResult tmpParserResult = parseResult;
			// 判断解析结果是否为空
			if (parseResult == null) {
				continue;
			}
			try {
				if (!parseResult.isQuery() || parseResult.getRecord().getChaxlx().equals(QueryConstants.QUERY_TYPE_CUSTOMER)
						|| !parseResult.getRecord().getNeedAuth().booleanValue()) {
					continue;
				}
				switch (EnumQueryKind.valueOf(EnumQueryKind.QK + parseResult.getRecord().getChaxzl())) {
				case QK4:// 证件查询
					IdentityParseResult idParseResult = (IdentityParseResult) parseResult;
					for (Entry<String, KehhaoParseResult> kehhaoInfo : idParseResult.getIdentityParseResult().entrySet()) {

						// 验证客户评估级别
						int khzhjb = kehhaoInfo.getValue().getKhzhjb();
						LOG.debug("客户评估级别：" + khzhjb);
						if (khzhjb == QueryConstants.PRIVATE_AUTHORITY_KHZHJB_14 || khzhjb == QueryConstants.PRIVATE_AUTHORITY_KHZHJB_15
								|| khzhjb == QueryConstants.PRIVATE_AUTHORITY_KHZHJB_16) {
							AuthorizeLevel info = khzhjbAuthorize.parse(kehhaoInfo.getValue());
							if (level.setKey(info.getKey())) {
								level.setValue(info.getValue());
								parseResult.getRecord().setBeizxx(info.getValue());
								parseResult.getRecord().setGuiyjb(info.getKey());
							}
						}
						// 是否查询本行的员工
						if (1 == kehhaoInfo.getValue().getShfbz()) {
							AuthorizeLevel info3 = employeeAuthorize.parse(kehhaoInfo.getValue());
							if (level.setKey(info3.getKey())) {
								level.setValue(info3.getValue());
								parseResult.getRecord().setBeizxx(info3.getValue());
								parseResult.getRecord().setGuiyjb(info3.getKey());
							}
						}
					}
					idParseResult.setAuthorityInfo(level);
					break;
				case QK2:// 客户账号查询
					// 通过0771确定营业机构和是否本行柜员的权限验证
					KehzhParserResult khzhParserResult = (KehzhParserResult) parseResult;
					Authorize0771 authorize0771 = new Authorize0771();
					level = authorize0771.checkAuthority(khzhParserResult);
					khzhParserResult.setAuthorityInfo(level);
					break;
				case QK3:// 客户号查询
					KehhaoParseResult kehhaoParseResult = (KehhaoParseResult) parseResult;
					// 验证客户评估级别
					int khzhjb = kehhaoParseResult.getKhzhjb();
					LOG.debug("客户评估级别：" + khzhjb);
					if (khzhjb == QueryConstants.PRIVATE_AUTHORITY_KHZHJB_14 || khzhjb == QueryConstants.PRIVATE_AUTHORITY_KHZHJB_15
							|| khzhjb == QueryConstants.PRIVATE_AUTHORITY_KHZHJB_16) {
						AuthorizeLevel info = khzhjbAuthorize.parse(kehhaoParseResult);
						if (level.setKey(info.getKey())) {
							level.setValue(info.getValue());
							parseResult.getRecord().setBeizxx(info.getValue());
							parseResult.getRecord().setGuiyjb(info.getKey());
						}
					}
					// 是否查询本行的员工
					if (kehhaoParseResult.getShfbz() == 1) {
						AuthorizeLevel info3 = employeeAuthorize.parse(kehhaoParseResult);
						if (level.setKey(info3.getKey())) {
							level.setValue(info3.getValue());
							parseResult.getRecord().setBeizxx(info3.getValue());
							parseResult.getRecord().setGuiyjb(info3.getKey());
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
