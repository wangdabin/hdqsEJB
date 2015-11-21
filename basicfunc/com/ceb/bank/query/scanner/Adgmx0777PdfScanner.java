package com.ceb.bank.query.scanner;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;

import com.ceb.bank.constants.FieldConstants;
import com.ceb.bank.constants.HBaseQueryConstants;
import com.ceb.bank.context.ZhanghContext;
import com.ceb.bank.hfield.HAdgmxField;
import com.ceb.bank.htable.AbstractQuery;
import com.ceb.bank.item.Item0770;
import com.ceb.hdqs.entity.PybjyEO;

public class Adgmx0777PdfScanner extends AbstractQuery<Item0770> {

	public Adgmx0777PdfScanner() {
		super();
	}

	/**
	 * 每次多取一条记录，用来提前预知是否查询完成
	 * 
	 * @param record
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public List<Item0770> query(PybjyEO record, ZhanghContext zhanghCtx, int pageSize) throws Exception {
		setTableName(zhanghCtx.getTableName());
		String account = null;
		if (zhanghCtx.getZhangh().startsWith(FieldConstants.NRA_PRIFIX)) {
			int len = FieldConstants.NRA_PRIFIX.length();
			String zhangh = zhanghCtx.getZhangh().substring(len);
			account = StringUtils.reverse(zhangh);
		} else {
			account = StringUtils.reverse(zhanghCtx.getZhangh());
		}

		int limit = pageSize + 1;
		Scan scanner = buildScan(record);
		scanner.setCaching(limit);
		if (zhanghCtx.isFirstQuery()) {
			scanner.setStartRow(buildStartRowKey(account, record.getStartDate()));
			zhanghCtx.setFirstQuery(false);
		} else {
			scanner.setStartRow(zhanghCtx.getLastRowkey());
		}
		scanner.setStopRow(buildStopRowKey(account, record.getEndDate()));
		List<Item0770> itemList = scan(scanner, limit);

		return itemList;
	}

	@Override
	protected Item0770 parse(Result result) throws IOException {
		Item0770 bean = new Item0770();
		bean.setRowkey(result.getRow());

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
		Scan scanner = new Scan();
		scanner.addFamily(HBaseQueryConstants.FAMILIER_BYTE_F);
		Map<String, byte[]> qualifiers = HAdgmxField.getMxQualifiers();
		for (Map.Entry<String, byte[]> entry : qualifiers.entrySet()) {
			scanner.addColumn(HBaseQueryConstants.FAMILIER_BYTE_F, entry.getValue());
		}
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