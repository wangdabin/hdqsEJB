package com.ceb.hdqs.action.authorize.parse;

import com.ceb.hdqs.entity.PybjyEO;
import com.ceb.hdqs.entity.SqbmEO;
import com.ceb.hdqs.entity.result.ParseResult;
import com.ceb.hdqs.query.entity.AuthorizeLevel;
import com.ceb.hdqs.query.utils.QueryMethodUtils;

/**
 * 授权抽象查询
 * 
 * @author user
 * 
 */
public abstract class AbstractAuthorizeParser implements IAuthorizeParser {
//	protected String ASY_FILE_QUERY_ATUHORIZE_CODE;

	@Override
	public AuthorizeLevel parse(ParseResult parseResult) {

		AuthorizeLevel level = new AuthorizeLevel();
		PybjyEO record = parseResult.getRecord();
		SqbmEO sqbmEO = QueryMethodUtils.getAuthorityInfo(getAuthorizeCode(), record.getQudaoo());
		if(sqbmEO!=null){
			if (level.setKey(sqbmEO.getGuiyjb())) {
				level.setValue(sqbmEO.getBeizxx());
			}
		}
		return level;

	}

	@Override
	public abstract String getAuthorizeCode();
}
