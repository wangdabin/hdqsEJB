package com.ceb.bank.query.impl;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.ceb.bank.authority.ZhanghAuthorize;
import com.ceb.bank.constants.HBaseQueryConstants;
import com.ceb.bank.context.RowkeyContext;
import com.ceb.bank.context.ZhanghContext;
import com.ceb.bank.query.IHandleQuery;
import com.ceb.bank.query.scanner.AdgfhScanner;
import com.ceb.bank.query.scanner.AdgmxLxScanner;
import com.ceb.bank.query.scanner.BhqtzChecker;
import com.ceb.bank.result.Query0770Result;
import com.ceb.hdqs.action.query.exception.AsynQueryException;
import com.ceb.hdqs.action.query.exception.ConditionNotExistException;
import com.ceb.hdqs.constants.HdqsConstants;
import com.ceb.hdqs.entity.PybjyEO;
import com.ceb.hdqs.po.Authorize;
import com.ceb.hdqs.utils.TimerUtils;

/**
 * 0770对公账号历史查询
 */
public class Handle0770Query implements IHandleQuery<Query0770Result> {
	private static final Logger log = Logger.getLogger(Handle0770Query.class);
	private ZhanghAuthorize authorizeChk = new ZhanghAuthorize();
	private BhqtzChecker bhqtzChecker = new BhqtzChecker();

	@Override
	public Query0770Result load(PybjyEO record) throws Exception {
		TimerUtils timer = new TimerUtils();
		timer.start();

		ZhanghContext context = parseCondition(record);
		if (context == null) {
			throw new ConditionNotExistException("账号" + record.getZhangh() + "不存在!");
		}
		log.info(record.getSlbhao() + "交易，账号" + record.getZhangh() + ",要查询的明细表: " + context.getTableName());

		Authorize authorize = authorizeChk.check(record, context);
		record.setGuiyjb(authorize.getGuiyjb());
		record.setBeizxx(authorize.getBeizxx());

		AdgmxLxScanner loadPageScanner = new AdgmxLxScanner(context.getTableName());
		Map<Integer, List<RowkeyContext>> pageCache = null;
		try {
			pageCache = loadPageScanner.loadPage(record);
		} catch (AsynQueryException e) {
			timer.stop();
			log.info("进入异步查询,业务编号" + record.getSlbhao() + ",Cost time(ms)=" + timer.getExecutionTime());
			throw new AsynQueryException("进入异步查询");
		}

		Query0770Result result = new Query0770Result();
		if (pageCache != null) {
			result.setPageCache(pageCache);
		}
		result.setAuthorize(authorize);
		result.setBishuu(record.getItemCount());
		result.setZhangh(record.getZhangh());
		result.setHuobdh(context.getHuobdh());
		result.setKehzwm(context.getKehzwm());
		result.setTableName(context.getTableName());

		timer.stop();
		log.info("查询完成,Cost time(ms)=" + timer.getExecutionTime());
		return result;
	}

	/**
	 * 根据分户文件确定查询哪个明细文件
	 * 
	 * @param record
	 * @return
	 * @throws Exception
	 */
	public ZhanghContext parseCondition(PybjyEO record) throws Exception {
		String tableName = null;// 确定将要查询的明细文件表的表名称
		ZhanghContext context = null;
		try {
			AdgfhScanner aghfhQuery = new AdgfhScanner(HBaseQueryConstants.TABLE_AGHFH);
			context = aghfhQuery.query(record);
			if (context == null) {
				AdgfhScanner agdfhQuery = new AdgfhScanner(HBaseQueryConstants.TABLE_AGDFH);
				context = agdfhQuery.query(record);
				if (context == null) {
					return null;
				} else {
					tableName = HBaseQueryConstants.TABLE_AGDMX;
				}
			} else {
				boolean ftAccount = checkBftzh(record);
				log.info("是否法透账号: " + ftAccount);
				if (ftAccount) {
					tableName = HBaseQueryConstants.TABLE_AGHMX_FT;
				} else {
					tableName = HBaseQueryConstants.TABLE_AGHMX;
				}
			}
			context.setTableName(tableName);
		} catch (Exception e) {
			record.setErrMsg("解析账号" + record.getZhangh() + "异常!");
			record.setRunStatus(HdqsConstants.RUNNING_STATUS_FAILURE);
			log.error(e.getMessage(), e);
		}
		return context;
	}

	/**
	 * 确认是否是法人活期透支户
	 */
	private boolean checkBftzh(PybjyEO record) throws Exception {
		return bhqtzChecker.check(record);
	}
}