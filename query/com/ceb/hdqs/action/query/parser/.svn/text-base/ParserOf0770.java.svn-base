package com.ceb.hdqs.action.query.parser;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.hbase.client.Scan;

import com.ceb.hdqs.action.query.IconditionProcessor;
import com.ceb.hdqs.action.query.exception.ConditionNotExistException;
import com.ceb.hdqs.action.query0770.Handle0770ItemQuery;
import com.ceb.hdqs.constants.HdqsConstants;
import com.ceb.hdqs.entity.PybjyEO;
import com.ceb.hdqs.entity.result.ZhangHParseResult;
import com.ceb.hdqs.query.utils.QueryConstants;

/**
 * 0770业务输入条件解析对象
 * 
 * @author user
 * 
 */
public class ParserOf0770 implements IconditionProcessor {
	private static final Log LOG = LogFactory.getLog(ParserOf0770.class);

	public ParserOf0770() {
	}

	public ZhangHParseResult parseCondition(PybjyEO record) throws Exception {
		LOG.debug("开始解析账号：" + record.getZhangh());
		String tableName = null;// 确定将要查询的明细文件表的表名称
		ZhangHParseResult zhanghResult = null;
		try {
			LOG.debug("开始解析" + QueryConstants.TABLE_NAME_AGHFH);
			ZhanghParser aghfhQuery = new ZhanghParser(QueryConstants.TABLE_NAME_AGHFH);
			zhanghResult = aghfhQuery.parseCondition(record);

			if (zhanghResult == null) {
				LOG.debug("开始解析" + QueryConstants.TABLE_NAME_AGDFH);
				ZhanghParser agdfhQuery = new ZhanghParser(QueryConstants.TABLE_NAME_AGDFH);
				zhanghResult = agdfhQuery.parseCondition(record);
				if (zhanghResult == null) {
					return null;
				} else {
					tableName = QueryConstants.TABLE_NAME_AGDMX;
				}
			} else {
				boolean ftAccount = checkBftzh(record);// 确认是否是法人活期透支户
				if (ftAccount) {
					tableName = QueryConstants.TABLE_NAME_AGHMX_FT;
					zhanghResult.setFtzh(true);
				} else {
					tableName = QueryConstants.TABLE_NAME_AGHMX;
				}
			}
			LOG.info("账号解析结束!");
			zhanghResult.setQueryExectuer(new Handle0770ItemQuery(tableName));
			zhanghResult.setQueryTableName(tableName);
			// 抬头中显示查询条件中的账号
			zhanghResult.setZHANGH(record.getZhangh());
		} catch (Exception e) {
			record.setErrMsg("解析账号" + record.getZhangh() + "异常!");
			record.setRunStatus(HdqsConstants.RUNNING_STATUS_FAILURE);
			LOG.error(e.getMessage(), e);
		}
		return zhanghResult;
	}

	@Override
	public Scan buildParseScanner(PybjyEO record) {
		throw new UnsupportedOperationException("本类不进行该方法的实现，在调用类ZhanghParser中进行实现。");
	}

	/**
	 * 确定是否是法人透支账户
	 * 
	 * @param record
	 * @return
	 * @throws Exception
	 * @throws ConditionNotExistException
	 * @throws IOException
	 */
	private boolean checkBftzh(PybjyEO record) throws Exception {
		BftzhParser ftParser = new BftzhParser();
		return ftParser.parseCondition(record);
	}
}