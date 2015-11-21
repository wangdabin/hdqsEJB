package com.ceb.hdqs.action.authorize.parse;

import com.ceb.hdqs.query.utils.QueryConstants;


/**
 * 业务代号权限解析
 * @author user
 *
 */
public class YewdhAuthorize extends AbstractAuthorizeParser{

	@Override
	public String getAuthorizeCode() {
		return QueryConstants.AuthorizeCode.yewdhauthorize_code;
	}

}
