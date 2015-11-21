package com.ceb.hdqs.entity.result;

import java.io.IOException;

import org.apache.hadoop.hbase.client.Scan;

import com.ceb.hdqs.action.query.ItemQueryProcessor;
import com.ceb.hdqs.entity.PybjyEO;
import com.ceb.hdqs.query.entity.Page;
import com.ceb.hdqs.query.entity.PageHeader;

/**
 * 所有输入条件输入后都会被解析成该对象的子类
 * 
 * @author user
 * 
 * @param <T>
 */
public abstract class ParseResult extends AbstractQueryResult {

	private static final long serialVersionUID = 2494614996253232881L;
	/**
	 * 对该条件执行查询的执行器
	 */
	protected ItemQueryProcessor queryExectuer;
	/**
	 * 该输入条件的客户中文名
	 */
	protected String kehzwm;

	/**
	 * 该输入条件的客户号
	 */
	protected String kehhao;

	protected Scan scanner;

	/**
	 * 查询的输入条件
	 */
	protected PybjyEO record;

	/**
	 * 查询条件的开始日期
	 */
	protected String startDate;
	/**
	 * 查询条件的结束日期
	 */
	protected String endDate;
	/**
	 * 发起查询的日期
	 */
	protected String queryDate;

	/**
	 * 输入条件的说明（包括解析结果说明）
	 */
	protected String tips;

	/**
	 * 每个结果的临时的存放目录
	 */
	protected String tempOutputDir;

	/**
	 * 每页查询记录数
	 * 
	 * @return
	 */
	protected int pageLineNumber;

	/**
	 * 明细总数
	 */
	protected long itemCount;
	
	/**
	 * 所属条件的总条数
	 */
	
	protected long allItemCount;
	
	/**
	 * 已经查询的条数
	 */
	protected long queriedCount;
	
	protected long allPageCount;

	/**
	 * 异步查询文件头的输出格式
	 */
	protected PageHeader pageHead;

	protected String ZHYYNG;
	protected String JIGOMC;
	protected int finished = 0;
	protected boolean isQuery = true;
	protected boolean isKuaJG = false;
	protected String noQueryReason;
	/**
	 * 当前输入条件解析出的账号的总量
	 */
	protected int zhanghTotal;
	/**
	 * 已经查询完成的账号的总量
	 */
	protected int queriedZhNum;
	
	/**
	 * 每个条件查询出的页面数
	 */
	private int pageTotal;
	
	public String getNoQueryReason() {
		return noQueryReason;
	}

	public void setNoQueryReason(String noQueryReason) {
		this.noQueryReason = noQueryReason;
	}

	public boolean isQuery() {
		return isQuery;
	}

	public void setQuery(boolean isQuery) {
		this.isQuery = isQuery;
	}

	public PageHeader getPageHead() {
		return pageHead;
	}

	public void setPageHead(PageHeader pageHead) {
		this.pageHead = pageHead;
	}

	public long getQueriedCount() {
		return queriedCount;
	}

	public void setQueriedCount(long queriedCount) {
		this.queriedCount = queriedCount;
	}

	public long getItemCount() {
		return itemCount;
	}

	public void setItemCount(long totalNumber) {
		this.itemCount = totalNumber;
	}

	public int getPageLineNumber() {
		return pageLineNumber;
	}

	public void setPageLineNumber(int pageNumber) {
		this.pageLineNumber = pageNumber;
	}

	public String getTempOutputDir() {
		return tempOutputDir;
	}

	public void setTempOutputDir(String tempOutputDir) {
		this.tempOutputDir = tempOutputDir;
	}

	public String getTips() {
		return tips;
	}

	public void setTips(String tips) {
		this.tips = tips;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getQueryDate() {
		return queryDate;
	}

	public void setQueryDate(String queryDate) {
		this.queryDate = queryDate;
	}

//	protected int queryTpye;// 查询类型：客户查询、非客户查询
//
//	public int getQueryTpye() {
//		return queryTpye;
//	}
//
//	public void setQueryTpye(int queryTpye) {
//		this.queryTpye = queryTpye;
//	}

	public Scan getScanner() {
		return scanner;
	}

	public void setScanner(Scan scanner) {
		this.scanner = scanner;
	}

	public String getKehzwm() {
		return kehzwm;
	}

	public void setKehzwm(String kehzwm) {
		this.kehzwm = kehzwm;
	}

	public String getKehhao() {
		return kehhao;
	}

	public void setKehhao(String kehhao) {
		this.kehhao = kehhao;
	}

	public PybjyEO getRecord() {
		return record;
	}

	public void setRecord(PybjyEO record) {
		this.record = record;
	}

	public ItemQueryProcessor getQueryExectuer() {
		return queryExectuer;
	}

	public void setQueryExectuer(ItemQueryProcessor queryExectuer) {
		this.queryExectuer = queryExectuer;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public abstract String parseToString();

	/**
	 * 查询下一页
	 * 
	 * @param queryNum
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	public abstract Page<? extends AbstractQueryResult> nextPage(long queryNum) throws IOException, Exception;

	public String getZHYYNG() {
		return ZHYYNG;
	}

	public void setZHYYNG(String zHYYNG) {
		ZHYYNG = zHYYNG;
	}

	public String getJIGOMC() {
		return JIGOMC;
	}

	public void setJIGOMC(String jIGOMC) {
		JIGOMC = jIGOMC;
	}

	public int getZhanghTotal() {
		return zhanghTotal;
	}

	public void setZhanghTotal(int zhanghTotal) {
		this.zhanghTotal = zhanghTotal;
	}

	public int getQueriedZhNum() {
		return queriedZhNum;
	}

	public void setQueriedZhNum(int queriedZhNum) {
		this.queriedZhNum = queriedZhNum;
	}

	public void incr(int num) {
		this.queriedZhNum += num;
	}

	public int getFinished() {
		return finished;
	}

	public void setFinished(int finished) {
		this.finished = finished;
	}

	public int getPageTotal() {
		return pageTotal;
	}

	public void setPageTotal(int pageTotal) {
		this.pageTotal = pageTotal;
	}

	public long getAllPageCount() {
		return allPageCount;
	}

	public void setAllPageCount(long allPageCount) {
		this.allPageCount = allPageCount;
	}

	public long getAllItemCount() {
		return allItemCount;
	}

	public void setAllItemCount(long allItemCount) {
		this.allItemCount = allItemCount;
	}

	public boolean isKuaJG() {
		return isKuaJG;
	}

	public void setKuaJG(boolean isKuaJG) {
		this.isKuaJG = isKuaJG;
	}

}
