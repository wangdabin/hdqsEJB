package com.ceb.hdqs.action.query.output;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ceb.hdqs.entity.PybjyEO;
import com.ceb.hdqs.query.entity.Page;
import com.ceb.hdqs.query.utils.QueryMethodUtils;

/**
 * 生成TXT文件,同步转为异步的过程中如果现在打印则生成TXT文档
 * 
 * @author user
 * 
 */
public abstract class TxtMaker<T> {
	private static final Log log = LogFactory.getLog(TxtMaker.class);
	public static final String DEFAULT_CHARSET = "GBK";
	private static final String TXT_SUFFIX = ".txt";
	protected static final char SWITCH_PAGER = (char) 0x0c;
	
	protected File txtFile;
	protected PrintStream printer;
	protected long pageCount = 0;

	public TxtMaker(PybjyEO record) {
		try {
			String outputDir = QueryMethodUtils.generateOutputDir(record);
			log.debug("查询任务的输入出目录是" + outputDir);
			File dirFile = new File(outputDir);
			dirFile.mkdirs();
			txtFile = new File(outputDir, record.getSlbhao() + TXT_SUFFIX);

			printer = new PrintStream(new FileOutputStream(txtFile), false, DEFAULT_CHARSET);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		log.info("开始为受理编码" + record.getSlbhao() + "构造TxtMaker,文件路径为：" + txtFile.getAbsolutePath());
	}

	public String getTxtFile() {
		return txtFile.getAbsolutePath();
	}

	public void closeDocument() {
		if (this.printer != null) {
			printer.flush();
			printer.close();
		}
	}

	public abstract void write(Page<T> page);
}
