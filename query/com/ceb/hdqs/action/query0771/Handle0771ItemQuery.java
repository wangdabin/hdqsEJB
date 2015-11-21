package com.ceb.hdqs.action.query0771;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;

import com.ceb.hdqs.action.query.AbstractPrivateItemQuery;
import com.ceb.hdqs.action.query.ItemQueryProcessor;
import com.ceb.hdqs.action.query.exception.BalanceBrokedException;
import com.ceb.hdqs.action.query.exception.NoNextPageException;
import com.ceb.hdqs.action.query.verification.ItemBalanceLxVerifierImpl;
import com.ceb.hdqs.entity.PybjyEO;
import com.ceb.hdqs.entity.field.AshmxField;
import com.ceb.hdqs.entity.result.AbstractQueryResult;
import com.ceb.hdqs.entity.result.KehzhParserResult;
import com.ceb.hdqs.entity.result.ParseResult;
import com.ceb.hdqs.entity.result.PrivateLiquidQueryItem;
import com.ceb.hdqs.entity.result.ZhangHParseResult;
import com.ceb.hdqs.query.entity.BalanceVerifyEntity;
import com.ceb.hdqs.query.entity.EnumQueryKind;
import com.ceb.hdqs.query.entity.Page;
import com.ceb.hdqs.query.entity.PageHeader;
import com.ceb.hdqs.query.entity.QueryDocumentContext;
import com.ceb.hdqs.query.entity.QueryPageInfo;
import com.ceb.hdqs.query.entity.SkyPair;
import com.ceb.hdqs.query.entity.TabRowMap;
import com.ceb.hdqs.query.utils.QueryConfUtils;
import com.ceb.hdqs.query.utils.QueryConstants;
import com.ceb.hdqs.query.utils.QueryMethodUtils;

/**
 * <p>
 * 对私活期查询{@link HandleQuery0771,HandleQuery0778}中实现明细查询
 * 
 * @author user
 * 
 */
public class Handle0771ItemQuery extends AbstractPrivateItemQuery<PrivateLiquidQueryItem> implements ItemQueryProcessor {
	private ItemBalanceLxVerifierImpl verifier = new ItemBalanceLxVerifierImpl();

	// 为了对明细进行赋值货币代号和钞汇标志

	public Handle0771ItemQuery() {
		super(QueryConstants.TABLE_NAME_ASHMX);
		setLog(LogFactory.getLog(Handle0771ItemQuery.class));
	}

	@Override
	protected PrivateLiquidQueryItem parse(Result result) throws IOException {
		PrivateLiquidQueryItem entity = new PrivateLiquidQueryItem();
		entity.setRowKey(result.getRow());
		String zhangh = Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AshmxField.ZHANGH));
		entity.setZHANGH(zhangh);
		String jioyrq = Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AshmxField.JIOYRQ));
		entity.setJIOYRQ(jioyrq);

		String jioysj = Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AshmxField.JIOYSJ));
		entity.setJIOYSJ(jioysj);

		String shjnch = Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AshmxField.SHJNCH));
		entity.setSHJNCH(shjnch);

		String cpzxh = Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AshmxField.CPZNXH));
		entity.setCPZNXH(cpzxh);

		String jioyje = Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AshmxField.JIO1JE));
		entity.setJIO1JE(jioyje);

		String zhhuye = Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AshmxField.ZHHUYE));
		entity.setZHHUYE(zhhuye);

		String yueefx = Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AshmxField.YUEEFX));
		entity.setYUEEFX(yueefx);

		String jiaoym = Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AshmxField.JIAOYM));
		entity.setJIAOYM(jiaoym);

		String jiedbz = Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AshmxField.JIEDBZ));
		entity.setJIEDBZ(jiedbz);

		String jioygy = Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AshmxField.JIO1GY));
		entity.setJIO1GY(jioygy);

		String shoqgy = Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AshmxField.SHOQGY));
		entity.setSHOQGY(shoqgy);

		String chbubz = Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AshmxField.CHBUBZ));
		entity.setCHBUBZ(chbubz);

		String guiyls = Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AshmxField.GUIYLS));
		entity.setGUIYLS(guiyls);

		String zhyodm = Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AshmxField.ZHYODM));
		entity.setZHYODM(zhyodm);
		String yngyjg = Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AshmxField.YNGYJG));
		entity.setYNGYJG(yngyjg);

		String khzhlx = Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AshmxField.KHZHLX));
		entity.setKHZHLX(khzhlx);

		String pngzhh = Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AshmxField.PNGZHH));
		entity.setPNGZHH(pngzhh);
		// 设置凭证种类
		if (pngzhh != null && pngzhh.length() >= 3) {
			entity.setPNZZLX(pngzhh.substring(0, 3));
		} else {
			entity.setPNZZLX("");
		}
		String duifzh = Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AshmxField.DUIFZH));
		entity.setDUIFZH(duifzh);

		String duifmc = Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AshmxField.DUIFMC));
		entity.setDUIFMC(duifmc);

		String danwmc = Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AshmxField.DANWMC));
		entity.setDANWMC(danwmc);

		entity.setCPZNXH(Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AshmxField.CPZNXH)));
		entity.setXNZHBZ(Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AshmxField.XNZHBZ)));

		entity.setSHDHDM(Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AshmxField.SHDHDM)));
		// 设置账号的货币代号和钞汇标志

		return entity;
	}

	/**
	 * 查询每个账号的nextpage
	 * 
	 * @param result
	 * @return
	 * @throws NoNextPageException
	 * @throws Exception
	 * @throws IOException
	 * @throws BalanceBrokedException
	 */
	@Override
	public Page<PrivateLiquidQueryItem> nextPage(ZhangHParseResult zhanghParseResult,
			QueryDocumentContext documentContext) throws IOException, BalanceBrokedException {
		getLog().debug("开始查询 table  " + this.getTableName());
		List<PrivateLiquidQueryItem> queryItem = new ArrayList<PrivateLiquidQueryItem>();
		long queriedCount = zhanghParseResult.getQueriedCount();
		long itemCount = zhanghParseResult.getItemCount();
		getLog().debug("总条数：" + itemCount);
		getLog().debug("已经查询的条数是：" + queriedCount);
		getLog().debug("查询的账号是：" + zhanghParseResult.getZHANGH());
		int pageNum = zhanghParseResult.getPageLineNumber();

		Page<PrivateLiquidQueryItem> page = new Page<PrivateLiquidQueryItem>();
		page.setParseResult(zhanghParseResult);

		if (zhanghParseResult.getFinished() == QueryConstants.ZHANGH_QUERY_FINISHED) {
			getLog().info("账号" + zhanghParseResult.getZHANGH() + "查询完成!");
			return null;
		}
		if (queriedCount == 0) {
			getLog().debug("第一次查询");
			Scan scan = buildItemQueryScanner(zhanghParseResult.getZHANGH(), zhanghParseResult.getRecord());
			/* 同步查询 */
			queryItem = scan(scan, pageNum + 1);
			zhanghParseResult.setScanner(scan);
			// 第一次查询，如果没有明细则生成noitempage对象
			if (queryItem == null || queryItem.isEmpty()) {
				zhanghParseResult.setFinished(QueryConstants.ZHANGH_QUERY_FINISHED);
				// String tips = "不存在" + zhanghParseResult.getZHANGH() +
				// "的交易明细.";
				BalanceVerifyEntity pair = QueryMethodUtils.getRecordPair(zhanghParseResult.getRecord());
				String tips = pair.getQueryKind() + pair.getValue() + "  账号:" + zhanghParseResult.getZHANGH()
						+ "不存在交易明细.";
				getLog().info("tips:"+tips+"of length is "+tips.length());
				page.setTip(tips);
				PageHeader header = this.getPageHeader(zhanghParseResult);
				header.setTips(tips);
				page.setHeader(header);
				page.setPageItem(queryItem);
			}
		} else {
			getLog().debug("第  n 次查询");
			Scan scan = zhanghParseResult.getScanner();
			scan.setStartRow(zhanghParseResult.getLastRowkey());
			queryItem = scan(scan, pageNum + 1);
		}
		// 进行余额连续性的校验
		BalanceVerifyEntity recordInfo = QueryMethodUtils.getRecordPair(zhanghParseResult.getRecord());

		// 统计查询明细集合中的记录条数
		if (queryItem == null || queryItem.isEmpty()) {
			pageNum = 0;
		} else if (queryItem.size() == pageNum + 1) { // 将limit替换成pageNum
			verifier.reset();
			verifier.verify(recordInfo, zhanghParseResult, queryItem.subList(0, queryItem.size() - 1),documentContext);
			pageNum = queryItem.size() - 1;
			byte[] lastRowKey = queryItem.get(pageNum).getRowKey();
			zhanghParseResult.setLastRowkey(lastRowKey);
			page.setPageItem(queryItem.subList(0, queryItem.size() - 1));
			page.setHeader(this.getPageHeader(zhanghParseResult));// 为页设置Header
		} else {
			verifier.reset();
			verifier.verify(recordInfo, zhanghParseResult, queryItem,documentContext);
			pageNum = queryItem.size();
			byte[] lastRowKey = queryItem.get(pageNum - 1).getRowKey();
			zhanghParseResult.setLastRowkey(lastRowKey);
			page.setPageItem(queryItem);
			page.setHeader(this.getPageHeader(zhanghParseResult));// 为页设置Header

		}

		// 判断是否完成,因为每次查询的时候会查询指定的queryNum+1条记录，如果查询结果为空或者查询的结果集的数量是< queryNum+1
		if (queryItem == null || queryItem.size() < pageNum + 1) {
			zhanghParseResult.setFinished(QueryConstants.ZHANGH_QUERY_FINISHED);
			int queriedZhNum = documentContext.getFinishedZhNum();
			zhanghParseResult.setQueriedZhNum(queriedZhNum + 1);
			documentContext.setFinishedZhNum(queriedZhNum + 1);
		}
		/*
		 * 记录该账号已经查询出的条数
		 */
		zhanghParseResult.setQueriedCount(queriedCount + pageNum);
		// 当前账号所属于的查询条件查询出的总条数
		long allItemCount = documentContext.getAllItemCount();
		documentContext.setAllItemCount(allItemCount + pageNum);
		zhanghParseResult.setAllItemCount(allItemCount + pageNum);
		// 当前账号的页数
		int zhanghPageTotal = zhanghParseResult.getPageTotal();
		zhanghParseResult.setPageTotal(zhanghPageTotal + 1);
		// 当前账号所属于的查询条件查询出的页数
		long allPageCount = documentContext.getAllPageCount();
		documentContext.setAllPageCount(allPageCount + 1);
		zhanghParseResult.setAllPageCount(allPageCount + 1);

		int oldCommNum = zhanghParseResult.getRecord().getCommNum();
		zhanghParseResult.getRecord().setCommNum(oldCommNum + 1);
		// 异步查询更新该账号的itemCount

		long ic = zhanghParseResult.getRecord().getItemCount();
		ic += pageNum;
		zhanghParseResult.getRecord().setItemCount(ic);
		queriedCount += pageNum;
		itemCount += pageNum;
		zhanghParseResult.setItemCount(itemCount);

		return page;
	}

	/**
	 * 确定该查询是否需要异步查询
	 * 
	 * @param record
	 * @param zhjxResult
	 * @return
	 * @throws IOException
	 * @throws BalanceBrokedException
	 */
	public void isAsynchronize(PybjyEO record, ParseResult parseRecord, AbstractQueryResult result) throws Exception {
		getLog().debug("开始检测是否进行异步查询....");

		int threshod = QueryConfUtils.getActiveConfig().getInt(QueryConstants.SYNCHRONIZE_SWITCH_THRESHOLD, 1000);// 开始异步查询的阀值
		/* 生成查询hbase的scan对象 ，由于一个客户账号存在多个账号的情况，用循环来进行扫描 */
		Map<ZhangHParseResult, Scan> scans = new HashMap<ZhangHParseResult, Scan>();
		KehzhParserResult parseResult = (KehzhParserResult) parseRecord;
		getLog().info(
				record.getSlbhao() + "交易，客户账号" + record.getKehuzh() + "查询出的账号个数是："
						+ parseResult.getZhanghParseResult().size());
		for (Entry<SkyPair<String, EnumQueryKind>, ZhangHParseResult> entryItem : parseResult.getZhanghParseResult()
				.entrySet()) {
			ZhangHParseResult zhanghParseRecord = (ZhangHParseResult) entryItem.getValue();
			zhanghParseRecord.setPageLineNumber(record.getQueryNum());// 记录每页的明细条数，在分页的时候使用
//			zhanghParseRecord.setQueryTpye(QueryConstants.PRIVATE_LIQUID_QUERY_INPUT_FIELD);
			getLog().debug("开始处理账号：" + zhanghParseRecord.getZHANGH());

			Scan scan = new Scan();
			scan.setCaching(threshod);
			scan.addFamily(QueryConstants.HBASE_TABLE_FAMILY_BYTE);
			scan.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AshmxField.SHJNCH);
			scan.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AshmxField.JIO1JE);
			scan.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AshmxField.ZHHUYE);
			scan.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AshmxField.YUEEFX);
			scan.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AshmxField.JIEDBZ);
			scan.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AshmxField.JIOYRQ);
			scan.setStartRow(makeStartRow(zhanghParseRecord.getZHANGH(), record));
			scan.setStopRow(makeStopRow(zhanghParseRecord.getZHANGH(), record));
			scans.put(zhanghParseRecord, scan);
			try {
				getLog().info(zhanghParseRecord.getZHANGH() + "的查询条件：" + scan.toJSON());
			} catch (Exception e) {
			}
		}

		getLog().debug("检测的scan 个数是" + scans.size());
		isAsynchronize(threshod, scans, result, record);
	}

	/**
	 * 生成结束键
	 * 
	 * @param condation
	 * @param account
	 * @return
	 */
	private byte[] makeStopRow(String zhangh, PybjyEO condition) {
		String rowkeySpliter = QueryConfUtils.getActiveConfig().get(QueryConstants.ROWKEY_SPLITTER, "|");
		String endDate = condition.getEndDate();

		StringBuilder sbendRow = new StringBuilder();
		sbendRow.append(StringUtils.reverse(zhangh)).append(rowkeySpliter).append(QueryConstants.JILUZT_NORMAL)
				.append(rowkeySpliter).append(endDate).append(rowkeySpliter).append(QueryConstants.MAX_CHAR);
		getLog().debug("结束key是：" + sbendRow.toString());
		return Bytes.toBytes(sbendRow.toString());

	}

	/**
	 * 生成开始键
	 * 
	 * @param condition
	 * @param account
	 * @return
	 */
	private byte[] makeStartRow(String zhangh, PybjyEO condition) {
		String rowkeySpliter = QueryConfUtils.getActiveConfig().get(QueryConstants.ROWKEY_SPLITTER, "|");
		String startDate = condition.getStartDate();

		StringBuilder sbStartRow = new StringBuilder();
		sbStartRow.append(StringUtils.reverse(zhangh)).append(rowkeySpliter).append(QueryConstants.JILUZT_NORMAL)
				.append(rowkeySpliter).append(startDate).append(rowkeySpliter).append(QueryConstants.MIN_NUM);
		getLog().debug("开始key是：" + sbStartRow.toString());
		return Bytes.toBytes(sbStartRow.toString());
	}

	/**
	 * 生成明细查询的Scan
	 * 
	 * @param po
	 * @return
	 */
	public Scan buildItemQueryScanner(String zhangh, PybjyEO po) {

		byte[] startRow = makeStartRow(zhangh, po);
		byte[] endRow = makeStopRow(zhangh, po);

		Scan scan = new Scan();
		scan.addFamily(QueryConstants.HBASE_TABLE_FAMILY_BYTE);
		scan.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AshmxField.JIOYSJ);
		scan.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AshmxField.ZHANGH);
		scan.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AshmxField.JIOYRQ);
		scan.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AshmxField.SHJNCH);
		scan.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AshmxField.JIO1JE);
		scan.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AshmxField.ZHHUYE);
		scan.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AshmxField.YUEEFX);
		scan.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AshmxField.JIAOYM);
		scan.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AshmxField.JIEDBZ);
		scan.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AshmxField.JIO1GY);
		scan.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AshmxField.SHOQGY);
		scan.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AshmxField.CHBUBZ);
		scan.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AshmxField.GUIYLS);
		scan.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AshmxField.ZHYODM);
		scan.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AshmxField.YNGYJG);
		scan.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AshmxField.KHZHLX);
		scan.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AshmxField.PNGZHH);
		scan.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AshmxField.DUIFZH);
		scan.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AshmxField.DUIFMC);
		scan.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AshmxField.DANWMC);
		scan.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AshmxField.CPZNXH);
		scan.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AshmxField.XNZHBZ);
		scan.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AshmxField.SHDHDM);
		scan.setStartRow(startRow);
		scan.setStopRow(endRow);
		return scan;
	}

	@Override
	public PageHeader getPageHeader(ZhangHParseResult parseResult) {

		PageHeader pageHeader = new PageHeader();
		pageHeader.setKehhao(parseResult.getKehhao());
		pageHeader.setCxqsrq(parseResult.getRecord().getStartDate());
		pageHeader.setCxzzrq(parseResult.getRecord().getEndDate());
		String yngyjg = parseResult.getZHYYNG();
		pageHeader.setFkjgHao(yngyjg == null ? "" : yngyjg);
		pageHeader.setFkjgName(parseResult.getJIGOMC());
		pageHeader.setGuiyuan(parseResult.getRecord().getJio1gy());
		pageHeader.setPrintDate(System.currentTimeMillis());
		pageHeader.setHbdh(parseResult.getHUOBDH());
		pageHeader.setKhzh(parseResult.getKehuzh());
		pageHeader.setKhzwm(parseResult.getKehzwm());
		pageHeader.setChbz(parseResult.getCHUIBZ());
		pageHeader.setZhangh(parseResult.getZHANGH());
		getLog().debug(pageHeader.toString());
		return pageHeader;
	}

	@Override
	public List<PrivateLiquidQueryItem> synQueryNextPage(QueryPageInfo pageInfo) throws Exception {
		Scan scanner = new Scan();
		// 定义存储查询结果的list集合
		List<PrivateLiquidQueryItem> items = new ArrayList<PrivateLiquidQueryItem>();
		for (Entry<TabRowMap, Integer> info : pageInfo.getPageInfo().entrySet()) {
			scanner.setStartRow(info.getKey().getRowKey());
			List<PrivateLiquidQueryItem> temItems = this.scan(scanner, info.getValue().intValue());
			if (temItems != null && !temItems.isEmpty()) {
				items.addAll(temItems);
			}
		}
		return items;
	}

	@Override
	public String getQueryTable() {
		return getTableName();
	}

	public static void main(String[] args) {
		PrivateLiquidQueryItem item = new PrivateLiquidQueryItem();
		item.setSHDHDM(Bytes.toString(null));
		System.out.println(item.getSHDHDM());
	}
}