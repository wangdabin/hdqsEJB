package com.ceb.bank.query.impl;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.ceb.bank.context.BkhhdConext;
import com.ceb.bank.context.RowkeyContext;
import com.ceb.bank.context.ZhanghContext;
import com.ceb.bank.query.IHandleQuery;
import com.ceb.bank.query.scanner.AdgmxLxScanner;
import com.ceb.bank.query.scanner.BkhhdLxScanner;
import com.ceb.bank.result.Query0770Result;
import com.ceb.bank.result.Query9019Result;
import com.ceb.hdqs.action.query.exception.AsynQueryException;
import com.ceb.hdqs.entity.PybjyEO;
import com.ceb.hdqs.utils.TimerUtils;

/**
 * 9019客户回单查询
 * @author wdb
 */
public class Handle9019Query implements IHandleQuery<Query9019Result>{
	private static final Logger log = Logger.getLogger(Handle9019Query.class);
	@Override
	public Query9019Result load(PybjyEO record) throws Exception {
		TimerUtils timer = new TimerUtils();
		timer.start();
		
		BkhhdConext context = parseCondition(record);
		
		BkhhdLxScanner loadPageScanner = new BkhhdLxScanner(context.getTableName());
		
		Map<Integer, List<RowkeyContext>> pageCache = null;
		try {
			pageCache = loadPageScanner.loadPage(record);
		} catch (AsynQueryException e) {
			timer.stop();
			log.info("进入异步查询,业务编号" + record.getSlbhao() + ",Cost time(ms)=" + timer.getExecutionTime());
			throw new AsynQueryException("进入异步查询");
		}
		Query9019Result result = new Query9019Result();
		if (pageCache != null) {
			result.setPageCache(pageCache);
		}
		result.setBishuu(record.getItemCount());
		result.setIndexTable(context.isIndexTable());
		result.setTableName(result.getTableName());
		
		timer.stop();
		log.info("查询完成,Cost time(ms)=" + timer.getExecutionTime());
		return result;
	}
	private BkhhdConext parseCondition(PybjyEO record) {
		BkhhdConext context = new BkhhdConext();
		//如果回单编号不为null，那么直接去对应的主表中去查询
		if("回单编号"!=null)
		{
			context.setTableName("BKHHD");
		}
		//如果客户账号不为null,而
		else if("客户账号"!=null)
		{
			context.setIndexTable(true);
			context.setTableName("索引表1");
		}
		else
		{
			context.setIndexTable(true);
			context.setTableName("索引表2");
		}
		return context;
	}
}
