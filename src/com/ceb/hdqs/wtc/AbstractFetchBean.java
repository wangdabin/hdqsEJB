package com.ceb.hdqs.wtc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import weblogic.wtc.jatmi.Reply;
import weblogic.wtc.jatmi.TPServiceInformation;
import weblogic.wtc.jatmi.TypedCArray;

import com.ceb.hdqs.config.ConfigLoader;
import com.ceb.hdqs.config.RegisterTable;
import com.ceb.hdqs.constants.ExchangeCode;
import com.ceb.hdqs.constants.HdqsConstants;
import com.ceb.hdqs.entity.GycsEO;
import com.ceb.hdqs.entity.PybjyEO;
import com.ceb.hdqs.entity.PjyjlEO;
import com.ceb.hdqs.po.Authorize;
import com.ceb.hdqs.po.DlFileRecord;
import com.ceb.hdqs.po.DownLoadFilePO;
import com.ceb.hdqs.service.YbjyService;
import com.ceb.hdqs.service.cache.C0779RowkeyItem;
import com.ceb.hdqs.service.cache.Handle0779Cache;
import com.ceb.hdqs.sop.SopIntf;
import com.ceb.hdqs.utils.DateTimeFormatUtils;
import com.ceb.hdqs.utils.FileCopyUtils;
import com.ceb.hdqs.utils.HdqsUtils;
import com.ceb.hdqs.utils.JNDIUtils;
import com.ceb.hdqs.utils.TimerUtils;
import com.ceb.hdqs.wtc.form.Handle0779Form;
import com.lowagie.text.pdf.PdfReader;

/**
 * 0776,0779父类
 * 
 * @author user
 * 
 */
public abstract class AbstractFetchBean extends AbstractQueryBean {
	private static final long KB_BYTES = 1024;
	public static final int BUFFER_SIZE = 4096;
	private static final String PDF_NAME_SUFFIX = ".pdf";
	private static final String XSL_NAME_SUFFIX = ".xlsx";

	public AbstractFetchBean() {
		super();
	}

	public Reply service(TPServiceInformation tpServiceInfor) throws Exception {
		SopIntf recvSop = getSopFromWTC(tpServiceInfor);
		SopIntf sendSop = new SopIntf();
		setSystemHeadForSop(recvSop, sendSop);
		printRecvHeaderPkt(recvSop);

		Handle0779Form form = buildForm(recvSop);
		PjyjlEO record = buildExchangeRecord(recvSop, form);
		printRecvFormPkt(form, record);
		try {
			validateField(form);
		} catch (HdqsWtcException e) {
			getLogger().info(getExCode() + " sop invalid: " + e.getMessage());
			if (isFrontAuthorize(record)) {
				return getAuthorizeBuffer(tpServiceInfor, sendSop, Authorize.NO_AUTH_MSG);
			}
			return getExceptionBuffer(tpServiceInfor, sendSop, e.getMessage());
		}
		getLogger().info("Start query:" + getExCode());
		TimerUtils timer = new TimerUtils();
		timer.start();

		String key = buildKey(form, record);
		// 根据判断,如果是0776,那么增加交易权限的检查,并且判断交易柜员是否在0780权限表中存在并处于维护期内
		if (ExchangeCode.EXCHANGECODE_0776.equals(getExCode()) && !getSqcsService().authorize(record.getJio1gy())) {
			GycsEO gycsEO = getGycsService().findByGuiydh(record.getJio1gy());
			if (gycsEO == null) {
				getLogger().info("操作柜员在柜员参数表中不存在.");
				return getExceptionBuffer(tpServiceInfor, sendSop, "操作柜员在柜员参数表中不存在");
			}
			// 由于对应的record的受理编号已经改变，所以使用对应的form对象中的原始提交的受理编号。
			String slgy = getGylsService().getJio1gyFromSlbhao(form.getSlbhao());
			GycsEO slGycsEO = getGycsService().findByGuiydh(slgy);
			if (slGycsEO == null) {
				getLogger().info("生成受理编码的柜员参数表中不存在.");
				return getExceptionBuffer(tpServiceInfor, sendSop, "生成受理编码的柜员参数表中不存在");
			}
			if (!getJgcsService().isParentQuery(gycsEO.getYngyjg(), slGycsEO.getYngyjg())) {
				getLogger().info("只有本机构和上级机构可以进行此操作!");
				return getExceptionBuffer(tpServiceInfor, sendSop, "只有本机构和上级机构可以进行此操作!");
			}
		}
		// 交易检查权限结束
		// 如果查询柜员不在0780维护的参数表内存在，就无权做此交易。并且判断交易柜员是否在0780权限表中存在并处于维护期内
		if (ExchangeCode.EXCHANGECODE_0779.equals(getExCode()) && !getSqcsService().authorize(record.getJio1gy())) {
			if (isFrontAuthorize(record)) {
				return getAuthorizeBuffer(tpServiceInfor, sendSop, Authorize.NO_AUTH_MSG);
			}
			return getExceptionBuffer(tpServiceInfor, sendSop, "无权进行此交易");
		}
		Authorize authorize = new Authorize();
		PjyjlEO sRecord = saveExchangeRecord(record);
		try {
			C0779RowkeyItem item = null;
			Handle0779Cache queryCache = Handle0779Cache.getInstance();

			if (queryCache.containsKey(key)) {
				getLogger().debug("Load data from cache,key " + key);
				item = queryCache.get(key);
				item.setLastModifiedT(System.currentTimeMillis());
				queryCache.put(key, item);
				if (form.getCzfshi().equals(Handle0779Form.CZFSHI_DOWNLOAD)) {
					authorize.reset();
				} else if (item.getAuthorize() != null) {
					authorize = isCacheNeedAuth(sRecord, form, item.getAuthorize());
					if (getSqcsService().authorize(record.getJio1gy())) {//如果交易柜员在0780权限表中存在并处于维护期内,那么不授权
						authorize.reset();
					}
					if (isFrontAuthorize(sRecord) || authorize.isNeedAuth()) {
						getLogger().info(authorize.getPrintMsg());
						updateExchangeEndTime(sRecord);
						return getAuthorizeBuffer(tpServiceInfor, recvSop, authorize.getPrintMsg());
					}
				}
			} else {
				YbjyService service = (YbjyService) JNDIUtils.lookup(YbjyService.class);
				long unCount = service.getUnCompleteCountsByHandleNo(form.getSlbhao());
				if (unCount > 0) {
					authorize = isUnCompleteNeedAuth(sRecord, form);
					if (getSqcsService().authorize(record.getJio1gy())) {//如果交易柜员在0780权限表中存在并处于维护期内,那么不授权
						authorize.reset();
					}
					if (isFrontAuthorize(sRecord) || authorize.isNeedAuth()) {
						getLogger().info(authorize.getPrintMsg());
						updateExchangeEndTime(sRecord);
						return getAuthorizeBuffer(tpServiceInfor, recvSop, authorize.getPrintMsg());
					}
					timer.stop();
					getLogger().info("未处理完成,请耐心等待, Cost time(ms)=" + timer.getExecutionTime());
					updateExchangeEndTime(sRecord);
					return getExceptionBuffer(tpServiceInfor, sendSop, "未处理完成,请耐心等待");
				}
				item = initCache(form, service);
				authorize = initCacheAuth(sRecord, form, item);
				item.setAuthorize(authorize);
				item.setLastModifiedT(System.currentTimeMillis());
				queryCache.put(key, item);
				if (form.getCzfshi().equals(Handle0779Form.CZFSHI_DOWNLOAD)) {// 图前无法处理此种授权，则不进行授权验证
					authorize.reset();
				}
				if (StringUtils.isNotBlank(sRecord.getShoqgy())) {
					authorize.reset();
				}
				if (getSqcsService().authorize(record.getJio1gy())) {//如果交易柜员在0780权限表中存在并处于维护期内,那么不授权
					authorize.reset();
				}
				if (isFrontAuthorize(sRecord) || authorize.isNeedAuth()) {
					getLogger().info(authorize.getPrintMsg());
					updateExchangeEndTime(sRecord);
					return getAuthorizeBuffer(tpServiceInfor, recvSop, authorize.getPrintMsg());
				}
			}
			if (HdqsUtils.isWorkingTime(Calendar.getInstance())) {
				long sizeThreshold = Long.parseLong(ConfigLoader.getInstance().getProperty(RegisterTable.QUERY_FILE_KB_THRESHOLD));
				if (item.getTotalSize() > sizeThreshold) {
					String errMsg = "超过文件定义阀值,请在非业务时间(" + ConfigLoader.getInstance().getProperty(RegisterTable.FREE_TIME_SPAN) + ")里获取";
					timer.stop();
					getLogger().info(errMsg);
					updateExchangeEndTime(sRecord);
					return getExceptionBuffer(tpServiceInfor, sendSop, errMsg);
				}
			}
			if (form.getCzfshi().equals(Handle0779Form.CZFSHI_DOWNLOAD)) {
				getTPServiceInfoAsResult(tpServiceInfor, sendSop, item, form, sRecord);
			} else {
				getTPServiceInfoAsResult(tpServiceInfor, sendSop, key, item, form, sRecord);
			}
		} catch (Exception e) {
			timer.stop();
			getLogger().error(e.getMessage(), e);
			updateExchangeRunErrStatus(sRecord);
			if (isFrontAuthorize(sRecord)) {
				return getAuthorizeBuffer(tpServiceInfor, sendSop, Authorize.NO_AUTH_MSG);
			}
			return getExceptionBuffer(tpServiceInfor, sendSop, "后台查询异常");
		}
		timer.stop();
		getLogger().info(getExCode() + " query complete, Cost time(ms)=" + timer.getExecutionTime());
		updateExchangeEndTime(sRecord);
		return tpServiceInfor;
	}

	private C0779RowkeyItem initCache(Handle0779Form form, YbjyService service) throws Exception {
		C0779RowkeyItem item = new C0779RowkeyItem();
		List<PybjyEO> list = service.findByHandleNo(form.getSlbhao());
		service.updateNotifyFlag(form.getSlbhao());

		item.setAscList(list);
		int succNum = 0;
		int failNum = 0;
		long itemCount = 0L;
		long pageCount = 0L;
		Authorize authorize = new Authorize();
		authorize.setGuiyjb(0);
		authorize.setBeizxx("后台不授权");
		if (list.size() > 0) {
			for (PybjyEO tmpObj : list) {
				if (tmpObj == null) {
					continue;
				}
				if (tmpObj.getRunStatus().equals(HdqsConstants.RUNNING_STATUS_FAILURE)) {
					failNum++;
				} else {
					succNum++;
				}
				itemCount += tmpObj.getItemCount();
				pageCount += tmpObj.getCommNum();
				if (tmpObj.getGuiyjb() > authorize.getGuiyjb()) {
					authorize.setGuiyjb(tmpObj.getGuiyjb());
					authorize.setBeizxx(tmpObj.getBeizxx());
				}
			}
			handleResultFile(form, item, list.get(0));
			item.getDlFilePo().setItemCount(itemCount);
			item.getDlFilePo().setPageCount(pageCount);
			item.setAuthorize(authorize);
		}
		item.setTotalNum(list.size());
		item.setSuccNum(succNum);
		item.setFailNum(failNum);
		return item;
	}

	private void handleResultFile(Handle0779Form form, C0779RowkeyItem item, PybjyEO record) {
		long totalSize = 0;
		String resultFile = "";
		File pdfFile = null;
		DownLoadFilePO po = new DownLoadFilePO();
		if (HdqsUtils.isNotBlank(record.getPdfFile())) {
			pdfFile = new File(record.getPdfFile());
		}
		File xlsFile = null;
		if (HdqsUtils.isNotBlank(record.getXlsFile())) {
			xlsFile = new File(record.getXlsFile());
		}
		String queryPath = ConfigLoader.getInstance().getProperty(RegisterTable.FTP_DOWNLOAD_PATH);
		File fileDir = new File(queryPath);
		fileDir.mkdirs();

		if (form.getFileType().equals(HdqsConstants.FILE_KIND_PDF) && pdfFile != null) {
			if (pdfFile.exists() && pdfFile.isFile()) {
				String fileName = pdfFile.getName();
				// int fileCount =
				// Integer.valueOf(fileName.substring(fileName.lastIndexOf("_")
				// + 1,fileName.lastIndexOf(".")));
				int fileCount = Integer.valueOf(fileName.substring(fileName.lastIndexOf("_") + 1, fileName.lastIndexOf(".")));
				String fileNamePreFix = fileName.substring(0, fileName.lastIndexOf("_") + 1);
				for (int i = 1; i <= fileCount; i++) {
					pdfFile = new File(pdfFile.getParentFile().getAbsolutePath(), fileNamePreFix + i + PDF_NAME_SUFFIX);
					File destFile = new File(queryPath, fileNamePreFix + i + PDF_NAME_SUFFIX);
					copyPdfFile(pdfFile, destFile);
					totalSize += pdfFile.length() / KB_BYTES;
				}
				resultFile = fileName;
			}
		} else if (form.getFileType().equals(HdqsConstants.FILE_KIND_XLS) && xlsFile != null) {
			if (xlsFile.exists() && xlsFile.isFile()) {
				po.setIsExistExl(true);
				String fileName = xlsFile.getName();
				int fileCount = Integer.valueOf(fileName.substring(fileName.lastIndexOf("_") + 1, fileName.lastIndexOf(".")));
				String fileNamePreFix = fileName.substring(0, fileName.lastIndexOf("_") + 1);
				for (int i = 1; i <= fileCount; i++) {
					xlsFile = new File(xlsFile.getParentFile().getAbsolutePath(), fileNamePreFix + i + XSL_NAME_SUFFIX);
					File destFile = new File(queryPath, fileNamePreFix + i + XSL_NAME_SUFFIX);
					copyXlsFile(xlsFile, destFile);
					totalSize += xlsFile.length() / KB_BYTES;
				}
				resultFile = fileName;

			}
		} else {
			if (pdfFile != null && pdfFile.exists() && pdfFile.isFile()) {
				String fileName = pdfFile.getName();
				int fileCount = Integer.valueOf(fileName.substring(fileName.lastIndexOf("_") + 1, fileName.lastIndexOf(".")));
				String fileNamePreFix = fileName.substring(0, fileName.lastIndexOf("_") + 1);
				for (int i = 1; i <= fileCount; i++) {
					pdfFile = new File(pdfFile.getParentFile().getAbsolutePath(), fileNamePreFix + i + PDF_NAME_SUFFIX);
					File destFile = new File(queryPath, fileNamePreFix + i + PDF_NAME_SUFFIX);
					copyPdfFile(pdfFile, destFile);
					totalSize += pdfFile.length() / KB_BYTES;
				}
				resultFile = fileName;
			}
			if (xlsFile != null && xlsFile.exists() && xlsFile.isFile()) {
				po.setIsExistExl(true);
				String fileName = xlsFile.getName();
				int fileCount = Integer.valueOf(fileName.substring(fileName.lastIndexOf("_") + 1, fileName.lastIndexOf(".")));
				String fileNamePreFix = fileName.substring(0, fileName.lastIndexOf("_") + 1);
				for (int i = 1; i <= fileCount; i++) {
					xlsFile = new File(xlsFile.getParentFile().getAbsolutePath(), fileNamePreFix + i + XSL_NAME_SUFFIX);
					File destFile = new File(queryPath, fileNamePreFix + i + XSL_NAME_SUFFIX);
					copyXlsFile(xlsFile, destFile);
					totalSize += xlsFile.length() / KB_BYTES;
				}
				if (HdqsUtils.isBlank(resultFile)) {
					resultFile = xlsFile.getName();
				} else {
					resultFile = resultFile + "|" + xlsFile.getName();
				}
			}
		}

		po.setFilePath(record.getPdfFile());
		item.setDlFilePo(po);
		item.setTotalSize(totalSize);
		item.setResultFile(resultFile);
	}

	private void copyPdfFile(File srcFile, File destFile) {
		if (destFile.exists()) {
			getLogger().debug("Destination file " + destFile.getName() + " already  exists ,copy task will exit!");
			return;
		}
		getLogger().debug("Begin to copy pdf  file " + srcFile.getName());
		InputStream in = null;
		OutputStream out = null;
		try {
			in = new FileInputStream(srcFile);
			out = new FileOutputStream(destFile);
			FileCopyUtils.copy(in, out);
			getLogger().debug("Copy pdf  file " + srcFile.getName() + " successed!");
		} catch (Exception e) {
			getLogger().error("Copy pdf  file " + srcFile.getName() + " failed!");
			getLogger().error(e.getMessage(), e);
		}
		// FileChannel in = null;
		// FileChannel out = null;
		// try {
		// in = new FileInputStream(srcFile).getChannel();
		// out = new FileOutputStream(destFile).getChannel();
		// out.transferFrom(in, 0, in.size());
		// } catch (Exception e) {
		// getLogger().error(e.getMessage(), e);
		// } finally {
		// if (out != null) {
		// try {
		// out.close();
		// } catch (IOException e) {
		// }
		// }
		// if (in != null) {
		// try {
		// in.close();
		// } catch (IOException e) {
		// }
		// }
		// }
	}

	private void copyXlsFile(File srcFile, File destFile) {
		if (destFile.exists()) {
			return;
		}
		InputStream in = null;
		OutputStream out = null;
		try {
			in = new FileInputStream(srcFile);
			out = new FileOutputStream(destFile);
			FileCopyUtils.copy(in, out);
		} catch (Exception e) {
			getLogger().error(e.getMessage(), e);
		}
	}

	private Handle0779Form buildForm(SopIntf recvSop) {
		Handle0779Form form = new Handle0779Form();
		form.setCzfshi(recvSop.getStr(this.getFileRecv(), "CZFSHI").trim());
		form.setSlbhao(recvSop.getStr(this.getFileRecv(), "SLBHAO").trim());
		String fileType = recvSop.getStr(this.getFileRecv(), "FILETYPE");
		if (HdqsUtils.isBlank(fileType)) {
			form.setFileType(HdqsConstants.FILE_KIND_PDF);
		} else {
			form.setFileType(fileType);
		}
		String startNum = recvSop.getStr(this.getFileRecv(), "QISHBS").trim();
		if (HdqsUtils.isNotBlank(startNum)) {
			form.setQishbs(Integer.parseInt(startNum));
		}
		String queryNum = recvSop.getStr(this.getFileRecv(), "CXUNBS").trim();
		if (HdqsUtils.isNotBlank(queryNum)) {
			form.setCxunbs(Integer.parseInt(queryNum));
		}

		return form;
	}

	/**
	 * 根据查询结果获取
	 * 
	 */
	private Reply getTPServiceInfoAsResult(TPServiceInformation tpServiceInfor, SopIntf sendSop, String key, C0779RowkeyItem item,
			Handle0779Form form, PjyjlEO record) {
		List<PybjyEO> list = item.getAscList();
		if (list == null || list.isEmpty()) {
			getLogger().info("存在[0]条记录");
			return getExceptionBuffer(tpServiceInfor, sendSop, "存在[0]条记录");
		}
		Handle0779Cache queryCache = Handle0779Cache.getInstance();
		if (!queryCache.containsKey(key)) {
			item.setLastModifiedT(System.currentTimeMillis());
			queryCache.put(key, item);
		}
		int totalCount = list.size();
		if (form.getQishbs() > totalCount) {
			getLogger().info("起始笔数不能大于总记录数");
			return getExceptionBuffer(tpServiceInfor, sendSop, "起始笔数不能大于总记录数");
		} else {
			int formIndex = form.getQishbs() - 1;
			int toIndex = formIndex + form.getCxunbs();
			if (toIndex > totalCount)
				toIndex = totalCount;
			List<PybjyEO> subList = list.subList(formIndex, toIndex);
			String[] inmap = new String[] { getFileSent() };
			byte[] sendData = new byte[200 + 148 + 569 + subList.size() * 357];
			setNormalMessageForSop(sendSop, item, subList, form, record);
			TypedCArray replyBuffer = getTypedCArray(sendData, inmap, sendSop);
			tpServiceInfor.setReplyBuffer(replyBuffer);
			return tpServiceInfor;
		}
	}

	/**
	 * 根据查询结果获取
	 * 
	 */
	private Reply getTPServiceInfoAsResult(TPServiceInformation tpServiceInfor, SopIntf sendSop, C0779RowkeyItem item, Handle0779Form form,
			PjyjlEO record) {
		DownLoadFilePO downLoadFilePO = item.getDlFilePo();
		getLogger().info("获取查询文件");
		String fileName = downLoadFilePO.getFilePath().substring(downLoadFilePO.getFilePath().lastIndexOf("/") + 1);
		String fileNamePre = fileName.substring(0, fileName.lastIndexOf("_"));
		int fileCount = Integer.valueOf(fileName.substring(fileName.lastIndexOf("_") + 1, fileName.lastIndexOf(".")));
		downLoadFilePO.setFileCount(fileCount);
		if (form.getQishbs() > downLoadFilePO.getFileCount()) {
			getLogger().info("起始笔数不能大于总记录数");
			return getExceptionBuffer(tpServiceInfor, sendSop, "起始笔数不能大于总记录数");
		}
		List<DlFileRecord> resultList = getResultList(form, downLoadFilePO, fileNamePre);
		String[] inmap = new String[] { this.getFileDownLoad() };
		// 设置输出报文头信息
		setNormalMessageForSop(sendSop, form, record, downLoadFilePO, resultList);
		byte[] sendData = new byte[200 + 148 + 569 + resultList.size() * 357];
		TypedCArray replyBuffer = getTypedCArray(sendData, inmap, sendSop);
		tpServiceInfor.setReplyBuffer(replyBuffer);
		getLogger().info("返回查询结果");
		return tpServiceInfor;

	}

	protected List<DlFileRecord> getResultList(Handle0779Form form, DownLoadFilePO downLoadFilePO, String fileNamePre) {
		int startIndex = form.getQishbs();
		int lastIndex = Math.min(downLoadFilePO.getFileCount(), form.getQishbs() + form.getCxunbs() - 1);
		DlFileRecord dlFileRecord = null;
		List<DlFileRecord> resultList = new ArrayList<DlFileRecord>();
		for (int i = startIndex; i <= lastIndex; i++) {
			dlFileRecord = new DlFileRecord();
			dlFileRecord.setFileSerial(String.valueOf(i));
			if (!form.getFileType().equals("2")) {
				dlFileRecord.setPdfName(fileNamePre + "_" + i + ".pdf");
				String pdfPath = downLoadFilePO.getFilePath().substring(0, downLoadFilePO.getFilePath().lastIndexOf(File.separator));
				pdfPath = pdfPath + File.separator + fileNamePre + "_" + i + ".pdf";
				String pdfPageSize = getNumberOfPages(pdfPath);
				dlFileRecord.setPdfPageSize(pdfPageSize);
			}
			if (!form.getFileType().equals("1") && downLoadFilePO.getIsExistExl())
				dlFileRecord.setXlsName(fileNamePre + "_" + i + ".xlsx");
			resultList.add(dlFileRecord);

		}
		return resultList;
	}

	protected void setNormalMessageForSop(SopIntf sendSop, Handle0779Form form, PjyjlEO record, DownLoadFilePO downLoadFilePO,
			List<DlFileRecord> resultList) {
		sendSop.put(null, "JIAOYM", this.getExCode());
		long currentT = System.currentTimeMillis();
		sendSop.put(null, "JIOYRQ", DateTimeFormatUtils.formatDate(currentT, "yyyymmdd"));
		sendSop.put(null, "JIOYSJ", Integer.valueOf(DateTimeFormatUtils.formatDate(currentT, "HHmmss")));
		sendSop.put(null, "GUIYLS", record.getGuiyls());
		Short FDCWJYXH = 0;
		sendSop.put(null, "CWJYXH", FDCWJYXH);
		sendSop.put(null, SopIntf.TPU_RETCODE, SopIntf.SOP_SUCC);

		// 设置列表显示title信息
		sendSop.put(this.getFileDownLoad(), "SLBHAO", form.getSlbhao());
		sendSop.put(this.getFileDownLoad(), "RDNUMR", downLoadFilePO.getItemCount().toString());
		sendSop.put(this.getFileDownLoad(), "PGNUMR", String.valueOf((downLoadFilePO.getPageCount())));
		sendSop.put(this.getFileDownLoad(), "FLNUMR", "" + downLoadFilePO.getFileCount());
		// 列表内容显示
		Map<String, List<String>> map = convertListResultToMap(resultList);
		sendSop.put(this.getFileDownLoad(), this.getFileDLTable(), "WJSUHA", getStrArrayFromList(map.get("0")));
		sendSop.put(this.getFileDownLoad(), this.getFileDLTable(), "PDFNAME", getStrArrayFromList(map.get("1")));
		sendSop.put(this.getFileDownLoad(), this.getFileDLTable(), "PAGESIZE", getStrArrayFromList(map.get("2")));
		if (ExchangeCode.EXCHANGECODE_0779.equals(record.getJiaoym()))
			sendSop.put(this.getFileDownLoad(), this.getFileDLTable(), "XSLNAME", getStrArrayFromList(map.get("3")));
	}

	private void setNormalMessageForSop(SopIntf sendSop, C0779RowkeyItem item, List<PybjyEO> list, Handle0779Form form, PjyjlEO record) {
		PybjyEO item0 = list.get(0);
		sendSop.put(null, "JIAOYM", this.getExCode());
		long currentT = System.currentTimeMillis();
		sendSop.put(null, "JIOYRQ", DateTimeFormatUtils.formatDate(currentT, "yyyymmdd"));
		sendSop.put(null, "JIOYSJ", Integer.valueOf(DateTimeFormatUtils.formatDate(currentT, "HHmmss")));
		sendSop.put(null, "GUIYLS", record.getGuiyls());
		Short FDCWJYXH = 0;
		sendSop.put(null, "CWJYXH", FDCWJYXH);
		sendSop.put(null, SopIntf.TPU_RETCODE, SopIntf.SOP_SUCC);

		sendSop.put(this.getFileSent(), "SLBHAO", item0.getSlbhao());
		sendSop.put(this.getFileSent(), "JIOYRQ", item0.getJioyrq());
		sendSop.put(this.getFileSent(), "YNGYJG", item0.getYngyjg());
		sendSop.put(this.getFileSent(), "TOTALNUM", "" + item.getTotalNum());
		sendSop.put(this.getFileSent(), "SUCCNUM", "" + item.getSuccNum());
		sendSop.put(this.getFileSent(), "FAILNUM", "" + item.getFailNum());
		setSopRecvFile(sendSop, item0.getRecvFile());
		sendSop.put(this.getFileSent(), "PDFPATH", item.getResultFile());

		Map<String, List<String>> map = convertResultToMap(list);
		sendSop.put(this.getFileSent(), this.getFileTable(), "CHAXLX", getStrArrayFromList(map.get("0")));
		sendSop.put(this.getFileSent(), this.getFileTable(), "ZHANGH", getStrArrayFromList(map.get("1")));
		sendSop.put(this.getFileSent(), this.getFileTable(), "KEHUZH", getStrArrayFromList(map.get("2")));
		sendSop.put(this.getFileSent(), this.getFileTable(), "KEHUHAO", getStrArrayFromList(map.get("3")));
		sendSop.put(this.getFileSent(), this.getFileTable(), "ZHJNZL", getStrArrayFromList(map.get("4")));
		sendSop.put(this.getFileSent(), this.getFileTable(), "ZHJHAO", getStrArrayFromList(map.get("5")));
		sendSop.put(this.getFileSent(), this.getFileTable(), "ZHHUXZ", getStrArrayFromList(map.get("6")));
		sendSop.put(this.getFileSent(), this.getFileTable(), "KHZWM", getStrArrayFromList(map.get("7")));
		sendSop.put(this.getFileSent(), this.getFileTable(), "QISHRQ", getStrArrayFromList(map.get("8")));
		sendSop.put(this.getFileSent(), this.getFileTable(), "ZZHIRQ", getStrArrayFromList(map.get("9")));
		sendSop.put(this.getFileSent(), this.getFileTable(), "STATUS", getStrArrayFromList(map.get("10")));
	}

	/**
	 * 将遍历结果对象转换map对象
	 * 
	 * @param iter
	 * @return
	 */
	private Map<String, List<String>> convertResultToMap(List<PybjyEO> list) {
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		for (int i = 0; i < this.getFieldCount(); i++) {
			map.put("" + i, new ArrayList<String>());
		}
		for (PybjyEO item : list) {
			map.get("0").add(item.getChaxlx() == null ? "" : item.getChaxlx());
			map.get("1").add(item.getZhangh() == null ? "" : item.getZhangh());
			map.get("2").add(item.getKehuzh() == null ? "" : item.getKehuzh());
			map.get("3").add(item.getKehuhao() == null ? "" : item.getKehuhao());
			map.get("4").add(item.getZhjnzl() == null ? "" : item.getZhjnzl());
			map.get("5").add(item.getZhjhao() == null ? "" : item.getZhjhao());
			map.get("6").add(item.getZhhuxz() == null ? "" : item.getZhhuxz());
			map.get("7").add(item.getKhzwm() == null ? "" : item.getKhzwm());
			map.get("8").add(item.getStartDate() == null ? "" : item.getStartDate());
			map.get("9").add(item.getEndDate() == null ? "" : item.getEndDate());
			map.get("10").add(item.getRunStatus());
		}
		return map;
	}

	private Map<String, List<String>> convertListResultToMap(List<DlFileRecord> list) {
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		for (int i = 0; i < 4; i++) {
			map.put("" + i, new ArrayList<String>());
		}
		for (DlFileRecord item : list) {
			map.get("0").add(item.getFileSerial() == null ? "" : item.getFileSerial().trim());
			map.get("1").add(item.getPdfName() == null ? "" : item.getPdfName().trim());
			map.get("2").add(item.getPdfPageSize() == null ? "" : item.getPdfPageSize().trim());
			map.get("3").add(item.getXlsName() == null ? "" : item.getXlsName().trim());

		}
		return map;
	}

	private void setSopRecvFile(SopIntf sendSop, String recvFileStr) {
		if (HdqsUtils.isBlank(recvFileStr)) {
			sendSop.put(this.getFileSent(), "FILEPATH", "");
		} else {
			File recvFile = new File(recvFileStr);
			if (recvFile.exists() && recvFile.isFile()) {
				sendSop.put(this.getFileSent(), "FILEPATH", recvFile.getName());
			} else {
				sendSop.put(this.getFileSent(), "FILEPATH", "");
			}
		}
	}

	protected abstract String buildKey(Handle0779Form form, PjyjlEO record);

	protected abstract void validateField(Handle0779Form form) throws HdqsWtcException;

	protected Authorize isCacheNeedAuth(PjyjlEO record, Handle0779Form form, Authorize cachedAuth) {
		Authorize authorize = new Authorize();
		if (StringUtils.isNotBlank(record.getShoqgy())) {
			return authorize;
		}
		authorize.setNeedAuth(cachedAuth.isNeedAuth());
		authorize.setGuiyjb(cachedAuth.getGuiyjb());
		authorize.setBeizxx(cachedAuth.getBeizxx());
		return authorize;
	}

	/**
	 * 1、 非本人查询获取查询结果，3级授权。 2、 非客户查询获取查询结果，3级授权。 3、 在后台查询数据过程中，如果存在授权，也需要带出。
	 * 
	 * @param sop
	 * @param item
	 * @return
	 */
	protected Authorize initCacheAuth(PjyjlEO record, Handle0779Form form, C0779RowkeyItem item) {
		Authorize authorize = new Authorize();
		businessAuthorize(record, form, authorize);
		Authorize dbAuthorize = item.getAuthorize();
		if (dbAuthorize != null && dbAuthorize.getGuiyjb() > authorize.getGuiyjb()) {
			authorize.setNeedAuth(true);
			authorize.setGuiyjb(dbAuthorize.getGuiyjb());
			authorize.setBeizxx(dbAuthorize.getBeizxx());
		}
		return authorize;
	}

	protected Authorize isUnCompleteNeedAuth(PjyjlEO record, Handle0779Form form) {
		Authorize authorize = new Authorize();
		if (StringUtils.isNotBlank(record.getShoqgy())) {
			return authorize;
		}
		businessAuthorize(record, form, authorize);
		return authorize;
	}

	private void businessAuthorize(PjyjlEO record, Handle0779Form form, Authorize authorize) {
		Authorize sAuthorize = noSelfAuthorize(record, form);
		if (sAuthorize.isNeedAuth() && sAuthorize.getGuiyjb() > authorize.getGuiyjb()) {
			authorize.setNeedAuth(true);
			authorize.setGuiyjb(sAuthorize.getGuiyjb());
			authorize.setBeizxx(sAuthorize.getBeizxx());
		}
	}

	private Authorize noSelfAuthorize(PjyjlEO record, Handle0779Form form) {
		Authorize authorize = new Authorize();
		if (!getGylsService().getJio1gyFromSlbhao(form.getSlbhao()).equals(record.getJio1gy())) {
			authorize.setNeedAuth(true);
			authorize.setGuiyjb(3);
			authorize.setBeizxx("非本人查询获取查询结果");
		}
		return authorize;
	}

	// public static String getNumberOfPages(D) {
	// PdfReader document=null;
	// try {
	//
	// document = new PdfReader(new FileInputStream(new File(filePath)));
	// } catch (FileNotFoundException e) {
	//
	// e.printStackTrace();
	// } catch (IOException e) {
	//
	// }
	// return String.valueOf(document.getNumberOfPages());
	// }
	public String getNumberOfPages(String filePath) {
		int count = 0;
		PdfReader reader = null;
		InputStream inputStream = null;
		try {
			File file = new File(filePath);
			inputStream = new FileInputStream(file);
			reader = new PdfReader(inputStream);
			count = reader.getNumberOfPages();
		} catch (FileNotFoundException e) {
			getLogger().error(e.getMessage(), e);
		} catch (IOException e) {
			getLogger().error(e.getMessage(), e);
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (Exception e) {
					getLogger().error(e.getMessage(), e);
				}
			}
			if (reader != null) {
				reader.close();
			}
		}
		return String.valueOf(count);
	}
}