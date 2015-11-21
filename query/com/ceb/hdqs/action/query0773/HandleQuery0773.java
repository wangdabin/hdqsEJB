package com.ceb.hdqs.action.query0773;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import com.ceb.bank.htable.HTablePoolHolder;
import com.ceb.hdqs.action.authorize.Authorize0773;
import com.ceb.hdqs.action.query.IQuery;
import com.ceb.hdqs.action.query.exception.ConditionNotExistException;
import com.ceb.hdqs.action.query.exception.NeedNewDocumentException;
import com.ceb.hdqs.action.query.output.PDFMaker;
import com.ceb.hdqs.action.query.output.pdf.PdfMaker0773;
import com.ceb.hdqs.action.query.output.txt.TxtPrinter0773;
import com.ceb.hdqs.action.query.parser.ParserOf0773;
import com.ceb.hdqs.constants.HdqsConstants;
import com.ceb.hdqs.entity.PybjyEO;
import com.ceb.hdqs.entity.result.Handle0773QueryResult;
import com.ceb.hdqs.entity.result.ReplacementCardItemResult;
import com.ceb.hdqs.entity.result.ReplacementCardParseResult;
import com.ceb.hdqs.query.entity.AuthorizeLevel;
import com.ceb.hdqs.query.entity.Page;
import com.ceb.hdqs.query.entity.PageHeader;
import com.ceb.hdqs.query.entity.QueryDocumentContext;
import com.ceb.hdqs.query.utils.QueryConfUtils;
import com.ceb.hdqs.query.utils.QueryConstants;
import com.ceb.hdqs.query.utils.QueryMethodUtils;

/**
 * 换卡记录查询
 * 
 * @author user
 * 
 */
public class HandleQuery0773 implements IQuery<Handle0773QueryResult> {
	private static final Log log = LogFactory.getLog(HandleQuery0773.class);

	private ParserOf0773 accountAnalyzeQuery = new ParserOf0773();

	public HandleQuery0773() {
	}

	public String generateFile(PybjyEO record, Handle0773QueryResult result, List<ReplacementCardItemResult> changeItems)
			throws Exception {
		if (changeItems == null || changeItems.size() == 0) {
			return null;
		}
		// 开始生成打印的文件
		if (HdqsConstants.SHFOBZ_DOWNLOAD.equals(record.getShfobz())) {
			QueryDocumentContext document = new QueryDocumentContext();
			String outputDir = QueryMethodUtils.generateOutputDir(record);
			log.info("查询任务的输出目录是" + outputDir);
			record.setPdfFile(outputDir + File.separator + record.getSlbhao() + ".pdf");
			PDFMaker pdfMaker = new PdfMaker0773(record, document);
			List<ReplacementCardItemResult> pageItem = new ArrayList<ReplacementCardItemResult>();
			// int itemNum = record.getQueryNum();
			int itemNum = QueryConfUtils.getActiveConfig().getInt(QueryConstants.HANDLE_LINE_PER_PAGE, 35);
			int pageNum = 0;
			for (ReplacementCardItemResult item : changeItems) {
				pageItem.add(item);
				if (pageItem.size() != 0 && pageItem.size() % itemNum == 0) {
					pageNum++;
					Page<ReplacementCardItemResult> page = new Page<ReplacementCardItemResult>();
					PageHeader header = getHeader(record, result);
					header.setPageCount("" + pageNum);
					page.setHeader(header);
					header.setGuiyuan(record.getJio1gy());
					page.setPageItem(pageItem);
					page.getHeader().setTips(result.getRemark());
					page.setParseResult(result.getParseResult());
					try {
						pdfMaker.write(page);
					} catch (NeedNewDocumentException nDocEx) {
						log.info("需要创建新文档");
						pdfMaker.endHtml();
						pdfMaker.makePdf();

						pageItem.clear();
						pdfMaker = new PdfMaker0773(record, document);
					} catch (Exception e) {
						throw e;
					}
					page = null;
				}
			}

			//
			if (pageItem.size() != 0) {
				pageNum++;
				Page<ReplacementCardItemResult> page = new Page<ReplacementCardItemResult>();
				PageHeader header = getHeader(record, result);
				page.setHeader(header);
				header.setPageCount("" + pageNum);
				page.setPageItem(pageItem);
				page.getHeader().setTips(result.getRemark());
				page.setParseResult(result.getParseResult());
				try {
					pdfMaker.write(page);
				} catch (NeedNewDocumentException nDocEx) {
					log.info("需要创建新文档");
					pdfMaker.endHtml();
					pdfMaker.makePdf();
					pageItem.clear();
					pdfMaker = new PdfMaker0773(record, document);
				} catch (Exception e) {
					log.error(e.getMessage(), e);
					throw e;
				}
				page = null;
			}
			pdfMaker.endHtml();
			pdfMaker.makePdf();
			pageItem.clear();
			record.setPdfFile(document.getCurrentPDF().getFileName());
			return record.getPdfFile();
		}
		// 生成txt文档
		else if (HdqsConstants.SHFOBZ_PRINT.equals(record.getShfobz())) {
			// 修改成生成txt
			log.debug("开始生成TXT文档.");
			TxtPrinter0773 txtCreater = new TxtPrinter0773(record);
			log.debug("TXT文档的输出路径是：" + txtCreater.getTxtFile());
			List<ReplacementCardItemResult> pageItem = new ArrayList<ReplacementCardItemResult>();
			int itemNum = QueryConfUtils.getActiveConfig().getInt(QueryConstants.PRINT_TXT_LINE_PER_PAGE, 20);
			int pageNum = 0;
			for (ReplacementCardItemResult item : changeItems) {
				pageItem.add(item);
				if (pageItem.size() != 0 && pageItem.size() % itemNum == 0) {
					pageNum++;
					Page<ReplacementCardItemResult> page = new Page<ReplacementCardItemResult>();
					PageHeader header = getHeader(record, result);
					header.setPageCount("" + pageNum);
					page.setHeader(header);
					page.setPageItem(pageItem);
					page.getHeader().setTips(result.getRemark());
					try {
						txtCreater.write(page);
					} catch (Exception e) {
						throw e;
					}
					page = null;
				}
			}
			//
			if (pageItem.size() != 0) {
				pageNum++;
				Page<ReplacementCardItemResult> page = new Page<ReplacementCardItemResult>();
				PageHeader header = getHeader(record, result);
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
			}

			return txtCreater.getTxtFile();
		}
		return null;
	}

	/**
	 * 获取换卡打印的Header
	 * 
	 * @param cardReplacementRecords
	 * @return
	 */
	private PageHeader getHeader(PybjyEO record, Handle0773QueryResult result) {
		PageHeader header = new PageHeader();
		header.setPrintDate(System.currentTimeMillis());
		header.setKehhao(result.getKEHUZH());
		header.setKhzwm(result.getGERZWM());
		header.setGuiyuan(record.getJio1gy());
		return header;
	}

	@Override
	public Handle0773QueryResult query(PybjyEO record) throws Exception {
		Handle0773QueryResult result = new Handle0773QueryResult();

		try {
			/*
			 * 解析输入的客户账号信息，查询出该客户账号的换卡记录
			 */
			ReplacementCardParseResult<ReplacementCardItemResult> cardReplacementRecords = parseInputCondidition(record);
			if (cardReplacementRecords == null) {
				throw new ConditionNotExistException(record.getKehuzh() + "在AKHZH中不存在");
			}

			// 判断授权级别
			Authorize0773 authorize0773 = new Authorize0773();
			AuthorizeLevel authorizeInfo = authorize0773.checkAuthority(cardReplacementRecords);

			/* 查询明细表获取换卡记录 */
			List<ReplacementCardItemResult> changeItems = cardReplacementRecords.getCardParseResult();
			// ======================================================查询换卡记录的详细信息========================
			List<Get> gets = new ArrayList<Get>(changeItems.size());
			for (ReplacementCardItemResult replacementCardItemResult : changeItems) {
				gets.add(buildChangeQueryGetter(replacementCardItemResult));
			}
			HTablePoolHolder factory = HTablePoolHolder.getInstance(QueryConfUtils.getActiveConfig());
			HTableInterface table = factory.getHTable(QueryConstants.TABLE_NAME_VYKTD);

			try {
				// 开始查询换卡记录的明细信息
				Result[] getResult = table.get(gets);
				// 解析详细信息
				for (int i = 0; i < getResult.length; i++) {
					ReplacementCardItemResult cardItem = changeItems.get(i);
					Result resTmp = getResult[i];
					String kehuHao = Bytes.toString(resTmp.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE,
							Bytes.toBytes("KEHHAO")));
					cardItem.setKehhao(kehuHao);

					String jdbz = Bytes.toString(resTmp.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE,
							Bytes.toBytes("JDJKBZ")));
					cardItem.setJdjkbz(jdbz);

					String jioyrq = Bytes.toString(resTmp.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE,
							Bytes.toBytes("FAKARQ")));
					cardItem.setFakarq(jioyrq);

					String cardTpye = Bytes.toString(resTmp.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE,
							Bytes.toBytes("KAAAZL")));
					cardItem.setKaaazl(cardTpye);
				}
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			} finally {
				factory.release(table);
			}

			log.info("卡片[" + record.getKehuzh() + "]经过[" + (cardReplacementRecords.getCardParseResult().size() - 1)
					+ "]次换卡");
			result.setRemark("卡片[" + record.getKehuzh() + "]经过["
					+ (cardReplacementRecords.getCardParseResult().size() - 1) + "]次换卡");

			// replacementCardResult.put(ZHANGH, list);
			result.setAuthorityInfo(authorizeInfo);
			result.setReplacementCardResult(changeItems);
			result.setKEHUZH(cardReplacementRecords.getKehhao());
			result.setGERZWM(cardReplacementRecords.getKehzwm() == null ? "" : cardReplacementRecords.getKehzwm());
			result.setParseResult(cardReplacementRecords);
			return result;
		} catch (ConditionNotExistException e) {
			throw e;
		} catch (RuntimeException e) {
			throw e;
		}

	}

	/**
	 * 创建查询换卡记录明细的Get对象，查询主要插件VYKTD(VYKTD以卡号作为rowkey)表
	 * 
	 * @param replacementCardItemResult
	 * @return
	 */
	private Get buildChangeQueryGetter(ReplacementCardItemResult replacementCardItemResult) {
		String cardNo = replacementCardItemResult.getKahaoo();
		Get get = new Get(Bytes.toBytes(StringUtils.reverse(cardNo)));
		return get;
	}

	/**
	 * 账号解析表账号解析表
	 * 
	 * @param record
	 * @return
	 * @throws Exception
	 */
	public ReplacementCardParseResult<ReplacementCardItemResult> parseInputCondidition(PybjyEO record) throws Exception {
		return accountAnalyzeQuery.parseCondition(record);
	}

}
