package com.ceb.bank.asyn;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.ceb.bank.authority.AuthorizeChk0777;
import com.ceb.bank.constants.ChaxzlEnum;
import com.ceb.bank.constants.FieldConstants;
import com.ceb.bank.constants.KhzhlxEnum;
import com.ceb.bank.context.KehuzhContext;
import com.ceb.bank.context.KemuccContext;
import com.ceb.bank.context.Output0777Context;
import com.ceb.bank.context.RZhanghContext;
import com.ceb.bank.context.YngyjgContext;
import com.ceb.bank.context.ZhanghContext;
import com.ceb.bank.item.Item0770;
import com.ceb.bank.query.impl.Handle0770Query;
import com.ceb.bank.query.lx.ILxVerifier;
import com.ceb.bank.query.lx.LxVerifierImpl;
import com.ceb.bank.query.output.pdf.PdfGenerator0777;
import com.ceb.bank.query.scanner.Adgmx0777PdfScanner;
import com.ceb.bank.query.scanner.Akhzh0777Scanner;
import com.ceb.bank.query.scanner.KemuccGetter;
import com.ceb.bank.query.scanner.YngyjgGetter;
import com.ceb.bank.utils.HdqsCommonUtils;
import com.ceb.hdqs.config.ConfigLoader;
import com.ceb.hdqs.config.RegisterTable;
import com.ceb.hdqs.constants.HdqsConstants;
import com.ceb.hdqs.entity.PybjyEO;
import com.ceb.hdqs.po.Authorize;
import com.ceb.hdqs.query.entity.CustomerInfo;
import com.ceb.hdqs.query.utils.QueryMethodUtils;
import com.ceb.hdqs.utils.TimerUtils;

/**
 * <PRE>
 * 第一字段：查询类型，0-客户查询，1-内部查询，2-监管查询 （必输）
 * 第二字段：查询条件种类： 1：账号 2：对公一号通号 3：客户号 （必输）
 * 第三字段：查询条件内容：对应查询条件种类内容（必输）
 * 第四字段：开始日期（必输）
 * 第五字段：结束日期（必输）
 * </PRE>
 */
public class HandleAsyn0777 extends AbstractHandleAsyn {
	private static final Logger log = Logger.getLogger(HandleAsyn0777.class);
	private AuthorizeChk0777 authorizeChk = new AuthorizeChk0777();
	private Akhzh0777Scanner khzhScanner = new Akhzh0777Scanner();
	private YngyjgGetter yngyjgGetter = new YngyjgGetter();
	private KemuccGetter kemuccGetter = new KemuccGetter();
	private Adgmx0777PdfScanner adgmxScanner = new Adgmx0777PdfScanner();
	private ILxVerifier lxVerifier = new LxVerifierImpl();

	public HandleAsyn0777(List<PybjyEO> list) {
		super(list);
	}

	@Override
	public void execute() throws Exception {
		log.info("开始异步查询0777...");
		TimerUtils timer = new TimerUtils();
		timer.start();
		PybjyEO record = getList().get(0);

		boolean isManagedGuiy = false;
		if (getSqcsService().authorize(record.getJio1gy())) {
			isManagedGuiy = true;
		}
		parseCondition(isManagedGuiy);
		timer.stop();
		log.info("异步0777查询完成,Cost time(ms)=" + timer.getExecutionTime());
	}

	private void parseCondition(boolean isManagedGuiy) throws Exception {
		int pageSize = ConfigLoader.getInstance().getInt(RegisterTable.HDQS_QUERY_HANDLE_LINE_PER_PAGE, 35);

		String filePath = null;// 存放最后一个条件的最后一个文件信息
		Output0777Context ctx = new Output0777Context();
		for (PybjyEO record : getList()) {
			ctx.initCurrentRecord();// 重置条件
			String tmpPath = parse(record, isManagedGuiy, pageSize, ctx);
			if (StringUtils.isNotBlank(tmpPath)) {
				filePath = tmpPath;
			}
		}
		for (PybjyEO record : getList()) {
			record.setPdfFile(filePath);
			update(record);
		}
		if (StringUtils.isNotBlank(filePath)) {
			log.info("输出目录:" + filePath.substring(0, filePath.lastIndexOf(File.separator)));
		}
	}

	private String parse(PybjyEO record, boolean isManagedGuiy, int pageSize, Output0777Context ctx) {
		log.info(record);
		String filePath = null;
		try {
			record.setRunStatus(HdqsConstants.RUNNING_STATUS_RUNNING);
			update(record);// 修改为正在运行状态

			filePath = handleCtx(record, ctx, pageSize, isManagedGuiy);
			Authorize authorize = authorizeChk.check(record, ctx);
			if (authorize.isNeedAuth()) {
				record.setBeizxx(authorize.getBeizxx());
				record.setGuiyjb(authorize.getGuiyjb());
			}

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
		}

		return filePath;
	}

	private String handleCtx(PybjyEO record, final Output0777Context ctx, int pageSize, boolean isManagedGuiy) throws Exception {
		String filePath = null;
		ChaxzlEnum chaxzl = ChaxzlEnum.valueOf(ChaxzlEnum.PREFIX + record.getChaxzl());
		switch (chaxzl) {
		case CHAXZL1:
			Handle0770Query handleQuery = new Handle0770Query();
			ZhanghContext zhanghCtx = handleQuery.parseCondition(record);
			ctx.setCxqsrq(record.getStartDate());
			ctx.setCxzzrq(record.getEndDate());
			ctx.setZhangh(record.getZhangh());
			ctx.setPrintDate(System.currentTimeMillis());
			ctx.setJio1gy(record.getJio1gy());
			if (zhanghCtx == null) {
				ctx.setQueryExist(false);
				ctx.getTips().add(HdqsCommonUtils.getNoRecordTips(record));
			} else {
				ctx.setZhanghCtx(zhanghCtx);
			}

			filePath = callZhanghMaker(record, ctx, pageSize, isManagedGuiy);
			break;
		case CHAXZL2:
			KehuzhContext kehuzhCtx = parseKehuzh(record);
			ctx.setCxqsrq(record.getStartDate());
			ctx.setCxzzrq(record.getEndDate());
			ctx.setPrintDate(System.currentTimeMillis());
			ctx.setJio1gy(record.getJio1gy());
			if (kehuzhCtx == null) {
				ctx.setQueryExist(false);
				ctx.getTips().add(HdqsCommonUtils.getNoRecordTips(record));
			} else {
				ctx.setKehuzhCtx(kehuzhCtx);
			}
			filePath = callKehuzhMaker(record, ctx, pageSize, isManagedGuiy);
			break;
		case CHAXZL3:
			filePath = callKehuhaoMaker(record, ctx, pageSize, isManagedGuiy);
			break;
		default:
			break;
		}
		return filePath;
	}

	private String callZhanghMaker(PybjyEO record, final Output0777Context ctx, final int pageSize, boolean isManagedGuiy) throws Exception {
		log.info("开始账号" + record.getZhangh() + "查询...");
		int count = ctx.getLastPageNum();
		final PdfGenerator0777 generator = new PdfGenerator0777();
		ZhanghContext zhanghCtx = ctx.getZhanghCtx();
		if (zhanghCtx == null) {// 没有查询上下文
			ctx.setZhanghNum(0);
			ctx.setNoItems();
			count++;
			generator.writeNoItems(record, ctx, count, null);
			flushBrokedReord(generator.getAbsolutePath(), record);
			ctx.setLastPageNum(ctx.getLastPageNum() + 1);
			log.info("账号" + record.getZhangh() + "查询结束,总记录数:" + ctx.getTotal() + ",总页数:" + ctx.getPageNum());
			return ctx.getLastPdfFile().getAbsolutePath();
		}

		ctx.setZhanghNum(1);
		zhanghCtx.setFirstQuery(true);
		List<String> blxCache = new ArrayList<String>();
		ctx.setZhangh(record.getZhangh());
		handleZhangh(generator, record, ctx, zhanghCtx, pageSize, true, blxCache, isManagedGuiy);
		flushBrokedReord(generator.getAbsolutePath(), record, blxCache);
		blxCache.clear();
		return ctx.getLastPdfFile().getAbsolutePath();
	}

	private String callKehuzhMaker(PybjyEO record, final Output0777Context ctx, int pageSize, boolean isManagedGuiy) throws Exception {
		log.info("开始客户账号" + record.getKehuzh() + "查询...");
		int count = ctx.getLastPageNum();
		final PdfGenerator0777 generator = new PdfGenerator0777();
		if (!ctx.isQueryExist()) {// 没有客户账号查询上下文
			ctx.setZhanghNum(0);
			ctx.setNoItems();
			count++;
			generator.writeNoItems(record, ctx, count, null);
			flushBrokedReord(generator.getAbsolutePath(), record);
			ctx.setLastPageNum(ctx.getLastPageNum() + 1);
			log.info("客户账号" + record.getKehuzh() + "查询结束,总记录数:" + ctx.getTotal() + ",总页数:" + ctx.getPageNum());
			return ctx.getLastPdfFile().getAbsolutePath();
		}
		List<ZhanghContext> zhanghList = ctx.getKehuzhCtx().getList();
		if (zhanghList == null || zhanghList.isEmpty()) {// 没有账号查询上下文
			ctx.setZhanghNum(0);
			ctx.setNoItems();
			count++;
			ctx.getTips().add("存在客户账号" + record.getKehuzh() + ",不存在明细数据.");
			generator.writeNoItems(record, ctx, count, null);
			flushBrokedReord(generator.getAbsolutePath(), record);
			ctx.setLastPageNum(ctx.getLastPageNum() + 1);
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
			ctx.setZhangh(zhanghCtx.getZhangh());
			handleZhangh(generator, record, ctx, zhanghCtx, pageSize, lastZhangh, blxCache, isManagedGuiy);
			flushBrokedReord(generator.getAbsolutePath(), record, blxCache);
			blxCache.clear();
		}
		log.info("客户账号" + record.getKehuzh() + "生成文件完成,总记录数:" + ctx.getTotal() + ",总页数:" + ctx.getPageNum());
		return ctx.getLastPdfFile().getAbsolutePath();
	}

	private String callKehuhaoMaker(PybjyEO record, Output0777Context ctx, int pageSize, boolean isManagedGuiy) {
		// TODO Auto-generated method stub
		return null;
	}

	private void handleZhangh(final PdfGenerator0777 generator, PybjyEO record, Output0777Context ctx, ZhanghContext zhanghCtx,
			int pageSize, boolean lastZhangh, List<String> blxCache, boolean isManagedGuiy) throws Exception {
		int count = ctx.getLastPageNum();
		List<Item0770> itemList = adgmxScanner.query(record, zhanghCtx, pageSize);

		if (itemList == null || itemList.isEmpty()) {// 没有明细
			ctx.setCurrentQueryFinish(true);
			if (lastZhangh) {
				ctx.setQueryFinish(true);
			}
			ctx.setCurrentPageNum(1);
			ctx.setPageNum(ctx.getPageNum() + 1);
			count++;
			ctx.setCurrentTotal(0);
			generator.writeNoItems(record, ctx, count, zhanghCtx);
			ctx.setLastPageNum(ctx.getLastPageNum() + 1);
			log.info("账号" + zhanghCtx.getZhangh() + "查询结束,总记录数:" + ctx.getTotal() + ",总页数:" + ctx.getPageNum());
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
			count++;
			generator.createPdf(record, ctx, itemList, count, zhanghCtx);
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
		count++;
		generator.createPdf(record, ctx, itemList.subList(0, pageSize), count, zhanghCtx);
		blxVerify(rCtx, record, ctx, itemList.subList(0, pageSize), lxVerifier, blxCache);
		while ((itemList = adgmxScanner.query(record, zhanghCtx, pageSize)).size() > pageSize) {// 多取了一条
			zhanghCtx.setLastRowkey(itemList.get(pageSize).getRowkey());
			ctx.setCurrentPageNum(ctx.getCurrentPageNum() + 1);
			ctx.setPageNum(ctx.getPageNum() + 1);
			ctx.setCurrentTotal(ctx.getCurrentTotal() + pageSize);
			count++;
			generator.appendPdf(record, ctx, itemList.subList(0, pageSize), count, zhanghCtx);
			blxVerify(rCtx, record, ctx, itemList.subList(0, pageSize), lxVerifier, blxCache);
		}
		// 查询结束
		if (itemList.size() > FieldConstants.LAST_PAGE_SIZE) {// 防止最后一页溢出
			ctx.setCurrentPageNum(ctx.getCurrentPageNum() + 1);
			ctx.setPageNum(ctx.getPageNum() + 1);
			count++;
			generator.appendPdf(record, ctx, itemList.subList(0, FieldConstants.LAST_PAGE_SIZE), count, zhanghCtx);
		}
		ctx.setCurrentQueryFinish(true);
		if (lastZhangh) {
			ctx.setQueryFinish(true);
		}
		ctx.setCurrentPageNum(ctx.getCurrentPageNum() + 1);
		ctx.setPageNum(ctx.getPageNum() + 1);
		ctx.setCurrentTotal(ctx.getCurrentTotal() + itemList.size());
		ctx.setTotal(ctx.getTotal() + ctx.getCurrentTotal());
		count++;
		if (itemList.size() > FieldConstants.LAST_PAGE_SIZE) {
			generator.appendPdf(record, ctx, itemList.subList(FieldConstants.LAST_PAGE_SIZE, itemList.size()), count, zhanghCtx);
		} else {
			generator.appendPdf(record, ctx, itemList, count, zhanghCtx);
		}
		generator.close(record, ctx);
		blxVerify(rCtx, record, ctx, itemList, lxVerifier, blxCache);
		splitPdf(record, ctx, isManagedGuiy, generator, zhanghCtx, blxCache);
		log.info("账号" + zhanghCtx.getZhangh() + "生成文件完成,总记录数:" + ctx.getTotal() + ",总页数:" + ctx.getPageNum());
	}

	private KehuzhContext parseKehuzh(PybjyEO record) throws Exception {
		log.info("Khzhlx: " + KhzhlxEnum.valueOf(KhzhlxEnum.PREFIX + record.getKhzhlx()));
		KehuzhContext context = new KehuzhContext();

		List<ZhanghContext> zhanghList = khzhScanner.query(record);
		if (zhanghList == null || zhanghList.size() == 0) {
			log.info("客户账号" + record.getKehuzh() + "在AKHZH中不存在");
			return null;
		}
		ZhanghContext zhCtx = zhanghList.get(0);
		CustomerInfo info = QueryMethodUtils.getCustomerChineseName(zhCtx.getKehhao());// 获取客户中文名

		YngyjgContext yngyjgCtx = null;
		if (HdqsConstants.KHZHLX_CARD.equals(record.getKhzhlx())) {
			log.info("Query yngyjg from VYKTD.");
			yngyjgCtx = yngyjgGetter.query(record.getKehuzh());
		}

		for (ZhanghContext zhanghCtx : zhanghList) {
			KemuccContext kemuccCtx = kemuccGetter.query(zhanghCtx.getZhangh());
			zhanghCtx.setKemucc(kemuccCtx.getKemucc());
			zhanghCtx.setYewudh(kemuccCtx.getYewudh());
			if (StringUtils.isNotBlank(kemuccCtx.getHuobdh())) {
				zhanghCtx.setHuobdh(kemuccCtx.getHuobdh());
			}
			if (HdqsConstants.KHZHLX_CARD.equals(record.getKhzhlx())) {
				zhanghCtx.setYngyjg(yngyjgCtx.getYngyjg());
				zhanghCtx.setJigomc(yngyjgCtx.getJigomc());
			} else {
				zhanghCtx.setYngyjg(kemuccCtx.getYngyjg());
				zhanghCtx.setJigomc(kemuccCtx.getJigomc());
			}
			zhanghCtx.setKehzwm(info.getKehzwm());
		}
		context.setList(zhanghList);
		return context;
	}
}