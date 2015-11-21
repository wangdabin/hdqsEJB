package com.ceb.hdqs.entity.result;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 异步业务输入条件解析结果
 * 
 * @author user
 * 
 */
public class ParseResultAsyn implements Serializable {

	private static final long serialVersionUID = 4505470529640691041L;
	private List<ParseResult> parseResults = new ArrayList<ParseResult>();

	public List<ParseResult> getParseResults() {
		return parseResults;
	}

	public void setParseResults(List<ParseResult> parseResults) {
		this.parseResults = parseResults;
	}

	public void setPdfFile(String pdfFile) {
		for (ParseResult result : parseResults) {
			result.getRecord().setPdfFile(pdfFile);
		}
	}

	public void setXlsFile(String xlsFile) {
		for (ParseResult result : parseResults) {
			result.getRecord().setXlsFile(xlsFile);
		}
	}
}
