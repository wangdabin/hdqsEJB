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
import com.ceb.bank.context.Output0781Context;
import com.ceb.bank.context.ZhanghContext;
import com.ceb.bank.hfield.HAdgmxField;
import com.ceb.bank.htable.AbstractQuery;
import com.ceb.bank.item.Item0781;
import com.ceb.bank.utils.HdqsCommonUtils;
import com.ceb.hdqs.entity.PybjyEO;

public abstract class Adgmx0781TxtScanner extends AbstractQuery<Item0781> {
	private static final Logger log = Logger.getLogger(Adgmx0781TxtScanner.class);

	public Adgmx0781TxtScanner() {
		super();
	}

	public void query(PybjyEO record, Output0781Context ctx, ZhanghContext zhanghCtx, int pageSize, boolean lastZhangh) throws Exception {
		log.info("开始生成文件");
		long total = 0;// 当前账号总记录数
		int pageNum = 0;// 当前账号总页数
		int currentPageCount = 0;// 当前页的记录条数
		ResultScanner scanner = null;
		HTableInterface hTable = null;
		try {
			log.info("解析账号" + zhanghCtx.getZhangh() + ",KEMUCC属性" + zhanghCtx.getKemucc().toString());
			HdqsCommonUtils.injectTblNameFromKemucc(record, zhanghCtx);
			if (StringUtils.isBlank(zhanghCtx.getTableName())) {
				log.error("无法从KEMUCC获取TableName.");
				return;
			}
			log.info("根据KEMUCC设置TableName: " + zhanghCtx.getTableName());
			setTableName(zhanghCtx.getTableName());
			hTable = getHTable();
			log.info("开始处理账号：" + zhanghCtx.getZhangh());

			Scan scan = buildScan(record, zhanghCtx);
			scan.setCaching(pageSize);
			scanner = hTable.getScanner(scan);
			if (scanner == null) {
				switchNoItems(record, ctx, lastZhangh);
				return;
			}
			List<Item0781> itemList = new ArrayList<Item0781>(pageSize);
			Result[] res = scanner.next(pageSize);
			if (res == null || res.length == 0) {
				switchNoItems(record, ctx, lastZhangh);
				return;
			}
			while (res != null && res.length > 0) {
				for (Result r : res) {// 解析记录
					Item0781 item = parse(r);
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

			if (lastZhangh) {
				close(record);// 输出账号汇总信息
			} else {
				closeZhangh(record);// 输出账号汇总信息
			}
		} finally {
			if (scanner != null)
				scanner.close();
			if (hTable != null)
				release(hTable);
		}
	}

	private void switchNoItems(PybjyEO record, Output0781Context ctx, boolean lastZhangh) throws Exception {
		ctx.setCurrentTotal(0);
		ctx.setCurrentPageNum(1);
		ctx.setPageNum(ctx.getPageNum() + 1);// 递增,每页打印都需要页数
		outputPage(record, null);// 生成文件
		log.debug("当前遍历总记录数：" + ctx.getTotal() + ",页数: " + ctx.getPageNum());

		if (lastZhangh) {
			close(record);// 输出汇总信息
		} else {
			closeZhangh(record);// 输出账号汇总信息
		}
	}

	private int switchPage(PybjyEO record, Output0781Context ctx, int pageNum, List<Item0781> itemList) throws Exception {
		if (pageNum > 0) {
			closePrePage();// 输出上一页尾部信息
		}
		ctx.setPageNum(ctx.getPageNum() + 1);// 递增,每页打印都需要页数
		ctx.setTotal(ctx.getTotal() + itemList.size());
		pageNum++;
		outputPage(record, itemList);// 生成文件
		log.debug("当前遍历总记录数：" + ctx.getTotal() + ",页数: " + ctx.getPageNum());
		return pageNum;
	}

	public abstract void closePrePage() throws Exception;

	public abstract void outputPage(PybjyEO record, List<Item0781> itemList) throws Exception;

	public abstract void closeZhangh(PybjyEO record) throws Exception;

	public abstract void close(PybjyEO record) throws Exception;

	@Override
	protected Item0781 parse(Result result) throws IOException {
		Item0781 bean = new Item0781();
		Map<String, byte[]> qualifiers = HAdgmxField.getDanweikaQualifiers();
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

	private Scan buildScan(PybjyEO record, ZhanghContext context) {
		String account = null;
		if (context.getZhangh().startsWith(FieldConstants.NRA_PRIFIX)) {
			int len = FieldConstants.NRA_PRIFIX.length();
			String zhangh = context.getZhangh().substring(len);
			account = StringUtils.reverse(zhangh);
		} else {
			account = StringUtils.reverse(context.getZhangh());
		}

		Scan scanner = new Scan();
		scanner.addFamily(HBaseQueryConstants.FAMILIER_BYTE_F);
		Map<String, byte[]> qualifiers = HAdgmxField.getDanweikaQualifiers();
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