package com.ceb.bank.query.scanner;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;

import com.ceb.bank.constants.FieldConstants;
import com.ceb.bank.constants.HBaseQueryConstants;
import com.ceb.bank.context.KehuzhContext;
import com.ceb.bank.context.LxContext;
import com.ceb.bank.context.RowkeyContext;
import com.ceb.bank.context.ZhanghContext;
import com.ceb.bank.hfield.HAsdmxField;
import com.ceb.bank.query.lx.ILxVerifier;
import com.ceb.bank.query.lx.LxVerifierImpl;
import com.ceb.hdqs.action.query.exception.AsynQueryException;
import com.ceb.hdqs.entity.PybjyEO;
import com.ceb.hdqs.query.utils.QueryConfUtils;
import com.ceb.hdqs.query.utils.QueryConstants;

public class AsdmxLxScanner extends AbstractLxScanner {
	private static final Logger log = Logger.getLogger(AsdmxLxScanner.class);
	private ILxVerifier verifier = new LxVerifierImpl();

	public AsdmxLxScanner() {
		super(HBaseQueryConstants.TABLE_ASDMX);
	}

	/**
	 * 加载起始笔数与(rowKey,pageCount)之间对应关系
	 * 
	 * @param record
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public Map<Integer, List<RowkeyContext>> loadPage(PybjyEO record, KehuzhContext context) throws Exception {
		int thresHold = QueryConfUtils.getActiveConfig().getInt(QueryConstants.SYNCHRONIZE_SWITCH_THRESHOLD, 1000);// 开始异步查询的阀值
		Map<Integer, List<RowkeyContext>> pageInfo = new HashMap<Integer, List<RowkeyContext>>();

		log.info("开始检测是否进行异步查询....");
		List<ZhanghContext> zhanghList = context.getList();
		long total = 0;
		int pageNum = 1;// 页数
		int currentPageCount = 0;// 当前页的记录条数
		ResultScanner scanner = null;
		HTableInterface hTable = getHTable();
		try {
			for (ZhanghContext ctx : zhanghList) {
				log.info("开始处理账号：" + ctx.getZhangh());
				Scan scan = buildScan(record, ctx);
				scan.setCaching(record.getQueryNum());
				scanner = hTable.getScanner(scan);
				if (scanner == null) {
					continue;
				}

				verifier.reset();
				List<LxContext> lxList = new ArrayList<LxContext>();// 进行余额连续性校验使用的List
				long currentTotal = 0;// 当前账号的明细条数
				Result[] res = scanner.next(record.getQueryNum() - currentPageCount);
				while (res != null && res.length > 0) {
					total += res.length;
					currentTotal += res.length;
					currentPageCount += res.length;
					log.debug("遍历总记录数：" + total + ",当前账号遍历记录数:" + currentTotal);
					if (total > thresHold) {
						throw new AsynQueryException("查询进入异步");
					}
					cachePageRowkeyAndSize(record, pageInfo, pageNum, res);// 分页存储
					for (Result r : res) {// 解析记录
						LxContext lxContext = parse(r);
						if (lxContext != null) {
							lxList.add(lxContext);
						}
					}
					if (lxList.size() > 0) {
						verifyLx(record, verifier, lxList);
						lxList.clear();
					}
					// 换页并进行连续性校验
					if (currentPageCount >= record.getQueryNum()) {
						pageNum++;
						currentPageCount = 0;
					}

					res = scanner.next(record.getQueryNum() - currentPageCount);
				}
			}
			record.setItemCount(total);
			log.info("判断是否异步完成,总明细数:" + total + ",总页数:" + pageInfo.size());
		} finally {
			if (scanner != null)
				scanner.close();
			release(hTable);
		}

		return pageInfo;

	}

	@Override
	protected LxContext parse(Result result) throws IOException {
		LxContext bean = new LxContext();
		Map<String, byte[]> qualifiers = HAsdmxField.getLxQualifiers();
		Map<String, String> properties = new HashMap<String, String>();
		for (Map.Entry<String, byte[]> entry : qualifiers.entrySet()) {
			String key = entry.getKey();
			byte[] value = result.getValue(HBaseQueryConstants.FAMILIER_BYTE_F, entry.getValue());
			if (value != null) {
				properties.put(key, Bytes.toString(value).trim());
			} else {
				properties.put(key, "");
			}
		}
		try {
			BeanUtils.populate(bean, properties);
		} catch (IllegalAccessException e) {
			return null;
		} catch (InvocationTargetException e) {
			return null;
		}
		return bean;
	}

	private Scan buildScan(PybjyEO record, ZhanghContext ctx) {
		String account = StringUtils.reverse(ctx.getZhangh());
		Scan scanner = new Scan();
		scanner.addFamily(HBaseQueryConstants.FAMILIER_BYTE_F);
		Map<String, byte[]> qualifiers = HAsdmxField.getLxQualifiers();
		for (Map.Entry<String, byte[]> entry : qualifiers.entrySet()) {
			scanner.addColumn(HBaseQueryConstants.FAMILIER_BYTE_F, entry.getValue());
		}

		scanner.setStartRow(buildStartRowKey(account, record.getStartDate()));
		scanner.setStopRow(buildStopRowKey(account, record.getEndDate()));
		return scanner;
	}

	private byte[] buildStartRowKey(String account, String startTime) {
		String rowKey = account + FieldConstants.ROWKEY_SPLITTER + FieldConstants.JILUZT_NORMAL + FieldConstants.ROWKEY_SPLITTER
				+ startTime + FieldConstants.ROWKEY_SPLITTER + FieldConstants.MIN_CHAR;
		log.debug("Start rowkey: " + rowKey);
		return Bytes.toBytes(rowKey);
	}

	private byte[] buildStopRowKey(String account, String endTime) {
		String rowKey = account + FieldConstants.ROWKEY_SPLITTER + FieldConstants.JILUZT_NORMAL + FieldConstants.ROWKEY_SPLITTER + endTime
				+ FieldConstants.ROWKEY_SPLITTER + FieldConstants.MAX_CHAR;
		log.debug("Stop rowkey: " + rowKey);
		return Bytes.toBytes(rowKey);
	}
}