package com.ceb.bank.query.output;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.ceb.bank.constants.FieldConstants;
import com.ceb.bank.context.KehuzhContext;
import com.ceb.bank.context.Output0781Context;
import com.ceb.bank.context.ZhanghContext;
import com.ceb.bank.item.Item0781;
import com.ceb.bank.query.impl.Handle0781Query;
import com.ceb.bank.query.output.pdf.PdfGenerator0781;
import com.ceb.bank.query.output.txt.TxtGenerator0781;
import com.ceb.bank.query.scanner.Adgmx0781PdfScanner;
import com.ceb.bank.query.scanner.Adgmx0781TxtScanner;
import com.ceb.bank.utils.HdqsCommonUtils;
import com.ceb.hdqs.config.ConfigLoader;
import com.ceb.hdqs.config.RegisterTable;
import com.ceb.hdqs.entity.PybjyEO;

public class Output0781 extends AbstractOutput {
	private static final Logger log = Logger.getLogger(Output0781.class);
	private Adgmx0781PdfScanner adgmxScanner = new Adgmx0781PdfScanner();
	private Handle0781Query handleQuery = new Handle0781Query();

	public Output0781() {
	}

	@Override
	protected String handlePrint(PybjyEO record) throws Exception {
		Output0781Context ctx = handleOutputContext(record);
		int pageSize = ConfigLoader.getInstance().getInt(RegisterTable.HDQS_QUERY_PRINT_TXT_LINE_PER_PAGE, 35);
		String filePath = callTxtMaker(record, ctx, pageSize);
		return filePath;
	}

	private String callTxtMaker(final PybjyEO record, final Output0781Context ctx, int pageSize) throws Exception {
		log.info("开始单位卡" + record.getKehuzh() + "查询...");
		final TxtGenerator0781 generator = new TxtGenerator0781();
		if (!ctx.isQueryExist()) {
			ctx.setPageNum(1);
			ctx.setCurrentPageNum(1);
			generator.writeNoItems(record, ctx, null);
			return generator.getAbsolutePath();
		}
		List<ZhanghContext> zhanghList = ctx.getHeader().getList();
		if (zhanghList == null || zhanghList.isEmpty()) {
			ctx.setPageNum(1);
			ctx.setCurrentPageNum(1);
			ctx.getTips().add("存在单位卡" + record.getKehuzh() + ",不存在明细数据.");
			generator.writeNoItems(record, ctx, null);
			return generator.getAbsolutePath();
		}
		final int zhanghNum = zhanghList.size();
		ctx.setZhanghNum(zhanghNum);
		boolean lastZhangh = false;
		for (int i = 0; i < zhanghNum; i++) {
			final ZhanghContext zhanghCtx = zhanghList.get(i);
			Adgmx0781TxtScanner queryMxScanner = new Adgmx0781TxtScanner() {
				@Override
				public void outputPage(PybjyEO record, List<Item0781> itemList) throws Exception {
					if (ctx.getPageNum() == 1) {// 第一页时创建文件
						generator.init(record);
					}
					generator.write(record, ctx, itemList, zhanghCtx);
				}

				@Override
				public void closePrePage() throws Exception {
					generator.appendPageTail(ctx);
				}

				@Override
				public void closeZhangh(PybjyEO record) throws Exception {
					generator.appendZhanghTail(ctx);
				}

				@Override
				public void close(PybjyEO record) throws Exception {
					generator.appendLastTail(ctx);
					generator.close();
				}
			};
			if (i == zhanghNum - 1) {// 最后一个账号
				lastZhangh = true;
			}
			queryMxScanner.query(record, ctx, zhanghCtx, pageSize, lastZhangh);
		}

		log.info("单位卡" + record.getKehuzh() + "查询结束.");
		return generator.getAbsolutePath();
	}

	@Override
	protected String handleDownload(PybjyEO record) throws Exception {
		Output0781Context ctx = handleOutputContext(record);
		int pageSize = ConfigLoader.getInstance().getInt(RegisterTable.HDQS_QUERY_HANDLE_LINE_PER_PAGE, 35);
		String filePath = callPdfMaker(record, ctx, pageSize);
		return filePath;
	}

	private String callPdfMaker(PybjyEO record, final Output0781Context ctx, int pageSize) throws Exception {
		log.info("开始单位卡" + record.getKehuzh() + "查询...");
		final PdfGenerator0781 generator = new PdfGenerator0781();
		if (!ctx.isQueryExist()) {
			ctx.setNoItems();
			generator.writeNoItems(record, ctx, 1, null);
			return ctx.getLastPdfFile().getAbsolutePath();
		}
		List<ZhanghContext> zhanghList = ctx.getHeader().getList();
		if (zhanghList == null || zhanghList.isEmpty()) {
			ctx.setNoItems();
			ctx.getTips().add("存在单位卡" + record.getKehuzh() + ",不存在明细数据.");
			generator.writeNoItems(record, ctx, 1, null);
			return ctx.getLastPdfFile().getAbsolutePath();
		}
		final int zhanghNum = zhanghList.size();
		ctx.setZhanghNum(zhanghNum);
		boolean lastZhangh = false;
		for (int i = 0; i < zhanghNum; i++) {
			if (i == zhanghNum - 1) {
				lastZhangh = true;
			}
			ZhanghContext zhanghCtx = zhanghList.get(i);
			zhanghCtx.setFirstQuery(true);

			ctx.initCurrentZhangh();
			handleZhangh(generator, record, ctx, zhanghCtx, pageSize, lastZhangh);
		}
		log.info("单位卡" + record.getKehuzh() + "生成文件完成,总记录数:" + ctx.getTotal() + ",总页数:" + ctx.getPageNum());
		return ctx.getLastPdfFile().getAbsolutePath();
	}

	private void handleZhangh(final PdfGenerator0781 generator, PybjyEO record, Output0781Context ctx, ZhanghContext zhanghCtx,
			int pageSize, boolean lastZhangh) throws Exception {
		log.info("解析账号" + zhanghCtx.getZhangh() + ",KEMUCC属性" + zhanghCtx.getKemucc().toString());
		HdqsCommonUtils.injectTblNameFromKemucc(record, zhanghCtx);
		if (StringUtils.isBlank(zhanghCtx.getTableName())) {
			log.error("无法从KEMUCC获取TableName.");
			return;
		}
		log.info("根据KEMUCC设置TableName: " + zhanghCtx.getTableName());
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
			log.info("账号" + zhanghCtx.getZhangh() + "无记录,总页数:" + ctx.getPageNum());
			return;
		}
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
			generator.splitPdf(record, ctx);
			log.info("账号" + zhanghCtx.getZhangh() + "生成文件完成,总记录数:" + ctx.getTotal() + ",总页数:" + ctx.getPageNum());
			return;
		}
		// 多取了一条
		zhanghCtx.setLastRowkey(itemList.get(pageSize).getRowkey());
		ctx.setCurrentPageNum(1);
		ctx.setPageNum(ctx.getPageNum() + 1);
		ctx.setCurrentTotal(pageSize);
		generator.createPdf(record, ctx, itemList.subList(0, pageSize), ctx.getPageNum(), zhanghCtx);

		while ((itemList = adgmxScanner.query(record, zhanghCtx, pageSize)).size() > pageSize) {// 多取了一条
			zhanghCtx.setLastRowkey(itemList.get(pageSize).getRowkey());
			ctx.setCurrentPageNum(ctx.getCurrentPageNum() + 1);
			ctx.setPageNum(ctx.getPageNum() + 1);
			ctx.setCurrentTotal(ctx.getCurrentTotal() + pageSize);
			generator.appendPdf(record, ctx, itemList.subList(0, pageSize), ctx.getPageNum(), zhanghCtx);
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
		generator.splitPdf(record, ctx);
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