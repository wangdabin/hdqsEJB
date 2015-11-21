package com.ceb.hdqs.action.asyn;

import java.util.List;

import org.apache.commons.logging.LogFactory;

import com.ceb.hdqs.action.query.ItemQueryProcessor;
import com.ceb.hdqs.action.query.parser.ParserOf0770;
import com.ceb.hdqs.action.query0770.Handle0770ItemQuery;
import com.ceb.hdqs.action.query0770.HandleQuery0770;
import com.ceb.hdqs.constants.HdqsConstants;
import com.ceb.hdqs.entity.PybjyEO;
import com.ceb.hdqs.entity.result.ZhangHParseResult;
import com.ceb.hdqs.query.utils.AutorizeUtils;
import com.ceb.hdqs.utils.TimerUtils;

/**
 * 同步查询{@link HandleQuery0770}转换成异步的查询类
 * 
 * @author user
 * 
 */
public class AsynQuery0770 extends AbstractAsynQuery {

	public AsynQuery0770(List<PybjyEO> list) {
		super(list);
		setLog(LogFactory.getLog(AsynQuery0770.class));
	}

	@Override
	public String startAsynchronizeQuery(boolean isSynPrint) throws Exception {
		getLog().info("开始异步查询0770.");
		TimerUtils timer = new TimerUtils();
		timer.start();

		String filePath = null;
		try {
			if (!isSynPrint) {
				getQueryCondition().setRunStatus(HdqsConstants.RUNNING_STATUS_RUNNING);
				update(getQueryCondition());// 修改为正在运行状态
				Boolean isAuthorize=AutorizeUtils.authorize(poList.get(0).getJio1gy());
				getQueryCondition().setRunStatus(HdqsConstants.RUNNING_STATUS_SUCCESS);// 默认为成功
				getQueryCondition().setAuthorize(isAuthorize);
			}
			initMaker();
			ParserOf0770 parser = new ParserOf0770();
			ZhangHParseResult zhangHParseResult = parser.parseCondition(getQueryCondition());
			if (zhangHParseResult == null) {
				zhangHParseResult = new ZhangHParseResult();
				zhangHParseResult.setQuery(false);
				String tip = "不存在账号" + getQueryCondition().getZhangh();
				zhangHParseResult.setTips(tip);
				ItemQueryProcessor itemQuery = new Handle0770ItemQuery(null);
				zhangHParseResult.setQueryExectuer(itemQuery);
			}
			zhangHParseResult.setZhanghTotal(1);
			filePath = doZhangHQuery(zhangHParseResult, true);
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
		getLog().info("异步0770查询结束,耗时: " + timer.getExecutionTime() + "ms.");
		return filePath;
	}
}