package com.ceb.hdqs.action.query.output.pdf;

import java.io.IOException;

import com.ceb.hdqs.action.query.output.PDFMaker;
import com.ceb.hdqs.constants.HdqsConstants;
import com.ceb.hdqs.entity.PybjyEO;
import com.ceb.hdqs.query.entity.QueryDocumentContext;

/**
 * 0777pdf生成器
 * 
 * @author user
 * 
 */
public class PdfMaker0777 extends PDFMaker {

	public PdfMaker0777(PybjyEO record, QueryDocumentContext documentContext) throws IOException {
		super(record, documentContext);
	}

	@Override
	public String getTmeplateFile(String exCode, String printStyle) {
		if (printStyle.equals(HdqsConstants.PRINT_STYLE_KEHU)) {// 客户对账单
			return "CqtemplateOfCorporate.html";
		} else {
			return "TemplateOfCorporate.html";
		}
	}
}
