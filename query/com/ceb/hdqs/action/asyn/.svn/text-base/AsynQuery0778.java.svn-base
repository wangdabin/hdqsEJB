package com.ceb.hdqs.action.asyn;

import java.util.List;

import org.apache.commons.logging.LogFactory;

import com.ceb.hdqs.action.authorize.Authorize0778;
import com.ceb.hdqs.action.query.parser.ParserOf0778;
import com.ceb.hdqs.constants.HdqsConstants;
import com.ceb.hdqs.entity.PybjyEO;
import com.ceb.hdqs.entity.result.ParseResultAsyn;
import com.ceb.hdqs.query.utils.AutorizeUtils;
import com.ceb.hdqs.utils.TimerUtils;

/**
 * 对私异步查询（0778查询）
 * <p>
 * 输入条件： 客户账号、客户号、证件 客户账号属性有客户账号类型、客户中文名、查询币种、账号性质（主是活期、定期、全部）、开始日期、终止日期
 * 客户号：客户号主要属性有客户号、客户中文名、查询币种、账号性质（主是活期、定期、全部）、开始日期、终止日期
 * 证件：证件类型、证件号码、客户中文名、查询币种、账号性质（主是活期、定期、全部）、开始日期、终止日期
 * <p>
 * 查询逻辑： 客户账号的查询： 客户账号的查询主要首先解析AZHJX03表，根据输入条件过滤出对应的账号，然后查询对应的明细表 客户号的查询：
 * 客户号查询首先查询AZHJX表，根据输入条件过滤出符合条件的账号，然后根据账号的KEMUCC查询对应的明细表 客户证件查询：
 * 客户证件查询首先查询BKHZJ表，查询出对应的客户号，然后通过客户号查询进行查询
 * 
 * 
 * @author user
 * 
 */
public class AsynQuery0778 extends AbstractAsynQuery {

	public AsynQuery0778(List<PybjyEO> list) {
		super(list);
		setLog(LogFactory.getLog(AsynQuery0778.class));
	}

	@Override
	public String startAsynchronizeQuery(boolean isSynPrint) throws Exception {
		getLog().info("开始异步查询0778.");
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
			getLog().info(this.getClass().getName() + "开始解析输入条件！");
			initMaker();
			ParserOf0778 _0778Parser = new ParserOf0778(poList);
			ParseResultAsyn _0778ParseResult = _0778Parser.parse();
			getLog().debug("AsynQuery0778解析输入条件结束，开始根据解析结果查询明细:");

			Authorize0778 authorizer = new Authorize0778();
			authorizer.grant(_0778ParseResult);

			/*
			 * 处理解析结果
			 */
			queryParseResult(_0778ParseResult);
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
		getLog().info("输入查询条件个数:" + poList.size() + "  成功条件个数:" + successNum + "    失败条件个数:" + failNum);
		getLog().info("异步查询0778查询结束,耗时: " + timer.getExecutionTime() + "ms.");
		return outputDir;
	}
}