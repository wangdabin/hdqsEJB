package com.ceb.hdqs.action.query.output;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.ceb.hdqs.entity.result.AbstractQueryResult;
import com.ceb.hdqs.query.entity.Page;
import com.ceb.hdqs.query.utils.QueryConfUtils;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfCopy;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;

/**
 * PDF 编辑器
 * 
 * @author user
 * 
 */
public class PdfEditor {
	private static final Log LOG = LogFactory.getLog(PdfEditor.class);

	/**
	 * 删除指定账号的信息，并且在删除明细的地方添加账号的不连续信息
	 * 
	 * @param pdfName
	 *            被删除的文件名称
	 * @param pageNum
	 *            需要被删除的页码
	 * @param errorMsg
	 *            需要填充进去的不连续说明
	 * @throws FileNotFoundException
	 */
	public static void deletePages(String pdfName, List<Integer> pageNum, String errorMsg) {
		LOG.info("当前删除页面的文档是:" + pdfName);
		for (Integer integer : pageNum) {
			LOG.info("当前删除页码:" + integer);
		}
		partitionPdfFile(pdfName, pageNum.get(0));
	}

	/**
	 * 删除包含不连续明细的完整文档
	 * 
	 * @param pdfName
	 *            被删除的文件名称
	 */
	public static void dropDocument(String pdfName) {
		File file = new File(pdfName);
		file.delete();
	}

	/**
	 * 格式化查询出的明细，输出能够被itext解析的html文件
	 * 
	 * @param page
	 * @return
	 */
	public static String formatPageByTemplate(HtmlTemplator templateUtils, Page<? extends AbstractQueryResult> page) {
		String pageStr = templateUtils.formatOutput(page);
		return pageStr;
	}

	/**
	 * 将HTML转换成Pdf文件
	 * 
	 * @param inFileUrl
	 * @param outFile_url_
	 * @throws IOException
	 * @throws DocumentException
	 */
	public static void html2Pdf(String inFileUrl, String outFile_url_) throws IOException, DocumentException {
		OutputStream output = new FileOutputStream(outFile_url_);
		ITextRenderer renderer = new ITextRenderer();
		LOG.debug("输入文件地址是：" + inFileUrl);
		File file=null;
		try {
			file=new File(inFileUrl);
		} catch (Exception e) {
			
		}
		
		renderer.setDocument(file);
		// 支持中文
		ITextFontResolver fontResolver = renderer.getFontResolver();
		fontResolver.addFont(QueryConfUtils.weblogic_domain_home + File.separator + "config" + File.separator + "ARIALUNI.TTF", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);

		// renderer.getSharedContext().setBaseURL("file:/templates/logo.jpg");

		renderer.layout();
		renderer.createPDF(output);
		output.close();
		renderer.finishPDF();
	}

	public static void partitionPdfFile(String filepath, int pageNum) {
		Document document = null;
		PdfCopy copy = null;
		int N = pageNum;
		try {
			PdfReader reader = new PdfReader(filepath);
			int n = reader.getNumberOfPages();
			if (n < N) {
				LOG.warn("The document does not have " + N + " pages to partition !");
				return;
			}
			String savepath = filepath.substring(0, filepath.lastIndexOf(".")) + "_bak.pdf";

			document = new Document();
			copy = new PdfCopy(document, new FileOutputStream(savepath));
			document.open();

			for (int i = 1; i < N; i++) {
				PdfImportedPage page = copy.getImportedPage(reader, i);
				copy.addPage(page);
			}
			copy.flush();
			copy.close();
			document.close();

			// 删除bak文件
			// 删除源文件
			File file = new File(filepath);
			file.delete();
			// 重命名
			File newFile = new File(savepath);
			newFile.renameTo(file);

		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		} catch (DocumentException e) {
			LOG.error(e.getMessage(), e);
		} finally {
			// if (copy != null)
			// copy.close();
			// if (document.isOpen())
			// document.close();
		}
	}

	public static void main(String[] args) {
		String fileName = "D:/pdfTest/delete.pdf";

		partitionPdfFile(fileName, 27);
	}
}