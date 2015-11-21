package com.ceb.hdqs.action.query.output;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;

import com.ceb.hdqs.action.query.exception.NeedNewDocumentException;
import com.ceb.hdqs.constants.HdqsConstants;
import com.ceb.hdqs.entity.PybjyEO;
import com.ceb.hdqs.entity.result.AbstractQueryResult;
import com.ceb.hdqs.query.entity.DocKey;
import com.ceb.hdqs.query.entity.EnumSizeUnit;
import com.ceb.hdqs.query.entity.Page;
import com.ceb.hdqs.query.entity.PdfDocument;
import com.ceb.hdqs.query.entity.QueryDocumentContext;
import com.ceb.hdqs.query.utils.QueryConfUtils;
import com.ceb.hdqs.query.utils.QueryConstants;
import com.ceb.hdqs.query.utils.QueryMethodUtils;
import com.ceb.hdqs.utils.FileCopyUtils;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfGState;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;

public abstract class PDFMaker {
	private static final Log LOG = LogFactory.getLog(PDFMaker.class);
	private static final long BYTES_PER_MB = 1024 * 1024;
	private static final long BYTES_PER_KB = 1024;
	private int DOCUMENT_THREAD_HOLE = QueryConfUtils.getActiveConfig().getInt(QueryConstants.HANDLE_PAGE_PER_FILE, 50);

	private BufferedWriter fos;
	private HtmlTemplator templateUtils;
	private BufferedReader reader;
	private PybjyEO record;
	private String guiyuan;
	private File htmlFile;
	private boolean isNull = true;
	/*
	 * 当前PDF文档信息
	 */
	private PdfDocument currentPDF;
	/*
	 * 记录页面的总数
	 */
	private int pageCount = 0;
	/*
	 * 当前查询任务的文档信息上下文
	 */
	private QueryDocumentContext documentContext;

	public PDFMaker(PybjyEO record, QueryDocumentContext docContext) throws IOException {
		try {
			this.record = record;
			this.documentContext = docContext;
			String exCode = record.getJiaoym();

			String outputDir = QueryMethodUtils.generateOutputDir(record);
			LOG.debug("查询任务PDF的输出目录是" + outputDir);

			guiyuan = record.getJio1gy();
			File fileDir = new File(outputDir);
			fileDir.mkdirs();
			// 指定当前PDF文档的文件名称
			String name = record.getSlbhao() + "_" + (documentContext.getPdfFileCount() + 1);
			htmlFile = new File(outputDir, name);
			if (htmlFile.exists()) {
				htmlFile.delete();
			}
			htmlFile.createNewFile();

			// 创建当前文档的信息记录对象

			currentPDF = documentContext.createCurrentPDF(htmlFile.getAbsolutePath() + ".pdf");

			String templateFile = getTmeplateFile(exCode, record.getPrintStyle());

			LOG.debug("加载的模板文件是：" + templateFile);

			templateUtils = new HtmlTemplator(templateFile);
			fos = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(htmlFile), "utf-8"));

			reader = new BufferedReader(new InputStreamReader(new FileInputStream(QueryConfUtils.weblogic_domain_home + File.separator + "config" + File.separator + "templates"
					+ File.separator + "header.css"), "UTF-8"));
			String line = null;
			while ((line = reader.readLine()) != null) {
				fos.write(line);
				fos.newLine();
			}
			fos.flush();
		} finally {
			try {
				if (reader != null)
					reader.close();
			} catch (IOException e) {
				LOG.error(e.getMessage(), e);
			}
		}

	}

	/**
	 * 根据查询业务代号，加载对应的template
	 * 
	 * @param exCode
	 * @return
	 */
	public String getTmeplateFile(String exCode, String printStyle) {
		if (exCode.equals(QueryConstants.CORPORATE_QUERY_CODE)) {
			return "CqtemplateOfCorporate.html";
		} else if (exCode.equals(QueryConstants.PRIVATE_FIX_QUERY_CODE) || exCode.equals(QueryConstants.PRIVATE_LIQUID_QUERY_CODE)) {
			return "CqtemplateOfPrivate.html";
		} else if (exCode.equals(QueryConstants.CARD_REPLACEMENT_CODE)) {
			return "CqtemplateOf0773.html";
		} else if (exCode.equals(QueryConstants.CORPORATE_BATCH_QUERY_CODE)) {
			if (printStyle.equals(HdqsConstants.PRINT_STYLE_KEHU)) {// 客户对账单
				return "CqtemplateOfCorporate.html";
			} else {
				return "TemplateOfCorporate.html";
			}

		} else if (exCode.equals(QueryConstants.PRIVATE_BATCH_QUERY_CODE)) {
			if (printStyle.equals(HdqsConstants.PRINT_STYLE_KEHU)) {// 客户对账单
				return "CqtemplateOfPrivate.html";
			} else {
				return "TemplateOfPrivate.html";
			}
		} else if (exCode.equals(QueryConstants.CORPORATE_CARD_QUERY_CODE)) {
			return "TemplateOf0781.html";
		}
		return null;
	}

	/**
	 * 输出查询出的数据
	 * 
	 * @param page
	 * @throws IOException
	 * @throws NeedNewDocumentException
	 * @throws DocumentException
	 * @throws Exception
	 */
	public void write(Page<? extends AbstractQueryResult> page) throws IOException, NeedNewDocumentException, DocumentException {

		if (isNull) {
			isNull = false;
		}
		String pageStr = null;

		recordCurrentPage(page.getParseResult().getRecord().getId());
		LOG.debug("开始输出:" + documentContext.getPdfPageCount() + "页," + currentPDF.getFileName() + "的第" + (pageCount) + "页");
		if (documentContext.getMayFlush()) {
			page.getHeader().setPageCount(documentContext.getPdfPageCount() + "");
			pageStr = PdfEditor.formatPageByTemplate(templateUtils, page);
		}
		fos.write(pageStr);
		fos.flush();

		// 控制文件的阀值
		Configuration conf = QueryConfUtils.getActiveConfig();
		LOG.debug("conf.get(QueryConfUtils.HANDLE_FILE_THRESHOLD_CTRL)  : " + conf.get(QueryConstants.HANDLE_FILE_THRESHOLD_CTRL));
		if ("0".equals(conf.get(QueryConstants.HANDLE_FILE_THRESHOLD_CTRL))) {
			LOG.debug("控制参数设置为0，不对文件个数和文件大小进行控制.");
		} else if ("1".equals(conf.get(QueryConstants.HANDLE_FILE_THRESHOLD_CTRL, "0"))) {// 文档个数控制
			long fileNum = conf.getLong(QueryConstants.HANDLE_FILE_COUNT_THRESHOLD, Integer.MAX_VALUE);
			if (documentContext.getPdfFileCount() > fileNum) {
				documentContext.setMayFlush(false);
			}

		} else if ("2".equals(conf.get(QueryConstants.HANDLE_FILE_THRESHOLD_CTRL, "0"))) {// 文件大小控制
			long fileSize = conf.getLong(QueryConstants.HANDLE_FILE_SIZE_THRESHOLD, Integer.MAX_VALUE);

			String sizeType = conf.get(QueryConstants.HANDLE_FILE_SIZE_UNIT, "KB");

			EnumSizeUnit unit = EnumSizeUnit.valueOf(sizeType);
			switch (unit) {
			case MB:
				fileSize = fileSize * BYTES_PER_MB;
				break;
			case KB:
				fileSize = fileSize * BYTES_PER_KB;
				break;
			default:
				break;
			}
			if (documentContext.getPdfDocumentSize() > fileSize) {
				documentContext.setMayFlush(false);
			}
		}

		// 校验输出完该页后，是否需要新创建一个PDF文档，接受输出
		if (isNeedNewDocument()) {
			currentPDF.setDocumentSize(getDocumentSize(currentPDF.getFileName()));
			// //设置文件页数
			// documentContext.setCommNum(DOCUMENT_THREAD_HOLE);
			documentContext.setMutilFile(true);
			// 0不限制， 1文件个数，2文件总大小
			throw new NeedNewDocumentException("第" + pageCount + "页完成，需要创建新文档");
		}
	}

	/**
	 * 记录当前页信息
	 */
	private void recordCurrentPage(Long id) {
		pageCount++;
		LOG.info("页码PageCount : " + pageCount);
		// 检测当前文档中有没有当前账号的记录信息
		List<PdfDocument> zhDocs = documentContext.getPdfDocByZhangh(new DocKey(id, documentContext.getCurrentZhangh()));
		if (zhDocs == null) {
			documentContext.recordCurrentZhanghPdf(id);
		} else {
			boolean isContains = false;
			for (PdfDocument pdfDocument : zhDocs) {
				if (pdfDocument.getFileName().equals(currentPDF.getFileName())) {
					isContains = true;
					break;
				}
			}
			if (!isContains) {
				LOG.info("文档切换，记录当前账号的文档信息");
				documentContext.recordCurrentZhanghPdf(id);
			}
		}

		if (currentPDF.getPages(new DocKey(id, documentContext.getCurrentZhangh())) == null) {
			List<Integer> pages = new ArrayList<Integer>();
			pages.add(pageCount);
			currentPDF.addPages(new DocKey(id, documentContext.getCurrentZhangh()), pages);
		} else {
			currentPDF.getPages(new DocKey(id, documentContext.getCurrentZhangh())).add(pageCount);
		}
	}

	/**
	 * 获取指定文件大小的大小
	 * 
	 * @param fileName
	 * @return
	 */
	private long getDocumentSize(String fileName) {
		File file = new File(fileName);
		long bySize = file.length();
		return bySize;
	}

	/**
	 * 判断是否需要创建新的文档
	 * 
	 * @return
	 */
	private boolean isNeedNewDocument() {
		return pageCount >= DOCUMENT_THREAD_HOLE;
	}

	// 打印业务数据对象baseInfo
	public void makePdf() throws IOException, DocumentException {

		if (currentPDF.getAllPageNum() == 0) {
			// 如果Document的中没有存储内容，则不在生成PDF;
			// 清楚当前的HTMLFile
			clearBuffer(htmlFile.getAbsolutePath(), null);
			return;
		}
		LOG.info("开始生成PDF文档");
		// 转换的文档路径
		String inFileUrl = htmlFile.getAbsolutePath();
		// 转换后PDF文件的输出路径
		String tempDir = inFileUrl.substring(0, inFileUrl.lastIndexOf(File.separator) + 1);
		String outFile_url_ = tempDir + record.getSlbhao() + "_tmp.pdf";
		// 实例ITextRenderer，加载html文档
		PdfEditor.html2Pdf(inFileUrl, outFile_url_);

		// 加水印后PDF文件输出路径
		PdfReader reader = new PdfReader(outFile_url_);
		String filePath_stamper = htmlFile + ".pdf";

		LOG.debug("输出PDF的路径是：" + filePath_stamper);
		PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(filePath_stamper));
		BaseFont base = BaseFont.createFont(BaseFont.SYMBOL, BaseFont.SYMBOL, BaseFont.NOT_EMBEDDED);
		// BaseFont base = BaseFont.createFont(arg0, arg1,
		// BaseFont.NOT_EMBEDDED);
		addWaterMark(reader, stamper, base);
		stamper.close();
		// 设置最后生成的PDF的文档
		record.setPdfFile(filePath_stamper);

		clearBuffer(outFile_url_, inFileUrl);
		if (!documentContext.isMutilFile()) {
			documentContext.setCommNum(pageCount);
		}

		// record.setLastNum(pageCount);
	}

	/**
	 * 给每页的pdf添加水印
	 * 
	 * @param reader
	 * @param stamper
	 * @param base
	 */
	private void addWaterMark(PdfReader reader, PdfStamper stamper, BaseFont base) {
		String interval = "     ";
		String markType = QueryConfUtils.getActiveConfig().get(QueryConstants.WATER_MARK_CTRL, "1");
		String markResource = QueryConfUtils.getActiveConfig().get(QueryConstants.WATER_MARK_CONTENT);
		StringBuilder sbCustomerWaterMark = new StringBuilder();
		if (markType.equals("2")) {// 如果是2，则水印为图片
			LOG.warn("暂不支持图片水印!");
		} else {
			LOG.debug("添加的水印是文字水印，水印内容是：" + markResource);
			sbCustomerWaterMark.append(guiyuan).append(markResource);
		}
		StringBuffer wm = new StringBuffer();
		for (int i = 0; i < 4; i++) {
			wm.append(interval).append(sbCustomerWaterMark.toString());
		}

		String customerWaterMark = wm.toString();
		LOG.info("完整水印内容：" + customerWaterMark);
		int total = reader.getNumberOfPages() + 1;
		PdfContentByte under;
		PdfGState gs = new PdfGState();
		gs.setFillOpacity(0.1f);
		for (int i = 1; i < total; i++) {
			under = stamper.getUnderContent(i);
			// under.addImage(image);
			float pageWidth = reader.getPageSize(i).getWidth();
			float pageHeigth = reader.getPageSize(i).getHeight();

			under.beginText();
			under.setFontAndSize(base, 40);
			under.setGState(gs);
			under.setTextRise(-50);
			// 添加柜员号为文字
			under.showTextAligned(Element.ALIGN_CENTER, customerWaterMark, 0, (float) (pageHeigth * 0.2), 45);
			under.showTextAligned(Element.ALIGN_CENTER, customerWaterMark, 0, (float) (pageHeigth * 0.4), 45);
			under.showTextAligned(Element.ALIGN_BOTTOM, customerWaterMark, 0, (float) (pageHeigth * 0.6), 45);
			under.showTextAligned(Element.ALIGN_BOTTOM, customerWaterMark, (float) (pageWidth * 0.2), (float) (pageHeigth * 0.2), 45);
			under.showTextAligned(Element.ALIGN_BOTTOM, customerWaterMark, (float) (pageWidth * 0.4), (float) (pageHeigth * 0.2), 45);
			under.showTextAligned(Element.ALIGN_CENTER, customerWaterMark, (float) (pageWidth * 0.6), (float) (pageHeigth * 0.2), 45);
			under.showTextAligned(Element.ALIGN_CENTER, customerWaterMark, (float) (pageWidth * 0.8), (float) (pageHeigth * 0.2), 45);
			under.endText();
		}
	}

	/**
	 * 删除临时文件
	 * 
	 * @param outFile_url_
	 * @param url
	 */
	private void clearBuffer(String outFile_url_, String url) {
		LOG.debug("开始清除临时文件!");
		if (outFile_url_ != null) {
			LOG.debug("开始清除文件:" + outFile_url_);
			File file = new File(outFile_url_);
			file.delete();
		}
		if (url != null) {
			LOG.debug("开始清除文件:" + url);
			File fileurl = new File(url);
			fileurl.delete();
		}
		if (isNull()) {
			LOG.debug("回滚的文件名称是：" + documentContext.getPrePdf().getFileName());
			currentPDF.setFileName(documentContext.getPrePdf().getFileName());
		}

	}

	/**
	 * 备份有问题的文件
	 */
	public void backupErrorFile() {
		String tmpFileName = currentPDF.getFileName().substring(0, currentPDF.getFileName().lastIndexOf('.'));
		LOG.info("开始备份问题文件.");
		String pathname = tmpFileName + "_bak_" + System.currentTimeMillis();
		LOG.info("备份文件名称为:" + pathname);
		InputStream in = null;
		OutputStream out = null;
		try {
			in = new FileInputStream(tmpFileName);
			out = new FileOutputStream(pathname);
			FileCopyUtils.copy(in, out);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
		LOG.info("问题文件备份结束.");
	}

	public void endHtml() throws IOException {
		LOG.debug("开始结束HTML，打印结束标志对</body></html>");
		fos.write("</body></html>");
		fos.flush();
		closeHtmlDocument();
	}

	private void closeHtmlDocument() {
		LOG.debug("开始关闭文档");
		try {
			if (fos != null) {
				fos.close();
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
	}

	public void getDocumentName() {
		this.currentPDF.getFileName();
	}

	public boolean isNull() {
		return isNull;
	}

	public void setNull(boolean isNull) {
		this.isNull = isNull;
	}

	public static void main(String[] args) throws Exception {

	}

}