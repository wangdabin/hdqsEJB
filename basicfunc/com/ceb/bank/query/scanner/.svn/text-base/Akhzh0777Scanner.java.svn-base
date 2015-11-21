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
import com.ceb.bank.hfield.HAkhzhField;
import com.ceb.bank.htable.AbstractQuery;
import com.ceb.hdqs.entity.PybjyEO;

public class Akhzh0777Scanner extends AbstractQuery<ZhanghContext> {

	public Akhzh0777Scanner() {
		super(HBaseQueryConstants.TABLE_AKHZH);
	}

	public List<ZhanghContext> query(PybjyEO record) throws IOException {
		List<ZhanghContext> zhanghList = scan(buildScan(record));
		if (zhanghList == null || zhanghList.isEmpty()) {
			return null;
		}

		return zhanghList;
	}

	@Override
	protected ZhanghContext parse(Result result) throws IOException {
		ZhanghContext bean = new ZhanghContext();
		Map<String, byte[]> qualifiers = HAkhzhField.getMxQualifiers();
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
		String kehuzh = StringUtils.reverse(record.getKehuzh());
		Scan scanner = null;
		scanner = new Scan();
		scanner.setStartRow(buildStartRowKey(kehuzh, record.getKhzhlx()));
		scanner.setStopRow(buildStopRowKey(kehuzh, record.getKhzhlx()));

		scanner.addFamily(HBaseQueryConstants.FAMILIER_BYTE_F);
		Map<String, byte[]> qualifiers = HAkhzhField.getMxQualifiers();
		for (Map.Entry<String, byte[]> entry : qualifiers.entrySet()) {
			scanner.addColumn(HBaseQueryConstants.FAMILIER_BYTE_F, entry.getValue());
		}
		return scanner;
	}

	private byte[] buildStartRowKey(String kehuzh, String khzhlx) {
		String rowKey = kehuzh + FieldConstants.ROWKEY_SPLITTER + khzhlx + FieldConstants.ROWKEY_SPLITTER + FieldConstants.MIN_CHAR;
		return Bytes.toBytes(rowKey);
	}

	private byte[] buildStopRowKey(String kehuzh, String khzhlx) {
		String rowKey = kehuzh + FieldConstants.ROWKEY_SPLITTER + khzhlx + FieldConstants.ROWKEY_SPLITTER + FieldConstants.MAX_CHAR;
		return Bytes.toBytes(rowKey);
	}
}