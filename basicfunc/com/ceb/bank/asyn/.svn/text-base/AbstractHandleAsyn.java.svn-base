package com.ceb.bank.asyn;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.ceb.bank.context.LxContext;
import com.ceb.bank.context.OutPdfContext;
import com.ceb.bank.context.RZhanghContext;
import com.ceb.bank.context.ZhanghContext;
import com.ceb.bank.query.lx.ILxVerifier;
import com.ceb.bank.query.output.pdf.PdfGenerator;
import com.ceb.hdqs.action.asyn.IAsynchronizeQuery;
import com.ceb.hdqs.entity.PybjyEO;
import com.ceb.hdqs.service.SqcsService;
import com.ceb.hdqs.service.YbjyService;
import com.ceb.hdqs.utils.JNDIUtils;
import com.lowagie.text.DocumentException;

public abstract class AbstractHandleAsyn implements IAsynchronizeQuery {
	private static final Logger log = Logger.getLogger(AbstractHandleAsyn.class);

	private List<PybjyEO> list = new ArrayList<PybjyEO>();
	private YbjyService ybjyService;
	private SqcsService sqcsService;
	private String taskName;
	private boolean firstFlush = true;

	public AbstractHandleAsyn(List<PybjyEO> list) {
		this.list = list;
		try {
			this.ybjyService = (YbjyService) JNDIUtils.lookup(YbjyService.class);
			this.sqcsService = (SqcsService) JNDIUtils.lookup(SqcsService.class);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	@Deprecated
	// will be replaced by execute method
	@Override
	public String startAsynchronizeQuery(boolean isSynPrint) throws Exception {
		execute();
		return null;
	}

	/**
	 * 更新记录运行状态等信息
	 */
	@Override
	public void update(PybjyEO record) {
		getYbjyService().update(record);
	}

	/**
	 * 批量更新记录运行状态等信息
	 */
	@Override
	public void batchUpdate(List<PybjyEO> list) {
		getYbjyService().batchUpdateRecords(list);
	}

	/**
	 * 异步处理调用接口
	 * 
	 * @throws Exception
	 */
	public abstract void execute() throws Exception;

	/**
	 * 不连续验证
	 * 
	 * @param rCtx
	 *            账号关联关系,客户号或证件查询时,保存客户账号和客户账号类型等属性信息
	 * @param record
	 *            查询条件
	 * @param ctx
	 *            输出PDF上下文
	 * @param itemList
	 *            明细集合
	 * @param lxVerifier
	 *            验证方法接口类
	 * @param blxCache
	 *            不连续信息存放集合
	 */
	protected void blxVerify(RZhanghContext rCtx, PybjyEO record, final OutPdfContext ctx, List<? extends LxContext> itemList,
			ILxVerifier lxVerifier, List<String> blxCache) {
		List<String> blxList = lxVerifier.checkAsyn(rCtx, itemList, record);
		if (blxList != null && blxList.size() > 0) {
			ctx.setCurrentQueryLX(false);
			blxCache.addAll(blxList);
		}
	}

	/**
	 * 每个账号生成独立PDF文件后，根据连续性及是否是授权参数表管理柜员来进行分解，满足每个PDF文件页数限制条件
	 * 
	 * @param record
	 *            查询条件
	 * @param ctx
	 *            输出PDF上下文
	 * @param isManagedGuiy
	 *            是否是授权参数表管理柜员
	 * @param generator
	 *            PDF文件生成接口
	 * @param zhanghCtx
	 *            账号上下文,查询出客户账号对应的多个账号信息
	 * @param blxCache
	 *            不连续信息存放集合
	 * @throws IOException
	 * @throws DocumentException
	 */
	protected void splitPdf(PybjyEO record, final OutPdfContext ctx, boolean isManagedGuiy, final PdfGenerator<?> generator,
			ZhanghContext zhanghCtx, List<String> blxCache) throws IOException, DocumentException {
		if (!ctx.isCurrentQueryLX()) {
			if (isManagedGuiy) {
				ctx.setLastPageNum(ctx.getLastPageNum() + ctx.getCurrentPageNum());
			} else {
				ctx.setLastPageNum(ctx.getLastPageNum() + 1);
				ctx.setPageNum(ctx.getPageNum() - ctx.getCurrentPageNum() + 1);
				ctx.setTotal(ctx.getTotal() - ctx.getCurrentTotal());
				ctx.setCurrentPageNum(1);
				ctx.setCurrentTotal(0);

				generator.createBlxPdf(record, ctx, blxCache, 1, zhanghCtx);
			}
		} else {
			ctx.setLastPageNum(ctx.getLastPageNum() + ctx.getCurrentPageNum());
		}
		generator.splitPdf(record, ctx);
	}

	/**
	 * 与图前约定,不论连续与否,都要生成一个blx.txt文件,文件可以追加
	 * 
	 * @param filePath
	 *            包含文件名称的全路径
	 * @param record
	 *            查询条件
	 * @param blxList
	 *            不连续信息集合
	 */
	protected void flushBrokedReord(String filePath, PybjyEO record) {
		this.flushBrokedReord(filePath, record, null);
	}

	protected void flushBrokedReord(String filePath, PybjyEO record, List<String> blxList) {
		log.info("Create " + record.getSlbhao() + "blx.txt.");
		String outputDir = filePath.substring(0, filePath.lastIndexOf(File.separator));
		File blxFile = new File(outputDir, record.getSlbhao() + "blx.txt");
		if (firstFlush) {
			if (blxFile.exists()) {
				blxFile.delete();
			}
			firstFlush = false;
		}

		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(blxFile, true));
			if (blxList != null && blxList.size() > 0) {
				for (String str : blxList) {
					bw.write(str);
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

	public List<PybjyEO> getList() {
		return list;
	}

	public void setList(List<PybjyEO> list) {
		this.list = list;
	}

	public YbjyService getYbjyService() {
		return ybjyService;
	}

	public SqcsService getSqcsService() {
		return sqcsService;
	}

	@Override
	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
}