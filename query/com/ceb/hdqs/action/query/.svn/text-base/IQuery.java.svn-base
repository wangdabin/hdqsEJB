package com.ceb.hdqs.action.query;

import java.io.IOException;

import com.ceb.hdqs.entity.PybjyEO;
import com.ceb.hdqs.entity.result.ParseResult;

/**
 * 查询业务借口
 * 
 * @author user
 * 
 * @param <T>
 *            查询结果输出类型
 */
public interface IQuery<T> {

	/**
	 * 解析输入的查询条件0770/账号，0771、0772/客户账号
	 * 
	 * @param record
	 * @return
	 * @throws Exception
	 */
	public ParseResult parseInputCondidition(PybjyEO record) throws Exception;

	/**
	 * 根据输入的查询条件进行查询
	 * 
	 * @param corporateQuery
	 *            查询条件
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	public T query(PybjyEO record) throws Exception;
}