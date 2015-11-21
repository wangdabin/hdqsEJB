package com.ceb.bank.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;

import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.ceb.bank.context.OutPdfContext;
import com.ceb.hdqs.config.ConfigLoader;
import com.ceb.hdqs.config.RegisterTable;
import com.ceb.hdqs.entity.PybjyEO;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfGState;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.PdfWriter;

public final class PdfUtils {

	private PdfUtils() {
	}

	public static void setFontAndSharedContext(ITextRenderer renderer) throws DocumentException, IOException, MalformedURLException {
		ITextFontResolver resolver = renderer.getFontResolver();
		String resourcePath = ConfigLoader.getInstance().getProperty(RegisterTable.HDQS_RESOURCE_PATH);
		if (!resourcePath.endsWith("/")) {
			resourcePath += "/";
		}
		resolver.addFont(resourcePath + "ARIALUNI.TTF", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
		renderer.getSharedContext().setBaseURL(new File(resourcePath).toURI().toURL().toExternalForm());// 图片路径
	}

	/**
	 * we need to create the target PDF we'll create one page per input string,
	 * but we call layout for the first
	 * 
	 * @param pageCtx
	 * @param itemList
	 * @param pageNum
	 * @throws IOException
	 * @throws DocumentException
	 */
	public static void createPage(ITextRenderer renderer, String xhtmlStr, OutputStream fos) throws IOException, DocumentException {
		renderer.setDocumentFromString(xhtmlStr);
		renderer.layout();
		renderer.createPDF(fos, false);
	}

	/**
	 * each page after the first we add using layout() followed by
	 * writeNextDocument()
	 * 
	 * @param pageCtx
	 * @param itemList
	 * @param pageNum
	 * @throws IOException
	 * @throws DocumentException
	 */
	public static void appendPage(ITextRenderer renderer, String xhtmlStr) throws IOException, DocumentException {
		renderer.setDocumentFromString(xhtmlStr);
		renderer.layout();
		renderer.writeNextDocument();
	}

	public static void close(ITextRenderer renderer, OutputStream fos) throws IOException, DocumentException {
		// complete the PDF
		renderer.finishPDF();
		if (fos != null) {
			fos.flush();
			fos.close();
		}
	}

	public static void addWatermark(PybjyEO record, File pdfFile) throws IOException, DocumentException {
		File tmpFile = new File(pdfFile.getParent(), pdfFile.getName() + ".tmp");
		if (tmpFile.exists()) {
			tmpFile.delete();
		}
		PdfReader reader = null;
		PdfStamper stamp = null;
		try {
			reader = new PdfReader(pdfFile.getAbsolutePath());

			stamp = new PdfStamper(reader, new FileOutputStream(tmpFile));
			BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.EMBEDDED);
			// 文字水印
			PdfGState gs = new PdfGState();
			gs.setFillOpacity(0.05f);
			String warterMark = buildWatermark(record.getJio1gy());
			PdfContentByte under = null;
			for (int i = 1; i <= reader.getNumberOfPages(); i++) {
				under = stamp.getUnderContent(i);
				float pageWidth = reader.getPageSize(i).getWidth();
				float pageHeight = reader.getPageSize(i).getHeight();

				under.beginText();
				under.setFontAndSize(bf, 40);
				under.setGState(gs);
				under.setTextRise(-50);
				under.setTextMatrix(30, 30);
				under.showTextAligned(Element.ALIGN_CENTER, warterMark, 0, (float) (pageHeight * 0.2), 45);
				under.showTextAligned(Element.ALIGN_CENTER, warterMark, 0, (float) (pageHeight * 0.45), 45);
				under.showTextAligned(Element.ALIGN_CENTER, warterMark, 0, (float) (pageHeight * 0.7), 45);
				under.showTextAligned(Element.ALIGN_CENTER, warterMark, (float) (pageWidth * 0.2), (float) (pageHeight * 0.2), 45);
				under.showTextAligned(Element.ALIGN_CENTER, warterMark, (float) (pageWidth * 0.4), (float) (pageHeight * 0.2), 45);
				under.showTextAligned(Element.ALIGN_CENTER, warterMark, (float) (pageWidth * 0.6), (float) (pageHeight * 0.2), 45);
				under.showTextAligned(Element.ALIGN_CENTER, warterMark, (float) (pageWidth * 0.8), (float) (pageHeight * 0.2), 45);
				under.endText();
			}
		} finally {
			if (stamp != null) {
				stamp.close();
			}
			if (reader != null) {
				reader.close();
			}
		}

		pdfFile.delete();
		tmpFile.renameTo(pdfFile);
	}

	private static String buildWatermark(String guiyan) {
		StringBuffer buffer = new StringBuffer();
		String watermark = ConfigLoader.getInstance().getTrimmed(RegisterTable.HDQS_QUERY_WATER_MARK_CONTENT);
		for (int i = 0; i < 8; i++) {
			buffer.append("     ").append(guiyan).append("     ").append(watermark);
		}
		return buffer.toString();
	}

	/**
	 * 删除文件，保留最大页数为remainMaxPage
	 * 
	 * @param srcFile
	 * @param destFile
	 * @param remainMaxPage
	 * @throws IOException
	 * @throws DocumentException
	 */
	public static void deletePage(String srcFile, String destFile, int remainMaxPage) throws IOException, DocumentException {
		PdfReader reader = null;
		PdfStamper stamp = null;
		try {
			reader = new PdfReader(srcFile);
			reader.selectPages(buildSelectPages(remainMaxPage));
			stamp = new PdfStamper(reader, new FileOutputStream(destFile));
		} finally {
			if (stamp != null) {
				stamp.close();
			}
			if (reader != null) {
				reader.close();
			}
		}
	}

	private static String buildSelectPages(int remainMaxPage) {
		StringBuffer buffer = new StringBuffer();
		for (int i = 1; i <= remainMaxPage; i++) {
			buffer.append(i + ",");
		}
		buffer.deleteCharAt(buffer.length() - 1);
		return buffer.toString();
	}

	/**
	 * 根据每个文件定义页数RegisterTable.HDQS_QUERY_HANDLE_PAGE_PER_FILE来分文件,返回当前已经分出的文件数
	 * 
	 * @param ctx
	 *            lastPdfFile:已经分出的最后一个文件,filecount:已经分出的文件数 ,用来产生新文件名称编号
	 * @param currentPdfFile
	 *            当前刚生成的文件 ,待分文件
	 * @param pageSize
	 *            每个文件页数
	 * @throws Exception
	 */
	public static void split(OutPdfContext ctx, File currentPdfFile, int pageSize) throws IOException, DocumentException {
		if (currentPdfFile == null || !currentPdfFile.exists()) {
			return;
		}

		PdfReader reader = new PdfReader(currentPdfFile.getAbsolutePath());
		File lastPdfFile = ctx.getLastPdfFile();
		if (lastPdfFile == null || !lastPdfFile.exists()) {
			ctx.setFileCount(0);
			splitPdf(reader, ctx, currentPdfFile, pageSize, 0);
			currentPdfFile.delete();
		} else {
			PdfReader lastReader = new PdfReader(lastPdfFile.getAbsolutePath());
			int lastPdfNumber = lastReader.getNumberOfPages();
			lastReader.close();
			if (lastPdfNumber == pageSize) {// 满页
				splitPdf(reader, ctx, currentPdfFile, pageSize, 0);
				currentPdfFile.delete();
			} else {// 非满页
				int currentPdfNum = reader.getNumberOfPages();
				if (lastPdfNumber + currentPdfNum <= pageSize) {// 合并不需要新增文件
					File mergePdfFile = new File(lastPdfFile.getParent(), lastPdfFile.getName() + ".tmp");
					appendPdf(ctx, currentPdfFile, mergePdfFile, currentPdfNum);
					lastPdfFile.delete();
					currentPdfFile.delete();
					mergePdfFile.renameTo(lastPdfFile);
				} else {// 先把前一个文件填满，然后递归生成其他文件
					File mergePdfFile = new File(lastPdfFile.getParent(), lastPdfFile.getName() + ".tmp");
					int needMergeNum = pageSize - lastPdfNumber;
					appendPdf(ctx, currentPdfFile, mergePdfFile, needMergeNum);
					lastPdfFile.delete();
					mergePdfFile.renameTo(lastPdfFile);

					splitPdf(reader, ctx, currentPdfFile, pageSize, needMergeNum);
					currentPdfFile.delete();
				}
			}
		}
		reader.close();
	}

	/**
	 * 
	 * @param reader
	 *            Reads a PDF document
	 * @param ctx
	 *            lastPdfFile:已经分出的最后一个文件,filecount:已经分出的文件数 ,用来产生新文件名称编号
	 * @param currentPdfFile
	 *            当前刚生成的文件 ,待分文件
	 * @param pageSize
	 *            每个文件页数
	 * @param readedPageNum
	 *            PDF文件已经读取的页数,起始为0
	 * @return
	 * @throws Exception
	 */
	public static void splitPdf(PdfReader reader, OutPdfContext ctx, File currentPdfFile, int pageSize, int readedPageNum)
			throws IOException, DocumentException {
		int pdfNum = reader.getNumberOfPages();
		Document document = new Document(reader.getPageSize(1));// 继承currentPdfFile的样式

		ctx.setFileCount(ctx.getFileCount() + 1);
		File splitFile = new File(currentPdfFile.getParent(), buildSplitFileName(currentPdfFile.getName(), ctx.getFileCount()));
		if (splitFile.exists()) {
			splitFile.delete();
		}
		OutputStream fout = new FileOutputStream(splitFile);
		PdfWriter writer = PdfWriter.getInstance(document, fout);
		document.open();
		PdfContentByte cb = writer.getDirectContent();
		while (readedPageNum < pdfNum) {
			readedPageNum++;
			document.newPage();
			cb.addTemplate(writer.getImportedPage(reader, readedPageNum), 0, 0);
			if (readedPageNum % pageSize == 0) {// 达到一个文件页数
				document.close();
				writer.flush();
				writer.close();
				ctx.setLastPdfFile(splitFile);// lastPdfFile 是满页
				if (readedPageNum < pdfNum) {// 还需不需要生成新文件
					ctx.setFileCount(ctx.getFileCount() + 1);
					document = new Document(reader.getPageSize(1));
					splitFile = new File(currentPdfFile.getParent(), buildSplitFileName(currentPdfFile.getName(), ctx.getFileCount()));
					if (splitFile.exists()) {
						splitFile.delete();
					}
					fout = new FileOutputStream(splitFile);
					writer = PdfWriter.getInstance(document, fout);
					document.open();
					cb = writer.getDirectContent();
				}
			}
		}
		if (readedPageNum % pageSize != 0) {// 最后一个文件没有满页
			document.close();
			writer.flush();
			writer.close();
			ctx.setLastPdfFile(splitFile);// lastPdfFile 非满页
		}
	}

	/**
	 * 
	 * @param fileName
	 *            pdf文件名称,格式为201405270106230020.pdf
	 * @param fileCount
	 *            已经分出的文件数 ,用来产生新文件名称编号
	 * @return 201405270106230020_1.pdf
	 */
	private static String buildSplitFileName(String fileName, int fileCount) {
		int index = fileName.lastIndexOf(".");
		String result = fileName.substring(0, index) + "_" + fileCount + fileName.substring(index);
		return result;
	}

	/**
	 * 
	 * @param ctx
	 *            lastPdfFile:已经分出的最后一个文件
	 * @param currentPdfFile
	 *            当前刚生成的文件 ,待分文件
	 * @param mergePdfFile
	 *            合并后文件
	 * @param needMergePageNum
	 *            需要从刚生成的文件中合并多少页到已经分出的最后一个文件中
	 * @throws Exception
	 */
	public static void mergePdf(OutPdfContext ctx, File currentPdfFile, File mergePdfFile, int needMergePageNum) throws IOException,
			DocumentException {
		PdfReader lastReader = null;
		PdfReader currentReader = null;
		Document document = null;
		PdfWriter writer = null;
		try {
			lastReader = new PdfReader(ctx.getLastPdfFile().getAbsolutePath());
			currentReader = new PdfReader(currentPdfFile.getAbsolutePath());

			FileOutputStream out = new FileOutputStream(mergePdfFile);
			document = new Document(currentReader.getPageSize(1));
			writer = PdfWriter.getInstance(document, out);

			document.open();
			PdfContentByte cb = writer.getDirectContent();

			int readedPageNum = 0;
			while (readedPageNum < lastReader.getNumberOfPages()) {
				document.newPage();
				readedPageNum++;
				PdfImportedPage page = writer.getImportedPage(lastReader, readedPageNum);
				cb.addTemplate(page, 0, 0);
			}
			readedPageNum = 0;
			while (readedPageNum < needMergePageNum) {
				document.newPage();
				readedPageNum++;
				PdfImportedPage page = writer.getImportedPage(currentReader, readedPageNum);
				cb.addTemplate(page, 0, 0);
			}
		} finally {
			if (document != null) {
				document.close();
			}
			if (writer != null) {
				writer.close();
			}
			if (lastReader != null) {
				lastReader.close();
			}
			if (currentReader != null) {
				currentReader.close();
			}
		}
	}

	/**
	 * 
	 * @param ctx
	 *            lastPdfFile:已经分出的最后一个文件
	 * @param currentPdfFile
	 *            当前刚生成的文件 ,待分文件
	 * @param mergePdfFile
	 *            合并后文件
	 * @param needMergePageNum
	 *            需要从刚生成的文件中合并多少页到已经分出的最后一个文件中
	 * @throws Exception
	 */
	public static void appendPdf(OutPdfContext ctx, File currentPdfFile, File mergePdfFile, int needMergePageNum) throws IOException,
			DocumentException {
		PdfStamper stamp = null;
		PdfReader lastReader = null;
		PdfReader currentReader = null;
		try {
			lastReader = new PdfReader(ctx.getLastPdfFile().getAbsolutePath());
			stamp = new PdfStamper(lastReader, new FileOutputStream(mergePdfFile));
			currentReader = new PdfReader(currentPdfFile.getAbsolutePath());

			int pageNum = lastReader.getNumberOfPages();
			int readedPageNum = 0;
			while (readedPageNum < needMergePageNum) {
				readedPageNum++;
				stamp.insertPage(pageNum + readedPageNum, lastReader.getPageSize(1));

				PdfImportedPage page = stamp.getImportedPage(currentReader, readedPageNum);
				Image image = Image.getInstance(page);
				image.setAbsolutePosition(0, 0);
				PdfContentByte over = stamp.getOverContent(pageNum + readedPageNum);
				over.addImage(image);
			}
		} finally {
			if (stamp != null) {
				stamp.close();
			}
			if (lastReader != null) {
				lastReader.close();
			}
			if (currentReader != null) {
				currentReader.close();
			}
		}
	}

	public static void main(String[] args) throws Exception {
		// 201405270106230020_1.pdf
		System.out.println(buildSplitFileName("201405270106230020.pdf", 1));
		System.out.println(new File("D:/abc/ac.pdf").getAbsolutePath());
		System.out.println(new File("D:/abc/ac.pdf").getPath());
	}
}
