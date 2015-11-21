package com.ceb.hdqs.action.query.output.pdf;

import java.io.IOException;

import com.ceb.hdqs.action.query.output.PDFMaker;
import com.ceb.hdqs.entity.PybjyEO;
import com.ceb.hdqs.query.entity.QueryDocumentContext;

/**
 * 0781pdf生成器
 * 
 * @author user
 * 
 */
public class PdfMaker0781 extends PDFMaker {

	public PdfMaker0781(PybjyEO record, QueryDocumentContext documentContext) throws IOException {
		super(record, documentContext);
	}

	@Override
	public String getTmeplateFile(String exCode, String printStyle) {
		return "TemplateOf0781.html";
	}
}
