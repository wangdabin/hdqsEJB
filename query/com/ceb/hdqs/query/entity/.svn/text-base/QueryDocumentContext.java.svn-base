package com.ceb.hdqs.query.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * 存储每次查询过程中的查询条件的信息以及查询过程中每个查询条件的进度和结果情况
 * 
 * @author user
 * 
 */
public class QueryDocumentContext implements Serializable {

	private static final long serialVersionUID = 7589872769957266037L;
	// 每个PDF文件的页数
	private int commNum = 0;
	// 是否已经分割多个文件了
	private boolean isMutilFile = false;
	private String currentZhangh;
	private boolean mayFlush = true;
	private int finishedZhNum = 0;
	private long allItemCount;
	private long allPageCount;
	private PdfDocument prePdf = new PdfDocument();
	private XlsDocument preXls = new XlsDocument();
	private TxtDocument preTxt = new TxtDocument();
	private Map<String, List<String>> brokedCache = new LinkedHashMap<String, List<String>>();
	
	

	public Map<String, List<String>> getBrokedCache() {
		return brokedCache;
	}

	public void setBrokedCache(Map<String, List<String>> brokedCache) {
		this.brokedCache = brokedCache;
	}

	/*
	 * 记录PDF文档的信息
	 */
	public Map<DocKey, List<PdfDocument>> pdfDocumentInfo = new HashMap<DocKey, List<PdfDocument>>();
	/*
	 * 记录EXCEL文档的信息
	 */
	public Map<DocKey, List<XlsDocument>> xlsDocumentInfo = new HashMap<DocKey, List<XlsDocument>>();
	/*
	 * 记录TXT文档的信息
	 */
	public Map<DocKey, List<TxtDocument>> txtDocumentInfo = new HashMap<DocKey, List<TxtDocument>>();

	/*
	 * 当前正在输出的PDF文档
	 */
	private PdfDocument currentPDF = new PdfDocument();
	/*
	 * 当前正在输出的EXCEL文档
	 */
	private XlsDocument currentXLS = new XlsDocument();

	/*
	 * 当前正在输出的Txt文档
	 */
	private TxtDocument currentTxt = new TxtDocument();

	public synchronized void setMayFlush(boolean mayFlush) {
		this.mayFlush = mayFlush;
	}

	public boolean getMayFlush() {
		return this.mayFlush;
	}

	/**
	 * 获取PDF文件的大小
	 * 
	 * @return
	 */
	public long getPdfDocumentSize() {
		long size = 0;
		Set<HdqsDocument> docSet = new HashSet<HdqsDocument>();
		// 去重，因为一个文档会在多个账号中留有记录
		for (Entry<DocKey, List<PdfDocument>> documentList : pdfDocumentInfo.entrySet()) {
			for (HdqsDocument document : documentList.getValue()) {
				docSet.add(document);
			}
		}
		// 计算文档大小
		Iterator<HdqsDocument> docs = docSet.iterator();
		while (docs.hasNext()) {
			size += docs.next().getDocumentSize();
		}
		return size;
	}

	/**
	 * 记录当前账号的PDF文档信息
	 */
	public void recordCurrentZhanghPdf(Long id) {
		synchronized (pdfDocumentInfo) {
			DocKey key = new DocKey(id, currentZhangh);
			if (pdfDocumentInfo.get(key) == null) {
				List<PdfDocument> documentList = new ArrayList<PdfDocument>();
				documentList.add(currentPDF);
				addPdfDocList(key, documentList);
			} else {
				pdfDocumentInfo.get(key).add(currentPDF);
			}
		}

	}

	/**
	 * 记录当前账号的Excel文档信息
	 */
	public void recordCurrentZhanghXls(Long id) {
		synchronized (xlsDocumentInfo) {
			DocKey key = new DocKey(id, currentZhangh);
			if (xlsDocumentInfo.get(key) == null) {
				List<XlsDocument> documentList = new ArrayList<XlsDocument>();
				documentList.add(currentXLS);
				xlsDocumentInfo.put(key, documentList);
			} else {
				xlsDocumentInfo.get(key).add(currentXLS);
			}
		}

	}

	/**
	 * 获取EXCEL文件的大小
	 * 
	 * @return
	 */
	public long getXlsDocumentSize() {
		long size = 0;
		Set<HdqsDocument> docSet = new HashSet<HdqsDocument>();
		// 去重，因为一个文档会在多个账号中留有记录
		for (Entry<DocKey, List<XlsDocument>> documentList : xlsDocumentInfo.entrySet()) {
			for (HdqsDocument document : documentList.getValue()) {
				docSet.add(document);
			}
		}
		// 计算文档大小
		Iterator<HdqsDocument> docs = docSet.iterator();
		while (docs.hasNext()) {
			size += docs.next().getDocumentSize();
		}
		return size;
	}

	public HdqsDocument getCurrentPDF() {
		return currentPDF;
	}

	/**
	 * 创建一个新的PDF
	 * 
	 * @param zhangh
	 * @param fileName
	 * @return
	 */
	public PdfDocument createCurrentPDF(String fileName) {
		// try {
		// BeanUtils.copyProperties(this.prePdf, this.currentPDF);
		// } catch (IllegalAccessException e) {
		// LOG.error(e.getMessage(), e);
		// } catch (InvocationTargetException e) {
		// LOG.error(e.getMessage(), e);
		// }
		prePdf.setFileName(currentPDF.getFileName());
		this.currentPDF = new PdfDocument();
		currentPDF.setFileName(fileName);
		return currentPDF;
	}

	/**
	 * 按账号记录每个账号生成的PDF的文件和页面数
	 * 
	 * @param zhangh
	 * @param documentList
	 */
	public void addPdfDocList(DocKey zhangh, List<PdfDocument> documentList) {
		synchronized (pdfDocumentInfo) {
			pdfDocumentInfo.put(zhangh, documentList);
		}

	}

	/**
	 * 按账号记录每个账号生成的excel的文件和页面数
	 * 
	 * @param zhangh
	 * @param documentList
	 */
	public void addXlsDocList(DocKey zhangh, List<XlsDocument> documentList) {
		synchronized (xlsDocumentInfo) {
			xlsDocumentInfo.put(zhangh, documentList);
		}

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
	public XlsDocument createCurrentXLS(String fileName) {
		// try {
		// BeanUtils.copyProperties(this.preXls, this.currentXLS);
		// } catch (IllegalAccessException e) {
		// LOG.error(e.getMessage(), e);
		// } catch (InvocationTargetException e) {
		// LOG.error(e.getMessage(), e);
		// }

		this.preXls.setFileName(currentXLS.getFileName());
		currentXLS = new XlsDocument();
		currentXLS.setFileName(fileName);

		return currentXLS;
	}

	/**
	 * 创建一个新的Txt文档
	 * 
	 * @param zhangh
	 * @param fileName
	 * @return
	 */
	public HdqsDocument createCurrentTxt(String fileName) {
		// try {
		// BeanUtils.copyProperties(this.preTxt, this.currentTxt);
		// } catch (IllegalAccessException e) {
		// LOG.error(e.getMessage(), e);
		// } catch (InvocationTargetException e) {
		// LOG.error(e.getMessage(), e);
		// }
		this.preTxt.setFileName(currentTxt.getFileName());
		currentTxt = new TxtDocument();
		currentTxt.setFileName(fileName);

		return currentTxt;
	}

	/**
	 * 记录当前txt的文档信息
	 */
	public void recordCurrentZhanghTxt(Long id) {
		synchronized (txtDocumentInfo) {
			DocKey key = new DocKey(id, currentZhangh);
			if (txtDocumentInfo.get(key) == null) {
				List<TxtDocument> documentList = new ArrayList<TxtDocument>();
				documentList.add(currentTxt);
				txtDocumentInfo.put(key, documentList);
			} else {
				txtDocumentInfo.get(key).add(currentTxt);
			}
		}

	}

	/**
	 * 获取指定账号生成的PDF的页数
	 */
	public long getPdfPageCount() {
		long pageCount = 0;
		for (Entry<DocKey, List<PdfDocument>> docList : pdfDocumentInfo.entrySet()) {
			DocKey zhangh = docList.getKey();
			List<PdfDocument> doc = getPdfDocByZhangh(zhangh);
			for (HdqsDocument hdqsDocument : doc) {
				pageCount += hdqsDocument.getPageNumByZhangh(zhangh);
			}
		}
		return pageCount;
	}

	/**
	 * 获取PDF文件的总个数
	 * 
	 * @return
	 */
	public long getPdfFileCount() {
		Set<String> fileNames = new HashSet<String>();
		for (Entry<DocKey, List<PdfDocument>> documentEntry : pdfDocumentInfo.entrySet()) {
			for (HdqsDocument doc : documentEntry.getValue()) {
				fileNames.add(doc.getFileName().trim());
			}
		}
		return fileNames.size();
	}

	/**
	 * 获取当前文件中的xls的文档个数
	 * 
	 * @return
	 */
	public long getXlsFileCount() {
		Set<String> fileNames = new HashSet<String>();
		for (Entry<DocKey, List<XlsDocument>> documentEntry : xlsDocumentInfo.entrySet()) {
			for (HdqsDocument doc : documentEntry.getValue()) {
				fileNames.add(doc.getFileName().trim());
			}
		}
		return fileNames.size();
	}

	public String getCurrentZhangh() {
		return currentZhangh;
	}

	public void setCurrentZhangh(String currentZhangh) {
		this.currentZhangh = currentZhangh;
	}

	public int getCommNum() {
		return commNum;
	}

	public void setCommNum(int commNum) {
		this.commNum = commNum;
	}

	public boolean isMutilFile() {
		return isMutilFile;
	}

	public void setMutilFile(boolean isMutilFile) {
		this.isMutilFile = isMutilFile;
	}

	public int getFinishedZhNum() {
		return finishedZhNum;
	}

	public void setFinishedZhNum(int finishedZhNum) {
		this.finishedZhNum = finishedZhNum;
	}

	public long getAllItemCount() {
		return allItemCount;
	}

	public void setAllItemCount(long allItemCount) {
		this.allItemCount = allItemCount;
	}

	public long getAllPageCount() {
		// TODO Auto-generated method stub
		return allPageCount;
	}

	public void setAllPageCount(long allPageCount) {
		this.allPageCount = allPageCount;
	}

	/**
	 * 获取当前账号的PDF文档分布情况
	 */
	public List<PdfDocument> getPdfDocByZhangh(DocKey zhangh) {
		return this.pdfDocumentInfo.get(zhangh);
	}

	/**
	 * 获取当前账号的excel的分布情况
	 * 
	 * @param zhangh
	 * @return
	 */
	public List<XlsDocument> getXlsDocByZhangh(DocKey zhangh) {
		return this.xlsDocumentInfo.get(zhangh);
	}

	/**
	 * 删除指定账号的PDF信息
	 * 
	 * @param zhangh
	 */
	public void removePdf(DocKey zhangh) {
		pdfDocumentInfo.remove(zhangh);
	}

	/**
	 * 删除指定账号的Excel信息
	 * 
	 * @param zhangh
	 */
	public void removeXls(DocKey zhangh) {
		xlsDocumentInfo.remove(zhangh);
	}

	public XlsDocument getPreXls() {
		return preXls;
	}

	public TxtDocument getPreTxt() {
		return preTxt;
	}

	public PdfDocument getPrePdf() {
		return prePdf;
	}

}
