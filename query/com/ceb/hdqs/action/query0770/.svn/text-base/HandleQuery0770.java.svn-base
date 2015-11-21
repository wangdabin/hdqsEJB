package com.ceb.hdqs.action.query0770;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ceb.hdqs.action.asyn.AsynQuery0770;
import com.ceb.hdqs.action.authorize.Authorize0770;
import com.ceb.hdqs.action.query.IQuery;
import com.ceb.hdqs.action.query.exception.AsynQueryException;
import com.ceb.hdqs.action.query.exception.ConditionNotExistException;
import com.ceb.hdqs.action.query.parser.ParserOf0770;
import com.ceb.hdqs.action.query.parser.ZhanghParser;
import com.ceb.hdqs.entity.PybjyEO;
import com.ceb.hdqs.entity.result.Handle0770QueryResult;
import com.ceb.hdqs.entity.result.ZhangHParseResult;
import com.ceb.hdqs.query.entity.AuthorizeLevel;
import com.ceb.hdqs.utils.TimerUtils;

/**
 * 对公查询(0770业务) 主要实现对公活期查询和对公定期查询
 * <p>
 * 首先根据输入的查询条件{@link PybjyEO}通过{@link ZhanghParser}查询该输入条件的相关信息， 然后根据查询结果调用
 * {@link Handle0770ItemQuery}查询出交易明细
 * <p>
 * 如果查询进入异步查询，则实例化{@link AsynQuery0770}来完成异步查询
 * 
 * @author user
 * 
 */
public class HandleQuery0770 implements IQuery<Handle0770QueryResult> {
	private static final Log log = LogFactory.getLog(HandleQuery0770.class);
	private ParserOf0770 zhangHPaser = new ParserOf0770();

	public HandleQuery0770() {
	}

	@Override
	public Handle0770QueryResult query(PybjyEO record) throws Exception {
		TimerUtils timer = new TimerUtils();
		timer.start();

		Handle0770QueryResult result = new Handle0770QueryResult();
		/* 根据账号，解析账号的信息 */
		ZhangHParseResult zhanghParseResult = parseInputCondidition(record);
		log.info("main table scan successful ,begin to scan item table");

		/* 查询需要授权的级别 */
		Authorize0770 authorize0770 = new Authorize0770();
		AuthorizeLevel authorizeLevel = authorize0770.checkAuthority(zhanghParseResult);
		// 赋值到record
		record.setGuiyjb(authorizeLevel.getKey());
		record.setBeizxx(authorizeLevel.getValue());
		/* 查询明细表获取交易记录 */
		Handle0770ItemQuery corporateItemQuery = (Handle0770ItemQuery) zhanghParseResult.getQueryExectuer();
		try {
			corporateItemQuery.isAsynchronize(record, zhanghParseResult, result);
		} catch (AsynQueryException e) {
			timer.stop();
			log.info("进入异步查询,业务编号" + record.getSlbhao() + ",Cost time(ms)=" + timer.getExecutionTime());
			throw new AsynQueryException("进入异步查询");
		}

		result.setAuthorityInfo(authorizeLevel);
		result.setKehzwm(zhanghParseResult.getKehzwm());
		result.setHuobdh(zhanghParseResult.getHUOBDH());
		result.setTotalNum(record.getItemCount());
		result.setQueryTableName(zhanghParseResult.getQueryTableName());
		timer.stop();
		log.info("查询完成，返回查询结果集，Cost time(ms)=" + timer.getExecutionTime());
		return result;
	}

	/**
	 * 根据账号查询出货币代码和客户中文名
	 * 
	 * @param account
	 * @return
	 * @throws Exception
	 * @throws IOException
	 */
	@Override
	public ZhangHParseResult parseInputCondidition(PybjyEO record) throws Exception {
		ZhangHParseResult result = zhangHPaser.parseCondition(record);
		if (result == null) {
			throw new ConditionNotExistException("账号" + record.getZhangh() + "不存在!");
		}
		return result;
	}
}