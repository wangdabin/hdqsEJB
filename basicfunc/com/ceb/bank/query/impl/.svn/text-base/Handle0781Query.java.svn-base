package com.ceb.bank.query.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.ceb.bank.authority.KehuzhAuthorize;
import com.ceb.bank.context.KehuzhContext;
import com.ceb.bank.context.KemuccContext;
import com.ceb.bank.context.RowkeyAndTblContext;
import com.ceb.bank.context.YngyjgContext;
import com.ceb.bank.context.ZhanghContext;
import com.ceb.bank.query.IHandleQuery;
import com.ceb.bank.query.scanner.Adgmx0781LxScanner;
import com.ceb.bank.query.scanner.Akhzh0781Scanner;
import com.ceb.bank.query.scanner.KaaadxGetter;
import com.ceb.bank.query.scanner.KemuccGetter;
import com.ceb.bank.result.Query0781Result;
import com.ceb.hdqs.action.query.exception.AsynQueryException;
import com.ceb.hdqs.action.query.exception.ConditionNotExistException;
import com.ceb.hdqs.entity.PybjyEO;
import com.ceb.hdqs.po.Authorize;
import com.ceb.hdqs.query.entity.CustomerInfo;
import com.ceb.hdqs.query.utils.QueryMethodUtils;
import com.ceb.hdqs.utils.TimerUtils;

/**
 * 0781单位卡查询
 */
public class Handle0781Query implements IHandleQuery<Query0781Result> {
	private static final Logger log = Logger.getLogger(Handle0781Query.class);
	private KehuzhAuthorize authorizeChk = new KehuzhAuthorize();
	private Adgmx0781LxScanner loadPageScanner = new Adgmx0781LxScanner();
	private KaaadxGetter kaaadxGetter = new KaaadxGetter();
	private Akhzh0781Scanner khzhScanner = new Akhzh0781Scanner();
	private KemuccGetter kemuccGetter = new KemuccGetter();

	@Override
	public Query0781Result load(PybjyEO record) throws Exception {
		TimerUtils timer = new TimerUtils();
		timer.start();
		KehuzhContext context = parseCondition(record);
		log.info(record.getSlbhao() + "交易，单位卡号" + record.getKehuzh() + "查询出的账号个数是：" + context.getList().size());

		Authorize authorize = authorizeChk.check(record, context);
		record.setGuiyjb(authorize.getGuiyjb());
		record.setBeizxx(authorize.getBeizxx());

		// 判断是否异步
		Map<Integer, List<RowkeyAndTblContext>> pageCache = null;
		try {
			pageCache = loadPageScanner.loadPage(record, context);
		} catch (AsynQueryException e) {
			timer.stop();
			log.info("进入异步查询,业务编号" + record.getSlbhao() + ",Cost time(ms)=" + timer.getExecutionTime());
			throw new AsynQueryException("进入异步查询");
		}

		Query0781Result result = new Query0781Result();
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

	public KehuzhContext parseCondition(PybjyEO record) throws Exception {
		KehuzhContext context = new KehuzhContext();
		YngyjgContext yngyjgCtx = kaaadxGetter.query(record.getKehuzh());// 判断是否为单位卡,因为是卡,所以从VYKTD获取YNGYJG字段

		List<ZhanghContext> zhanghList = khzhScanner.query(record);
		if (zhanghList == null || zhanghList.size() == 0) {
			log.info("单位卡号" + record.getKehuzh() + "在AKHZH中不存在");
			throw new ConditionNotExistException("查询条件" + record.getKehuzh() + "不存在");
		}
		ZhanghContext zhCtx = zhanghList.get(0);
		CustomerInfo info = QueryMethodUtils.getCustomerChineseName(zhCtx.getKehhao());// 获取客户中文名

		for (ZhanghContext zhanghCtx : zhanghList) {
			KemuccContext kemuccCtx = kemuccGetter.query(zhanghCtx.getZhangh());
			zhanghCtx.setKemucc(kemuccCtx.getKemucc());
			zhanghCtx.setYewudh(kemuccCtx.getYewudh());
			if (StringUtils.isNotBlank(kemuccCtx.getHuobdh())) {
				zhanghCtx.setHuobdh(kemuccCtx.getHuobdh());
			}
			// BeanUtils.copyProperties(zhanghCtx, kemuccCtx);//因为是卡,不拷贝yngyjg
			zhanghCtx.setKehzwm(info.getKehzwm());
			zhanghCtx.setYngyjg(yngyjgCtx.getYngyjg());
			zhanghCtx.setJigomc(yngyjgCtx.getJigomc());
		}
		context.setList(zhanghList);
		context.setKehhao(zhCtx.getKehhao());
		context.setKehzwm(info.getKehzwm());
		context.setShfobz(info.getShfobz());
		context.setKhzhjb(info.getKhzhjb());
		
		return context;
	}
}