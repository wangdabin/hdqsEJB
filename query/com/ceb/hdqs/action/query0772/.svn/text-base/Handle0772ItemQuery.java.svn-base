package com.ceb.hdqs.action.query0772;

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
import com.ceb.hdqs.action.query.exception.NoItemException;
import com.ceb.hdqs.action.query.exception.NoNextPageException;
import com.ceb.hdqs.action.query.verification.ItemBalanceLxVerifierImpl;
import com.ceb.hdqs.entity.PybjyEO;
import com.ceb.hdqs.entity.field.AsdmxField;
import com.ceb.hdqs.entity.field.AshmxField;
import com.ceb.hdqs.entity.result.AbstractQueryResult;
import com.ceb.hdqs.entity.result.KehzhParserResult;
import com.ceb.hdqs.entity.result.ParseResult;
import com.ceb.hdqs.entity.result.PrivateFixQueryItem;
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
 * 对私定期查询{@link HandleQuery0772,HandleQuery0778}中实现明细查询
 * 
 * @author user
 * 
 */
public class Handle0772ItemQuery extends AbstractPrivateItemQuery<PrivateFixQueryItem> implements ItemQueryProcessor {
	// 为了对明细进行赋值货币代号和钞汇标志
	private ZhangHParseResult zhanghParserResult;
	private ItemBalanceLxVerifierImpl verifier = new ItemBalanceLxVerifierImpl();

	public Handle0772ItemQuery() {
		super(QueryConstants.TABLE_NAME_ASDMX);
		setLog(LogFactory.getLog(Handle0772ItemQuery.class));
	}

	@Override
	protected PrivateFixQueryItem parse(Result result) throws IOException {
		PrivateFixQueryItem privateFixQueryItem = new PrivateFixQueryItem();

		// 取出本条记录的rowkey
		byte[] rowKey = result.getRow();
		String zhangh = Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AsdmxField.ZHANGH));
		String jioyrq = Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AsdmxField.JIOYRQ));
		String jioysh = Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AsdmxField.JIOYSJ));
		String jio1je = Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AsdmxField.JIO1JE));
		String zhhuye = Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AsdmxField.ZHHUYE));
		String yueefx = Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AsdmxField.YUEEFX));

		String jiaoym = Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AsdmxField.JIAOYM));
		String jiedbz = Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AsdmxField.JIEDBZ));
		String jio1gy = Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AsdmxField.JIO1GY));
		String shoqgy = Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AsdmxField.SHOQGY));
		String chbubz = Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AsdmxField.CHBUBZ));
		String guiyls = Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AsdmxField.GUIYLS));
		String zhyodm = Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AsdmxField.ZHYODM));
		String YNGYJG = Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AsdmxField.YNGYJG));
		String KHZHLX = Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AsdmxField.KHZHLX));
		String PNGZHH = Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AsdmxField.PNGZHH));
		String HUOBDH = Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AsdmxField.HUOBDH));
		String DUIFZH = Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AsdmxField.DUIFZH));
		String DUIFMC = Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AsdmxField.DUIFMC));
		String danwcm = Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AsdmxField.DANWMC));

		String shjnch = Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AsdmxField.SHJNCH));

		String cpznxh = Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AsdmxField.CPZNXH));
		String xnzhbz = Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AsdmxField.XNZHBZ));
		String shdhdm = Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AsdmxField.SHDHDM));

		privateFixQueryItem.setRowKey(rowKey == null ? Bytes.toBytes("") : rowKey);
		privateFixQueryItem.setSHJNCH(shjnch);
		privateFixQueryItem.setZHANGH(zhangh);
		privateFixQueryItem.setJIOYRQ(jioyrq);
		privateFixQueryItem.setJIOYSJ(jioysh);
		privateFixQueryItem.setJIO1JE(jio1je);
		privateFixQueryItem.setZHHUYE(zhhuye);
		privateFixQueryItem.setYUEEFX(yueefx);
		privateFixQueryItem.setJIAOYM(jiaoym);
		privateFixQueryItem.setJIEDBZ(jiedbz);
		privateFixQueryItem.setJIO1GY(jio1gy);
		privateFixQueryItem.setSHOQGY(shoqgy);
		privateFixQueryItem.setCHBUBZ(chbubz);
		privateFixQueryItem.setGUIYLS(guiyls);
		privateFixQueryItem.setZHYODM(zhyodm);
		privateFixQueryItem.setYNGYJG(YNGYJG);
		privateFixQueryItem.setKHZHLX(KHZHLX);
		privateFixQueryItem.setPNGZHH(PNGZHH);
		privateFixQueryItem.setSHDHDM(shdhdm);
		privateFixQueryItem.setHUOBDH(HUOBDH);
		// 设置凭证种类
		if (PNGZHH != null && PNGZHH.length() >= 3) {
			privateFixQueryItem.setPNZZLX(PNGZHH.substring(0, 3));
		} else {
			privateFixQueryItem.setPNZZLX("");
		}
		privateFixQueryItem.setDUIFZH(DUIFZH);
		privateFixQueryItem.setDUIFMC(DUIFMC);
		privateFixQueryItem.setDANWMC(danwcm);
		privateFixQueryItem.setCPZNXH(cpznxh);
		privateFixQueryItem.setXNZHBZ(xnzhbz);

		// 设置账号的货币代号和钞汇标志

		privateFixQueryItem.setHUOBDH(zhanghParserResult.getHUOBDH());

		return privateFixQueryItem;
	}

	/**
	 * 判断该查询是否需要启动异步查询
	 * 
	 * @param condation
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
			getLog().debug("开始处理账号：" + zhanghParseRecord.getZHANGH());

			Scan scan = new Scan();
			scan.setCaching(threshod);
			scan.addFamily(QueryConstants.HBASE_TABLE_FAMILY_BYTE);
			scan.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AsdmxField.SHJNCH);
			scan.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AsdmxField.JIO1JE);
			scan.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AsdmxField.ZHHUYE);
			scan.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AsdmxField.YUEEFX);
			scan.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AsdmxField.JIEDBZ);
			scan.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AsdmxField.JIOYRQ);
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
	 * 查询每个账号的nextpage
	 * 
	 * @param result
	 * @return
	 * @throws NoNextPageException
	 * @throws Exception
	 * @throws IOException
	 * @throws BalanceBrokedException
	 * @throws NoItemException
	 */
	public Page<PrivateFixQueryItem> nextPage(ZhangHParseResult zhanghParseResult, QueryDocumentContext documentContext)
			throws IOException, BalanceBrokedException {

		this.zhanghParserResult = zhanghParseResult;
		getLog().debug("开始查询 table  " + this.getTableName());
		List<PrivateFixQueryItem> queryItem = new ArrayList<PrivateFixQueryItem>();
		long queriedCount = zhanghParseResult.getQueriedCount();
		long itemCount = zhanghParseResult.getItemCount();
		getLog().debug("总条数：" + itemCount);
		getLog().debug("已经查询的条数是：" + queriedCount);
		getLog().debug("查询的账号是：" + zhanghParseResult.getZHANGH());
		int pageNum = zhanghParseResult.getPageLineNumber();

		Page<PrivateFixQueryItem> page = new Page<PrivateFixQueryItem>();
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
				BalanceVerifyEntity pair = QueryMethodUtils.getRecordPair(zhanghParseResult.getRecord());
				String tips = pair.getQueryKind() + pair.getValue() + "  账号:" + zhanghParseResult.getZHANGH()
						+ "不存在交易明细.";
				PageHeader header = this.getPageHeader(zhanghParseResult);
				header.setTips(tips);
				page.setHeader(header);
				page.setPageItem(queryItem);
			}
		} else {
			Scan scan = zhanghParseResult.getScanner();
			scan.setStartRow(zhanghParseResult.getLastRowkey());
			queryItem = scan(scan, pageNum + 1);
		}
		// 进行余额连续性的校验
		BalanceVerifyEntity recordInfo = QueryMethodUtils.getRecordPair(zhanghParseResult.getRecord());

		// 统计查询明细集合中的记录条数
		if (queryItem == null || queryItem.isEmpty()) {
			pageNum = 0;
		} else if (queryItem.size() == pageNum + 1) {
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
			byte[] lastRowKey = queryItem.get((pageNum - 1)).getRowKey();
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
		// 当前账号所属Record的明细
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

	public Scan buildItemQueryScanner(String zhangh, PybjyEO po) {
		byte[] startRow = makeStartRow(zhangh, po);
		byte[] endRow = makeStopRow(zhangh, po);

		Scan scan = new Scan();
		scan.addFamily(QueryConstants.HBASE_TABLE_FAMILY_BYTE);

		scan.setStartRow(startRow);
		scan.setStopRow(endRow);
		scan.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AsdmxField.ZHANGH);
		scan.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AsdmxField.JIOYRQ);
		scan.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AsdmxField.JIOYSJ);
		scan.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AsdmxField.JIO1JE);
		scan.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AsdmxField.ZHHUYE);
		scan.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AsdmxField.YUEEFX);
		scan.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AsdmxField.HUOBDH);
		scan.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AsdmxField.JIAOYM);
		scan.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AsdmxField.JIEDBZ);
		scan.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AsdmxField.JIO1GY);
		scan.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AsdmxField.SHOQGY);
		scan.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AsdmxField.CHBUBZ);
		scan.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AsdmxField.GUIYLS);
		scan.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AsdmxField.ZHYODM);
		scan.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AsdmxField.YNGYJG);
		scan.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AsdmxField.KHZHLX);
		scan.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AsdmxField.PNGZHH);
		scan.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AsdmxField.DUIFZH);
		scan.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AsdmxField.DUIFMC);
		scan.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AsdmxField.SHJNCH);
		scan.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AsdmxField.CPZNXH);
		scan.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AsdmxField.XNZHBZ);
		scan.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AshmxField.SHDHDM);
		scan.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AshmxField.HUOBDH);

		return scan;
	}

	@Override
	public PageHeader getPageHeader(ZhangHParseResult parseResult) {
		PageHeader pageHeader = new PageHeader();
		pageHeader.setCxqsrq(parseResult.getRecord().getStartDate());
		pageHeader.setCxzzrq(parseResult.getRecord().getEndDate());
		pageHeader.setKehhao(parseResult.getKehhao());
		pageHeader.setGuiyuan(parseResult.getRecord().getJio1gy());
		pageHeader.setPrintDate(System.currentTimeMillis());
		pageHeader.setHbdh(parseResult.getHUOBDH());
		pageHeader.setChbz(parseResult.getCHUIBZ());
		pageHeader.setKhzh(parseResult.getKehuzh());
		pageHeader.setKhzwm(parseResult.getKehzwm());
		String yngyjg = parseResult.getZHYYNG();
		pageHeader.setFkjgHao(yngyjg == null ? "" : yngyjg);
		pageHeader.setFkjgName(parseResult.getJIGOMC());
		pageHeader.setZhangh(parseResult.getZHANGH());
		return pageHeader;
	}

	@Override
	public List<PrivateFixQueryItem> synQueryNextPage(QueryPageInfo pageInfo) throws Exception {
		Scan scanner = new Scan();
		// 定义存储查询结果的list集合
		List<PrivateFixQueryItem> items = new ArrayList<PrivateFixQueryItem>();
		for (Entry<TabRowMap, Integer> info : pageInfo.getPageInfo().entrySet()) {
			scanner.setStartRow(info.getKey().getRowKey());
			zhanghParserResult = info.getKey().getZhangHParseResult();
			List<PrivateFixQueryItem> temItems = this.scan(scanner, info.getValue().intValue());
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

}
