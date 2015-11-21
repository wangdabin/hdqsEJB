package com.ceb.bank.query.scanner;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;

import com.ceb.bank.constants.HBaseQueryConstants;
import com.ceb.bank.context.RowkeyContext;
import com.ceb.bank.hfield.HAsdmxField;
import com.ceb.bank.htable.AbstractQuery;
import com.ceb.bank.item.Item0771;
import com.ceb.hdqs.entity.PybjyEO;

public class AsdmxScanner extends AbstractQuery<Item0771> {
	private static final Logger log = Logger.getLogger(AsdmxScanner.class);

	public AsdmxScanner() {
		super(HBaseQueryConstants.TABLE_ASDMX);
	}

	public List<Item0771> query(PybjyEO record, List<RowkeyContext> list) throws Exception {
		List<Item0771> itemList = new ArrayList<Item0771>();

		Scan scanner = buildScan();
		for (RowkeyContext info : list) {
			log.debug("传入的StartNum:" + record.getStartNum() + ",rowkey:" + Bytes.toString(info.getRowkey()) + ":" + info.getPageCount());
			scanner.setStartRow(info.getRowkey());
			List<Item0771> tmpList = this.scan(scanner, info.getPageCount());
			if (tmpList == null || tmpList.isEmpty()) {
				continue;
			}
			itemList.addAll(tmpList);
		}
		return itemList;
	}

	@Override
	protected Item0771 parse(Result result) throws IOException {
		Item0771 bean = new Item0771();
		Map<String, byte[]> qualifiers = HAsdmxField.getMxQualifiers();
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

	private Scan buildScan() {
		Scan scanner = new Scan();
		scanner.addFamily(HBaseQueryConstants.FAMILIER_BYTE_F);
		Map<String, byte[]> qualifiers = HAsdmxField.getMxQualifiers();
		for (Map.Entry<String, byte[]> entry : qualifiers.entrySet()) {
			scanner.addColumn(HBaseQueryConstants.FAMILIER_BYTE_F, entry.getValue());
		}
		return scanner;
	}
}