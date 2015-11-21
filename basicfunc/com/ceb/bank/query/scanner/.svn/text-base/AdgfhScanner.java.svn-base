package com.ceb.bank.query.scanner;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;

import com.ceb.bank.constants.HBaseQueryConstants;
import com.ceb.bank.context.ZhanghContext;
import com.ceb.bank.hfield.HAdgfhField;
import com.ceb.bank.htable.AbstractQuery;
import com.ceb.hdqs.entity.PybjyEO;
import com.ceb.hdqs.query.utils.QueryMethodUtils;

public class AdgfhScanner extends AbstractQuery<ZhanghContext> {
	private static final Logger log = Logger.getLogger(AdgfhScanner.class);

	public AdgfhScanner(String tableName) {
		super(tableName);
	}

	/**
	 * 查询账号信息
	 * 
	 * @param account
	 * @return
	 * @throws IOException
	 */
	public ZhanghContext query(PybjyEO record) throws IOException {
		log.info("Start parse " + getTableName());
		ZhanghContext context = doGet(buildGet(record));
		log.info("Parse " + getTableName() + " complete.");
		if (context == null) {
			return null;
		}
		// 如果图形前段回显了客户中文名，但是给解析出的客户中文名不一致，则直接返回空
		if (StringUtils.isNotBlank(record.getKhzwm()) && !QueryMethodUtils.checkKehuzwm(record.getKhzwm(), context.getKehzwm())) {
			log.warn("图形前段回显客户中文名" + record.getKhzwm() + ",与解析出的客户中文名" + context.getKehzwm() + "不一致，直接返回空.");
			return null;
		}
		return context;
	}

	@Override
	protected ZhanghContext parse(Result result) throws IOException {
		ZhanghContext bean = new ZhanghContext();
		Map<String, byte[]> qualifiers = HAdgfhField.getZhanghQualifiers();
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
			log.error(e.getMessage(), e);
		} catch (InvocationTargetException e) {
			log.error(e.getMessage(), e);
		}
		return bean;
	}

	public Get buildGet(PybjyEO record) {
		Get getter = new Get(buildRowKey(record));
		getter.addFamily(HBaseQueryConstants.FAMILIER_BYTE_F);
		Map<String, byte[]> qualifiers = HAdgfhField.getZhanghQualifiers();
		for (Map.Entry<String, byte[]> entry : qualifiers.entrySet()) {
			getter.addColumn(HBaseQueryConstants.FAMILIER_BYTE_F, entry.getValue());
		}
		return getter;
	}

	private byte[] buildRowKey(PybjyEO record) {
		String rowkey = StringUtils.reverse(record.getZhangh());
		return Bytes.toBytes(rowkey);
	}
}