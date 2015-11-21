package com.ceb.hdqs.action.authorize;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ceb.hdqs.entity.result.KehzhParserResult;
import com.ceb.hdqs.entity.result.ParseResult;
import com.ceb.hdqs.query.entity.AuthorizeLevel;
import com.ceb.hdqs.query.utils.QueryConstants;

/**
 * 0772查询业务授权
 * 
 * @author user 客户账号 azhjx查询出的YNGYJG│营业机构号！=查的营业机构，输入授权编码1 查询授权级别和授权原因
 *         非客户查询，查BDSKH表 查出 SHFOBZ=1输入授权编码3 查询授权级别和授权原因。
 */
public class Authorize0772 extends AbsAuthorize {

	private static final Log LOG = LogFactory.getLog(Authorize0772.class);

	@Override
	public AuthorizeLevel checkAuthority(ParseResult parseResult) {

		KehzhParserResult khzhParseResult = (KehzhParserResult) parseResult;
		AuthorizeLevel level = new AuthorizeLevel();
		if (!parseResult.isQuery() || parseResult.getRecord().getChaxlx().equals(QueryConstants.QUERY_TYPE_CUSTOMER)
				|| !khzhParseResult.getRecord().getNeedAuth().booleanValue()) {
			return level;
		}
		// for (Entry<SkyPair<String, EnumQueryKind>, ZhangHParseResult>
		// zhanghInfo : khzhParseResult
		// .getZhanghParseResult().entrySet()) {
		// // 非客户查询，查询出的YNGYJG│营业机构号！=查的营业机构，输入授权编码1
		// String zhyyng = zhanghInfo.getValue().getZHYYNG();
		// LOG.debug("查询出的营业机构是：" + zhyyng);
		// LOG.debug("查询的营业机构是：" + parseResult.getRecord().getOperAgency());
		// if (zhyyng != null
		// && !zhyyng.equals(parseResult.getRecord().getOperAgency())) {
		// AuthorizeLevel info = yngyjgAuthorize.parse(parseResult);
		// if (level.setKey(info.getKey())) {
		// level.setValue(info.getValue());
		// }
		// }
		// }

		// 验证客户评估级别
		int khzhjb = khzhParseResult.getKhzhjb();
		LOG.debug("客户评估级别：" + khzhjb);
		if (khzhjb == QueryConstants.PRIVATE_AUTHORITY_KHZHJB_14
				|| khzhjb == QueryConstants.PRIVATE_AUTHORITY_KHZHJB_15
				|| khzhjb == QueryConstants.PRIVATE_AUTHORITY_KHZHJB_16) {
			AuthorizeLevel info = khzhjbAuthorize.parse(parseResult);
			if (level.setKey(info.getKey())) {
				level.setValue(info.getValue());
				parseResult.getRecord().setBeizxx(info.getValue());
				parseResult.getRecord().setGuiyjb(info.getKey());
			}
		}

		// 是否查询本行员工
		LOG.debug("柜员的是否标志是：" + khzhParseResult.getShfobz());
		if (1 == khzhParseResult.getShfobz()) {
			AuthorizeLevel info = employeeAuthorize.parse(parseResult);
			if (level.setKey(info.getKey())) {
				level.setValue(info.getValue());
				parseResult.getRecord().setBeizxx(info.getValue());
				parseResult.getRecord().setGuiyjb(info.getKey());
			}
		}
		return level;
	}

}
