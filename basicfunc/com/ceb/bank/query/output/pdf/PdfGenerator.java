package com.ceb.bank.query.output.pdf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.xhtmlrenderer.pdf.ITextRenderer;

import com.ceb.bank.context.OutPdfContext;
import com.ceb.bank.query.output.AbstractGenerator;
import com.ceb.bank.utils.PdfUtils;
import com.ceb.hdqs.config.ConfigLoader;
import com.ceb.hdqs.config.RegisterTable;
import com.ceb.hdqs.entity.PybjyEO;
import com.lowagie.text.DocumentException;

public abstract class PdfGenerator<T> extends AbstractGenerator {

	private static final String FILE_SUFFIX_PDF = ".pdf";
	private OutputStream fos = null;
	private ITextRenderer renderer = new ITextRenderer();

	/**
	 * 
	 * @param record
	 * @param pageCtx
	 * @param pageNum
	 * @param obj
	 * @throws Exception
	 */
	public void writeNoItems(PybjyEO record, OutPdfContext pageCtx, int pageNum, Object obj) throws Exception {
		if (obj == null) {
			createNoItemsPdf(record, pageCtx, null, pageNum);
		} else {
			createPdf(record, pageCtx, null, pageNum, obj);
		}

		close(record, pageCtx);
		splitPdf(record, pageCtx);
	}

	/**
	 * 
	 * @param record
	 * @param pageCtx
	 * @param itemList
	 * @param pageNum
	 * @throws IOException
	 * @throws DocumentException
	 */
	private void createNoItemsPdf(PybjyEO record, OutPdfContext pageCtx, List<T> itemList, int pageNum) throws IOException,
			DocumentException {
		init(record, pageCtx);

		String xhtmlStr = makeXhtml(record, pageCtx, itemList, pageNum, null);
		fos = new FileOutputStream(getFile());
		PdfUtils.createPage(renderer, xhtmlStr, fos);
	}

	/**
	 * 
	 * @param record
	 * @param pageCtx
	 * @throws IOException
	 * @throws DocumentException
	 */
	private void init(PybjyEO record, OutPdfContext pageCtx) throws IOException, DocumentException {
		String outputDir = buildOutputDir(record);
		String filename = record.getSlbhao() + FILE_SUFFIX_PDF;
		File file = new File(outputDir, filename);
		if (file.exists()) {
			file.delete();
		}

		PdfUtils.setFontAndSharedContext(renderer);
		this.setFile(file);
	}

	/**
	 * 当前账号不连续时,生成不连续PDF页
	 * 
	 * @param record
	 *            查询条件
	 * @param pageCtx
	 *            PDF输出上下文
	 * @param blxList
	 *            不连续信息集合
	 * @param pageNum
	 *            PDF页数计数器,同一个受理编号的依次递增,不论几个文件
	 * @param obj
	 *            当前账号上下文
	 * @throws IOException
	 * @throws DocumentException
	 */
	public void createBlxPdf(PybjyEO record, OutPdfContext pageCtx, List<String> blxList, int pageNum, Object obj) throws IOException,
			DocumentException {
		init(record, pageCtx);

		String xhtmlStr = makeBlxXhtml(record, pageCtx, blxList, pageNum, obj);
		fos = new FileOutputStream(getFile());
		PdfUtils.createPage(renderer, xhtmlStr, fos);
		close(record, pageCtx);
	}

	/**
	 * we need to create the target PDF we'll create one page per input string,
	 * but we call layout for the first
	 * 
	 * @param record
	 *            查询条件
	 * @param pageCtx
	 *            PDF输出上下文
	 * @param itemList
	 *            一页明细
	 * @param pageNum
	 *            PDF页数计数器,同一个受理编号的依次递增,不论几个文件
	 * @param obj
	 *            当前账号上下文
	 * @throws IOException
	 * @throws DocumentException
	 */

	public void createPdf(PybjyEO record, OutPdfContext pageCtx, List<T> itemList, int pageNum, Object obj) throws IOException,
			DocumentException {
		init(record, pageCtx);

		String xhtmlStr = makeXhtml(record, pageCtx, itemList, pageNum, obj);
		fos = new FileOutputStream(getFile());
		PdfUtils.createPage(renderer, xhtmlStr, fos);
	}

	/**
	 * each page after the first we add using layout() followed by
	 * writeNextDocument()
	 * 
	 * @param record
	 *            查询条件
	 * @param pageCtx
	 *            PDF输出上下文
	 * @param itemList
	 *            一页明细
	 * @param pageNum
	 *            PDF页数计数器,同一个受理编号的依次递增,不论几个文件
	 * @param obj
	 *            当前账号上下文
	 * @throws IOException
	 * @throws DocumentException
	 */
	public void appendPdf(PybjyEO record, OutPdfContext pageCtx, List<T> itemList, int pageNum, Object obj) throws IOException,
			DocumentException {
		String xhtmlStr = makeXhtml(record, pageCtx, itemList, pageNum, obj);
		PdfUtils.appendPage(renderer, xhtmlStr);
	}

	public void close(PybjyEO record, OutPdfContext pageCtx) throws IOException, DocumentException {
		PdfUtils.close(renderer, fos);
	}

	/**
	 * 按照配置的目标页数进行切分PDF
	 * 
	 * @param record
	 *            查询条件
	 * @param pageCtx
	 *            PDF输出上下文
	 * @throws IOException
	 * @throws DocumentException
	 */
	public void splitPdf(PybjyEO record, OutPdfContext pageCtx) throws IOException, DocumentException {
		// 添加水印
		String markCtl = ConfigLoader.getInstance().getProperty(RegisterTable.HDQS_QUERY_WATER_MARK_CTRL, "1");
		if (markCtl.equals("1")) {
			PdfUtils.addWatermark(record, getFile());
		}

		int pageSize = ConfigLoader.getInstance().getInt(RegisterTable.HDQS_QUERY_HANDLE_PAGE_PER_FILE, 800);
		PdfUtils.split(pageCtx, getFile(), pageSize);
	}

	/**
	 * 组装模板
	 * 
	 * @param record
	 *            查询条件
	 * @param pageCtx
	 *            输出PDF上下文
	 * @param itemList
	 *            该页输出明细列表
	 * @param pageNum
	 *            PDF页数计数器,同一个受理编号的依次递增,不论几个文件
	 * @param obj
	 *            当前账号上下文
	 * @return
	 * @throws IOException
	 */
	public abstract String makeXhtml(PybjyEO record, OutPdfContext pageCtx, List<T> itemList, int pageNum, Object obj) throws IOException;

	/**
	 * 组装模板
	 * 
	 * @param record
	 *            查询条件
	 * @param pageCtx
	 *            输出PDF上下文
	 * @param blxList
	 *            不连续信息集合
	 * @param pageNum
	 *            PDF页数计数器,同一个受理编号的依次递增,不论几个文件
	 * @param obj
	 *            当前账号上下文
	 * @return
	 * @throws IOException
	 */
	public abstract String makeBlxXhtml(PybjyEO record, OutPdfContext pageCtx, List<String> blxList, int pageNum, Object obj)
			throws IOException;

	public static void main(String[] args) throws Exception {
		System.setProperty("HDQS_HOME", "D:/Workspaces/代码/v1.4/源代码/hdqsEJB/src");
		System.setProperty("HDQS_LOG", "D:/logs");
		StringBuffer buffer = new StringBuffer();
		for (int i = 1; i <= 10; i++) {
			buffer.append(i + ",");
		}
		buffer.deleteCharAt(buffer.length() - 1);
		System.out.println(buffer.toString());
		System.out.println(new File("/weblogic/deploy/resource/").toURI().toURL().toString());
		PybjyEO record = new PybjyEO();
		record.setJio1gy("010988");
		PdfUtils.addWatermark(record, new File("D:/201405260209740008_1.pdf"));
	}
}