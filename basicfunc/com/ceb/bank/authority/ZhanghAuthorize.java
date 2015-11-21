package com.ceb.bank.authority;

import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.ceb.bank.context.ZhanghContext;
import com.ceb.hdqs.action.query.exception.NoAuthorityException;
import com.ceb.hdqs.entity.PybjyEO;
import com.ceb.hdqs.entity.SqbmEO;
import com.ceb.hdqs.po.Authorize;
import com.ceb.hdqs.query.utils.QueryConstants;
import com.ceb.hdqs.service.AuthorizeService;
import com.ceb.hdqs.service.JgcsService;
import com.ceb.hdqs.utils.JNDIUtils;

/**
 * 对公查询授权不进行客户和非客户区别
 * 
 */
public class ZhanghAuthorize {
	private static final Logger log = Logger.getLogger(ZhanghAuthorize.class);

	private JgcsService jgcsService;
	private AuthorizeService authorizeService;

	public ZhanghAuthorize() {
		try {
			jgcsService = (JgcsService) JNDIUtils.lookup(JgcsService.class);
			authorizeService = (AuthorizeService) JNDIUtils.lookup(AuthorizeService.class);
		} catch (NamingException e) {
			log.error(e.getMessage(), e);
		}
	}

	public Authorize check(PybjyEO record, ZhanghContext context) throws NoAuthorityException {
		Authorize authorize = new Authorize();
		if (!record.getNeedAuth().booleanValue()) {
			log.info("不需要进行授权!");
			return authorize;
		}

		// 查询出的YNGYJG│营业机构号！=查的营业机构，输入授权编码1
		log.debug("发起查询的YNGYJG: " + record.getYngyjg() + ",查询出的YNGYJG: " + context.getYngyjg());
		boolean isParent = false;

		isParent = jgcsService.isParentQuery(record.getYngyjg(), context.getYngyjg());

		log.debug("是否是上级查询下级: " + isParent);
		if (!isParent) {
			String errMsg = record.getZhangh() + "只有本机构和上级机构可以进行此操作!";
			record.setErrMsg(errMsg);
			throw new NoAuthorityException(errMsg);
		}
		log.debug("查询出的业务代号是: " + context.getYewudh());
		// 账号的业务代码是2608,查询明细数据,4级授权,提示特殊账户查询需要4级授权
		if ("2608".equals(context.getYewudh())) {
			SqbmEO sqbmEO = authorizeService.findByAuthorizedCode(QueryConstants.AuthorizeCode.yewdhauthorize_code, record.getQudaoo());

			if (sqbmEO != null) {
				authorize.setNeedAuth(true);
				authorize.setGuiyjb(sqbmEO.getGuiyjb());
				authorize.setBeizxx(sqbmEO.getBeizxx());
			}
		}
		log.info(authorize.getPrintMsg());
		return authorize;
	}
}