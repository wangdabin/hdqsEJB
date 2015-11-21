package com.ceb.bank.asyn;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.ceb.bank.constants.FieldConstants;
import com.ceb.bank.context.KehuzhContext;
import com.ceb.bank.context.Output0771Context;
import com.ceb.bank.context.RZhanghContext;
import com.ceb.bank.context.ZhanghContext;
import com.ceb.bank.item.Item0771;
import com.ceb.bank.query.impl.Handle0772Query;
import com.ceb.bank.query.lx.ILxVerifier;
import com.ceb.bank.query.lx.LxVerifierImpl;
import com.ceb.bank.query.output.pdf.PdfGenerator0771;
import com.ceb.bank.query.scanner.AsdmxPdfScanner;
import com.ceb.bank.utils.HdqsCommonUtils;
import com.ceb.hdqs.config.ConfigLoader;
import com.ceb.hdqs.config.RegisterTable;
import com.ceb.hdqs.constants.HdqsConstants;
import com.ceb.hdqs.entity.PybjyEO;
import com.ceb.hdqs.utils.TimerUtils;

public class HandleAsyn0772 extends AbstractHandleAsyn {
	private static final Logger log = Logger.getLogger(HandleAsyn0772.class);
	private Handle0772Query handleQuery = new Handle0772Query();
	private AsdmxPdfScanner asdmxScanner = new AsdmxPdfScanner();
	private ILxVerifier lxVerifier = new LxVerifierImpl();

	public HandleAsyn0772(List<PybjyEO> list) {
		super(list);
	}

	@Override
	public void execute() throws Exception {
		log.info("开始异步查询0772...");
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

			Output0771Context ctx = handleOutputContext(record);
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
			log.info("异步0772查询完成,Cost time(ms)=" + timer.getExecutionTime());
		}
	}

	private String callPdfMaker(PybjyEO record, final Output0771Context ctx, int pageSize, boolean isManagedGuiy) throws Exception {
		log.info("开始客户账号" + record.getKehuzh() + "查询...");
		final PdfGenerator0771 generator = new PdfGenerator0771();
		if (!ctx.isQueryExist()) {
			ctx.setNoItems();
			generator.writeNoItems(record, ctx, 1, null);
			flushBrokedReord(generator.getAbsolutePath(), record);
			return ctx.getLastPdfFile().getAbsolutePath();
		}
		List<ZhanghContext> zhanghList = ctx.getHeader().getList();
		if (zhanghList == null || zhanghList.isEmpty()) {
			ctx.setNoItems();
			ctx.getTips().add("存在客户账号" + record.getKehuzh() + ",不存在明细数据.");
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
			handleZhangh(generator, record, ctx, zhanghCtx, pageSize, lastZhangh, blxCache, isManagedGuiy);
			flushBrokedReord(generator.getAbsolutePath(), record, blxCache);
			blxCache.clear();
		}
		log.info("客户账号" + record.getKehuzh() + "生成文件完成,总记录数:" + ctx.getTotal() + ",总页数:" + ctx.getPageNum());
		return ctx.getLastPdfFile().getAbsolutePath();
	}

	private void handleZhangh(final PdfGenerator0771 generator, PybjyEO record, Output0771Context ctx, ZhanghContext zhanghCtx,
			int pageSize, boolean lastZhangh, List<String> blxCache, boolean isManagedGuiy) throws Exception {
		List<Item0771> itemList = asdmxScanner.query(record, zhanghCtx, pageSize);
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
		while ((itemList = asdmxScanner.query(record, zhanghCtx, pageSize)).size() > pageSize) {// 多取了一条
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

	private Output0771Context handleOutputContext(PybjyEO record) throws Exception {
		KehuzhContext kehuzhCtx = handleQuery.parseCondition(record);
		Output0771Context ctx = new Output0771Context();
		ctx.setCxqsrq(record.getStartDate());
		ctx.setCxzzrq(record.getEndDate());
		ctx.setPrintDate(System.currentTimeMillis());
		ctx.setJio1gy(record.getJio1gy());
		if (kehuzhCtx == null) {
			ctx.setQueryExist(false);
			ctx.getTips().add(HdqsCommonUtils.getNoRecordTips(record));
		} else {
			ctx.setHeader(kehuzhCtx);
		}
		return ctx;
	}
}