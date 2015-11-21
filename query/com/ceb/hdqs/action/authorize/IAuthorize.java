package com.ceb.hdqs.action.authorize;

import com.ceb.hdqs.action.query.exception.NoAuthorityException;
import com.ceb.hdqs.entity.result.ParseResult;
import com.ceb.hdqs.query.entity.AuthorizeLevel;

/**
 * 授权业务接口
 * 
 * @author user
 * 
 */
public interface IAuthorize {

	public boolean isAuthorize(boolean isAuthorze);

	public AuthorizeLevel checkAuthority(ParseResult parseResult) throws NoAuthorityException;

	/**
	 * 记录查询任务需要的授权信息
	 * 
	 * @param authorizeLevel
	 *            需要的授权信息（授权级别和授权原因）
	 */
	public void grant(AuthorizeLevel authorizeLevel);

	/**
	 * 返回查询业务授权级别
	 * 
	 * @return
	 */
	public AuthorizeLevel getAuthorizeLevel();

}
