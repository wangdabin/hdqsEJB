package com.ceb.bank.authority;

import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.ceb.bank.context.KehuzhContext;
import com.ceb.hdqs.action.query.exception.NoAuthorityException;
import com.ceb.hdqs.entity.PybjyEO;
import com.ceb.hdqs.entity.SqbmEO;
import com.ceb.hdqs.po.Authorize;
import com.ceb.hdqs.query.utils.QueryConstants;
import com.ceb.hdqs.service.AuthorizeService;
import com.ceb.hdqs.utils.JNDIUtils;

/**
 * 客户查询不需要授权
 */
public class KehuzhAuthorize {
	private static final Logger log = Logger.getLogger(KehuzhAuthorize.class);
	private AuthorizeService authorizeService;

	public KehuzhAuthorize() {
		try {
			authorizeService = (AuthorizeService) JNDIUtils.lookup(AuthorizeService.class);
		} catch (NamingException e) {
			log.error(e.getMessage(), e);
		}
	}

	public Authorize check(PybjyEO record, KehuzhContext context) throws NoAuthorityException {
		Authorize authorize = new Authorize();
		if (!record.getNeedAuth().booleanValue() || record.getChaxlx().equals(QueryConstants.QUERY_TYPE_CUSTOMER)) {
			log.info("不需要进行授权!");
			return authorize;
		}
		int khzhjb = context.getKhzhjb();
		log.info("客户评估级别：" + khzhjb);
		if (khzhjb == QueryConstants.PRIVATE_AUTHORITY_KHZHJB_14 || khzhjb == QueryConstants.PRIVATE_AUTHORITY_KHZHJB_15
				|| khzhjb == QueryConstants.PRIVATE_AUTHORITY_KHZHJB_16) {
			SqbmEO sqbmEO = authorizeService.findByAuthorizedCode(QueryConstants.AuthorizeCode.khzhjbauthorize_code, record.getQudaoo());
			if (sqbmEO != null) {
				authorize.setNeedAuth(true);
				if (sqbmEO.getGuiyjb() > authorize.getGuiyjb()) {
					authorize.setGuiyjb(sqbmEO.getGuiyjb());
					authorize.setBeizxx(sqbmEO.getBeizxx());
				}
			}
		}
		log.info("是否查询本行员工标志：" + context.getShfobz());
		if (1 == context.getShfobz()) {
			SqbmEO sqbmEO = authorizeService.findByAuthorizedCode(QueryConstants.AuthorizeCode.employeeauthorize_code, record.getQudaoo());
			if (sqbmEO != null) {
				authorize.setNeedAuth(true);
				if (sqbmEO.getGuiyjb() > authorize.getGuiyjb()) {
					authorize.setGuiyjb(sqbmEO.getGuiyjb());
					authorize.setBeizxx(sqbmEO.getBeizxx());
				}
			}
		}
		log.info(authorize.getPrintMsg());
		return authorize;
	}
}
