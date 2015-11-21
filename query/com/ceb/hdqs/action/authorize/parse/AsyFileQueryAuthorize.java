package com.ceb.hdqs.action.authorize.parse;

import com.ceb.hdqs.query.utils.QueryConstants;

/**
 * 异步查询，提交文件查询条件
 * 
 * @author user
 * 
 */
public class AsyFileQueryAuthorize extends AbstractAuthorizeParser {

	@Override
	public String getAuthorizeCode() {
		return QueryConstants.AuthorizeCode.asyfilequeryauthorize_code;
	}

}
