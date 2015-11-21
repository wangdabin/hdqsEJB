package com.ceb.hdqs.action.asyn;

import java.util.List;

import org.apache.commons.logging.LogFactory;

import com.ceb.hdqs.action.authorize.Authorize0777;
import com.ceb.hdqs.action.query.exception.ConditionNotExistException;
import com.ceb.hdqs.action.query.exception.NoAuthorityException;
import com.ceb.hdqs.action.query.parser.AzhjxParser;
import com.ceb.hdqs.action.query.parser.KhzhParserof0777;
import com.ceb.hdqs.action.query.parser.ParserOf0770;
import com.ceb.hdqs.action.query.parser.ParserOf0777;
import com.ceb.hdqs.constants.HdqsConstants;
import com.ceb.hdqs.entity.PybjyEO;
import com.ceb.hdqs.entity.result.ParseResult;
import com.ceb.hdqs.entity.result.ParseResultAsyn;
import com.ceb.hdqs.query.utils.AutorizeUtils;
import com.ceb.hdqs.utils.TimerUtils;

/**
 * <P>
 * 对公异步查询业务控制类
 * <p>
 * 输入条件：账号、对公一号通、客户号、开始日期、终止日期
 * 
 * <p>
 * 给类主要控制对公异步查询的业务流程，具体的输入条件的解析是由{@link ParserOf0777}进行解析； 账号查询： 账号查询主要通
 * {@link ParserOf0770}，解析AGHFH和AGDFH,查询出对应的活期明细还是定期明细，然后查询对应的明细表； 对公一号通主要通过
 * {@link KhzhParserof0777},解析AKHZH表，解析出对应的账号，然后查询具体的明细表 客户号主要通过
 * {@link AzhjxParser},解析AZHJX表，解析出对应的账号，然后查询具体的明细表
 * 
 * <p>
 * 具体的查询过程仍然调用对公同步查询的代码进行查询
 * 
 * @author user
 * 
 */
public class AsynQuery0777 extends AbstractAsynQuery {

	public AsynQuery0777(List<PybjyEO> list) {
		super(list);
		setLog(LogFactory.getLog(AsynQuery0777.class));
	}

	@Override
	public String startAsynchronizeQuery(boolean isSynPrint) throws Exception {
		getLog().info("开始异步查询0777.");
		TimerUtils timer = new TimerUtils();
		timer.start();

		int successNum = 0;
		int failNum = 0;
		String outputDir = null;
		Boolean isAuthorize=AutorizeUtils.authorize(poList.get(0).getJio1gy());
		try {
			for (PybjyEO record : poList) {
				record.setRunStatus(HdqsConstants.RUNNING_STATUS_RUNNING);
			}
			batchUpdate(poList);
			for (PybjyEO record : poList) {
				record.setRunStatus(HdqsConstants.RUNNING_STATUS_SUCCESS);
				record.setAuthorize(isAuthorize);
			}
			getLog().info("AsynQuery0777开始解析输入条件！");
			initMaker();
			
			ParserOf0777 _0777Parser = new ParserOf0777(poList);
			ParseResultAsyn _0777ParseResult = _0777Parser.parse();

			getLog().info("AsynQuery0777解析输入条件结束，开始根据解析结果查询明细:");

			Authorize0777 authorizer = new Authorize0777();
			authorizer.grant(_0777ParseResult);
			/*
			 * 处理解析结果
			 */

			queryParseResult(_0777ParseResult);
		} catch (Exception e) {
			getLog().error(e.getMessage(), e);
		} finally {
			for (PybjyEO record : poList) {
				if (HdqsConstants.RUNNING_STATUS_FAILURE.equals(record.getRunStatus())) {
					failNum++;
				} else {
					successNum++;
				}
			}
			batchUpdate(poList);
		}
		timer.stop();
		getLog().info("输入查询条件个数:" + poList.size() + "  成功条件个数:" + successNum + " " + "   失败条件个数:" + failNum);
		getLog().info("异步查询0777查询结束,耗时(ms): " + timer.getExecutionTime());
		return outputDir;
	}

	public void checkAuthorize() throws NoAuthorityException, ConditionNotExistException {
		ParserOf0777 _0777Parser = new ParserOf0777(poList);
		ParseResultAsyn _0777ParseResult = null;
		try {
			_0777ParseResult = _0777Parser.parse();
		} catch (ConditionNotExistException e) {
			throw e;
		} catch (Exception e) {
			getLog().error(e.getMessage(), e);
		}
		Authorize0777 authorizer = new Authorize0777();
		authorizer.grant(_0777ParseResult);
		String tips = "";
		for (ParseResult parseResult : _0777ParseResult.getParseResults()) {
			if (!parseResult.isKuaJG()) {
				return;
			} else {
				tips = parseResult.getTips();
			}
		}
		throw new NoAuthorityException(tips);
	}
}