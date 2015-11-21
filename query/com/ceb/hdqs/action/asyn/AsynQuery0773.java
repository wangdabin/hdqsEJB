package com.ceb.hdqs.action.asyn;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.LogFactory;

import com.ceb.hdqs.action.query.exception.NeedNewDocumentException;
import com.ceb.hdqs.action.query.output.PDFMaker;
import com.ceb.hdqs.action.query.output.pdf.PdfMaker0773;
import com.ceb.hdqs.action.query.output.txt.TxtPrinter0773;
import com.ceb.hdqs.action.query0773.HandleQuery0773;
import com.ceb.hdqs.constants.HdqsConstants;
import com.ceb.hdqs.entity.PybjyEO;
import com.ceb.hdqs.entity.result.Handle0773QueryResult;
import com.ceb.hdqs.entity.result.ReplacementCardItemResult;
import com.ceb.hdqs.query.entity.Page;
import com.ceb.hdqs.query.entity.PageHeader;
import com.ceb.hdqs.query.entity.QueryDocumentContext;
import com.ceb.hdqs.query.utils.QueryConfUtils;
import com.ceb.hdqs.query.utils.QueryConstants;
import com.ceb.hdqs.query.utils.QueryMethodUtils;

/**
 * 0773同步打印、导出，生成pdf
 * 
 * @author user
 * 
 */
public class AsynQuery0773 extends AbstractAsynQuery {

	public AsynQuery0773(List<PybjyEO> list) {
		super(list);
		setLog(LogFactory.getLog(AsynQuery0773.class));
	}

	@Override
	public String startAsynchronizeQuery(boolean isAsyPrint) throws Exception {
		HandleQuery0773 query = new HandleQuery0773();
		Handle0773QueryResult result = query.query(getQueryCondition());
		List<ReplacementCardItemResult> changeItems = result.getReplacementCardResult();
		if (changeItems == null || changeItems.isEmpty()) {
			return null;
		}
		// 开始生成打印的文件
		if (HdqsConstants.SHFOBZ_DOWNLOAD.equals(getQueryCondition().getShfobz())) {
			return handleDownload(result, changeItems);
		} else if (HdqsConstants.SHFOBZ_PRINT.equals(getQueryCondition().getShfobz())) {// 生成txt文档
			return handlePrint(result, changeItems);
		}
		return null;
	}

	private String handleDownload(Handle0773QueryResult result, List<ReplacementCardItemResult> changeItems) throws Exception {
		QueryDocumentContext document = new QueryDocumentContext();
		String outputDir = QueryMethodUtils.generateOutputDir(getQueryCondition());
		getLog().debug("查询任务的输入出目录是" + outputDir);
		getQueryCondition().setPdfFile(outputDir + File.separator + getQueryCondition().getSlbhao() + ".pdf");
		PDFMaker pdfMaker = new PdfMaker0773(getQueryCondition(), document);
		List<ReplacementCardItemResult> pageItem = new ArrayList<ReplacementCardItemResult>();
		int itemNum = QueryConfUtils.getActiveConfig().getInt(QueryConstants.HANDLE_LINE_PER_PAGE, 35);
		int pageNum = 0;
		for (ReplacementCardItemResult item : changeItems) {
			pageItem.add(item);
			if (pageItem.size() != 0 && pageItem.size() % itemNum == 0) {
				pageNum++;
				Page<ReplacementCardItemResult> page = new Page<ReplacementCardItemResult>();
				PageHeader header = getHeader(result.getKEHUZH(), result.getGERZWM());
				header.setPageCount("" + pageNum);
				page.setHeader(header);
				header.setGuiyuan(getQueryCondition().getJio1gy());
				page.setPageItem(pageItem);
				page.getHeader().setTips(result.getRemark());
				try {
					pdfMaker.write(page);
				} catch (NeedNewDocumentException nDocEx) {
					getLog().info("需要创建新文档");
					createNewDocument(getQueryCondition());
				}
				page = null;
			}
		}

		if (pageItem.size() != 0) {
			pageNum++;
			Page<ReplacementCardItemResult> page = new Page<ReplacementCardItemResult>();
			PageHeader header = getHeader(result.getKEHUZH(), result.getGERZWM());
			header.setPageCount("" + pageNum);
			page.setHeader(header);

			page.setPageItem(pageItem);
			page.getHeader().setTips(result.getRemark());
			try {
				pdfMaker.write(page);
			} catch (NeedNewDocumentException nDocEx) {
				getLog().info("需要创建新文档");
				pdfMaker = new PdfMaker0773(getQueryCondition(), document);
			}
			page = null;
		}
		pdfMaker.endHtml();
		pdfMaker.makePdf();
		getQueryCondition().setPdfFile(document.getCurrentPDF().getFileName());
		return getQueryCondition().getPdfFile();
	}

	private String handlePrint(Handle0773QueryResult result, List<ReplacementCardItemResult> changeItems) throws Exception {
		getLog().debug("开始生成TXT文档.");
		TxtPrinter0773 txtCreater = new TxtPrinter0773(getQueryCondition());
		getLog().debug("TXT文档的输出路径是：" + txtCreater.getTxtFile());
		List<ReplacementCardItemResult> pageItem = new ArrayList<ReplacementCardItemResult>();
		int itemNum = QueryConfUtils.getActiveConfig().getInt(QueryConstants.PRINT_TXT_LINE_PER_PAGE, 20);
		int pageNum = 0;
		for (ReplacementCardItemResult item : changeItems) {
			pageItem.add(item);
			if (pageItem.size() != 0 && pageItem.size() % itemNum == 0) {
				flushTxt(result, txtCreater, pageItem, pageNum);
			}
		}

		if (pageItem.size() != 0) {
			flushTxt(result, txtCreater, pageItem, pageNum);
		}

		return txtCreater.getTxtFile();
	}

	private int flushTxt(Handle0773QueryResult result, TxtPrinter0773 txtCreater, List<ReplacementCardItemResult> pageItem, int pageNum) throws Exception {
		pageNum++;
		Page<ReplacementCardItemResult> page = new Page<ReplacementCardItemResult>();
		PageHeader header = getHeader(result.getKEHUZH(), result.getGERZWM());
		page.setHeader(header);
		header.setPageCount("" + pageNum);
		page.setPageItem(pageItem);
		page.getHeader().setTips(result.getRemark());
		try {
			txtCreater.write(page);
		} catch (Exception e) {
			throw e;
		}
		page = null;
		return pageNum;
	}

	/**
	 * 获取换卡打印的Header
	 * 
	 * @param cardReplacementRecords
	 * @return
	 */
	private PageHeader getHeader(String kehhao, String khzwm) {
		PageHeader header = new PageHeader();
		header.setPrintDate(System.currentTimeMillis());
		header.setKehhao(kehhao);
		header.setKhzwm(khzwm);
		header.setGuiyuan(getQueryCondition().getJio1gy());
		return header;
	}
}