package com.ceb.hdqs.action.query;

import org.apache.hadoop.hbase.client.Scan;

import com.ceb.hdqs.entity.PybjyEO;
import com.ceb.hdqs.entity.result.ParseResult;

/**
 * 输入条件解析接口
 * 
 * @author user
 * 
 */
public interface IconditionProcessor {

	/**
	 * 创建解析输入条件的Scan对象
	 * 
	 * @param record
	 * @return
	 * @throws Exception
	 */
	public Scan buildParseScanner(PybjyEO record) throws Exception;

	/**
	 * 开始解析输入条件
	 * 
	 * @param record
	 * @param queryExecutor
	 * @return
	 * @throws Exception
	 */
	public ParseResult parseCondition(PybjyEO record) throws Exception;
}
