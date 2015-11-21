package com.ceb.bank.query.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ceb.bank.authority.KehuzhAuthorize;
import com.ceb.bank.constants.KhzhlxEnum;
import com.ceb.bank.context.KehuzhContext;
import com.ceb.bank.context.RowkeyContext;
import com.ceb.bank.query.IHandleQuery;
import com.ceb.bank.query.scanner.Akhzh0771Scanner;
import com.ceb.bank.query.scanner.AshmxLxScanner;
import com.ceb.bank.result.Query0771Result;
import com.ceb.hdqs.action.query.exception.AsynQueryException;
import com.ceb.hdqs.action.query.exception.ConditionNotExistException;
import com.ceb.hdqs.entity.PybjyEO;
import com.ceb.hdqs.po.Authorize;
import com.ceb.hdqs.utils.TimerUtils;

/**
 * 0771对私活期账号历史查询
 */
public class Handle0771Query implements IHandleQuery<Query0771Result> {
	private static final Log log = LogFactory.getLog(Handle0771Query.class);
	private KehuzhAuthorize authorizeChk = new KehuzhAuthorize();
	private AshmxLxScanner loadPageScanner = new AshmxLxScanner();
	private Akhzh0771Scanner akhzhScanner = new Akhzh0771Scanner();

	@Override
	public Query0771Result load(PybjyEO record) throws Exception {
		TimerUtils timer = new TimerUtils();
		timer.start();

		KehuzhContext context = parseCondition(record);
		log.info(record.getSlbhao() + "交易，客户账号" + record.getKehuzh() + "查询出的账号个数是：" + context.getList().size());

		Authorize authorize = authorizeChk.check(record, context);
		record.setGuiyjb(authorize.getGuiyjb());
		record.setBeizxx(authorize.getBeizxx());

		Map<Integer, List<RowkeyContext>> pageCache = null;
		try {
			pageCache = loadPageScanner.loadPage(record, context);
		} catch (AsynQueryException e) {
			timer.stop();
			log.info("进入异步查询,业务编号" + record.getSlbhao() + ",Cost time(ms)=" + timer.getExecutionTime());
			throw new AsynQueryException("进入异步查询");
		}

		Query0771Result result = new Query0771Result();
		if (pageCache != null) {
			result.setPageCache(pageCache);
		}
		result.setAuthorize(authorize);
		result.setBishuu(record.getItemCount());
		result.setKehhao(context.getKehhao());
		result.setKehuzh(record.getKehuzh());
		result.setGerzwm(context.getKehzwm());

		timer.stop();
		log.info("查询完成,Cost time(ms)=" + timer.getExecutionTime());
		return result;
	}

	public KehuzhContext parseCondition(PybjyEO record) throws IOException, ConditionNotExistException {
		log.info("Khzhlx: " + KhzhlxEnum.valueOf(KhzhlxEnum.PREFIX + record.getKhzhlx()));
		KehuzhContext context = akhzhScanner.query(record);
		if (context == null) {
			log.info(record.getKehuzh() + "在AKHZH中不存在");
			throw new ConditionNotExistException(record.getKehuzh() + "在客户账号表中不存在");
		}
		return context;
	}
}