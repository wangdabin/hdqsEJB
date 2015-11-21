package com.ceb.hdqs.action.asyn;

import java.util.List;

import org.apache.commons.logging.LogFactory;

import com.ceb.hdqs.action.query.exception.ConditionNotExistException;
import com.ceb.hdqs.action.query.parser.ParserOf0781;
import com.ceb.hdqs.constants.HdqsConstants;
import com.ceb.hdqs.entity.PybjyEO;
import com.ceb.hdqs.entity.result.KehzhParserResult;
import com.ceb.hdqs.query.utils.AutorizeUtils;
import com.ceb.hdqs.utils.TimerUtils;

/**
 * 0781同步转异步
 * 
 * @author user
 * 
 */
public class AsynQuery0781 extends AbstractAsynQuery {
	private ParserOf0781 parser = new ParserOf0781();

	public AsynQuery0781(List<PybjyEO> list) {
		super(list);
		setLog(LogFactory.getLog(AsynQuery0781.class));
	}

	@Override
	public String startAsynchronizeQuery(boolean isSynPrint) throws Exception {
		getLog().info("开始异步查询0781.");
		TimerUtils timer = new TimerUtils();
		timer.start();

		String outputdir = null;
		KehzhParserResult khzhParseResult = null;
		try {
			if (!isSynPrint) {
				getQueryCondition().setRunStatus(HdqsConstants.RUNNING_STATUS_RUNNING);
				update(getQueryCondition());
				Boolean isAuthorize=AutorizeUtils.authorize(poList.get(0).getJio1gy());
				getQueryCondition().setRunStatus(HdqsConstants.RUNNING_STATUS_SUCCESS);
				getQueryCondition().setAuthorize(isAuthorize);
			}
			initMaker();
			khzhParseResult = parser.parseCondition(getQueryCondition());

			outputdir = doKhzhQuery(khzhParseResult, true);
		} catch (ConditionNotExistException e) {// 查无此号的标志
			khzhParseResult = new KehzhParserResult();
			khzhParseResult.setQuery(false);
			String tips = "不存在客户账号 " + getQueryCondition().getKehuzh();
			khzhParseResult.setTips(tips);
		} catch (Exception e) {
			getQueryCondition().setRunStatus(HdqsConstants.RUNNING_STATUS_FAILURE);
			getLog().error(e.getMessage(), e);
		} finally {
			if (!isSynPrint) {
				update(getQueryCondition());
			}
		}
		flushBrokedReord();
		timer.stop();
		getLog().info("异步查询0778查询结束,耗时: " + timer.getExecutionTime() + "ms.");
		return outputdir;
	}
}