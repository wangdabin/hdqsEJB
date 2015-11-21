package com.ceb.bank.query.output.txt;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.List;

import org.apache.log4j.Logger;

import com.ceb.bank.context.OutPdfContext;
import com.ceb.bank.query.output.AbstractGenerator;
import com.ceb.hdqs.entity.PybjyEO;

public abstract class TxtGenerator<T> extends AbstractGenerator {
	private static final Logger log = Logger.getLogger(TxtGenerator.class);
	public static final String ENCODING_GBK = "GBK";
	private static final String FILE_SUFFIX_TXT = ".txt";
	protected static final char PAGER_SWITCHOR = (char) 0x0c;

	private PrintStream printer;

	public TxtGenerator() {

	}

	public void init(PybjyEO record) throws Exception {
		String outputDir = buildOutputDir(record);
		log.debug("查询任务的输出目录是: " + outputDir);
		File file = new File(outputDir, record.getSlbhao() + FILE_SUFFIX_TXT);
		if (file.exists()) {
			file.delete();
		}
		printer = new PrintStream(new FileOutputStream(file), false, ENCODING_GBK);
		this.setFile(file);
	}

	public void close() throws Exception {
		if (printer != null) {
			printer.flush();
			printer.close();
		}
	}

	public abstract void write(PybjyEO record, OutPdfContext pageCtx, List<T> list, Object zhangCtx);

	public abstract void writeNoItems(PybjyEO record, OutPdfContext pageCtx, Object zhangCtx) throws Exception;

	public abstract void appendPageTail(OutPdfContext pageCtx);

	public abstract void appendZhanghTail(OutPdfContext pageCtx);

	public abstract void appendLastTail(OutPdfContext pageCtx);

	public PrintStream getPrinter() {
		return printer;
	}

	public void setPrinter(PrintStream printer) {
		this.printer = printer;
	}
}