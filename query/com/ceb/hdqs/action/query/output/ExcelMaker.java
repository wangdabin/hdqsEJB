package com.ceb.hdqs.action.query.output;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.ceb.hdqs.entity.PybjyEO;
import com.ceb.hdqs.entity.result.AbstractQueryResult;
import com.ceb.hdqs.query.entity.DocKey;
import com.ceb.hdqs.query.entity.Page;
import com.ceb.hdqs.query.entity.PageHeader;
import com.ceb.hdqs.query.entity.QueryDocumentContext;
import com.ceb.hdqs.query.entity.XlsDocument;
import com.ceb.hdqs.query.utils.QueryConfUtils;
import com.ceb.hdqs.query.utils.QueryMethodUtils;

/**
 * 生成Excel文件， 为了防止内存溢出，采用{@link XSSFWorkbook}进行输出文件
 * 由于需要分文件，所以在输出文件头的过程中需要存在不规则的问题
 * 
 * @author user
 * 
 */
public abstract class ExcelMaker {
	private static final Log LOG = LogFactory.getLog(ExcelMaker.class);

	public static final String TITLE_LOAD_PATH = QueryConfUtils.weblogic_domain_home + File.separator + "config"
			+ File.separator + "templates" + File.separator + "excelTitle.properties";
	private static final String XLSX_SUFFIX = ".xlsx";

	private SXSSFWorkbook workBook;
	// private XSSFWorkbook xbook;
	private FileOutputStream fos;
	protected SXSSFSheet sheet;
	private int count = 1;
	private int pageCount = 0;
	protected String printStyle;
	protected File xlsFile;
	int rowNums = 0;
	protected PybjyEO record;
	protected XlsDocument currentXls;
	protected QueryDocumentContext documentContext;
	private boolean isNull = true;
	String[] titleLine = null;

	/**
	 * ExcelGenerater 对AsynRecord的xlsfile进行了赋值
	 * 
	 * @param record
	 */
	public ExcelMaker(PybjyEO record, QueryDocumentContext documentContext) {
		try {
			// String path, String fileName, String printStyle,
			this.record = record;
			this.documentContext = documentContext;
			this.printStyle = record.getPrintStyle();
			String outputDir = QueryMethodUtils.generateOutputDir(record);
			LOG.debug("查询任务Excel的输出目录是" + outputDir);
			xlsFile = new File(outputDir, record.getSlbhao() + "_" + (documentContext.getXlsFileCount() + 1)
					+ XLSX_SUFFIX);
			fos = new FileOutputStream(xlsFile);
			XSSFWorkbook xbook = new XSSFWorkbook();
			workBook = new SXSSFWorkbook(xbook, 1000);
		} catch (FileNotFoundException e) {
			LOG.error(e.getMessage(), e);
		}

		// // 创建当前文档对应的文档信息对象
		currentXls = documentContext.createCurrentXLS(xlsFile.getAbsolutePath());

		LOG.info("开始为受理编码" + record.getSlbhao() + "构造ExcelGenerater,文件路径为：" + xlsFile.getAbsolutePath());
	}

	/**
	 * 解析输入的Page对象为Excel的row，然后将row缓存在buffer中，待达到阀值后，批量flush到Excel中
	 * 
	 * @param page
	 * @return
	 * @throws IOException
	 */
	public void getRowsBuffer(Page<? extends AbstractQueryResult> page) throws IOException {
		// 首先创建一个Sheet
		sheet = (SXSSFSheet) createSheet();
		int rowNums = sheet.getLastRowNum();

		PageHeader header = page.getHeader();
		String[] headerTmp = header.getHeaderArray(record.getJiaoym());
		LOG.debug("开始输出EXCEL的文件头");
		// 输出页头信息
		for (String field : headerTmp) {
			Row row = sheet.createRow(rowNums);
			row.createCell(0).setCellValue(field);
			rowNums++;
		}
		// 输出行标题
		String[] titleArr = getTitleLine();
		if (titleArr != null) {
			Row row = sheet.createRow(rowNums);
			for (int i = 0; i < titleArr.length; i++) {
				row.createCell(i).setCellValue(titleArr[i]);
			}
			rowNums++;
		}
		// 输出交易明细内容
		flushItems(page, sheet, rowNums);
	}

	public void flushItems(Page<? extends AbstractQueryResult> page, SXSSFSheet sheet, int rowNums) throws IOException {
		if (!page.getParseResult().isQuery() || StringUtils.isNotBlank(page.getHeader().getTips())) {
			if (rowNums > 100000) {
				sheet.flushRows();
				sheet = (SXSSFSheet) createSheet();
				rowNums = sheet.getLastRowNum();
			}
			Row row = sheet.createRow(rowNums);
			LOG.info("输出的tips内容是：" + page.getHeader().getTips());
			row.createCell(0).setCellValue(page.getHeader().getTips());
			rowNums++;
			return;
		}
		for (AbstractQueryResult line : page.getPageItem()) {
			if (rowNums > 100000) {
				sheet.flushRows();
				sheet = (SXSSFSheet) createSheet();
				rowNums = sheet.getLastRowNum();
			}

			Row row = sheet.createRow(rowNums);
			// String temp[] = line.split("\\|");
			String temp[] = line.toArray(record.getPrintStyle(), page.getHeader().getHbdh());
			for (int i = 0; i < temp.length; i++) {
				row.createCell(i).setCellValue(temp[i]);
			}
			rowNums++;
		}
	}

	/**
	 * 获取行标题
	 * 
	 * @return
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public abstract String[] getTitleLine() throws FileNotFoundException, IOException;

	public Sheet createSheet() {
		// LOG.info("创建sheetName + _+ num 是：" + name + index);
		Sheet newSheet = workBook.createSheet("page_" + count);
		count++;
		return newSheet;
	}

	/**
	 * 记录当前页信息
	 */
	public void recordCurrentPage(Long id) {
		pageCount++;
		// 首先检测当前文档上下文中有没有当前账号的文档记录信息,如果没有则将当前文档对应到当前账号的明细上面，如果存在则直接在当前文件中记录该账号的明细的页面分布情况
		// 检测当前文档中有没有当前账号的记录信息
		List<XlsDocument> zhDocs = documentContext
				.getXlsDocByZhangh(new DocKey(id, documentContext.getCurrentZhangh()));

		if (zhDocs == null) {
			documentContext.recordCurrentZhanghXls(id);
		} else {
			boolean isContains = false;
			for (XlsDocument xlsDocument : zhDocs) {
				if (xlsDocument.getFileName().equals(currentXls.getFileName())) {
					isContains = true;
					break;
				}
			}

			if (!isContains) {
				LOG.info("文档切换，记录当前账号的文档信息");
				documentContext.recordCurrentZhanghXls(id);
			}
		}

		// 其次检测当前文档中有没有当前账号的记录信息
		if (currentXls.getPages(new DocKey(id, documentContext.getCurrentZhangh())) == null) {
			List<Integer> pages = new ArrayList<Integer>();
			pages.add(pageCount);
			currentXls.addPages(new DocKey(id, documentContext.getCurrentZhangh()), pages);
		} else {
			currentXls.getPages(new DocKey(id, documentContext.getCurrentZhangh())).add(pageCount);
		}
	}

	public void write(Page<? extends AbstractQueryResult> page) throws IOException {
		if (isNull()) {
			// 创建当前文档对应的文档信息对象
			setNull(false);
		}
		getRowsBuffer(page);
		sheet.flushRows();
		recordCurrentPage(page.getParseResult().getRecord().getId());
	}

	public void close() {
		try {

			if (fos != null) {
				workBook.write(fos);
				fos.close();
				workBook.dispose();
			}
			// dispose the temporary file backing this workbook on disk

			if (isNull()) {
				LOG.debug("回滚的EXCEL文件名称是：" + documentContext.getPreXls().getFileName());
				XlsEditor.dropDocument(currentXls.getFileName());
				currentXls.setFileName(documentContext.getPreXls().getFileName());
			}
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		}
	}

	public boolean isNull() {
		return isNull;
	}

	public void setNull(boolean isNull) {
		this.isNull = isNull;
	}

	public SXSSFSheet getSheet() {
		return sheet;
	}

	public static void main(String[] args) throws FileNotFoundException, IOException {
		Properties properties = new Properties();
		properties.load(new FileInputStream(new File("templates/excelTitle.properties")));
		String titleStr = properties.getProperty("tmplatePrivate");
		for (String string : titleStr.split(",")) {
			System.out.println(string);
		}
	}
}