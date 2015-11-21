package com.ceb.hdqs.action.asyn;

import java.util.List;

import org.apache.commons.logging.LogFactory;

import com.ceb.hdqs.action.query.exception.ConditionNotExistException;
import com.ceb.hdqs.action.query.parser.ParserOf0772New;
import com.ceb.hdqs.action.query0771.Handle0771ItemQuery;
import com.ceb.hdqs.action.query0772.HandleQuery0772;
import com.ceb.hdqs.constants.HdqsConstants;
import com.ceb.hdqs.entity.PybjyEO;
import com.ceb.hdqs.entity.result.KehzhParserResult;
import com.ceb.hdqs.query.utils.AutorizeUtils;
import com.ceb.hdqs.utils.TimerUtils;

/**
 * <p>
 * 同步对私定期查询{@link HandleQuery0772} 进入异步查询时，实例化该对象进行异步查询任务
 * 
 * @author user
 * 
 */
public class AsynQuery0772 extends AbstractAsynQuery {

	public AsynQuery0772(List<PybjyEO> list) {
		super(list);
		setLog(LogFactory.getLog(AsynQuery0772.class));
	}

	@Override
	public String startAsynchronizeQuery(boolean isSynPrint) throws Exception {
		getLog().info("开始异步查询0772.");
		TimerUtils timer = new TimerUtils();
		timer.start();

		String filePath = null;
		try {
			if (!isSynPrint) {
				getQueryCondition().setRunStatus(HdqsConstants.RUNNING_STATUS_RUNNING);
				update(getQueryCondition());
				Boolean isAuthorize=AutorizeUtils.authorize(poList.get(0).getJio1gy());
				getQueryCondition().setRunStatus(HdqsConstants.RUNNING_STATUS_SUCCESS);// 默认为成功
				getQueryCondition().setAuthorize(isAuthorize);
			}
			initMaker();
			ParserOf0772New parser = new ParserOf0772New();
			KehzhParserResult khzhParseResult = null;
			try {
				khzhParseResult = parser.parseCondition(getQueryCondition());
			} catch (ConditionNotExistException e) {// 查无此号的标志
				khzhParseResult = new KehzhParserResult();
				khzhParseResult.setQuery(false);
				String tips = "不存在客户账号  " + getQueryCondition().getKehuzh();
				khzhParseResult.setTips(tips);
				khzhParseResult.setQueryExectuer(new Handle0771ItemQuery());
			}
			filePath = doKhzhQuery(khzhParseResult, true);
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
		getLog().info("异步查询0772查询结束,耗时: " + timer.getExecutionTime() + "ms.");
		return filePath;
	}
}