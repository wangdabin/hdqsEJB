package com.ceb.hdqs.action.query0772;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ceb.hdqs.action.asyn.AsynQuery0772;
import com.ceb.hdqs.action.authorize.Authorize0772;
import com.ceb.hdqs.action.query.IQuery;
import com.ceb.hdqs.action.query.exception.AsynQueryException;
import com.ceb.hdqs.action.query.exception.ConditionNotExistException;
import com.ceb.hdqs.action.query.exception.InvalidRecordParameterException;
import com.ceb.hdqs.action.query.parser.ParserOf0772New;
import com.ceb.hdqs.entity.PybjyEO;
import com.ceb.hdqs.entity.result.Handle0772QueryResult;
import com.ceb.hdqs.entity.result.KehzhParserResult;
import com.ceb.hdqs.query.entity.AuthorizeLevel;
import com.ceb.hdqs.utils.TimerUtils;

/**
 * 对私定期查询
 * <p>
 * 该查询主要实现 根据用户输入的客户账号、客户账号类型、顺序号，通过{@link ParserOf0772New}
 * 解析出该客户账号的信息和对应的账号信息，然后通过 {@link Handle0772ItemQuery}查询出明细
 * <p>
 * 如果在查询明细的时候,会实例化{@link AsynQuery0772}去完成查询任务
 * 
 * @author user
 * 
 */
public class HandleQuery0772 implements IQuery<Handle0772QueryResult> {
	private static final Log log = LogFactory.getLog(HandleQuery0772.class);

	public HandleQuery0772() {
	}

	@Override
	public Handle0772QueryResult query(PybjyEO record) throws Exception {
		TimerUtils timer = new TimerUtils();
		timer.start();

		KehzhParserResult khzhParseResult = parseInputCondidition(record);
		if (khzhParseResult == null) {
			throw new ConditionNotExistException(record.getKehuzh() + "在客户账号表中不存在!");
		}
		// 判断授权级别
		Authorize0772 authorize0772 = new Authorize0772();
		AuthorizeLevel authorizeLevel = authorize0772.checkAuthority(khzhParseResult);
		// 将授权放入record
		record.setGuiyjb(authorizeLevel.getKey());
		record.setBeizxx(authorizeLevel.getValue());
		Handle0772QueryResult result = new Handle0772QueryResult();
		try {
			khzhParseResult.getQueryExectuer().isAsynchronize(record, khzhParseResult, result);
		} catch (AsynQueryException e) {
			timer.stop();
			log.info("进入异步查询,业务编号" + record.getSlbhao() + ",Cost time(ms)=" + timer.getExecutionTime());
			throw new AsynQueryException("进入异步查询");
		}

		if (khzhParseResult.getItemCount() == 0) {
			log.info(record.getKehuzh() + "  不存在交易明细");
			result.setAuthorityInfo(authorizeLevel);
			return result;
		}

		result.setAuthorityInfo(authorizeLevel);
		result.setTotalNum(record.getItemCount());
		result.setKehuhao(khzhParseResult.getKehhao());
		result.setKhzwm(khzhParseResult.getKehzwm() == null ? "" : khzhParseResult.getKehzwm());
		log.info("客户账号" + record.getKehuzh() + "查询出的结果大小是：" + result.getItemList().size());
		timer.stop();
		log.info("查询完成，返回查询结果集，Cost time(ms)=" + timer.getExecutionTime());
		return result;
	}

	/**
	 * 从客户账户表(AKHZH)中查询出对应的账号
	 * 
	 * @param condation
	 * @return
	 * @throws Exception
	 * @throws ConditionNotExistException
	 * @throws InvalidRecordParameterException
	 * @throws IOException
	 */
	public KehzhParserResult parseInputCondidition(PybjyEO record) throws ConditionNotExistException, Exception {
		ParserOf0772New khzhParser = new ParserOf0772New();
		return khzhParser.parseCondition(record);
	}
}