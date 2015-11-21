/*
 * (C) Copyright  2013  BeagleData Corporation, All Rights Reserved.            
 */
package com.ceb.hdqs.action.query0770;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;

import com.ceb.bank.constants.FieldConstants;
import com.ceb.bank.htable.AbstractQuery;
import com.ceb.hdqs.action.query.ItemQueryProcessor;
import com.ceb.hdqs.action.query.exception.AsynQueryException;
import com.ceb.hdqs.action.query.exception.BalanceBrokedException;
import com.ceb.hdqs.action.query.verification.ItemBalanceLxVerifierImpl;
import com.ceb.hdqs.constants.HdqsConstants;
import com.ceb.hdqs.entity.PybjyEO;
import com.ceb.hdqs.entity.field.AghmxField;
import com.ceb.hdqs.entity.result.AbstractQueryResult;
import com.ceb.hdqs.entity.result.BalanceItemCounter;
import com.ceb.hdqs.entity.result.CorporateQueryItem;
import com.ceb.hdqs.entity.result.ParseResult;
import com.ceb.hdqs.entity.result.ZhangHParseResult;
import com.ceb.hdqs.query.entity.BalanceVerifyEntity;
import com.ceb.hdqs.query.entity.Page;
import com.ceb.hdqs.query.entity.PageHeader;
import com.ceb.hdqs.query.entity.QueryDocumentContext;
import com.ceb.hdqs.query.entity.QueryPageInfo;
import com.ceb.hdqs.query.entity.TabRowMap;
import com.ceb.hdqs.query.entity.TransferItem;
import com.ceb.hdqs.query.utils.QueryConfUtils;
import com.ceb.hdqs.query.utils.QueryConstants;
import com.ceb.hdqs.query.utils.QueryMethodUtils;

/**
 * 对公查询明细查询类
 * 
 * @author user
 * 
 */
public class Handle0770ItemQuery extends AbstractQuery<CorporateQueryItem> implements ItemQueryProcessor {
	private static final Log LOG = LogFactory.getLog(Handle0770ItemQuery.class);
	private ItemBalanceLxVerifierImpl verifier = new ItemBalanceLxVerifierImpl();

	// 为了对明细进行赋值货币代号和钞汇标志
	private ZhangHParseResult zhanghParserResult;

	public Handle0770ItemQuery(String tableName) {
		super(tableName);
	}

	/**
	 * 异步查询，开始查询该账号的下一页数据
	 * 
	 * @param zhanghParseResult
	 * @return
	 * @throws IOException
	 * @throws BalanceBrokedException
	 * @throws Exception
	 */
	@Override
	public Page<CorporateQueryItem> nextPage(ZhangHParseResult zhanghParseResult, QueryDocumentContext ctx) throws IOException,
			BalanceBrokedException {
		LOG.debug("查询账号" + zhanghParseResult.getZHANGH() + ",页数: " + (zhanghParseResult.getPageTotal() + 1));
		this.zhanghParserResult = zhanghParseResult;
		List<CorporateQueryItem> itemList = new ArrayList<CorporateQueryItem>();
		Page<CorporateQueryItem> page = new Page<CorporateQueryItem>();
		page.setParseResult(zhanghParseResult);
		long limit = zhanghParseResult.getPageLineNumber();

		if (zhanghParseResult.getFinished() == QueryConstants.ZHANGH_QUERY_FINISHED) {
			return null;
		}
		// 如果是第一次查询
		if (zhanghParseResult.getQueriedCount() == 0) {
			Scan scan = buildScanner(zhanghParseResult);
			/* 同步查询，每次查询queryNum+1,最后一条为下一页的起始key */
			itemList = scan(scan, limit + 1);
			if (itemList == null || itemList.isEmpty()) {
				zhanghParseResult.setFinished(QueryConstants.ZHANGH_QUERY_FINISHED);
				page.setPageItem(itemList);
				PageHeader header = this.getPageHeader(zhanghParseResult);
				BalanceVerifyEntity pair = QueryMethodUtils.getRecordPair(zhanghParseResult.getRecord());
				String tipsText = "";
				if (HdqsConstants.CHAXZL_ZHANGH.equals(zhanghParseResult.getRecord().getChaxzl())) {
					tipsText = "账号:" + zhanghParseResult.getZHANGH() + " 不存在交易明细.";
				} else {
					tipsText = pair.getQueryKind() + pair.getValue() + ",账号:" + zhanghParseResult.getZHANGH() + " 不存在交易明细.";

				}
				header.setTips(tipsText);
				page.setHeader(header);
			}
		} else {
			Scan scan = new Scan();
			scan.setStartRow(zhanghParseResult.getLastRowkey());
			scan.setStopRow(buildStopRow(zhanghParseResult));
			Handle0770ItemQuery itemQuery = (Handle0770ItemQuery) zhanghParseResult.getQueryExectuer();
			itemList = itemQuery.scan(scan, limit + 1);
		}

		// 进行余额连续性的校验
		BalanceVerifyEntity recordInfo = QueryMethodUtils.getRecordPair(zhanghParseResult.getRecord());
		// 判断查询结果集是否是全部结果
		if (itemList == null || itemList.isEmpty()) {
			limit = 0;
		} else if (itemList.size() == limit + 1) {
			// 去除掉最后一条，因为最后一条是下一页的rowkey，不输出
			verifier.reset();
			verifier.verify(recordInfo, zhanghParseResult, itemList.subList(0, (int) limit), ctx);
			page.setPageItem(itemList.subList(0, (int) limit));
			page.setHeader(this.getPageHeader(zhanghParseResult));
			zhanghParseResult.setLastRowkey(itemList.get((int) (limit)).getRowKey());
		} else {
			verifier.reset();
			verifier.verify(recordInfo, zhanghParseResult, itemList, ctx);
			limit = itemList.size();
			page.setPageItem(itemList);
			page.setHeader(this.getPageHeader(zhanghParseResult));
			zhanghParseResult.setLastRowkey(itemList.get((int) (limit - 1)).getRowKey());
		}
		zhanghParseResult.setItemCount(zhanghParseResult.getItemCount() + limit);

		zhanghParseResult.getRecord().setItemCount(zhanghParseResult.getRecord().getItemCount() + limit);
		// 判断是否完成,因为每次查询的时候会查询指定的queryNum+1条记录，如果查询结果为空或者查询的结果集的数量是<= queryNum
		if (itemList == null || itemList.size() <= limit) {
			zhanghParseResult.setFinished(QueryConstants.ZHANGH_QUERY_FINISHED);
			int finishedZhNum = ctx.getFinishedZhNum();
			zhanghParseResult.setQueriedZhNum(finishedZhNum + 1);
			ctx.setFinishedZhNum(finishedZhNum + 1);

			LOG.info("账号" + zhanghParseResult.getZHANGH() + "查询完成,总页数:" + zhanghParseResult.getPageTotal() + ",总明细数:"
					+ zhanghParseResult.getItemCount());
		}
		/*
		 * 记录该账号已经查询出的条数和查询出的页数
		 */
		zhanghParseResult.setQueriedCount(zhanghParseResult.getQueriedCount() + limit);
		long allItemCount = ctx.getAllItemCount();
		ctx.setAllItemCount(allItemCount + limit);
		zhanghParseResult.setAllItemCount(allItemCount + limit);
		// 当前账号的页数
		zhanghParseResult.setPageTotal(zhanghParseResult.getPageTotal() + 1);
		// 当前账号所属于的查询条件查询出的页数
		long allPageCount = ctx.getAllPageCount();
		ctx.setAllPageCount(allPageCount + 1);
		zhanghParseResult.setAllPageCount(allPageCount + 1);
		// 当前账号所属Record的明细
		int oldCommNum = zhanghParseResult.getRecord().getCommNum();
		zhanghParseResult.getRecord().setCommNum(oldCommNum + 1);
		return page;
	}

	@Override
	public void isAsynchronize(PybjyEO record, ParseResult parseRecord, AbstractQueryResult result) throws Exception {
		LOG.info("开始检测任务" + record.getSlbhao() + "是否需要异步查询.");

		ZhangHParseResult zhanghParseRecord = (ZhangHParseResult) parseRecord;
		Scan scan = buildCountScanner(record, zhanghParseRecord);
		isAsynchronize(scan, record, zhanghParseRecord, result);
	}

	@Override
	public List<CorporateQueryItem> synQueryNextPage(QueryPageInfo pageInfo) throws Exception {
		// 定义存储查询结果的list集合
		List<CorporateQueryItem> itemList = new ArrayList<CorporateQueryItem>();

		Scan scanner = new Scan();
		scanner.addFamily(QueryConstants.HBASE_TABLE_FAMILY_BYTE);
		for (Entry<TabRowMap, Integer> info : pageInfo.getPageInfo().entrySet()) {
			LOG.debug("传入的startnum :rowkey是" + Bytes.toString(info.getKey().getRowKey()) + ":" + info.getValue());
			zhanghParserResult = info.getKey().getZhangHParseResult();
			scanner.setStartRow(info.getKey().getRowKey());
			// 动态的获取当前页需要查询的表
			setTableName(info.getKey().getTableName());
			List<CorporateQueryItem> tmpList = this.scan(scanner, info.getValue().intValue());
			if (tmpList != null && !tmpList.isEmpty()) {
				itemList.addAll(tmpList);
			}
		}
		return itemList;
	}

	@Override
	protected CorporateQueryItem parse(Result result) throws IOException {
		CorporateQueryItem entity = new CorporateQueryItem();
		byte[] family = QueryConstants.HBASE_TABLE_FAMILY_BYTE;

		entity.setRowKey(result.getRow());
		entity.setJIAOYM(Bytes.toString(result.getValue(family, AghmxField.JIAOYM)));
		entity.setJIOYRQ(Bytes.toString(result.getValue(family, AghmxField.JIOYRQ)));
		entity.setYNGYJG(Bytes.toString(result.getValue(family, AghmxField.YNGYJG)));
		entity.setJIEDBZ(Bytes.toString(result.getValue(family, AghmxField.JIEDBZ)));
		entity.setJIO1JE(Bytes.toString(result.getValue(family, AghmxField.JIO1JE)));
		entity.setZHHUYE(Bytes.toString(result.getValue(family, AghmxField.ZHHUYE)));
		entity.setYUEEFX(Bytes.toString(result.getValue(family, AghmxField.YUEEFX)));
		entity.setZHYODM(Bytes.toString(result.getValue(family, AghmxField.ZHYODM)));
		String pngzhh = Bytes.toString(result.getValue(family, AghmxField.PNGZHH));

		entity.setPNGZHH(pngzhh);
		// 设这凭证种类
		if (pngzhh != null && pngzhh.length() >= 3) {
			entity.setPNZZLX(pngzhh.substring(0, 3));
		} else {
			entity.setPNZZLX("");
		}
		entity.setKEHUZH(Bytes.toString(result.getValue(family, AghmxField.KEHUZH)));
		entity.setJIO1GY(Bytes.toString(result.getValue(family, AghmxField.JIO1GY)));
		entity.setSHOQGY(Bytes.toString(result.getValue(family, AghmxField.SHOQGY)));
		entity.setJIOYSJ(Bytes.toString(result.getValue(family, AghmxField.JIOYSJ)));
		entity.setCHBUBZ(Bytes.toString(result.getValue(family, AghmxField.CHBUBZ)));
		entity.setZHUJRQ(Bytes.toString(result.getValue(family, AghmxField.ZHUJRQ)));
		entity.setGUIYLS(Bytes.toString(result.getValue(family, AghmxField.GUIYLS)));
		entity.setDUIFZH(Bytes.toString(result.getValue(family, AghmxField.DUIFZH)));
		entity.setDUIFMC(Bytes.toString(result.getValue(family, AghmxField.DUIFMC)));
		entity.setZHANGH(Bytes.toString(result.getValue(family, AghmxField.ZHANGH)));
		entity.setCPZNXH(Bytes.toString(result.getValue(family, AghmxField.CPZNXH)));
		entity.setXNZHBZ(Bytes.toString(result.getValue(family, AghmxField.XNZHBZ)));
		entity.setSHJNCH(Bytes.toString(result.getValue(family, AghmxField.SHJNCH)));
		entity.setKHZHLX(Bytes.toString(result.getValue(family, AghmxField.KHZHLX)));

		// 设置账号的货币代号和钞汇标志

		entity.setHUOBDH(zhanghParserResult.getHUOBDH());
		entity.setCHUIBZ(zhanghParserResult.getCHUIBZ());
		return entity;
	}

	/**
	 * 不从AsyQueryRecord中获取zhangh信息,因为0781传递的是kehuzh.zhangh信息为空
	 * 
	 * @param record
	 * @return
	 */
	private Scan buildCountScanner(PybjyEO record, ZhangHParseResult zhanghResult) {
		String account = StringUtils.reverse(removeNRA(zhanghResult.getZHANGH()));
		Scan scanner = new Scan();
		scanner.addFamily(QueryConstants.HBASE_TABLE_FAMILY_BYTE);
		scanner.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AghmxField.SHJNCH);
		scanner.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AghmxField.JIO1JE);
		scanner.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AghmxField.ZHHUYE);
		scanner.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AghmxField.YUEEFX);
		scanner.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AghmxField.JIEDBZ);
		scanner.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AghmxField.JIOYRQ);
		scanner.setStartRow(makeStartRow(account, record.getStartDate()));
		scanner.setStopRow(makeStopRow(account, record.getEndDate()));
		return scanner;
	}

	private String removeNRA(String zhangh) {
		if (StringUtils.isBlank(zhangh)) {
			throw new InvalidParameterException("账号查询时账号为空");
		}
		if (zhangh.startsWith(FieldConstants.NRA_PRIFIX)) {
			int len = FieldConstants.NRA_PRIFIX.length();
			String account = zhangh.substring(len);
			return account;
		} else {
			return zhangh;
		}
	}

	/**
	 * 不从AsyQueryRecord中获取zhangh信息,因为0781传递的是kehuzh.zhangh信息为空
	 * 
	 * @param zhanghResult
	 * @return
	 */
	public Scan buildScanner(ZhangHParseResult zhanghResult) {
		String account = StringUtils.reverse(removeNRA(zhanghResult.getZHANGH()));

		Scan scan = new Scan();
		scan.setCaching(100);
		scan.addFamily(QueryConstants.HBASE_TABLE_FAMILY_BYTE);
		scan.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AghmxField.ZHANGH);
		scan.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AghmxField.CPZNXH);
		scan.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AghmxField.JIAOYM);
		scan.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AghmxField.JIOYRQ);
		scan.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AghmxField.ZHUJRQ);
		scan.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AghmxField.YNGYJG);
		scan.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AghmxField.JIEDBZ);
		scan.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AghmxField.JIO1JE);
		scan.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AghmxField.ZHHUYE);
		scan.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AghmxField.YUEEFX);
		scan.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AghmxField.ZHYODM);
		scan.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AghmxField.PNGZHH);
		scan.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AghmxField.KEHUZH);
		scan.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AghmxField.JIO1GY);
		scan.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AghmxField.SHOQGY);
		scan.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AghmxField.JIOYSJ);
		scan.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AghmxField.CHBUBZ);
		scan.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AghmxField.GUIYLS);
		scan.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AghmxField.DUIFZH);
		scan.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AghmxField.DUIFMC);
		scan.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AghmxField.XNZHBZ);
		scan.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AghmxField.SHJNCH);
		scan.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AghmxField.KHZHLX);
		scan.setStartRow(makeStartRow(account, zhanghResult.getRecord().getStartDate()));
		scan.setStopRow(makeStopRow(account, zhanghResult.getRecord().getEndDate()));

		return scan;
	}

	/**
	 * 创建开始key
	 * 
	 * @param account
	 * @param startTime
	 * @return
	 */
	private byte[] makeStartRow(String account, String startTime) {
		String fieldSpit = QueryConfUtils.getActiveConfig().get(QueryConstants.ROWKEY_SPLITTER, "|");
		String startRow = account + fieldSpit + QueryConstants.JILUZT_NORMAL + fieldSpit + startTime + fieldSpit + QueryConstants.MIN_NUM;
		return Bytes.toBytes(startRow);
	}

	/**
	 * 创建结束key
	 * 
	 * @param account
	 * @param endTime
	 * @return
	 */
	private byte[] makeStopRow(String account, String endTime) {
		String fieldSpit = QueryConfUtils.getActiveConfig().get(QueryConstants.ROWKEY_SPLITTER, "|");
		String stopRow = account + fieldSpit + QueryConstants.JILUZT_NORMAL + fieldSpit + endTime + fieldSpit + QueryConstants.MAX_CHAR;
		return Bytes.toBytes(stopRow);
	}

	/**
	 * 构造下一页的起始Key
	 * 
	 * @param zhangHParseResult
	 * @return
	 */
	public byte[] buildStopRow(ZhangHParseResult zhangHParseResult) {
		String spliter = QueryConfUtils.getActiveConfig().get(QueryConstants.ROWKEY_SPLITTER, "|");
		StringBuilder sRowKey = new StringBuilder();

		String account = StringUtils.reverse(removeNRA(zhangHParseResult.getZHANGH()));
		sRowKey.append(account).append(spliter).append("0").append(spliter).append(zhangHParseResult.getRecord().getEndDate())
				.append(spliter).append(QueryConstants.MAX_CHAR);
		LOG.debug("corporateItemQuery  endKey:" + sRowKey.toString());
		return Bytes.toBytes(sRowKey.toString());
	}

	@Override
	public PageHeader getPageHeader(ZhangHParseResult parseResult) {
		PageHeader header = new PageHeader();
		header.setCxqsrq(parseResult.getRecord().getStartDate());
		header.setCxzzrq(parseResult.getRecord().getEndDate());
		header.setGuiyuan(parseResult.getRecord().getJio1gy());
		header.setPrintDate(System.currentTimeMillis());
		header.setKhzwm(parseResult.getKehzwm());
		header.setKehhao(parseResult.getKehhao());
		header.setZhangh(parseResult.getZHANGH());
		header.setHbdh(parseResult.getHUOBDH());
		header.setChbz(parseResult.getCHUIBZ());
		header.setKhzh(parseResult.getKehuzh());
		header.setFkjgHao(parseResult.getZHYYNG());
		header.setFkjgName(parseResult.getJIGOMC());
		header.setLogoUrl(QueryConfUtils.LOGO_PATH);
		LOG.debug(header.toString());
		return header;
	}

	@Override
	public String getQueryTable() {
		return getTableName();
	}

	/**
	 * 判断是否启动异步查询
	 * 
	 * @param scan
	 * 
	 * @return
	 * @throws IOException
	 * @throws BalanceBrokedException
	 */
	public void isAsynchronize(Scan scan, PybjyEO record, ZhangHParseResult zhangHParseResult, AbstractQueryResult result) throws Exception {
		// 定义一个map存储当前输入的条件的分页情况，该map将会返回给调用者，以供调用者进行查询使用。
		int thresHold = QueryConfUtils.getActiveConfig().getInt(QueryConstants.SYNCHRONIZE_SWITCH_THRESHOLD, 1000);// 开始异步查询的阀值
		Map<Integer, QueryPageInfo> pageInfo = new HashMap<Integer, QueryPageInfo>();

		long total = 0;
		int pageNum = 1;// 页数
		int start;// 每页起始条数
		int currentPageContentNum = 0;// 当前页的记录条数
		ResultScanner scanner = null;
		HTableInterface queryTable = getHTable();
		try {
			scanner = queryTable.getScanner(scan);
			if (scanner == null) {
				return;
			}
			LOG.info("开始判断是否异步");
			Result[] res = scanner.next(record.getQueryNum());
			List<TransferItem> counterList = new ArrayList<TransferItem>();// 进行余额连续性校验使用的Map
			verifier.reset();
			while (res != null && res.length > 0) {
				total += res.length;
				currentPageContentNum += res.length;
				LOG.debug("当前遍历总条数：" + total);
				if (total > thresHold) {
					throw new AsynQueryException("查询进入异步");
				}

				// 进行逻辑分页的操作,
				// 如果满足一页，则进行分页存储
				start = (pageNum - 1) * record.getQueryNum() + 1;
				byte[] rowKey = res[0].getRow();
				TabRowMap trMap = new TabRowMap(rowKey, zhangHParseResult.getQueryTableName());
				trMap.setZhangHParseResult(zhangHParseResult);
				LOG.debug("页码：" + pageNum + "  起始条数：" + start + "rowkey：" + Bytes.toString(rowKey));
				if (pageInfo.get(start) == null) {
					Map<TabRowMap, Integer> page = new HashMap<TabRowMap, Integer>();
					page.put(trMap, res.length);
					QueryPageInfo currentPageInfo = new QueryPageInfo();
					currentPageInfo.setPageInfo(page);
					pageInfo.put(start, currentPageInfo);
				} else {
					pageInfo.get(start).getPageInfo().put(trMap, res.length);
				}

				if (currentPageContentNum >= record.getQueryNum()) {
					pageNum++;
					currentPageContentNum = 0;
				}

				// ================判断当前一页交易明细的余额是否连续================
				for (Result r : res) {
					String jio1je = Bytes.toString(r.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AghmxField.JIO1JE));
					String zhhuye = Bytes.toString(r.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AghmxField.ZHHUYE));
					String yueefx = Bytes.toString(r.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AghmxField.YUEEFX));
					String jiedbz = Bytes.toString(r.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AghmxField.JIEDBZ));
					String jioyrq = Bytes.toString(r.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AghmxField.JIOYRQ));
					TransferItem item = new BalanceItemCounter(jio1je, zhhuye, yueefx, jiedbz, jioyrq);
					counterList.add(item);
				}
				// 检查是否余额连续
				try {
					BalanceVerifyEntity recordInfo = QueryMethodUtils.getRecordPair(record);
					verifier.verify(recordInfo, zhangHParseResult, counterList);
					counterList.clear();
				} catch (Exception e) {
					String emsg = e.getMessage() + QueryConstants.ERROR_MSG_SUFFIX;
					throw new BalanceBrokedException(emsg);
				}
				res = scanner.next(record.getQueryNum() - currentPageContentNum);
			}
			result.setPageMap(pageInfo);
			record.setItemCount(total);
			LOG.info("判断是否异步完成，判断出的总的明细数量是：  " + total);
			LOG.info("解析出的页数:" + pageInfo.size());
		} finally {
			if (scanner != null)
				scanner.close();
			release(queryTable);
		}
	}
}