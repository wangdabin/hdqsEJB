package com.ceb.hdqs.action.authorize;

import javax.naming.NamingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ceb.hdqs.action.authorize.parse.AsyFileQueryAuthorize;
import com.ceb.hdqs.action.authorize.parse.EmployeeAuthorize;
import com.ceb.hdqs.action.authorize.parse.KhzhjbAuthorize;
import com.ceb.hdqs.action.authorize.parse.YewdhAuthorize;
import com.ceb.hdqs.action.authorize.parse.YngyjgAuthorize;
import com.ceb.hdqs.action.query.exception.NoAuthorityException;
import com.ceb.hdqs.entity.result.ParseResult;
import com.ceb.hdqs.query.entity.AuthorizeLevel;
import com.ceb.hdqs.service.JgcsService;
import com.ceb.hdqs.utils.JNDIUtils;

/**
 * 授权业务抽象类
 * 
 * @author user
 * 
 */
public abstract class AbsAuthorize implements IAuthorize {

	private static final Log LOG = LogFactory.getLog(AbsAuthorize.class);
	/**
	 * 当前查询业务的授权级别
	 */
	protected AuthorizeLevel authorizeLevel;

	/**
	 * 当前查询业务是否需要授权
	 */
	protected boolean isAuthorize;
	/**
	 * 异步文件查询授权解析
	 */
	protected AsyFileQueryAuthorize asyFileQueryAuthorize;
	/**
	 * 查询本行员工交易明细授权解析
	 */
	protected EmployeeAuthorize employeeAuthorize;
	/**
	 * 查询中涉及业务代号授权的解析
	 */
	protected YewdhAuthorize yewdhAuthorize;
	/**
	 * 跨营业机构查询授权的解析
	 */
	protected YngyjgAuthorize yngyjgAuthorize;

	/**
	 * 客户评估级别授权解析
	 */
	protected KhzhjbAuthorize khzhjbAuthorize;

	protected JgcsService jgcsService;

	public AbsAuthorize() {
		this.authorizeLevel = new AuthorizeLevel();
		this.asyFileQueryAuthorize = new AsyFileQueryAuthorize();
		this.employeeAuthorize = new EmployeeAuthorize();
		this.yewdhAuthorize = new YewdhAuthorize();
		this.yngyjgAuthorize = new YngyjgAuthorize();
		this.khzhjbAuthorize = new KhzhjbAuthorize();

		try {
			// 获取判断机构上下级的服务
			jgcsService = (JgcsService) JNDIUtils.lookup(JgcsService.class);
		} catch (NamingException e) {
			LOG.error(e.getMessage(), e);
		}
	}

	@Override
	public boolean isAuthorize(boolean isAuthorze) {
		return this.isAuthorize = isAuthorze;
	}

	@Override
	public void grant(AuthorizeLevel authorizeLevel) {
		if (authorizeLevel.getKey().intValue() > this.authorizeLevel.getKey().intValue()) {
			this.authorizeLevel = authorizeLevel;
		}
	}

	@Override
	public AuthorizeLevel getAuthorizeLevel() {
		return authorizeLevel;
	}

	@Override
	public abstract AuthorizeLevel checkAuthority(ParseResult parseResult) throws NoAuthorityException;
}
