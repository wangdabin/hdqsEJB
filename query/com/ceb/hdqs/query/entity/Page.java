package com.ceb.hdqs.query.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.ceb.hdqs.entity.result.ParseResult;

/**
 * 输出文档的每页的信息
 * 
 * @author user
 * 
 * @param <T>
 */
public class Page<T> implements Serializable {

	private static final long serialVersionUID = 2981508416038888513L;

	private PageHeader header;// 文件头
	private List<T> pageItem=new ArrayList<T>();// 页面内容
	protected String tip;// 查询说明
	protected long totalItemNum;// 总的明细条数
	protected int zhanghFinishedNum = 0;
	protected ParseResult parseResult;

	public PageHeader getHeader() {
		return header;
	}

	public void setHeader(PageHeader header) {
		this.header = header;
	}

	public List<T> getPageItem() {
		return pageItem;
	}

	public void setPageItem(List<T> pageItem) {
		this.pageItem = pageItem;
	}

	public String getTip() {
		return tip;
	}

	public void setTip(String tip) {
		this.tip = tip;
	}

	public long getTotalItemNum() {
		return totalItemNum;
	}

	public void setTotalItemNum(long totalItemNum) {
		this.totalItemNum = totalItemNum;
	}

	public int getZhanghFinishedNum() {
		return zhanghFinishedNum;
	}

	public void setZhanghFinishedNum(int zhanghFinishedNum) {
		this.zhanghFinishedNum = zhanghFinishedNum;
	}

	public ParseResult getParseResult() {
		return parseResult;
	}

	public void setParseResult(ParseResult parseResult) {
		this.parseResult = parseResult;
	}

}
