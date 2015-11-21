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
import com.ceb.bank.hfield.HBkhhdField;
import com.ceb.bank.htable.AbstractQuery;
import com.ceb.bank.item.Item9019;
import com.ceb.hdqs.entity.PybjyEO;

public class BkhhdScanner extends AbstractQuery<Item9019>{
	private static final Logger log = Logger.getLogger(BkhhdScanner.class);

	public BkhhdScanner() {
		super(HBaseQueryConstants.TABLE_BKHHD);
	}

	public List<Item9019> query(PybjyEO record, List<RowkeyContext> list) throws Exception {
		List<Item9019> itemList = new ArrayList<Item9019>();

		Scan scanner = buildScan();
		for (RowkeyContext info : list) {
			log.debug("传入的StartNum:" + record.getStartNum() + ",rowkey:" + Bytes.toString(info.getRowkey()) + ":" + info.getPageCount());
			scanner.setStartRow(info.getRowkey());
			List<Item9019> tmpList = this.scan(scanner, info.getPageCount());
			if (tmpList == null || tmpList.isEmpty()) {
				continue;
			}
			itemList.addAll(tmpList);
		}
		return itemList;
	}
	
	
	public List<Item9019> query(List<String> rowkeys) throws Exception {
		List<Item9019> itemList = new ArrayList<Item9019>();

		for (String info : rowkeys) {
			Item9019 tmpItem = this.doGet(info);
			if (tmpItem == null) {
				continue;
			}
			itemList.add(tmpItem);
		}
		return itemList;
	}
	

	@Override
	protected Item9019 parse(Result result) throws IOException {
		Item9019 bean = new Item9019();
		Map<String, byte[]> qualifiers = HBkhhdField.getMxQualifiers();
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
		Map<String, byte[]> qualifiers = HBkhhdField.getMxQualifiers();
		for (Map.Entry<String, byte[]> entry : qualifiers.entrySet()) {
			scanner.addColumn(HBaseQueryConstants.FAMILIER_BYTE_F, entry.getValue());
		}
		return scanner;
	}

}
