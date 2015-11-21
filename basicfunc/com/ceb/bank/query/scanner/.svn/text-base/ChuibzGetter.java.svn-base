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
import com.ceb.bank.hfield.HAkhzh0773Field;
import com.ceb.bank.htable.AbstractQuery;

/**
 * 在AZHJX表查询KEHUZH、KHZHLX、CHUIBZ为空的时候,查询AKHZH表获取对应值
 */
public class ChuibzGetter extends AbstractQuery<ZhanghContext> {
	public ChuibzGetter() {
		super(HBaseQueryConstants.TABLE_AKHZH_0773);
	}

	/**
	 * 获取指定账号的钞汇标志
	 * 
	 * @param zhangh
	 *            需要查询钞汇标志的账号
	 * @return ZhanghContext 输入账号的钞汇标志，如果没有则返回 null
	 * @throws IOException
	 */
	public ZhanghContext query(String zhangh) throws IOException {
		List<ZhanghContext> list = scan(buildScan(zhangh));
		if (list == null || list.isEmpty()) {
			return null;
		}
		return list.get(0);
	}

	@Override
	protected ZhanghContext parse(Result result) throws IOException {
		ZhanghContext bean = new ZhanghContext();
		Map<String, byte[]> qualifiers = HAkhzh0773Field.getMxQualifiers();
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
		Map<String, byte[]> qualifiers = HAkhzh0773Field.getMxQualifiers();
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