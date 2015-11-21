package com.ceb.hdqs.action.query0781;

import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ceb.hdqs.action.authorize.Authorize0781;
import com.ceb.hdqs.action.query.IQuery;
import com.ceb.hdqs.action.query.exception.AsynQueryException;
import com.ceb.hdqs.action.query.parser.ParserOf0781;
import com.ceb.hdqs.entity.PybjyEO;
import com.ceb.hdqs.entity.result.Handle0781QueryResult;
import com.ceb.hdqs.entity.result.KehzhParserResult;
import com.ceb.hdqs.entity.result.ZhangHParseResult;
import com.ceb.hdqs.query.entity.AuthorizeLevel;
import com.ceb.hdqs.query.entity.EnumQueryKind;
import com.ceb.hdqs.query.entity.SkyPair;
import com.ceb.hdqs.query.utils.QueryConfUtils;
import com.ceb.hdqs.query.utils.QueryConstants;
import com.ceb.hdqs.utils.TimerUtils;

/**
 * 0781单位卡查询
 * 
 * @author user
 * 
 */
public class HandleQuery0781 implements IQuery<Handle0781QueryResult> {
	private static final Log LOG = LogFactory.getLog(HandleQuery0781.class);
	private ParserOf0781 cardParser = new ParserOf0781();

	@Override
	public KehzhParserResult parseInputCondidition(PybjyEO record) throws Exception {
		return cardParser.parseCondition(record);
	}

	@Override
	public Handle0781QueryResult query(PybjyEO record) throws Exception {
		Handle0781QueryResult result = new Handle0781QueryResult();
		TimerUtils timer = new TimerUtils();
		timer.start();

		// 开始解析和查询
		KehzhParserResult cardParseResult = parseInputCondidition(record);
		int itemCount = 0;
		int threshold = QueryConfUtils.getActiveConfig().getInt(QueryConstants.SYNCHRONIZE_SWITCH_THRESHOLD, 1000);
		for (Entry<SkyPair<String, EnumQueryKind>, ZhangHParseResult> zhanghInfo : cardParseResult.getZhanghParseResult().entrySet()) {
			// will throw AsynQueryException
			ZhangHParseResult zhanghParseResult = zhanghInfo.getValue();
			zhanghParseResult.getQueryExectuer().isAsynchronize(record, zhanghParseResult, result);

			itemCount += record.getItemCount();
			if (itemCount > threshold) {
				throw new AsynQueryException("进入异步查询");
			}
		}

		// 判断授权级别
		Authorize0781 authorize0781 = new Authorize0781();
		AuthorizeLevel authInfo = authorize0781.checkAuthority(cardParseResult);
		// 将授权放入record
		record.setGuiyjb(authInfo.getKey());
		record.setBeizxx(authInfo.getValue());

		if (itemCount == 0) {
			LOG.info(record.getKehuzh() + "  不存在交易明细");
			result.setAuthorityInfo(authInfo);
			return result;
		}
		cardParseResult.setPageLineNumber(record.getQueryNum());
		result.setAuthorityInfo(authInfo);
		result.setTotalNum(itemCount);
		result.setKehuhao(cardParseResult.getKehhao());
		result.setKhzwm(cardParseResult.getKehzwm() == null ? "" : cardParseResult.getKehzwm());
		timer.stop();
		LOG.info("查询完成，返回查询结果集，Cost time(ms)=" + timer.getExecutionTime());
		return result;
	}
}
