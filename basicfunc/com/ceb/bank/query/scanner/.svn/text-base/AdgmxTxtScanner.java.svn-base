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
import com.ceb.bank.context.Output0770Context;
import com.ceb.bank.hfield.HAdgmxField;
import com.ceb.bank.htable.AbstractQuery;
import com.ceb.bank.item.Item0770;
import com.ceb.hdqs.entity.PybjyEO;

public abstract class AdgmxTxtScanner extends AbstractQuery<Item0770> {
	private static final Logger log = Logger.getLogger(AdgmxTxtScanner.class);

	public AdgmxTxtScanner(String tableName) {
		super(tableName);
	}

	public void query(PybjyEO record, final Output0770Context ctx, int pageSize) throws Exception {
		log.info("开始生成文件");
		long total = 0;// 当前账号总记录数
		int pageNum = 0;// 当前账号总页数
		int currentPageCount = 0;// 当前页的记录条数
		ResultScanner scanner = null;
		HTableInterface hTable = getHTable();
		try {
			Scan scan = buildScan(record);
			scan.setCaching(pageSize);
			scanner = hTable.getScanner(scan);
			if (scanner == null) {
				switchNoItemPage(record, ctx);
				return;
			}
			List<Item0770> itemList = new ArrayList<Item0770>(pageSize);
			Result[] res = scanner.next(pageSize);
			if (res == null || res.length == 0) {
				switchNoItemPage(record, ctx);
				return;
			}
			while (res != null && res.length > 0) {
				for (Result r : res) {// 解析记录
					Item0770 item = parse(r);
					if (item != null) {
						itemList.add(item);
						total++;
						currentPageCount++;
					}
				}
				if (currentPageCount >= pageSize) {// 换页
					pageNum = switchPage(record, ctx, pageNum, itemList);
					itemList.clear();
					currentPageCount = 0;
				}
				res = scanner.next(pageSize - currentPageCount);
			}
			if (itemList.size() > 0) {// 最后一页不满页时处理
				pageNum = switchPage(record, ctx, pageNum, itemList);
			}
			ctx.setCurrentTotal(total);
			ctx.setCurrentPageNum(pageNum);

			close(record);// 输出账号汇总信息
			record.setItemCount(total);
		} finally {
			if (scanner != null)
				scanner.close();
			release(hTable);
		}
	}

	private void switchNoItemPage(PybjyEO record, Output0770Context ctx) throws Exception {
		ctx.setCurrentTotal(0);
		ctx.setCurrentPageNum(1);
		ctx.setPageNum(ctx.getPageNum() + 1);// 递增,每页打印都需要页数
		outputPage(record, null);// 生成文件
		log.debug("当前遍历总记录数：" + ctx.getTotal() + ",页数: " + ctx.getPageNum());

		close(record);// 输出账号汇总信息
	}

	private int switchPage(PybjyEO record, Output0770Context ctx, int pageNum, List<Item0770> itemList) throws Exception {
		if (pageNum > 0) {
			closePrePage();// 输出上一页尾部信息
		}
		ctx.setPageNum(ctx.getPageNum() + 1);// 递增,每页打印需要都需要页数
		ctx.setTotal(ctx.getTotal() + itemList.size());
		pageNum++;
		outputPage(record, itemList);// 生成文件
		log.debug("当前遍历总记录数：" + ctx.getTotal() + ",页数: " + ctx.getPageNum());
		return pageNum;
	}

	public abstract void closePrePage() throws Exception;

	public abstract void outputPage(PybjyEO record, List<Item0770> itemList) throws Exception;

	public abstract void close(PybjyEO record) throws Exception;

	@Override
	protected Item0770 parse(Result result) throws IOException {
		Item0770 bean = new Item0770();
		Map<String, byte[]> qualifiers = HAdgmxField.getMxQualifiers();
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

	private Scan buildScan(PybjyEO record) {
		String account = null;
		if (record.getZhangh().startsWith(FieldConstants.NRA_PRIFIX)) {
			int len = FieldConstants.NRA_PRIFIX.length();
			String zhangh = record.getZhangh().substring(len);
			account = StringUtils.reverse(zhangh);
		} else {
			account = StringUtils.reverse(record.getZhangh());
		}

		Scan scanner = new Scan();
		scanner.addFamily(HBaseQueryConstants.FAMILIER_BYTE_F);
		Map<String, byte[]> qualifiers = HAdgmxField.getMxQualifiers();
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
		return Bytes.toBytes(rowKey);
	}

	private byte[] buildStopRowKey(String account, String endTime) {
		String rowKey = account + FieldConstants.ROWKEY_SPLITTER + FieldConstants.JILUZT_NORMAL + FieldConstants.ROWKEY_SPLITTER + endTime
				+ FieldConstants.ROWKEY_SPLITTER + FieldConstants.MAX_CHAR;
		return Bytes.toBytes(rowKey);
	}
}