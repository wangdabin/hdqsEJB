package com.ceb.bank.authority;

import org.apache.log4j.Logger;

import com.ceb.bank.constants.ChaxzlEnum;
import com.ceb.bank.context.KehuhaoContext;
import com.ceb.bank.context.KehuzhContext;
import com.ceb.bank.context.Output0777Context;
import com.ceb.bank.context.ZhanghContext;
import com.ceb.hdqs.action.query.exception.NoAuthorityException;
import com.ceb.hdqs.entity.PybjyEO;
import com.ceb.hdqs.po.Authorize;

public class AuthorizeChk0777 {
	private static final Logger log = Logger.getLogger(AuthorizeChk0777.class);

	private ZhanghAuthorize authorizeChk = new ZhanghAuthorize();

	public AuthorizeChk0777() {

	}

	public Authorize check(PybjyEO record, Output0777Context context) throws NoAuthorityException {
		Authorize authorize = new Authorize();
		if (!record.getNeedAuth().booleanValue()) {
			log.info("不需要进行授权!");
			return authorize;
		}
		try {
			ChaxzlEnum chaxzl = ChaxzlEnum.valueOf(ChaxzlEnum.PREFIX + record.getChaxzl());
			switch (chaxzl) {
			case CHAXZL1:
				authorize = handleZhangh(record, context.getZhanghCtx());
				break;
			case CHAXZL2:
				handleKehuzh(record, context.getKehuzhCtx(), authorize);
				break;
			case CHAXZL3:
				handleKehuhao(record, context.getKehuhaoCtx(), authorize);
				break;
			default:
				break;
			}
		} catch (Exception e) {// 本处异常不抛出,记录到数据库中
			log.error(e.getMessage(), e);
		}
		return authorize;
	}

	private Authorize handleZhangh(PybjyEO record, ZhanghContext ctx) throws NoAuthorityException {
		return authorizeChk.check(record, ctx);
	}

	private void handleKehuzh(PybjyEO record, KehuzhContext ctx, Authorize authorize) throws NoAuthorityException {
		for (ZhanghContext zhCtx : ctx.getList()) {
			Authorize tmpAuthorize = authorizeChk.check(record, zhCtx);
			if (tmpAuthorize.isNeedAuth() && tmpAuthorize.getGuiyjb() > authorize.getGuiyjb()) {
				authorize.setNeedAuth(true);
				authorize.setGuiyjb(tmpAuthorize.getGuiyjb());
				authorize.setBeizxx(tmpAuthorize.getBeizxx());
			}
		}
	}

	private void handleKehuhao(PybjyEO record, KehuhaoContext ctx, Authorize authorize) throws NoAuthorityException {
		for (KehuzhContext khCtx : ctx.getList()) {
			for (ZhanghContext zhCtx : khCtx.getList()) {
				Authorize tmpAuthorize = authorizeChk.check(record, zhCtx);
				if (tmpAuthorize.isNeedAuth() && tmpAuthorize.getGuiyjb() > authorize.getGuiyjb()) {
					authorize.setNeedAuth(true);
					authorize.setGuiyjb(tmpAuthorize.getGuiyjb());
					authorize.setBeizxx(tmpAuthorize.getBeizxx());
				}
			}
		}
	}
}