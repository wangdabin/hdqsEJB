package com.ceb.hdqs.query.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 记录一次查询中的产生的文档的信息
 * 
 * @author user
 * 
 */
public class DocumentInfo implements Serializable {

	private static final long serialVersionUID = 2585757270128229525L;
	private boolean mayFlush = true;

	public synchronized void setMayFlush(boolean mayFlush) {
		this.mayFlush = mayFlush;
	}

	public boolean getMayFlush() {
		return this.mayFlush;
	}

	/**
	 * PDF文件页数信息
	 */
	private List<HdqsDocument> pdfDocumentInfo = new ArrayList<HdqsDocument>();

	/**
	 * EXCEL文件信息
	 */
	private List<HdqsDocument> xlsDocumentInfo = new ArrayList<HdqsDocument>();

	/**
	 * TXT文件信息
	 */
	private List<HdqsDocument> txtDocumentInfo = new ArrayList<HdqsDocument>();

	/**
	 * 获取PDF文件的大小
	 * 
	 * @return
	 */
	public long getPdfDocumentSize() {
		long size = 0;
		for (HdqsDocument fileSize : pdfDocumentInfo) {
			size += fileSize.getDocumentSize();
		}

		return size;
	}

	/**
	 * 获取EXCEL文件的大小
	 * 
	 * @return
	 */
	public long getXlsDocumentSize() {
		long size = 0;
		for (HdqsDocument fileSize : xlsDocumentInfo) {
			size += fileSize.getDocumentSize();
		}
		return size;
	}

	public void setXlsDocumentSize(List<HdqsDocument> xlsDocumentSize) {
		this.xlsDocumentInfo = xlsDocumentSize;
	}

	/**
	 * 当前正在输出的PDF文档
	 */
	private HdqsDocument currentPDF = new PdfDocument();
	/**
	 * 当前正在输出的EXCEL文档
	 */
	private HdqsDocument currentXLS = new XlsDocument();

	/**
	 * 当前正在输出的Txt文档
	 */
	private HdqsDocument currentTxt = new TxtDocument();

	public HdqsDocument getCurrentPDF() {
		return currentPDF;
	}

	public HdqsDocument createCurrentPDF(String fileName) {
		this.currentPDF = new PdfDocument();
		currentPDF.setFileName(fileName);
		pdfDocumentInfo.add(currentPDF);
		return currentPDF;
	}

	public HdqsDocument getCurrentXLS() {
		return currentXLS;
	}

	/**
	 * 创建一个新的xls
	 * 
	 * @param fileName
	 * @return
	 */
	public HdqsDocument createCurrentXLS(String fileName) {
		currentXLS = new XlsDocument();
		currentXLS.setFileName(fileName);
		xlsDocumentInfo.add(currentXLS);
		return currentXLS;
	}

	public HdqsDocument createCurrentTxt(String fileName) {
		currentTxt = new TxtDocument();
		currentTxt.setFileName(fileName);
		txtDocumentInfo.add(currentTxt);
		return currentXLS;
	}

	/**
	 * 添加txt
	 */
	public void addTXT() {
		txtDocumentInfo.add(currentTxt);
	}

	/**
	 * 添加pdf文档信息
	 * 
	 * @param fileName
	 * @param pageNum
	 */
	public void addPDF() {
		pdfDocumentInfo.add(currentPDF);
	}

	/**
	 * 添加Excel文档信息
	 * 
	 * @param fileName
	 * @param pageNum
	 */
	public void addXLS() {
		xlsDocumentInfo.add(currentXLS);
	}

	public List<HdqsDocument> getPdfDocumentInfo() {
		return pdfDocumentInfo;
	}

	public List<HdqsDocument> getXlsDocumentInfo() {
		return xlsDocumentInfo;
	}

	public List<HdqsDocument> getTxtDocumentInfo() {
		return txtDocumentInfo;
	}

	/**
	 * 获取PDF文件的总个数
	 * 
	 * @return
	 */
	public long getPdfFileCount() {
		long pageCount = 0;
		pageCount = pdfDocumentInfo.size();
		return pageCount;
	}

	public long getXlsFileCount() {
		long pageCount = 0;
		pageCount = xlsDocumentInfo.size();
		return pageCount;
	}
}
