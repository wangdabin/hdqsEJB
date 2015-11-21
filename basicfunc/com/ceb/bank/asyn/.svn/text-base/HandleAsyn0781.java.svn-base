package com.ceb.bank.asyn;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.ceb.bank.constants.FieldConstants;
import com.ceb.bank.context.KehuzhContext;
import com.ceb.bank.context.Output0781Context;
import com.ceb.bank.context.RZhanghContext;
import com.ceb.bank.context.ZhanghContext;
import com.ceb.bank.item.Item0781;
import com.ceb.bank.query.impl.Handle0781Query;
import com.ceb.bank.query.lx.ILxVerifier;
import com.ceb.bank.query.lx.LxVerifierImpl;
import com.ceb.bank.query.output.pdf.PdfGenerator0781;
import com.ceb.bank.query.scanner.Adgmx0781PdfScanner;
import com.ceb.bank.utils.HdqsCommonUtils;
import com.ceb.hdqs.config.ConfigLoader;
import com.ceb.hdqs.config.RegisterTable;
import com.ceb.hdqs.constants.HdqsConstants;
import com.ceb.hdqs.entity.PybjyEO;
import com.ceb.hdqs.utils.TimerUtils;

public class HandleAsyn0781 extends AbstractHandleAsyn {
	private static final Logger log = Logger.getLogger(HandleAsyn0781.class);
	private Handle0781Query handleQuery = new Handle0781Query();
	private Adgmx0781PdfScanner adgmxScanner = new Adgmx0781PdfScanner();
	private ILxVerifier lxVerifier = new LxVerifierImpl();

	public HandleAsyn0781(List<PybjyEO> list) {
		super(list);
	}

	@Override
	public void execute() throws Exception {
		log.info("开始异步查询0781...");
		TimerUtils timer = new TimerUtils();
		timer.start();
		PybjyEO record = getList().get(0);

		boolean isManagedGuiy = false;
		if (getSqcsService().authorize(record.getJio1gy())) {
			isManagedGuiy = true;
		}
		try {
			record.setRunStatus(HdqsConstants.RUNNING_STATUS_RUNNING);
			update(record);// 修改为正在运行状态

			Output0781Context ctx = handleOutputContext(record);
			int pageSize = ConfigLoader.getInstance().getInt(RegisterTable.HDQS_QUERY_HANDLE_LINE_PER_PAGE, 35);
			String filePath = callPdfMaker(record, ctx, pageSize, isManagedGuiy);
			log.info("输出目录:" + filePath.substring(0, filePath.lastIndexOf(File.separator)));

			record.setPdfFile(filePath);
			record.setItemCount(ctx.getTotal());
			record.setCommNum(ctx.getPageNum());
			if (ctx.isCurrentQueryLX()) {
				record.setRunStatus(HdqsConstants.RUNNING_STATUS_SUCCESS);
			} else {
				record.setRunStatus(HdqsConstants.RUNNING_STATUS_UNSEQUENCE);
			}
		} catch (Exception e) {
			record.setRunStatus(HdqsConstants.RUNNING_STATUS_FAILURE);
			log.error(e.getMessage(), e);
		} finally {
			update(record);
			timer.stop();
			log.info("异步0781查询完成,Cost time(ms)=" + timer.getExecutionTime());
		}
	}

	private String callPdfMaker(PybjyEO record, final Output0781Context ctx, int pageSize, boolean isManagedGuiy) throws Exception {
		log.info("开始单位卡" + record.getKehuzh() + "查询...");
		final PdfGenerator0781 generator = new PdfGenerator0781();
		if (!ctx.isQueryExist()) {
			ctx.setNoItems();
			generator.writeNoItems(record, ctx, 1, null);
			flushBrokedReord(generator.getAbsolutePath(), record);
			return ctx.getLastPdfFile().getAbsolutePath();
		}
		List<ZhanghContext> zhanghList = ctx.getHeader().getList();
		if (zhanghList == null || zhanghList.isEmpty()) {
			ctx.setNoItems();
			ctx.getTips().add("存在单位卡" + record.getKehuzh() + ",不存在明细数据.");
			generator.writeNoItems(record, ctx, 1, null);
			flushBrokedReord(generator.getAbsolutePath(), record);
			return ctx.getLastPdfFile().getAbsolutePath();
		}

		final int zhanghNum = zhanghList.size();
		ctx.setZhanghNum(zhanghNum);
		boolean lastZhangh = false;
		List<String> blxCache = new ArrayList<String>();
		for (int i = 0; i < zhanghNum; i++) {
			if (i == zhanghNum - 1) {
				lastZhangh = true;
			}
			ZhanghContext zhanghCtx = zhanghList.get(i);
			zhanghCtx.setFirstQuery(true);

			ctx.initCurrentZhangh();
			log.info("解析账号" + zhanghCtx.getZhangh() + ",KEMUCC属性" + zhanghCtx.getKemucc().toString());
			HdqsCommonUtils.injectTblNameFromKemucc(record, zhanghCtx);
			if (StringUtils.isBlank(zhanghCtx.getTableName())) {
				log.error("无法从KEMUCC获取TableName.");
				continue;
			}
			log.info("根据KEMUCC设置TableName: " + zhanghCtx.getTableName());
			handleZhangh(generator, record, ctx, zhanghCtx, pageSize, lastZhangh, blxCache, isManagedGuiy);
			flushBrokedReord(generator.getAbsolutePath(), record, blxCache);
			blxCache.clear();
		}
		log.info("单位卡" + record.getKehuzh() + "生成文件完成,总记录数:" + ctx.getTotal() + ",总页数:" + ctx.getPageNum());
		return ctx.getLastPdfFile().getAbsolutePath();
	}

	private void handleZhangh(final PdfGenerator0781 generator, PybjyEO record, Output0781Context ctx, ZhanghContext zhanghCtx,
			int pageSize, boolean lastZhangh, List<String> blxCache, boolean isManagedGuiy) throws Exception {
		List<Item0781> itemList = adgmxScanner.query(record, zhanghCtx, pageSize);

		if (itemList == null || itemList.isEmpty()) {
			ctx.setCurrentQueryFinish(true);
			if (lastZhangh) {
				ctx.setQueryFinish(true);
			}
			ctx.setCurrentPageNum(1);
			ctx.setPageNum(ctx.getPageNum() + 1);
			ctx.setCurrentTotal(0);
			generator.writeNoItems(record, ctx, ctx.getPageNum(), zhanghCtx);
			log.info("账号" + zhanghCtx.getZhangh() + "查询结束,总记录数:" + ctx.getTotal() + ",总页数:" + ctx.getPageNum());
			ctx.setLastPageNum(ctx.getPageNum());
			return;
		}
		lxVerifier.reset();
		RZhanghContext rCtx = new RZhanghContext();
		rCtx.setZhangh(zhanghCtx.getZhangh());

		if (itemList.size() <= pageSize) {// 不满页,查询结束
			ctx.setCurrentQueryFinish(true);
			if (lastZhangh) {
				ctx.setQueryFinish(true);
			}
			ctx.setCurrentPageNum(1);
			ctx.setPageNum(ctx.getPageNum() + 1);
			ctx.setCurrentTotal(itemList.size());
			ctx.setTotal(ctx.getTotal() + itemList.size());
			generator.createPdf(record, ctx, itemList, ctx.getPageNum(), zhanghCtx);
			generator.close(record, ctx);
			blxVerify(rCtx, record, ctx, itemList, lxVerifier, blxCache);
			splitPdf(record, ctx, isManagedGuiy, generator, zhanghCtx, blxCache);
			log.info("账号" + zhanghCtx.getZhangh() + "生成文件完成,总记录数:" + ctx.getTotal() + ",总页数:" + ctx.getPageNum());
			return;
		}
		// 多取了一条
		zhanghCtx.setLastRowkey(itemList.get(pageSize).getRowkey());
		ctx.setCurrentPageNum(1);
		ctx.setPageNum(ctx.getPageNum() + 1);
		ctx.setCurrentTotal(pageSize);
		generator.createPdf(record, ctx, itemList.subList(0, pageSize), ctx.getPageNum(), zhanghCtx);
		blxVerify(rCtx, record, ctx, itemList.subList(0, pageSize), lxVerifier, blxCache);
		while ((itemList = adgmxScanner.query(record, zhanghCtx, pageSize)).size() > pageSize) {// 多取了一条
			zhanghCtx.setLastRowkey(itemList.get(pageSize).getRowkey());
			ctx.setCurrentPageNum(ctx.getCurrentPageNum() + 1);
			ctx.setPageNum(ctx.getPageNum() + 1);
			ctx.setCurrentTotal(ctx.getCurrentTotal() + pageSize);
			generator.appendPdf(record, ctx, itemList.subList(0, pageSize), ctx.getPageNum(), zhanghCtx);
			blxVerify(rCtx, record, ctx, itemList.subList(0, pageSize), lxVerifier, blxCache);
		}
		// 查询结束
		if (itemList.size() > FieldConstants.LAST_PAGE_SIZE) {// 防止最后一页溢出
			ctx.setCurrentPageNum(ctx.getCurrentPageNum() + 1);
			ctx.setPageNum(ctx.getPageNum() + 1);
			generator.appendPdf(record, ctx, itemList.subList(0, FieldConstants.LAST_PAGE_SIZE), ctx.getPageNum(), zhanghCtx);
		}
		ctx.setCurrentQueryFinish(true);
		if (lastZhangh) {
			ctx.setQueryFinish(true);
		}
		ctx.setCurrentPageNum(ctx.getCurrentPageNum() + 1);
		ctx.setPageNum(ctx.getPageNum() + 1);
		ctx.setCurrentTotal(ctx.getCurrentTotal() + itemList.size());
		ctx.setTotal(ctx.getTotal() + ctx.getCurrentTotal());
		if (itemList.size() > FieldConstants.LAST_PAGE_SIZE) {
			generator.appendPdf(record, ctx, itemList.subList(FieldConstants.LAST_PAGE_SIZE, itemList.size()), ctx.getPageNum(), zhanghCtx);
		} else {
			generator.appendPdf(record, ctx, itemList, ctx.getPageNum(), zhanghCtx);
		}
		generator.close(record, ctx);
		blxVerify(rCtx, record, ctx, itemList, lxVerifier, blxCache);
		splitPdf(record, ctx, isManagedGuiy, generator, zhanghCtx, blxCache);
		log.info("账号" + zhanghCtx.getZhangh() + "生成文件完成,总记录数:" + ctx.getTotal() + ",总页数:" + ctx.getPageNum());
	}

	private Output0781Context handleOutputContext(PybjyEO record) throws Exception {
		KehuzhContext kehuzhCtx = handleQuery.parseCondition(record);
		Output0781Context ctx = new Output0781Context();
		ctx.setCxqsrq(record.getStartDate());
		ctx.setCxzzrq(record.getEndDate());
		ctx.setPrintDate(System.currentTimeMillis());
		ctx.setJio1gy(record.getJio1gy());
		if (kehuzhCtx == null) {
			ctx.setQueryExist(false);
			ctx.getTips().add("单位" + HdqsCommonUtils.getNoRecordTips(record));
		} else {
			ctx.setHeader(kehuzhCtx);
		}
		return ctx;
	}
}