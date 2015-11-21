package com.ceb.bank.authority;

import org.apache.log4j.Logger;

import com.ceb.bank.constants.ChaxzlEnum;
import com.ceb.bank.context.KehuhaoContext;
import com.ceb.bank.context.KehuzhContext;
import com.ceb.bank.context.ZhjhaoContext;
import com.ceb.hdqs.action.query.exception.NoAuthorityException;
import com.ceb.hdqs.entity.PybjyEO;
import com.ceb.hdqs.po.Authorize;

public class AuthorizeChk0778 {
	private static final Logger log = Logger.getLogger(AuthorizeChk0778.class);

	private KehuzhAuthorize authorizeChk = new KehuzhAuthorize();

	public AuthorizeChk0778() {

	}

	public Authorize check(PybjyEO record, Object context) throws NoAuthorityException {
		Authorize authorize = new Authorize();
		if (!record.getNeedAuth().booleanValue()) {
			log.info("不需要进行授权!");
			return authorize;
		}
		try {
			ChaxzlEnum chaxzl = ChaxzlEnum.valueOf(ChaxzlEnum.PREFIX + record.getChaxzl());
			switch (chaxzl) {
			case CHAXZL2:
				handleKehuzh(record, context, authorize);
				break;
			case CHAXZL3:
				handleKehuhao(record, context, authorize);
				break;
			case CHAXZL4:
				handleZhjhao(record, context, authorize);
				break;
			default:
				break;
			}
		} catch (Exception e) {// 本处异常不抛出,记录到数据库中
			log.error(e.getMessage(), e);
		}
		return authorize;
	}

	private void handleKehuzh(PybjyEO record, Object context, Authorize authorize) throws NoAuthorityException {
		if (context instanceof KehuzhContext) {
			KehuzhContext ctx = (KehuzhContext) context;
			Authorize tmpAuthorize = authorizeChk.check(record, ctx);
			if (tmpAuthorize.isNeedAuth() && tmpAuthorize.getGuiyjb() > authorize.getGuiyjb()) {
				authorize.setNeedAuth(true);
				authorize.setGuiyjb(tmpAuthorize.getGuiyjb());
				authorize.setBeizxx(tmpAuthorize.getBeizxx());
			}
		}
	}

	private void handleKehuhao(PybjyEO record, Object context, Authorize authorize) throws NoAuthorityException {
		if (context instanceof KehuhaoContext) {
			KehuhaoContext ctx = (KehuhaoContext) context;
			for (KehuzhContext khCtx : ctx.getList()) {
				Authorize tmpAuthorize = authorizeChk.check(record, khCtx);
				if (tmpAuthorize.isNeedAuth() && tmpAuthorize.getGuiyjb() > authorize.getGuiyjb()) {
					authorize.setNeedAuth(true);
					authorize.setGuiyjb(tmpAuthorize.getGuiyjb());
					authorize.setBeizxx(tmpAuthorize.getBeizxx());
				}
			}
		}
	}

	private void handleZhjhao(PybjyEO record, Object context, Authorize authorize) throws NoAuthorityException {
		if (context instanceof ZhjhaoContext) {
			ZhjhaoContext ctx = (ZhjhaoContext) context;
			for (KehuhaoContext koCtx : ctx.getList()) {
				for (KehuzhContext khCtx : koCtx.getList()) {
					Authorize tmpAuthorize = authorizeChk.check(record, khCtx);
					if (tmpAuthorize.isNeedAuth() && tmpAuthorize.getGuiyjb() > authorize.getGuiyjb()) {
						authorize.setNeedAuth(true);
						authorize.setGuiyjb(tmpAuthorize.getGuiyjb());
						authorize.setBeizxx(tmpAuthorize.getBeizxx());
					}
				}
			}
		}
	}
}