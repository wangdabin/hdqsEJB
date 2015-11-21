package com.ceb.hdqs.action.asyn;

import java.util.List;

import com.ceb.hdqs.entity.PybjyEO;

/**
 * 异步查询抽象接口 ，异步查询实现该接口并实现接口的方法，完成对异步查询的操作
 * 
 * @author
 */
public interface IAsynchronizeQuery {
	/**
	 * txt生成器前缀
	 */
	public static final String TXT_PRINTER = "com.ceb.hdqs.action.query.output.txt.TxtPrinter";
	/**
	 * Pdf生成器前缀
	 */
	public static final String PDF_PRINTER = "com.ceb.hdqs.action.query.output.pdf.PdfMaker";
	/**
	 * Excel生成器前缀
	 */
	public static final String EXCEL_PRINTER = "com.ceb.hdqs.action.query.output.excel.ExcelMaker";
	public static final String XLSX = ".xlsx";
	public static final String PDF = ".pdf";

	/**
	 * 启动查询并返回最后一个文件的路径
	 * 
	 * @param isSynPrint
	 *            是否是同步打印
	 * @return 查询任务的输出文件路径
	 * @throws Exception
	 */
	public String startAsynchronizeQuery(boolean isSynPrint) throws Exception;

	/**
	 * 更新单个异步查询任务的状态到数据库中
	 * 
	 * @param taskState
	 * @return
	 */
	public void update(PybjyEO record);

	/**
	 * 批量更改一次查询中多个查询条件的记录状态
	 * 
	 * @param list
	 * @return
	 */
	public void batchUpdate(List<PybjyEO> list);

	/**
	 * 获取任务名称(受理编码)
	 * 
	 * @return
	 */
	public String getTaskName();
}