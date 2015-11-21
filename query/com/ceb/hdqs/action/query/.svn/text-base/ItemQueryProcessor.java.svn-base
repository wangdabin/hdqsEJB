package com.ceb.hdqs.action.query;

import java.io.IOException;
import java.util.List;

import com.ceb.hdqs.action.query.exception.BalanceBrokedException;
import com.ceb.hdqs.entity.PybjyEO;
import com.ceb.hdqs.entity.result.AbstractQueryResult;
import com.ceb.hdqs.entity.result.ParseResult;
import com.ceb.hdqs.entity.result.ZhangHParseResult;
import com.ceb.hdqs.query.entity.Page;
import com.ceb.hdqs.query.entity.PageHeader;
import com.ceb.hdqs.query.entity.QueryDocumentContext;
import com.ceb.hdqs.query.entity.QueryPageInfo;

public interface ItemQueryProcessor {

	/**
	 * 获取当前查询的parseresult的输出文件头
	 * 
	 * @param result
	 * @return
	 */
	public PageHeader getPageHeader(ZhangHParseResult parseResult);

	/**
	 * 具体的异步查询，查询过程
	 * 
	 * @param t
	 * @return
	 * @throws IOException
	 */
	public Page<? extends AbstractQueryResult> nextPage(ZhangHParseResult parseResult, QueryDocumentContext documentContext) throws IOException, BalanceBrokedException;

	/**
	 * 判断是否进入异步查询
	 * 
	 * @param record
	 * @param parseRecord
	 * @return
	 */
	public void isAsynchronize(PybjyEO record, ParseResult parseRecord, AbstractQueryResult result) throws Exception;

	/**
	 * 同步查询，进行翻页查询的操作
	 * 
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public List<? extends AbstractQueryResult> synQueryNextPage(QueryPageInfo pageInfo) throws Exception;

	public String getQueryTable();
}
