package com.ceb.bank.query.output;

import java.util.List;

import org.apache.log4j.Logger;

import com.ceb.bank.constants.FieldConstants;
import com.ceb.bank.context.Output0770Context;
import com.ceb.bank.context.ZhanghContext;
import com.ceb.bank.item.Item0770;
import com.ceb.bank.query.impl.Handle0770Query;
import com.ceb.bank.query.output.pdf.PdfGenerator0770;
import com.ceb.bank.query.output.txt.TxtGenerator0770;
import com.ceb.bank.query.scanner.AdgmxPdfScanner;
import com.ceb.bank.query.scanner.AdgmxTxtScanner;
import com.ceb.bank.utils.HdqsCommonUtils;
import com.ceb.hdqs.config.ConfigLoader;
import com.ceb.hdqs.config.RegisterTable;
import com.ceb.hdqs.entity.PybjyEO;

/**
 * 0770同步查询{@link Handle0770Query}打印导出
 * 
 * @author user
 * 
 */
public class Output0770 extends AbstractOutput {
	private static final Logger log = Logger.getLogger(Output0770.class);
	private Handle0770Query handleQuery = new Handle0770Query();

	public Output0770() {
	}

	protected String handlePrint(PybjyEO record) throws Exception {
		Output0770Context ctx = handleOutputContext(record);
		int pageSize = ConfigLoader.getInstance().getInt(RegisterTable.HDQS_QUERY_PRINT_TXT_LINE_PER_PAGE, 35);
		String filePath = callTxtMaker(record, ctx, pageSize);
		return filePath;
	}

	private String callTxtMaker(final PybjyEO record, final Output0770Context ctx, int pageSize) throws Exception {
		log.info("开始账号" + record.getZhangh() + "查询...");
		final TxtGenerator0770 generator = new TxtGenerator0770();
		if (!ctx.isQueryExist()) {
			ctx.setPageNum(1);
			ctx.setCurrentPageNum(1);
			generator.writeNoItems(record, ctx, null);
			return generator.getAbsolutePath();
		}
		AdgmxTxtScanner queryMxScanner = new AdgmxTxtScanner(ctx.getHeader().getTableName()) {
			@Override
			public void outputPage(PybjyEO record, List<Item0770> itemList) throws Exception {
				if (ctx.getPageNum() == 1) {// 第一页时创建文件
					generator.init(record);
				}
				generator.write(record, ctx, itemList, ctx.getHeader());
			}

			@Override
			public void closePrePage() throws Exception {
				generator.appendPageTail(ctx);
			}

			@Override
			public void close(PybjyEO record) throws Exception {
				generator.appendZhanghTail(ctx);
				generator.close();
			}
		};
		queryMxScanner.query(record, ctx, pageSize);
		log.info("账号" + record.getZhangh() + "查询结束.");
		return generator.getAbsolutePath();
	}

	protected String handleDownload(PybjyEO record) throws Exception {
		Output0770Context ctx = handleOutputContext(record);
		int pageSize = ConfigLoader.getInstance().getInt(RegisterTable.HDQS_QUERY_HANDLE_LINE_PER_PAGE, 35);
		String filePath = callPdfMaker(record, ctx, pageSize);
		return filePath;
	}

	private String callPdfMaker(PybjyEO record, final Output0770Context ctx, final int pageSize) throws Exception {
		log.info("开始账号" + record.getZhangh() + "查询...");
		final PdfGenerator0770 generator = new PdfGenerator0770();
		ZhanghContext zhanghCtx = ctx.getHeader();
		if (zhanghCtx == null) {
			ctx.setZhanghNum(0);
			ctx.setNoItems();
			generator.writeNoItems(record, ctx, 1, null);
			log.info("账号" + record.getZhangh() + "查询结束.");
			return ctx.getLastPdfFile().getAbsolutePath();
		}

		ctx.setZhanghNum(1);
		zhanghCtx.setFirstQuery(true);
		handleZhangh(generator, record, ctx, zhanghCtx, pageSize);
		return ctx.getLastPdfFile().getAbsolutePath();
	}

	private void handleZhangh(final PdfGenerator0770 generator, PybjyEO record, final Output0770Context ctx, ZhanghContext zhanghCtx,
			final int pageSize) throws Exception {
		AdgmxPdfScanner scanner = new AdgmxPdfScanner(zhanghCtx.getTableName());
		List<Item0770> itemList = scanner.query(record, zhanghCtx, pageSize);

		if (itemList == null || itemList.isEmpty()) {
			ctx.setNoItems();
			generator.writeNoItems(record, ctx, ctx.getPageNum(), zhanghCtx);
			log.info("账号" + record.getZhangh() + "无记录,总页数:" + ctx.getPageNum());
			return;
		}
		if (itemList.size() <= pageSize) {// 查询结束,表示多取得了一条但是还比每页小，说明已经处理完毕。
			ctx.setCurrentQueryFinish(true);
			ctx.setQueryFinish(true);
			ctx.setCurrentPageNum(1);
			ctx.setPageNum(1);
			ctx.setCurrentTotal(itemList.size());
			ctx.setTotal(itemList.size());
			generator.createPdf(record, ctx, itemList, 1, zhanghCtx);
			generator.close(record, ctx);
			generator.splitPdf(record, ctx);
			log.info("账号" + record.getZhangh() + "生成文件完成,总记录数:" + ctx.getTotal() + ",总页数:" + ctx.getPageNum());
			return;
		}
		// 多取了一条
		zhanghCtx.setLastRowkey(itemList.get(pageSize).getRowkey());
		ctx.setCurrentPageNum(1);
		ctx.setPageNum(1);
		ctx.setCurrentTotal(pageSize);
		generator.createPdf(record, ctx, itemList.subList(0, pageSize), ctx.getPageNum(), zhanghCtx);

		while ((itemList = scanner.query(record, zhanghCtx, pageSize)).size() > pageSize) {// 多取了一条
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
		ctx.setQueryFinish(true);
		ctx.setCurrentPageNum(ctx.getCurrentPageNum() + 1);
		ctx.setPageNum(ctx.getPageNum() + 1);
		ctx.setCurrentTotal(ctx.getCurrentTotal() + itemList.size());
		ctx.setTotal(ctx.getCurrentTotal());
		if (itemList.size() > FieldConstants.LAST_PAGE_SIZE) {
			generator.appendPdf(record, ctx, itemList.subList(FieldConstants.LAST_PAGE_SIZE, itemList.size()), ctx.getPageNum(), zhanghCtx);
		} else {
			generator.appendPdf(record, ctx, itemList, ctx.getPageNum(), zhanghCtx);
		}
		generator.close(record, ctx);
		generator.splitPdf(record, ctx);

		log.info("账号" + record.getZhangh() + "生成文件完成,总记录数:" + ctx.getTotal() + ",总页数:" + ctx.getPageNum());
	}

	private Output0770Context handleOutputContext(PybjyEO record) throws Exception {
		ZhanghContext zhanghCtx = handleQuery.parseCondition(record);
		Output0770Context ctx = new Output0770Context();
		ctx.setCxqsrq(record.getStartDate());
		ctx.setCxzzrq(record.getEndDate());
		ctx.setZhangh(record.getZhangh());
		ctx.setPrintDate(System.currentTimeMillis());
		ctx.setJio1gy(record.getJio1gy());
		if (zhanghCtx == null) {
			ctx.setQueryExist(false);
			ctx.getTips().add(HdqsCommonUtils.getNoRecordTips(record));
		} else {
			ctx.setHeader(zhanghCtx);
		}
		return ctx;
	}
}