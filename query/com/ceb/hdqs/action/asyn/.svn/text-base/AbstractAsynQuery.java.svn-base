package com.ceb.hdqs.action.asyn;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;

import com.ceb.hdqs.action.query.ItemQueryProcessor;
import com.ceb.hdqs.action.query.exception.BalanceBrokedException;
import com.ceb.hdqs.action.query.exception.NeedNewDocumentException;
import com.ceb.hdqs.action.query.output.BrokedDocumentEditor;
import com.ceb.hdqs.action.query.output.ExcelMaker;
import com.ceb.hdqs.action.query.output.HtmlTemplator;
import com.ceb.hdqs.action.query.output.PDFMaker;
import com.ceb.hdqs.action.query.output.TxtMaker;
import com.ceb.hdqs.constants.HdqsConstants;
import com.ceb.hdqs.entity.PybjyEO;
import com.ceb.hdqs.entity.result.AbstractQueryResult;
import com.ceb.hdqs.entity.result.IdentityParseResult;
import com.ceb.hdqs.entity.result.KehhaoParseResult;
import com.ceb.hdqs.entity.result.KehzhParserResult;
import com.ceb.hdqs.entity.result.ParseResult;
import com.ceb.hdqs.entity.result.ParseResultAsyn;
import com.ceb.hdqs.entity.result.ZhangHParseResult;
import com.ceb.hdqs.query.entity.DocKey;
import com.ceb.hdqs.query.entity.EnumQueryKind;
import com.ceb.hdqs.query.entity.Page;
import com.ceb.hdqs.query.entity.PageHeader;
import com.ceb.hdqs.query.entity.PdfDocument;
import com.ceb.hdqs.query.entity.QueryDocumentContext;
import com.ceb.hdqs.query.entity.SkyPair;
import com.ceb.hdqs.query.entity.XlsDocument;
import com.ceb.hdqs.query.utils.BrokedReordUtils;
import com.ceb.hdqs.query.utils.QueryConfUtils;
import com.ceb.hdqs.query.utils.QueryConstants;
import com.ceb.hdqs.service.YbjyService;
import com.ceb.hdqs.utils.JNDIUtils;
import com.lowagie.text.DocumentException;

/**
 * 异步查询抽象类
 * 
 * @author user
 */
public abstract class AbstractAsynQuery implements IAsynchronizeQuery {
	private Log log;
	private YbjyService ybjyService;
	private PybjyEO queryCondition;
	protected List<PybjyEO> poList = new ArrayList<PybjyEO>();

	private PDFMaker pdfMaker;
	private ExcelMaker excelMaker;
	private TxtMaker<AbstractQueryResult> txtMaker;

	private QueryDocumentContext documentContext = new QueryDocumentContext();
//	private Map<String, List<String>> brokedCache = new LinkedHashMap<String, List<String>>();

	public AbstractAsynQuery(List<PybjyEO> list) {
		this.poList = list;
		queryCondition = poList.get(0);
		try {
			this.ybjyService = (YbjyService) JNDIUtils.lookup(YbjyService.class);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	/**
	 * 根据输入条件动态的调用对应的文档生成类
	 * 
	 * @throws ClassNotFoundException
	 * @throws NoSuchMethodException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws IOException
	 */
	public void initMaker() throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, IOException {
		if (HdqsConstants.SHFOBZ_PRINT.equals(queryCondition.getShfobz())) {
			String txtClassName = TXT_PRINTER + queryCondition.getJiaoym();
			log.debug("启动的查询类是:" + txtClassName);
			Class txtClazz = Class.forName(txtClassName);
			Constructor txtConstructor = txtClazz.getDeclaredConstructor(PybjyEO.class);
			txtConstructor.setAccessible(true);
			txtMaker = (TxtMaker) txtConstructor.newInstance(queryCondition);
			log.debug(txtMaker.getTxtFile());
		} else if (HdqsConstants.SHFOBZ_DOWNLOAD.equals(queryCondition.getShfobz())) {
			String pdfClassName = PDF_PRINTER + queryCondition.getJiaoym();
			Class pdfClazz = Class.forName(pdfClassName);
			Constructor pdfConstructor = pdfClazz.getDeclaredConstructor(PybjyEO.class, QueryDocumentContext.class);
			pdfConstructor.setAccessible(true);
			pdfMaker = (PDFMaker) pdfConstructor.newInstance(queryCondition, documentContext);
			if (isPrintExcel()) {
				// 非客户查询
				String xlsClassName = EXCEL_PRINTER + queryCondition.getJiaoym();
				Class xlsClazz = Class.forName(xlsClassName);
				Constructor xlsConstructor = xlsClazz.getDeclaredConstructor(PybjyEO.class, QueryDocumentContext.class);
				xlsConstructor.setAccessible(true);
				excelMaker = (ExcelMaker) xlsConstructor.newInstance(queryCondition, documentContext);
			}

		}
	}

	/**
	 * 判断是否生成Excel,批量处理时都需要生成Excel
	 * 
	 * @return
	 */
	public boolean isPrintExcel() {
		return QueryConstants.CORPORATE_BATCH_QUERY_CODE.equals(queryCondition.getJiaoym()) || QueryConstants.PRIVATE_BATCH_QUERY_CODE.equals(queryCondition.getJiaoym());
	}

	@Override
	public abstract String startAsynchronizeQuery(boolean isAsyPrint) throws Exception;

	@Override
	public void update(PybjyEO record) {
		ybjyService.update(record);
	}

	@Override
	public void batchUpdate(List<PybjyEO> list) {
		ybjyService.batchUpdateRecords(list);
	}

	/**
	 * 开始查询解析结果
	 * 
	 * @param parseResults
	 * @return
	 * @throws Exception
	 */
	protected String queryParseResult(ParseResultAsyn parseResults) throws Exception {
		try {
			for (ParseResult parseResult : parseResults.getParseResults()) {
				// 如果标记为不能查询的时候，说明该查询条件存在两种情况（查无此号、此号不存在明细）
				if (!parseResult.isQuery()) {
					Page<AbstractQueryResult> page = new Page<AbstractQueryResult>();
					PageHeader header = new PageHeader();
					header.setTips(parseResult.getTips());
					header.setPrintDate(System.currentTimeMillis());
					header.setKhzwm(parseResult.getKehzwm());
					page.setHeader(header);
					page.setParseResult(parseResult);

					/* ================ 记录页数================================= */
					documentContext.setAllPageCount(documentContext.getAllPageCount() + 1);
					// 当前账号所属Record的明细
					int oldCommNum = parseResult.getRecord().getCommNum();
					parseResult.getRecord().setCommNum(oldCommNum + 1);
					parseResult.setAllPageCount(parseResult.getAllPageCount() + 1);
					/* ==============================记录页数 ====结束============ */
					try {
						flushPageToDocument(parseResult.getRecord(), page);
					} catch (NeedNewDocumentException e) {
						createNewDocument(parseResult.getRecord());
					}

					documentContext.setAllItemCount(0);
					documentContext.setAllPageCount(0);

					continue;
				}
				String type = parseResult.getRecord().getChaxzl();
				switch (EnumQueryKind.valueOf(EnumQueryKind.QK + type)) {
				case QK1:// 账号查询
					ZhangHParseResult zhanghResult = (ZhangHParseResult) parseResult;
					try {
						doZhangHQuery(zhanghResult, false);
					} catch (org.xhtmlrenderer.util.XRRuntimeException e) {
						zhanghResult.getRecord().setRunStatus(HdqsConstants.RUNNING_STATUS_FAILURE);
						if (isPrintExcel())
							excelMaker.close();
						long itemCount = parseResult.getAllItemCount();
						documentContext.setAllItemCount(documentContext.getAllItemCount() - itemCount);
						long pageCount = parseResult.getAllPageCount();
						documentContext.setAllPageCount(documentContext.getAllPageCount() - pageCount);
						pdfMaker.backupErrorFile();
						initMaker();
						log.error(e.getMessage(), e);
					} catch (Exception e) {
						zhanghResult.getRecord().setRunStatus(HdqsConstants.RUNNING_STATUS_FAILURE);
						log.error(e.getMessage(), e);
					} finally {
						documentContext.setAllItemCount(0);
						documentContext.setAllPageCount(0);
						documentContext.setFinishedZhNum(0);
						zhanghResult.getRecord().setItemCount((zhanghResult.getItemCount()));
					}
					break;
				case QK2:// 客户账号查询
					KehzhParserResult khzhParseResult = (KehzhParserResult) parseResult;
					try {
						doKhzhQuery(khzhParseResult, false);
					} catch (org.xhtmlrenderer.util.XRRuntimeException e) {
						khzhParseResult.getRecord().setRunStatus(HdqsConstants.RUNNING_STATUS_FAILURE);
						if (isPrintExcel())
							excelMaker.close();
						long itemCount = parseResult.getAllItemCount();
						documentContext.setAllItemCount(documentContext.getAllItemCount() - itemCount);
						long pageCount = parseResult.getAllPageCount();
						documentContext.setAllPageCount(documentContext.getAllPageCount() - pageCount);
						pdfMaker.backupErrorFile();
						initMaker();
						log.error(e.getMessage(), e);
					} catch (Exception e) {
						khzhParseResult.getRecord().setRunStatus(HdqsConstants.RUNNING_STATUS_FAILURE);
						log.error(e.getMessage(), e);
					} finally {
						documentContext.setAllItemCount(0);
						documentContext.setAllPageCount(0);
						documentContext.setFinishedZhNum(0);
					}

					break;
				case QK3:// 客户号查询
					KehhaoParseResult kehhaoParseResult = (KehhaoParseResult) parseResult;
					try {
						doKehhaoQuery(kehhaoParseResult);
					} catch (org.xhtmlrenderer.util.XRRuntimeException e) {
						kehhaoParseResult.getRecord().setRunStatus(HdqsConstants.RUNNING_STATUS_FAILURE);
						if (isPrintExcel())
							excelMaker.close();
						long itemCount = parseResult.getAllItemCount();
						documentContext.setAllItemCount(documentContext.getAllItemCount() - itemCount);
						long pageCount = parseResult.getAllPageCount();
						documentContext.setAllPageCount(documentContext.getAllPageCount() - pageCount);
						pdfMaker.backupErrorFile();
						initMaker();
						log.error(e.getMessage(), e);
					} catch (Exception e) {
						kehhaoParseResult.getRecord().setRunStatus(HdqsConstants.RUNNING_STATUS_FAILURE);
						log.error(e.getMessage(), e);
					} finally {
						documentContext.setAllItemCount(0);
						documentContext.setAllPageCount(0);
						documentContext.setFinishedZhNum(0);
					}

					break;
				case QK4:// 证件类型查询
					IdentityParseResult idParseResult = (IdentityParseResult) parseResult;

					try {
						doIdentityQuery(idParseResult);
					} catch (org.xhtmlrenderer.util.XRRuntimeException e) {
						idParseResult.getRecord().setRunStatus(HdqsConstants.RUNNING_STATUS_FAILURE);
						if (isPrintExcel())
							excelMaker.close();
						long itemCount = parseResult.getAllItemCount();
						documentContext.setAllItemCount(documentContext.getAllItemCount() - itemCount);
						long pageCount = parseResult.getAllPageCount();
						documentContext.setAllPageCount(documentContext.getAllPageCount() - pageCount);
						pdfMaker.backupErrorFile();
						initMaker();
						log.error(e.getMessage(), e);
					} catch (Exception e) {
						idParseResult.getRecord().setRunStatus(HdqsConstants.RUNNING_STATUS_FAILURE);
						log.error(e.getMessage(), e);
					} finally {
						documentContext.setAllItemCount(0);
						documentContext.setAllPageCount(0);
						documentContext.setFinishedZhNum(0);
					}
					break;
				default:
					throw new UnsupportedOperationException("Unsupported chaxzl " + type);
				}
			}
			// }
			log.info("开始结束HTML输出");
			if (HdqsConstants.SHFOBZ_DOWNLOAD.equals(queryCondition.getShfobz())) {
				if (isPrintExcel()) {
					excelMaker.close();
				}
				pdfMaker.endHtml();
				pdfMaker.makePdf();
				for (PybjyEO record : poList) {
					record.setPdfFile(documentContext.getCurrentPDF().getFileName());
					record.setXlsFile(documentContext.getCurrentXLS().getFileName());
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw e;
		}

		// 输出不连续的输入条件及其账号
		flushBrokedReord();
		log.info("一共查询出的页数：" + documentContext.getAllPageCount());
		if (HdqsConstants.SHFOBZ_DOWNLOAD.equals(queryCondition.getShfobz())) {
			return documentContext.getCurrentPDF().getFileName();
		} else {
			return txtMaker.getTxtFile();
		}

	}

	/**
	 * 按照账号进行具体的交易明细查询(Asyn0770)
	 * 
	 * @param value
	 * @param outputDir2
	 * @throws Exception
	 */
	public String doZhangHQuery(ZhangHParseResult zhanghParseResult, boolean isSyn) throws Exception {
		try {
			log.info("开始账号查询  : " + zhanghParseResult.getZHANGH());
			asyZhanghQuery(zhanghParseResult, zhanghParseResult);
		} catch (BalanceBrokedException ex) {// 处理余额不连续，输出文档
			String zhangh = zhanghParseResult.getRecord().getZhangh();
			String errorMsg = ex.getMessage();
			BrokedReordUtils.cacheBrokedReord(documentContext, zhangh, errorMsg);
			// 构建显示不连续的页面文件
			Page<AbstractQueryResult> page = new Page<AbstractQueryResult>();
			PageHeader header = new PageHeader();
			header.setZhangh(zhangh);
			header.setChbz(zhanghParseResult.getCHUIBZ());
			header.setHbdh(zhanghParseResult.getHUOBDH());
			header.setKehhao(zhanghParseResult.getKehhao());
			header.setCxqsrq(zhanghParseResult.getRecord().getStartDate());
			header.setCxzzrq(zhanghParseResult.getRecord().getEndDate());
			header.setKhzh(zhanghParseResult.getKehuzh());
			header.setKhzwm(zhanghParseResult.getKehzwm());
			header.setTips(errorMsg);
			header.setPrintDate(System.currentTimeMillis());
			page.setHeader(header);
			zhanghParseResult.setQuery(false);
			zhanghParseResult.getRecord().setRunStatus(HdqsConstants.RUNNING_STATUS_UNSEQUENCE);
			page.setParseResult(zhanghParseResult);
			page.setTip(errorMsg);
			dropBrokedDocument(new DocKey(zhanghParseResult.getRecord().getId(), zhangh), page);
		} finally {
			// 只有在0770的时候才关闭文档
			log.debug("异步标志位：" + isSyn);
			if (isSyn) {
				zhanghParseResult.getRecord().setItemCount(zhanghParseResult.getItemCount());
				if (HdqsConstants.SHFOBZ_DOWNLOAD.equals(zhanghParseResult.getRecord().getShfobz())) {
					pdfMaker.endHtml();
					pdfMaker.makePdf();
					if (isPrintExcel()) {
						excelMaker.close();
					}
				} else {
					txtMaker.closeDocument();
				}
			}
		}
		/**
		 * updated by chengqi 20140607 for the blx file output
		 */
		if (documentContext.getBrokedCache().get(zhanghParseResult.getZHANGH()+zhanghParseResult.getRecord().getId())!=null) {
			zhanghParseResult.getRecord().setRunStatus(HdqsConstants.RUNNING_STATUS_UNSEQUENCE);

		}
		log.info("账号 " + zhanghParseResult.getZHANGH() + " 查询 结束");
		// 返回查询结果文档中最后一个文档的名称
		if (HdqsConstants.SHFOBZ_DOWNLOAD.equals(zhanghParseResult.getRecord().getShfobz())) {
			return documentContext.getCurrentPDF().getFileName();
		} else {
			return txtMaker.getTxtFile();
		}
	}

	/**
	 * 异步查询解析出的客户账号0771、0772
	 * 
	 * @param khzhParseResult
	 * @throws Exception
	 */
	protected String doKhzhQuery(KehzhParserResult khzhParseResult, boolean isSyn) throws Exception {
		try {
			log.info("开始客户账号查询  : " + khzhParseResult.getKehuzh());
			for (Entry<SkyPair<String, EnumQueryKind>, ZhangHParseResult> zhanghInfo : khzhParseResult.getZhanghParseResult().entrySet()) {
				try {
					asyZhanghQuery(khzhParseResult, zhanghInfo.getValue());
				} catch (BalanceBrokedException e) {
					String parentRecord = khzhParseResult.getRecord().getKehuzh();
					String errorMsg = e.getMessage();
					BrokedReordUtils.cacheBrokedReord(documentContext, parentRecord, errorMsg);
					// 构建显示不连续的页面文件
					Page<AbstractQueryResult> page = new Page<AbstractQueryResult>();
					PageHeader header = new PageHeader();
					header.setZhangh(zhanghInfo.getValue().getZHANGH());
					header.setKhzh(khzhParseResult.getKehuzh());
					header.setChbz(zhanghInfo.getValue().getCHUIBZ());
					header.setHbdh(zhanghInfo.getValue().getHUOBDH());
					header.setKehhao(khzhParseResult.getKehhao());
					header.setKhzwm(khzhParseResult.getKehzwm());
					header.setFkjgHao(khzhParseResult.getZHYYNG());
					header.setFkjgName(khzhParseResult.getJIGOMC());
					header.setCxqsrq(khzhParseResult.getRecord().getStartDate());
					header.setCxzzrq(khzhParseResult.getRecord().getEndDate());
					header.setTips(errorMsg);
					header.setPrintDate(System.currentTimeMillis());
					khzhParseResult.setQuery(false);
					khzhParseResult.getRecord().setRunStatus(HdqsConstants.RUNNING_STATUS_UNSEQUENCE);
					page.setParseResult(zhanghInfo.getValue());
					page.setHeader(header);
					page.setTip(errorMsg);
					dropBrokedDocument(new DocKey(zhanghInfo.getValue().getRecord().getId(), zhanghInfo.getValue().getZHANGH()), page);
				}
			}
		} finally {
			// 只有在0771 ,0772,0781的时候才关闭文档
			if (isSyn) {
				khzhParseResult.getRecord().setItemCount(khzhParseResult.getItemCount());
				// 0771和0772的时候只有一个客户账号，因此查询完一个客户账号后可以关闭生成的文档
				if (HdqsConstants.SHFOBZ_DOWNLOAD.equals(khzhParseResult.getRecord().getShfobz())) {
					pdfMaker.endHtml();
					pdfMaker.makePdf();
					if (isPrintExcel()) {
						excelMaker.close();
					}
				} else {
					txtMaker.closeDocument();
				}
			}
		}
		if (documentContext.getBrokedCache().get(khzhParseResult.getKehuzh()+khzhParseResult.getRecord().getId())!=null) {
			khzhParseResult.getRecord().setRunStatus(HdqsConstants.RUNNING_STATUS_UNSEQUENCE);

		}
		log.info("客户账号 :" + khzhParseResult.getKehuzh() + "查询 结束");
		// documentContext.setFinishedZhNum(0);
		// documentContext.setAllItemCount(0);
		// documentContext.setAllPageCount(0);
		if (HdqsConstants.SHFOBZ_DOWNLOAD.equals(khzhParseResult.getRecord().getShfobz())) {
			return documentContext.getCurrentPDF().getFileName();
		} else {
			return txtMaker.getTxtFile();
		}
	}

	/**
	 * 查询每个客户号的交易明细
	 * 
	 * @param kehhaoParseResult
	 * @throws Exception
	 */
	public void doKehhaoQuery(KehhaoParseResult kehhaoParseResult) throws Exception {
		log.info("开始客户号查询: " + kehhaoParseResult.getKehhao());
		for (Entry<SkyPair<String, EnumQueryKind>, KehzhParserResult> khzhInfo : kehhaoParseResult.getKhzhParseResult().entrySet()) {
			for (Entry<SkyPair<String, EnumQueryKind>, ZhangHParseResult> zhanghInfo : khzhInfo.getValue().getZhanghParseResult().entrySet()) {
				try {
					asyZhanghQuery(kehhaoParseResult, zhanghInfo.getValue());
				} catch (BalanceBrokedException e) {// 处理余额不连续的情况
					String parentRecord = kehhaoParseResult.getRecord().getKehuhao();
					String errorMsg = e.getMessage();
					BrokedReordUtils.cacheBrokedReord(documentContext, parentRecord, errorMsg);
					// 构建显示不连续的页面文件
					Page<AbstractQueryResult> page = new Page<AbstractQueryResult>();
					PageHeader header = new PageHeader();
					header.setZhangh(zhanghInfo.getValue().getZHANGH());
					header.setKhzh(zhanghInfo.getValue().getKehuzh());
					header.setChbz(zhanghInfo.getValue().getCHUIBZ());
					header.setHbdh(zhanghInfo.getValue().getHUOBDH());
					header.setKehhao(zhanghInfo.getValue().getKehhao());
					header.setKhzwm(zhanghInfo.getValue().getKehzwm());
					header.setFkjgHao(zhanghInfo.getValue().getZHYYNG());
					header.setFkjgName(zhanghInfo.getValue().getJIGOMC());
					header.setCxqsrq(zhanghInfo.getValue().getRecord().getStartDate());
					header.setCxzzrq(zhanghInfo.getValue().getRecord().getEndDate());
					header.setTips(errorMsg);
					header.setPrintDate(System.currentTimeMillis());
					kehhaoParseResult.setQuery(false);
					kehhaoParseResult.getRecord().setRunStatus(HdqsConstants.RUNNING_STATUS_UNSEQUENCE);
					page.setParseResult(zhanghInfo.getValue());
					page.setHeader(header);
					page.setTip(errorMsg);
					dropBrokedDocument(new DocKey(zhanghInfo.getValue().getRecord().getId(), zhanghInfo.getValue().getZHANGH()), page);
				}
			}
		}
		/**
		 * updated by chengqi 20140607
		 */
		if (documentContext.getBrokedCache().get(kehhaoParseResult.getKehhao()+kehhaoParseResult.getRecord().getId())!=null) {
			kehhaoParseResult.getRecord().setRunStatus(HdqsConstants.RUNNING_STATUS_UNSEQUENCE);

		}
		log.info("客户号查询: " + kehhaoParseResult.getKehhao() + "查询结束.");
	}

	/**
	 * 查询输入证件时的交易明细
	 * 
	 * @throws Exception
	 */
	public void doIdentityQuery(IdentityParseResult idParseResult) throws Exception {
		log.info("开始证件查询 ,证件种类:" + idParseResult.getIdentityType().getDisplay() + ",证件号码:" + idParseResult.getRecord().getZhjhao());
		for (Entry<String, KehhaoParseResult> identityInfo : idParseResult.getIdentityParseResult().entrySet()) {

			// 如果标记为不能查询的时候，说明该查询条件存在两种情况（查无此号、此号不存在明细）
			if (!identityInfo.getValue().isQuery()) {
				Page<AbstractQueryResult> page = new Page<AbstractQueryResult>();
				PageHeader header = new PageHeader();
				header.setTips(identityInfo.getValue().getTips());
				header.setPrintDate(System.currentTimeMillis());
				header.setKhzwm(identityInfo.getValue().getKehzwm());
				page.setHeader(header);
				page.setParseResult(identityInfo.getValue());

				/* ================ 记录页数================================= */
				documentContext.setAllPageCount(documentContext.getAllPageCount() + 1);
				// 当前账号所属Record的明细
				int oldCommNum = identityInfo.getValue().getRecord().getCommNum();
				identityInfo.getValue().getRecord().setCommNum(oldCommNum + 1);
				identityInfo.getValue().setAllPageCount(identityInfo.getValue().getAllPageCount() + 1);
				/* ==============================记录页数 ====结束============ */
				try {
					flushPageToDocument(identityInfo.getValue().getRecord(), page);
				} catch (NeedNewDocumentException e) {
					createNewDocument(identityInfo.getValue().getRecord());
				}

				documentContext.setAllItemCount(0);
				documentContext.setAllPageCount(0);

				continue;
			}
			for (Entry<SkyPair<String, EnumQueryKind>, KehzhParserResult> khzhInfo : identityInfo.getValue().getKhzhParseResult().entrySet()) {
				for (Entry<SkyPair<String, EnumQueryKind>, ZhangHParseResult> zhanghInfo : khzhInfo.getValue().getZhanghParseResult().entrySet()) {
					try {

						asyZhanghQuery(idParseResult, zhanghInfo.getValue());
					} catch (BalanceBrokedException e) {// 处理余额不连续的情况
						String parentRecord = idParseResult.getRecord().getZhjhao();
						String errorMsg = e.getMessage();
						BrokedReordUtils.cacheBrokedReord(documentContext, parentRecord, errorMsg);
						// 构建显示不连续的页面文件
						Page<AbstractQueryResult> page = new Page<AbstractQueryResult>();
						PageHeader header = new PageHeader();
						header.setZhangh(zhanghInfo.getValue().getZHANGH());
						header.setChbz(zhanghInfo.getValue().getCHUIBZ());
						header.setHbdh(zhanghInfo.getValue().getHUOBDH());
						header.setKhzh(zhanghInfo.getValue().getKehuzh());
						header.setKehhao(zhanghInfo.getValue().getKehhao());
						header.setKhzwm(zhanghInfo.getValue().getKehzwm());
						header.setFkjgHao(zhanghInfo.getValue().getZHYYNG());
						header.setFkjgName(zhanghInfo.getValue().getJIGOMC());
						header.setCxqsrq(zhanghInfo.getValue().getRecord().getStartDate());
						header.setCxzzrq(zhanghInfo.getValue().getRecord().getEndDate());
						header.setTips(errorMsg);
						header.setPrintDate(System.currentTimeMillis());
						idParseResult.setQuery(false);
						page.setParseResult(zhanghInfo.getValue());
						idParseResult.getRecord().setRunStatus(HdqsConstants.RUNNING_STATUS_UNSEQUENCE);
						page.setHeader(header);
						page.setTip(errorMsg);
						dropBrokedDocument(new DocKey(zhanghInfo.getValue().getRecord().getId(), zhanghInfo.getValue().getZHANGH()), page);
					}
				}
			}
		}
		/**
		 * updated by chengqi 20140607 for the blx file output
		 */
		if (documentContext.getBrokedCache().get(idParseResult.getRecord().getZhjhao()+idParseResult.getRecord().getId())!=null) {
			idParseResult.getRecord().setRunStatus(HdqsConstants.RUNNING_STATUS_UNSEQUENCE);

		}
		log.info("证件查询 ,证件种类:" + idParseResult.getIdentityType().getDisplay() + ",证件号码:" + idParseResult.getRecord().getZhjhao() + " 查询结束.");
	}

	/**
	 * 将不连续的输入条件输出到文件中 parentRecord:当前账号的输入条件 errorMsg:
	 * 当前账号的不连续信息(包含具体账号和不连续的时间点) new String("不连续账户清单".getBytes(), "UTF-8")
	 * 
	 * @throws IOException
	 */
	protected void flushBrokedReord() {
		// 如果brokedCache中有不连续的账号记录，则输出
		String outputDir = null;
		if (HdqsConstants.SHFOBZ_DOWNLOAD.equals(queryCondition.getShfobz())) {
			outputDir = documentContext.getCurrentPDF().getFileName();
		} else {
			outputDir = txtMaker.getTxtFile();
		}
		outputDir = outputDir.substring(0, outputDir.lastIndexOf(File.separator));

		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(outputDir + File.separator + queryCondition.getSlbhao() + "blx.txt"));
			for (Entry<String, List<String>> brokedItem : documentContext.getBrokedCache().entrySet()) {
				for (String brokedInfo : brokedItem.getValue()) {
					bw.write(brokedInfo);
					bw.write("\r\n");
				}
			}
		} catch (IOException e) {
			log.error("输出不连续记录失败!", e);
		} finally {
			if (bw != null)
				try {
					bw.close();
				} catch (IOException e) {
				}
		}
	}

	// /**
	// * 将不连续的账号缓存到 brokedCache
	// *
	// * @param key
	// * @param errorMsg
	// */
	// public void cacheBrokedReord(QueryDocumentContext documentContext,String
	// key, String errorMsg) {
	// if (documentContext.getBrokedCache().get(key) == null) {
	// List<String> list = new ArrayList<String>();
	// list.add(errorMsg);
	// documentContext.getBrokedCache().put(key, list);
	// } else {
	// documentContext.getBrokedCache().get(key).add(errorMsg);
	// }
	// }

	/**
	 * 删除不连续账号已经输出的明细页面
	 * 
	 * @param zhangh
	 * @throws Exception
	 */
	private void dropBrokedDocument(DocKey zhangh, Page brokedPage) throws Exception {
		PybjyEO record = brokedPage.getParseResult().getRecord();
		// 记录不连续状态
		record.setRunStatus(HdqsConstants.RUNNING_STATUS_UNSEQUENCE);
		// 首先关闭当前的文档
		if (HdqsConstants.SHFOBZ_DOWNLOAD.equals(record.getShfobz())) {
			// 首先关闭老的pdf
			if (isPrintExcel()) {
				excelMaker.write(brokedPage);
				excelMaker.close();
			}
			try {
				pdfMaker.write(brokedPage);
			} catch (NeedNewDocumentException e) {
				// 不做任何处理，此时输出只为了记录当前页的信息
			}
			pdfMaker.endHtml();
			pdfMaker.makePdf();
		}
		List<PdfDocument> pdfList = documentContext.getPdfDocByZhangh(zhangh);
		BrokedDocumentEditor pdfEditor = new BrokedDocumentEditor();
		pdfEditor.deletePdfBrokedRecords(pdfList, zhangh);
		documentContext.removePdf(zhangh);
		if (isPrintExcel()) {
			List<XlsDocument> xlsList = documentContext.getXlsDocByZhangh(zhangh);
			BrokedDocumentEditor xlsEditor = new BrokedDocumentEditor();
			xlsEditor.deleteXlsBrokedRecords(xlsList, zhangh);
			documentContext.removeXls(zhangh);
		}
		// 删除完余额不连续的明细后，重新打开新的文档生成器
		initMaker();

		// 将不连续的信息打印到文档中
		try {
			record.setCommNum(record.getCommNum() + 1);
			flushPageToDocument(record, brokedPage);
			// HtmlTemplator.clear(brokedPage);
		} catch (NeedNewDocumentException e) {
			HtmlTemplator.clear(brokedPage);
			createNewDocument(record);
		}
	}

	/**
	 * 生成新的文档
	 * 
	 * @param zhanghParseResult
	 * @throws Exception
	 */
	protected void createNewDocument(PybjyEO record) throws Exception {
		log.info("Begin to create new document : " + (documentContext.getPdfFileCount() + 1));
		// 首先关闭老的pdf
		if (isPrintExcel()) {
			excelMaker.close();
		}
		pdfMaker.endHtml();
		pdfMaker.makePdf();

		initMaker();
	}

	/**
	 * 对每个账号进行异步查询，直接将查询结果输出到文件当中 通过
	 * doZhangHQuery,doKhzhQuery,doKehhaoQuery,doIdentityQuery
	 * ,queryParseResult发起查询
	 * 
	 * @param zhanghParseResult
	 *            每个账号的解析结果
	 * @throws DocumentException
	 * @throws BalanceBrokedException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws NoSuchMethodException
	 * @throws ClassNotFoundException
	 * @throws NeedNewDocumentException
	 * @throws Exception
	 */
	private void asyZhanghQuery(ParseResult parseResult, ZhangHParseResult zhanghParseResult) throws Exception {
		// 记录当前在正在查询的账号到DocumentInfo中,并且记录当前账号查询出的文档的个数以及每个文件中该账号明细的页码
		documentContext.setCurrentZhangh(zhanghParseResult.getZHANGH());
		recordCurrentZhangh(parseResult.getRecord().getId());

		// 开始处理当前账号
		if (!zhanghParseResult.isQuery()) {
			Page<AbstractQueryResult> page = new Page<AbstractQueryResult>();
			PageHeader header = new PageHeader();
			header.setPrintDate(System.currentTimeMillis());
			header.setTips(zhanghParseResult.getTips());
			page.setHeader(header);
			page.setParseResult(zhanghParseResult);
			long allPageCount = documentContext.getAllPageCount();
			documentContext.setAllPageCount(allPageCount + 1);
			zhanghParseResult.setAllPageCount(allPageCount + 1);
			// 当前账号所属Record的明细
			int oldCommNum = zhanghParseResult.getRecord().getCommNum();
			zhanghParseResult.getRecord().setCommNum(oldCommNum + 1);

			int queriedZhNum = documentContext.getFinishedZhNum();
			documentContext.setFinishedZhNum(queriedZhNum + 1);
			zhanghParseResult.setQueriedZhNum(queriedZhNum + 1);
			try {
				flushPageToDocument(zhanghParseResult.getRecord(), page);
			} catch (NeedNewDocumentException e) {
				createNewDocument(zhanghParseResult.getRecord());
			}
			return;
		}
		ItemQueryProcessor queryExecuter = zhanghParseResult.getQueryExectuer();

		log.debug("Begion to query account : " + zhanghParseResult.getZHANGH() + ", ItemQueryProcessor is : " + queryExecuter);

		int pageLine = 0;
		if (HdqsConstants.SHFOBZ_PRINT.equals(queryCondition.getShfobz())) {
			pageLine = QueryConfUtils.getActiveConfig().getInt(QueryConstants.PRINT_TXT_LINE_PER_PAGE, 20);
		} else {
			pageLine = QueryConfUtils.getActiveConfig().getInt(QueryConstants.HANDLE_LINE_PER_PAGE, 35);
		}
		zhanghParseResult.setPageLineNumber(pageLine);
		Page<? extends AbstractQueryResult> page = null;
		try {
			while ((page = queryExecuter.nextPage(zhanghParseResult, documentContext)) != null) {
				try {
					// 输出明细
					flushPageToDocument(zhanghParseResult.getRecord(), page);
				} catch (NeedNewDocumentException e) {
					createNewDocument(zhanghParseResult.getRecord());
				}
			}
		} catch (BalanceBrokedException ex) {// 余额不连续的情况
			log.warn(ex.getMessage());
			// 由于出现余额不连续因素，所以此时记录账号已经查询完成
			int finishedZhNum = documentContext.getFinishedZhNum();
			zhanghParseResult.setQueriedZhNum(finishedZhNum + 1);
			zhanghParseResult.setFinished(QueryConstants.ZHANGH_QUERY_FINISHED);
			long allItemCount = documentContext.getAllItemCount();
			zhanghParseResult.setAllItemCount(allItemCount - zhanghParseResult.getItemCount());
			documentContext.setAllItemCount(allItemCount - zhanghParseResult.getItemCount());
			int zhanghTotalPage = zhanghParseResult.getPageTotal();
			PybjyEO rec = zhanghParseResult.getRecord();
			rec.setCommNum(rec.getCommNum() - zhanghTotalPage);
			rec.setItemCount(rec.getItemCount() - zhanghParseResult.getItemCount());
			zhanghParseResult.setItemCount(0);
			zhanghParseResult.setPageTotal(1);
			documentContext.setFinishedZhNum(finishedZhNum + 1);
			long allPageCount = documentContext.getAllPageCount();
			documentContext.setAllPageCount(allPageCount - zhanghTotalPage + 1);
			zhanghParseResult.setAllPageCount(allPageCount - zhanghTotalPage + 1);
			throw ex;
		}
		/**
		 * updated by chengqi 20140607 for the blx file output
		 */
		if(documentContext.getBrokedCache().get(zhanghParseResult.getZHANGH()+zhanghParseResult.getRecord().getId())!=null){
			zhanghParseResult.getRecord().setRunStatus(HdqsConstants.RUNNING_STATUS_UNSEQUENCE);
		}
	}

	/**
	 * 记录当前账号的文档信息
	 */
	private void recordCurrentZhangh(Long id) {
		if (HdqsConstants.SHFOBZ_PRINT.equals(queryCondition.getShfobz())) {
			documentContext.recordCurrentZhanghTxt(id);
		} else {
			documentContext.recordCurrentZhanghPdf(id);
			if (isPrintExcel()) {
				documentContext.recordCurrentZhanghXls(id);
			}
		}
	}

	/**
	 * 将数据刷到文件中
	 * 
	 * @param record
	 * @param page
	 * @throws Exception
	 */
	protected void flushPageToDocument(PybjyEO record, Page page) throws Exception {
		try {
			if (HdqsConstants.SHFOBZ_DOWNLOAD.equals(record.getShfobz())) {
				if (isPrintExcel()) {
					excelMaker.write(page);
				}
				pdfMaker.write(page);
			} else {
				txtMaker.write(page);
			}
		} finally {
			HtmlTemplator.clear(page);
		}
	}

	@Override
	public String getTaskName() {
		return getQueryCondition().getSlbhao();
	}

	public QueryDocumentContext getDocumentInfo() {
		return documentContext;
	}

	public PybjyEO getQueryCondition() {
		return queryCondition;
	}

	public Log getLog() {
		return log;
	}

	public void setLog(Log log) {
		this.log = log;
	}

}