package com.ceb.hdqs.action.query.output;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ceb.hdqs.query.entity.DocKey;
import com.ceb.hdqs.query.entity.HdqsDocument;
import com.ceb.hdqs.query.entity.PdfDocument;
import com.ceb.hdqs.query.entity.XlsDocument;

public class BrokedDocumentEditor {
	private static final Log LOG = LogFactory.getLog(BrokedDocumentEditor.class);

	/**
	 * 删除余额不连续的明细的PDF文档和页面
	 */
	public void deletePdfBrokedRecords(List<PdfDocument> pdfList, DocKey zhangh) {
		if (pdfList == null || pdfList.size() == 0) {
			return;
		}
		for (HdqsDocument hdqsDocument : pdfList) {
			LOG.info("执行删除操作的文档是: " + hdqsDocument.getFileName());
			hdqsDocument.deleteZhangItem(zhangh, null);
		}
	}

	/**
	 * 删除余额不连续的Excel文档和sheet
	 * 
	 * @param documentContext
	 */
	public void deleteXlsBrokedRecords(List<XlsDocument> pdfList, DocKey zhangh) {
		if (pdfList == null || pdfList.size() == 0) {
			return;
		}
		for (HdqsDocument hdqsDocument : pdfList) {
			hdqsDocument.deleteZhangItem(zhangh, null);
		}
	}
}