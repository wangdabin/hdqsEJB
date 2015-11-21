package com.ceb.hdqs.action.authorize;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ceb.hdqs.entity.result.KehzhParserResult;
import com.ceb.hdqs.entity.result.ParseResult;
import com.ceb.hdqs.query.entity.AuthorizeLevel;
import com.ceb.hdqs.query.utils.QueryConstants;

/**
 * 0781授权业务，该业务的授权逻辑和0771授权逻辑一样（20140225）
 * 
 * @author user
 * 
 */
public class Authorize0781 extends AbsAuthorize {
	private static final Log LOG = LogFactory.getLog(Authorize0781.class);

	@Override
	public AuthorizeLevel checkAuthority(ParseResult parseResult) {
		KehzhParserResult khzhParseResult = (KehzhParserResult) parseResult;
		AuthorizeLevel level = new AuthorizeLevel();
		LOG.debug("开始进行授权检验");
		if (!parseResult.isQuery() || parseResult.getRecord().getChaxlx().equals(QueryConstants.QUERY_TYPE_CUSTOMER)
				|| !khzhParseResult.getRecord().getNeedAuth().booleanValue()) {
			return level;
		}

		// 验证客户评估级别
		int khzhjb = khzhParseResult.getKhzhjb();
		LOG.debug("客户评估级别：" + khzhjb);
		if (khzhjb == QueryConstants.PRIVATE_AUTHORITY_KHZHJB_14 || khzhjb == QueryConstants.PRIVATE_AUTHORITY_KHZHJB_15
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
