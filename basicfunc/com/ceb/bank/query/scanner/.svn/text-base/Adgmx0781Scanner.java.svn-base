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
import com.ceb.bank.context.RowkeyAndTblContext;
import com.ceb.bank.hfield.HAdgmxField;
import com.ceb.bank.htable.AbstractQuery;
import com.ceb.bank.item.Item0781;
import com.ceb.hdqs.entity.PybjyEO;

public class Adgmx0781Scanner extends AbstractQuery<Item0781> {
	private static final Logger log = Logger.getLogger(Adgmx0781Scanner.class);

	public Adgmx0781Scanner() {
		super();
	}

	public List<Item0781> query(PybjyEO record, List<RowkeyAndTblContext> list) throws Exception {
		List<Item0781> itemList = new ArrayList<Item0781>();

		Scan scanner = buildScan();
		for (RowkeyAndTblContext info : list) {
			log.debug("查询表:" + info.getTableName() + ",StartNum:" + record.getStartNum() + ",rowkey:" + Bytes.toString(info.getRowkey())
					+ ":" + info.getPageCount());
			scanner.setStartRow(info.getRowkey());
			this.setTableName(info.getTableName());
			List<Item0781> tmpList = this.scan(scanner, info.getPageCount());
			if (tmpList == null || tmpList.isEmpty()) {
				continue;
			}
			// 明细里面没有HUOBDH,CHUIBZ,需要缓存注入到Item0781明细中
			for (Item0781 item : tmpList) {
				item.setHuobdh(info.getHuobdh());
				item.setChuibz(info.getChuibz());
			}
			itemList.addAll(tmpList);
		}
		return itemList;
	}

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

	private Scan buildScan() {
		Scan scanner = new Scan();
		scanner.addFamily(HBaseQueryConstants.FAMILIER_BYTE_F);
		Map<String, byte[]> qualifiers = HAdgmxField.getDanweikaQualifiers();
		for (Map.Entry<String, byte[]> entry : qualifiers.entrySet()) {
			scanner.addColumn(HBaseQueryConstants.FAMILIER_BYTE_F, entry.getValue());
		}
		return scanner;
	}
}