package com.ceb.hdqs.action.query;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;

import com.ceb.bank.htable.AbstractQuery;
import com.ceb.hdqs.action.query.exception.AsynQueryException;
import com.ceb.hdqs.action.query.exception.BalanceBrokedException;
import com.ceb.hdqs.action.query.verification.ItemBalanceLxVerifierImpl;
import com.ceb.hdqs.entity.PybjyEO;
import com.ceb.hdqs.entity.field.AghmxField;
import com.ceb.hdqs.entity.result.AbstractQueryResult;
import com.ceb.hdqs.entity.result.BalanceItemCounter;
import com.ceb.hdqs.entity.result.ZhangHParseResult;
import com.ceb.hdqs.query.entity.BalanceVerifyEntity;
import com.ceb.hdqs.query.entity.QueryPageInfo;
import com.ceb.hdqs.query.entity.TabRowMap;
import com.ceb.hdqs.query.entity.TransferItem;
import com.ceb.hdqs.query.utils.QueryConstants;
import com.ceb.hdqs.query.utils.QueryMethodUtils;

public abstract class AbstractPrivateItemQuery<T> extends AbstractQuery<T> {
	private Log log;
	private ItemBalanceLxVerifierImpl verifier = new ItemBalanceLxVerifierImpl();

	public AbstractPrivateItemQuery(String tableName) {
		super(tableName);
	}

	/**
	 * 判断多个账号的情况下是否启动异步查询，如果不进入异步查询则对rowkey进行逻辑分页，并返回逻辑分页的结果
	 * 
	 * @param scan
	 * 
	 * @return
	 * @throws IOException
	 */
	public void isAsynchronize(int thresHold, Map<ZhangHParseResult, Scan> scans, AbstractQueryResult result, PybjyEO record) throws Exception {
		// 定义一个map存储当前输入的条件的分页情况，改map将会返回给调用者，以供调用者进行查询使用。
		Map<Integer, QueryPageInfo> pageInfo = new HashMap<Integer, QueryPageInfo>();

		// 进行余额连续性校验使用的Map
		List<TransferItem> counterList = new ArrayList<TransferItem>();
		long total = 0;
		int pageNum = 1;// 页数
		int currentPageKey;// 每页的第一条在查询结果中的位置((pageNum - 1) * numPerPage + 1)
		int currentPageContentNum = 0;// 每页已有的记录条数
		ResultScanner resscanner = null;
		HTableInterface queryTable = this.getHTable();
		try {
			for (Entry<ZhangHParseResult, Scan> scanner : scans.entrySet()) {
				verifier.reset();
				long currentTotal = 0;// 当前账号的明细条数
				Scan scan = scanner.getValue();
				int numPerPage = record.getQueryNum();
				resscanner = queryTable.getScanner(scan);
				if (resscanner == null) {
					continue;
				}
				Result[] res = resscanner.next(numPerPage - currentPageContentNum);
				/*
				 * 通过while循环查询出当前账号的明细条数
				 */
				while (res != null && res.length > 0) {
					// 计算总量，以判断是否进入异步查询
					int tmp = res.length;
					total += tmp;
					currentTotal += tmp;
					currentPageContentNum += tmp;
					log.debug("本次scan已获取数量：" + currentTotal);
					if (total > thresHold) {
						throw new AsynQueryException("查询进入异步");
					}
					// 进行逻辑分页的操作,
					// 如果满足一页，则进行分页存储
					byte[] rowKey = res[0].getRow();
					String tableName = scanner.getKey().getQueryExectuer().getQueryTable();
					TabRowMap trMap = new TabRowMap(rowKey, tableName);
					trMap.setZhangHParseResult(scanner.getKey());
					currentPageKey = (pageNum - 1) * numPerPage + 1;
					if (pageInfo.get(currentPageKey) == null) {
						Map<TabRowMap, Integer> page = new HashMap<TabRowMap, Integer>();
						page.put(trMap, tmp);
						QueryPageInfo currentPageInfo = new QueryPageInfo();
						currentPageInfo.setPageInfo(page);
						pageInfo.put(currentPageKey, currentPageInfo);
					} else {
						pageInfo.get(currentPageKey).getPageInfo().put(trMap, tmp);
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
					try {
						BalanceVerifyEntity recordInfo = QueryMethodUtils.getRecordPair(scanner.getKey().getRecord());
						verifier.verify(recordInfo, scanner.getKey(), counterList);
						// 清空list集合
						counterList.clear();
					} catch (BalanceBrokedException e) {
						String emsg = e.getMessage() + QueryConstants.ERROR_MSG_SUFFIX;
						throw new BalanceBrokedException(emsg);
					}

					if (currentPageContentNum >= numPerPage) {// 换页
						pageNum++;
						currentPageContentNum = 0;
					}
					res = resscanner.next(numPerPage - currentPageContentNum);
				}
				scanner.getKey().setItemCount(currentTotal);
			}
			result.setPageMap(pageInfo);
			record.setItemCount(total);
			log.info("判断是否异步完成，判断出的总的明细数量是：  " + total);
			log.info("解析出的页数:" + pageInfo.size());
		} finally {
			if (resscanner != null)
				resscanner.close();
			release(queryTable);
		}
	}

	public Log getLog() {
		return log;
	}

	public void setLog(Log log) {
		this.log = log;
	}
}