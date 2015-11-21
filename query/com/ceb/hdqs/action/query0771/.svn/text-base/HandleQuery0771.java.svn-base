package com.ceb.hdqs.action.query0771;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ceb.hdqs.action.authorize.Authorize0771;
import com.ceb.hdqs.action.query.IQuery;
import com.ceb.hdqs.action.query.exception.AsynQueryException;
import com.ceb.hdqs.action.query.exception.ConditionNotExistException;
import com.ceb.hdqs.action.query.parser.Parserof0771New;
import com.ceb.hdqs.entity.PybjyEO;
import com.ceb.hdqs.entity.result.Handle0771QueryResult;
import com.ceb.hdqs.entity.result.KehzhParserResult;
import com.ceb.hdqs.query.entity.AuthorizeLevel;
import com.ceb.hdqs.utils.TimerUtils;

public class HandleQuery0771 implements IQuery<Handle0771QueryResult> {
	private static final Log log = LogFactory.getLog(HandleQuery0771.class);

	public HandleQuery0771() {

	}

	@Override
	public Handle0771QueryResult query(PybjyEO record) throws Exception {
		TimerUtils timer = new TimerUtils();
		timer.start();

		KehzhParserResult khzhParseResult = parseInputCondidition(record);
		Handle0771QueryResult result = new Handle0771QueryResult();// 查询结果
		if (khzhParseResult == null) {// 解析结果为不存在客户账号的情况
			log.info(record.getKehuzh() + "在账户解析表中不存在");
			throw new ConditionNotExistException(record.getKehuzh() + "在AKHZH中不存在");
		}
		try {
			khzhParseResult.getQueryExectuer().isAsynchronize(record, khzhParseResult, result);
		} catch (AsynQueryException e) {
			timer.stop();
			log.info("进入异步查询,业务编号" + record.getSlbhao() + ",Cost time(ms)=" + timer.getExecutionTime());
			throw new AsynQueryException("进入异步查询");
		}

		// 判断授权级别
		Authorize0771 authorize0771 = new Authorize0771();
		AuthorizeLevel authInfo = authorize0771.checkAuthority(khzhParseResult);
		// 将授权放入record
		record.setGuiyjb(authInfo.getKey());
		record.setBeizxx(authInfo.getValue());

		if (khzhParseResult.getItemCount() == 0) {
			log.info(record.getKehuzh() + "  不存在交易明细");
			result.setAuthorityInfo(authInfo);
			return result;
		}
		// khzhParseResult.setPageLineNumber(record.getQueryNum());
		result.setAuthorityInfo(authInfo);
		result.setTotalNum(record.getItemCount());
		result.setKehuhao(khzhParseResult.getKehhao());
		result.setKhzwm(khzhParseResult.getKehzwm() == null ? "" : khzhParseResult.getKehzwm());
		timer.stop();
		log.info("查询完成，返回查询结果集，Cost time(ms)=" + timer.getExecutionTime());
		return result;
	}

	@Override
	public KehzhParserResult parseInputCondidition(PybjyEO record) throws Exception {
		Parserof0771New parserOf0771 = new Parserof0771New();
		return parserOf0771.parseCondition(record);
	}
}