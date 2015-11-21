package com.ceb.hdqs.action.authorize;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ceb.hdqs.action.query.exception.NoAuthorityException;
import com.ceb.hdqs.entity.result.ParseResult;
import com.ceb.hdqs.entity.result.ZhangHParseResult;
import com.ceb.hdqs.query.entity.AuthorizeLevel;
import com.ceb.hdqs.service.HdqsServiceException;

/**
 * 0770业务查询授权
 * 
 * @author user
 * 
 */
public class Authorize0770 extends AbsAuthorize {

	private static final Log LOG = LogFactory.getLog(Authorize0770.class);

	@Override
	public AuthorizeLevel checkAuthority(ParseResult parseResult) throws NoAuthorityException {
		ZhangHParseResult zhangHParseResult = (ZhangHParseResult) parseResult;
		AuthorizeLevel level = new AuthorizeLevel();
		// if (parseResult.getRecord().getQueryType()
		// .equals(QueryConstants.QUERY_TYPE_CUSTOMER) ||
		// !zhangHParseResult.getRecord().getNeedAuth().booleanValue()) {
		// return level;
		// }
		// 对公查询授权不进行客户和非客户区别
		if (!parseResult.isQuery() || !zhangHParseResult.getRecord().getNeedAuth().booleanValue()) {
			LOG.info("不需要进行授权!");
			return level;
		}
		// 非客户查询，查询出的YNGYJG│营业机构号！=查的营业机构，输入授权编码1
		LOG.info("查询类型：" + parseResult.getRecord().getChaxlx());
		LOG.info("发起查询的营业机构：" + parseResult.getRecord().getYngyjg());
		LOG.info("查询出的营业机构：" + zhangHParseResult.getZHYYNG());
		boolean isParent = false;

		isParent = jgcsService.isParentQuery(parseResult.getRecord().getYngyjg(), zhangHParseResult.getZHYYNG());

		LOG.info("是否是上级查询下级:" + isParent);
		if (!isParent) {
			// String errMsg = "营业机构" + parseResult.getRecord().getOperAgency()
			// + "无权查询营业机构" + zhangHParseResult.getZHYYNG() + "的信息";

			String errMsg = zhangHParseResult.getZHANGH() + "  只有本机构和上级机构可以进行此操作!";
			zhangHParseResult.getRecord().setErrMsg(errMsg);
			zhangHParseResult.setTips(errMsg);
			zhangHParseResult.setKuaJG(true);
			zhangHParseResult.setQuery(false);
			throw new NoAuthorityException(errMsg);
		}

		LOG.debug("查询出的业务代号是：" + zhangHParseResult.getYEWUDH());
		// 业务代号是2608，查询出YEWUDH│业务代号 =2608 , 输入授权编码1
		if ("2608".equals(zhangHParseResult.getYEWUDH())) {
			AuthorizeLevel info = yewdhAuthorize.parse(parseResult);
			if (level.setKey(info.getKey()))
				level.setValue(info.getValue());
		}
		return level;
	}
}