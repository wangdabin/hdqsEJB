package com.ceb.bank.query.scanner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;

import com.ceb.bank.context.RowkeyAndTblContext;
import com.ceb.bank.context.RowkeyContext;
import com.ceb.bank.context.ZhanghContext;
import com.ceb.bank.htable.AbstractQuery;
import com.ceb.bank.utils.HdqsCommonUtils;
import com.ceb.hdqs.entity.PybjyEO;

public abstract class AbstractCacheScanner<T> extends AbstractQuery<T> {
	private static final Logger log = Logger.getLogger(AbstractCacheScanner.class);

	/**
	 * 调用getHTable()方式前,必须调用setTableName(String tableName)
	 * 
	 * @param conf
	 */
	public AbstractCacheScanner() {
		super();
	}

	public AbstractCacheScanner(String tableName) {
		super(tableName);
	}

	protected void cachePageRowkeyAndSize(PybjyEO record, Map<Integer, List<RowkeyContext>> pageInfo, int pageNum, Result[] res) {
		HdqsCommonUtils.cachePageRowkeyAndSize(record, pageInfo, pageNum, res);
	}

	protected void cachePageRowkeyAndTblname(PybjyEO record, ZhanghContext ctx, Map<Integer, List<RowkeyAndTblContext>> pageInfo,
			int pageNum, Result[] res) {
		int start = (pageNum - 1) * record.getQueryNum() + 1;
		byte[] rowKey = res[0].getRow();
		RowkeyAndTblContext rowkeyCtx = new RowkeyAndTblContext();
		rowkeyCtx.setRowkey(rowKey);
		rowkeyCtx.setPageCount(res.length);
		rowkeyCtx.setTableName(this.getTableName());
		// 明细里面没有HUOBDH,CHUIBZ,需要注入到Item0781明细中
		rowkeyCtx.setHuobdh(ctx.getHuobdh());
		rowkeyCtx.setChuibz(ctx.getChuibz());

		log.debug("页码：" + pageNum + ",起始条数：" + start + ",rowkey：" + Bytes.toString(rowKey));
		if (pageInfo.get(start) == null) {
			List<RowkeyAndTblContext> list = new ArrayList<RowkeyAndTblContext>();
			list.add(rowkeyCtx);
			pageInfo.put(start, list);
		} else {
			pageInfo.get(start).add(rowkeyCtx);
		}
	}
}
