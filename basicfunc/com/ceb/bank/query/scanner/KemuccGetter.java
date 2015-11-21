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
import com.ceb.bank.context.KemuccContext;
import com.ceb.bank.hfield.HAzhjx0772Field;
import com.ceb.bank.htable.AbstractQuery;
import com.ceb.hdqs.query.utils.QueryMethodUtils;

public class KemuccGetter extends AbstractQuery<KemuccContext> {

	public KemuccGetter() {
		super(HBaseQueryConstants.TABLE_AZHJX0772);
	}

	public KemuccContext query(String zhangh) throws IOException {
		List<KemuccContext> list = scan(buildScan(zhangh));
		if (list == null || list.isEmpty()) {
			return null;
		}
		KemuccContext context = list.get(0);
		if (StringUtils.isNotBlank(context.getYngyjg())) {
			String jigomc = QueryMethodUtils.getJGMC(context.getYngyjg());
			context.setJigomc(jigomc);
		}
		return context;
	}

	@Override
	protected KemuccContext parse(Result result) throws IOException {
		KemuccContext bean = new KemuccContext();
		Map<String, byte[]> qualifiers = HAzhjx0772Field.getKemuccQualifiers();
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

	private Scan buildScan(String zhangh) {
		String rZhangh = StringUtils.reverse(zhangh);
		Scan scanner = new Scan();
		scanner.addFamily(HBaseQueryConstants.FAMILIER_BYTE_F);
		Map<String, byte[]> qualifiers = HAzhjx0772Field.getKemuccQualifiers();
		for (Map.Entry<String, byte[]> entry : qualifiers.entrySet()) {
			scanner.addColumn(HBaseQueryConstants.FAMILIER_BYTE_F, entry.getValue());
		}
		scanner.setStartRow(buildStartRowKey(rZhangh));
		scanner.setStopRow(buildStopRowKey(rZhangh));

		return scanner;
	}

	private byte[] buildStartRowKey(String zhangh) {
		String rowKey = zhangh + FieldConstants.ROWKEY_SPLITTER + FieldConstants.MIN_CHAR;
		return Bytes.toBytes(rowKey);
	}

	private byte[] buildStopRowKey(String zhangh) {
		String rowKey = zhangh + FieldConstants.ROWKEY_SPLITTER + FieldConstants.MAX_CHAR;
		return Bytes.toBytes(rowKey);
	}
}