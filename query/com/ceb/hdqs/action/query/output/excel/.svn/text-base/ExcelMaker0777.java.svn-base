package com.ceb.hdqs.action.query.output.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ceb.hdqs.action.query.output.ExcelMaker;
import com.ceb.hdqs.entity.PybjyEO;
import com.ceb.hdqs.query.entity.QueryDocumentContext;

/**
 * 0777业务生成Excel工具类
 * 
 * @author user
 * 
 */
public class ExcelMaker0777 extends ExcelMaker {
	private static final Log LOG = LogFactory.getLog(ExcelMaker0777.class);

	public ExcelMaker0777(PybjyEO record, QueryDocumentContext document) {
		super(record, document);
	}

	@Override
	public String[] getTitleLine() throws FileNotFoundException, IOException {
		Properties properties = new Properties();
		properties.load(new FileInputStream(new File(TITLE_LOAD_PATH)));
		String titleStr = properties.getProperty("tmplateCorporate");
		LOG.debug("titleStr :  " + titleStr);
		return titleStr.split(",");
	}
}