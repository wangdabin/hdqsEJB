package com.ceb.hdqs.action.query0773;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.FilterList.Operator;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;

import com.ceb.bank.htable.AbstractQuery;
import com.ceb.hdqs.action.query.ItemQueryProcessor;
import com.ceb.hdqs.entity.PybjyEO;
import com.ceb.hdqs.entity.result.AbstractQueryResult;
import com.ceb.hdqs.entity.result.ParseResult;
import com.ceb.hdqs.entity.result.ReplacementCardItemResult;
import com.ceb.hdqs.entity.result.ZhangHParseResult;
import com.ceb.hdqs.query.entity.Page;
import com.ceb.hdqs.query.entity.PageHeader;
import com.ceb.hdqs.query.entity.QueryDocumentContext;
import com.ceb.hdqs.query.entity.QueryPageInfo;
import com.ceb.hdqs.query.utils.QueryConfUtils;
import com.ceb.hdqs.query.utils.QueryConstants;

/**
 * 查询换卡登记过程中，通过 主账号（zhuuzh） 查询AKHZH查询出换卡历史记录
 * 
 * @author user
 * 
 */
public class Handle0773ItemQuery extends AbstractQuery<ReplacementCardItemResult> implements ItemQueryProcessor {

	private static final Log LOG = LogFactory.getLog(Handle0773ItemQuery.class);

	public Handle0773ItemQuery() {
		super(QueryConstants.TABLE_NAME_AKHZH_0773);
	}

	@Override
	public Page<ReplacementCardItemResult> nextPage(ZhangHParseResult zhangHResult, QueryDocumentContext documentContext) throws IOException {
		Configuration conf = QueryConfUtils.getActiveConfig();
		String rowKeySplit = conf.get(QueryConstants.ROWKEY_SPLITTER, "|");
		Scan scan = new Scan();
		// 创建查询AKHZH_0773的rowkey
		StringBuilder startBuilder = new StringBuilder();
		startBuilder.append(StringUtils.reverse(zhangHResult.getZHANGH().trim())).append(rowKeySplit).append(QueryConstants.MIN_NUM);

		StringBuilder stopBuilder = new StringBuilder();
		stopBuilder.append(StringUtils.reverse(zhangHResult.getZHANGH().trim())).append(rowKeySplit).append(QueryConstants.MAX_CHAR);

		scan.addFamily(QueryConstants.HBASE_TABLE_FAMILY_BYTE);
		scan.setStartRow(Bytes.toBytes(startBuilder.toString()));
		scan.setStopRow(Bytes.toBytes(stopBuilder.toString()));

		// 货币类型:人民币
		String HUOBDH = "01";
		// 活期账号

		// 设置货币代号过滤器
		SingleColumnValueFilter huobiFileter = new SingleColumnValueFilter(QueryConstants.HBASE_TABLE_FAMILY_BYTE, Bytes.toBytes("HUOBDH"), CompareOp.EQUAL, Bytes.toBytes(HUOBDH));

		// ZHHUXZ=‘0001’（普通活期）、
		String ZHHUXZ = "0001";
		SingleColumnValueFilter zhhuxzFileter = new SingleColumnValueFilter(QueryConstants.HBASE_TABLE_FAMILY_BYTE, Bytes.toBytes("ZHHUXZ"), CompareOp.EQUAL, Bytes.toBytes(ZHHUXZ));
		// CHUIBZ=‘0’ （钞户）、KHZHLX=‘1’（卡）”
		String CHUIBZ = "0";
		SingleColumnValueFilter chuibzFileter = new SingleColumnValueFilter(QueryConstants.HBASE_TABLE_FAMILY_BYTE, Bytes.toBytes("CHUIBZ"), CompareOp.EQUAL, Bytes.toBytes(CHUIBZ));

		// KHZHLX=‘1’（卡）”
		String KHZHLX = "1";
		SingleColumnValueFilter khzhlxFileter = new SingleColumnValueFilter(QueryConstants.HBASE_TABLE_FAMILY_BYTE, Bytes.toBytes("KHZHLX"), CompareOp.EQUAL, Bytes.toBytes(KHZHLX));

		FilterList list = new FilterList(Operator.MUST_PASS_ALL);
		list.addFilter(huobiFileter);
		list.addFilter(zhhuxzFileter);
		list.addFilter(chuibzFileter);
		list.addFilter(khzhlxFileter);
		scan.setFilter(list);
		LOG.debug("换卡明细查询的Scan是" + scan.toJSON());
		// 开始查询AKZH_0773，查询出该客户账号对应的换卡记录
		List<ReplacementCardItemResult> cardPeplacementItems = this.scan(scan);

		if (cardPeplacementItems == null || cardPeplacementItems.isEmpty()) {
			return null;
		}

		LOG.info("换卡明细查询的查询结果是" + cardPeplacementItems.size());
		Page<ReplacementCardItemResult> page = new Page<ReplacementCardItemResult>();
		page.setPageItem(cardPeplacementItems);

		return page;
	}

	@Override
	public void isAsynchronize(PybjyEO condition, ParseResult parseResult, AbstractQueryResult result) throws Exception {
		throw new UnsupportedOperationException("0773 not need Asynchronize!");
	}

	@Override
	protected ReplacementCardItemResult parse(Result result) throws IOException {

		// 1 KEHHAO│客户号 │ │VARCHAR2(10),
		// 2 KEHUZH│客户帐号 │ │VARCHAR2(21),
		// 3 KHZHLX│客户帐号类型 │ │VARCHAR2(1),
		// 4 ZHHUXZ│帐户性质 │ │VARCHAR2(4),
		// 5 HUOBDH│货币代号 │ │VARCHAR2(2),
		// 6 CHUIBZ│钞汇标志 │ │VARCHAR2(1),
		// 7 SHUNXH│顺序号 │ │VARCHAR2(4),
		// 8 ZHUZZH│主帐号 │ │VARCHAR2(21),
		// 9 SHJNCH│时间戳 │ │NUMBER(16,0)
		// 10 JILUZT│记录状态 │ │VARCHAR2(1)
		// 11 RIZHXH│日志序号 │ │VARCHAR2(25)

		ReplacementCardItemResult entity = new ReplacementCardItemResult();
		String KEHHAO = Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, Bytes.toBytes("KEHHAO")));
		entity.setKehhao(KEHHAO);
		String JILUZT = Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, Bytes.toBytes("JILUZT")));
		entity.setJiluzt(JILUZT);

		String LAOKHAO = Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, Bytes.toBytes("KEHUZH")));
		entity.setKahaoo(LAOKHAO);

		entity.setZhuzzh(Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, Bytes.toBytes("ZHUZZH"))));
		return entity;
	}

	@Override
	public PageHeader getPageHeader(ZhangHParseResult parseResult) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<? extends AbstractQueryResult> synQueryNextPage(QueryPageInfo pageInfo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getQueryTable() {
		return getTableName();
	}
}
