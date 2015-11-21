package com.ceb.bank.context;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class OutPdfContext implements Serializable {
	private static final long serialVersionUID = 7911135106986466594L;
	private int zhanghNum = 0;// 当前条件需处理账号数
	private boolean queryFinish = false;// 当前条件是否查询完成
	private long total = 0;// 当前条件总记录数
	private int pageNum = 0;// 当前条件总页数
	private String cxqsrq;// 当前条件
	private String cxzzrq;// 当前条件
	private long printDate;// 当前条件
	private String jio1gy;// 当前条件

	private boolean currentQueryFinish = false;// 当前账号是否查询完成
	private boolean currentQueryLX = true;// 当前账号是否连续
	private long currentTotal = 0;// 当前账号已经查询总记录数
	private int currentPageNum = 0;// 当前账号已经查询页码,没有记录时会打印提示信息
	private List<String> tips = new ArrayList<String>();// 当前账号提示信息
	private boolean queryExist = true;// 当前条件查询记录或当前账号明细是否存在

	// 全局的,对应一个受理编号
	private int fileCount = 0;// 初始值为0,已经分出的文件数 ,用来产生新文件名称编号
	private File lastPdfFile;// 已经分出的最后一个文件
	private int lastPageNum = 0;// 已经分出的最后文件的页数,多个文件中页数是连续的,需要累计.

	/**
	 * 当一个查询条件没有明细时,调用该方法
	 */
	public void setNoItems() {
		this.setCurrentQueryFinish(true);
		this.setQueryFinish(true);
		this.setCurrentPageNum(1);// 存在一页打印提示提示
		this.setPageNum(1);// 存在一页打印提示提示
		this.setCurrentTotal(0);
		this.setTotal(0);
	}

	/**
	 * 重置当前账号的计数信息
	 */
	public void initCurrentZhangh() {
		this.setCurrentQueryFinish(false);
		this.setCurrentQueryLX(true);
		this.setCurrentTotal(0);
		this.setCurrentPageNum(0);
		this.getTips().clear();
		this.setQueryExist(true);
	}

	public void initCurrentRecord() {
		this.setZhanghNum(0);
		this.setQueryFinish(false);
		this.setTotal(0);
		this.setPageNum(0);
		initCurrentZhangh();
	}

	/**
	 * 当前条件需处理账号数
	 * 
	 * @return
	 */
	public int getZhanghNum() {
		return zhanghNum;
	}

	/**
	 * 当前条件需处理账号数
	 * 
	 * @param zhanghNum
	 */
	public void setZhanghNum(int zhanghNum) {
		this.zhanghNum = zhanghNum;
	}

	/**
	 * 当前条件是否查询完成
	 * 
	 * @return
	 */
	public boolean isQueryFinish() {
		return queryFinish;
	}

	/**
	 * 当前条件是否查询完成
	 * 
	 * @param queryFinish
	 */
	public void setQueryFinish(boolean queryFinish) {
		this.queryFinish = queryFinish;
	}

	/**
	 * 当前条件总记录数
	 * 
	 * @return
	 */
	public long getTotal() {
		return total;
	}

	/**
	 * 当前条件总记录数
	 * 
	 * @param total
	 */
	public void setTotal(long total) {
		this.total = total;
	}

	/**
	 * 当前条件总页数
	 * 
	 * @return
	 */
	public int getPageNum() {
		return pageNum;
	}

	/**
	 * 当前条件总页数
	 * 
	 * @param pageNum
	 */
	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public String getCxqsrq() {
		return cxqsrq;
	}

	public void setCxqsrq(String cxqsrq) {
		this.cxqsrq = cxqsrq;
	}

	public String getCxzzrq() {
		return cxzzrq;
	}

	public void setCxzzrq(String cxzzrq) {
		this.cxzzrq = cxzzrq;
	}

	public long getPrintDate() {
		return printDate;
	}

	public void setPrintDate(long printDate) {
		this.printDate = printDate;
	}

	public String getJio1gy() {
		return jio1gy;
	}

	public void setJio1gy(String jio1gy) {
		this.jio1gy = jio1gy;
	}

	/**
	 * 当前账号是否查询完成
	 * 
	 * @return
	 */
	public boolean isCurrentQueryFinish() {
		return currentQueryFinish;
	}

	/**
	 * 当前账号是否查询完成
	 * 
	 * @param currentQueryFinish
	 */
	public void setCurrentQueryFinish(boolean currentQueryFinish) {
		this.currentQueryFinish = currentQueryFinish;
	}

	/**
	 * 当前账号是否连续
	 * 
	 * @return
	 */
	public boolean isCurrentQueryLX() {
		return currentQueryLX;
	}

	/**
	 * 当前账号是否连续
	 * 
	 * @param currentQueryLX
	 */
	public void setCurrentQueryLX(boolean currentQueryLX) {
		this.currentQueryLX = currentQueryLX;
	}

	/**
	 * 当前账号已经查询总记录数
	 * 
	 * @return
	 */
	public long getCurrentTotal() {
		return currentTotal;
	}

	/**
	 * 当前账号已经查询总记录数
	 * 
	 * @param currentTotal
	 */
	public void setCurrentTotal(long currentTotal) {
		this.currentTotal = currentTotal;
	}

	/**
	 * 当前账号已经查询页码,没有记录时会打印提示信息
	 * 
	 * @return
	 */
	public int getCurrentPageNum() {
		return currentPageNum;
	}

	/**
	 * 当前账号已经查询页码,没有记录时会打印提示信息
	 * 
	 * @param currentPageNum
	 */
	public void setCurrentPageNum(int currentPageNum) {
		this.currentPageNum = currentPageNum;
	}

	public List<String> getTips() {
		return tips;
	}

	public void setTips(List<String> tips) {
		this.tips = tips;
	}

	public boolean isQueryExist() {
		return queryExist;
	}

	public void setQueryExist(boolean queryExist) {
		this.queryExist = queryExist;
	}

	/**
	 * 初始值为0,已经分出的文件数 ,用来产生新文件名称编号
	 * 
	 * @return
	 */
	public int getFileCount() {
		return fileCount;
	}

	/**
	 * 初始值为0,已经分出的文件数 ,用来产生新文件名称编号
	 * 
	 * @param fileCount
	 */
	public void setFileCount(int fileCount) {
		this.fileCount = fileCount;
	}

	/**
	 * 已经分出的最后一个文件
	 * 
	 * @return
	 */
	public File getLastPdfFile() {
		return lastPdfFile;
	}

	/**
	 * 已经分出的最后一个文件
	 * 
	 * @param lastPdfFile
	 */
	public void setLastPdfFile(File lastPdfFile) {
		this.lastPdfFile = lastPdfFile;
	}

	/**
	 * 已经分出的最后文件的页数
	 * 
	 * @return
	 */
	public int getLastPageNum() {
		return lastPageNum;
	}

	/**
	 * 已经分出的最后文件的页数
	 * 
	 * @param lastPageNum
	 */
	public void setLastPageNum(int lastPageNum) {
		this.lastPageNum = lastPageNum;
	}
}