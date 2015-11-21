package com.ceb.bank.query.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ceb.bank.authority.KehuzhAuthorize;
import com.ceb.bank.context.KehuzhContext;
import com.ceb.bank.context.RowkeyContext;
import com.ceb.bank.query.IHandleQuery;
import com.ceb.bank.query.scanner.Akhzh0772Scanner;
import com.ceb.bank.query.scanner.AsdmxLxScanner;
import com.ceb.bank.query.scanner.Kehuzh_zhanghScanner;
import com.ceb.bank.result.Query0772Result;
import com.ceb.hdqs.action.query.exception.AsynQueryException;
import com.ceb.hdqs.action.query.exception.ConditionNotExistException;
import com.ceb.hdqs.entity.PybjyEO;
import com.ceb.hdqs.po.Authorize;
import com.ceb.hdqs.query.utils.QueryConstants;
import com.ceb.hdqs.utils.TimerUtils;

/**
 * 0772对私定期账号历史查询
 */
public class Handle0772Query implements IHandleQuery<Query0772Result> {
	private static final Log log = LogFactory.getLog(Handle0772Query.class);
	private KehuzhAuthorize authorizeChk = new KehuzhAuthorize();
	private AsdmxLxScanner loadPageScanner = new AsdmxLxScanner();
	private Kehuzh_zhanghScanner azhjxScanner = new Kehuzh_zhanghScanner();
	private Akhzh0772Scanner akhzhScanner = new Akhzh0772Scanner();

	@Override
	public Query0772Result load(PybjyEO record) throws Exception {
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

		Query0772Result result = new Query0772Result();
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
		KehuzhContext context;
		// 如果输入的客户账号类型是存单、国债、储蓄存款存折则查询表AZHJX_0772进行解析
		if (QueryConstants._0772_KHZHLX_CUNDAN.equals(record.getKhzhlx()) || QueryConstants._0772_KHZHLX_CXCKCZ.equals(record.getKhzhlx())
				|| QueryConstants._0772_KHZHLX_GUOZHAI.equals(record.getKhzhlx())) {
			context = azhjxScanner.query(record);
			if (context == null) {
				log.info(record.getKehuzh() + "在AZHJX0772中不存在");
				throw new ConditionNotExistException(record.getKehuzh() + "在账号解析表中不存在");
			}
		} else {
			context = akhzhScanner.query(record);
			if (context == null) {
				StringBuffer buffer = new StringBuffer("客户账号" + record.getKehuzh());
				if (StringUtils.isNotBlank(record.getShunxh()) && !record.getShunxh().equals("0000")) {
					buffer.append(",账户序号" + record.getShunxh());
				}
				buffer.append("在客户账号表中不存在");
				log.info(buffer.toString());
				throw new ConditionNotExistException(buffer.toString());
			}
		}
		return context;
	}
}