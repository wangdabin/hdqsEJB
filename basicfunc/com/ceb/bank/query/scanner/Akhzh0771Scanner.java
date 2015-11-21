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
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.FilterList.Operator;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;

import com.ceb.bank.constants.FieldConstants;
import com.ceb.bank.constants.HBaseQueryConstants;
import com.ceb.bank.context.KehuzhContext;
import com.ceb.bank.context.YngyjgContext;
import com.ceb.bank.context.ZhanghContext;
import com.ceb.bank.hfield.HAkhzhField;
import com.ceb.bank.htable.AbstractQuery;
import com.ceb.hdqs.constants.HdqsConstants;
import com.ceb.hdqs.entity.PybjyEO;
import com.ceb.hdqs.query.entity.CustomerInfo;
import com.ceb.hdqs.query.utils.QueryMethodUtils;

public class Akhzh0771Scanner extends AbstractQuery<ZhanghContext> {
	private YngyjgGetter yngyjgGetter = new YngyjgGetter();
	private KemuccGetter kemuccGetter = new KemuccGetter();

	public Akhzh0771Scanner() {
		super(HBaseQueryConstants.TABLE_AKHZH);
	}

	public KehuzhContext query(PybjyEO record) throws IOException {
		List<ZhanghContext> zhanghList = scan(buildScan(record));
		if (zhanghList == null || zhanghList.isEmpty()) {
			return null;
		}
		
		ZhanghContext zhanghCtx = zhanghList.get(0);
		CustomerInfo info = QueryMethodUtils.getCustomerChineseName(zhanghCtx.getKehhao());
		YngyjgContext yngyjgCtx = null;
		if (HdqsConstants.KHZHLX_CARD.equals(record.getKhzhlx())) {
			yngyjgCtx = yngyjgGetter.query(record.getKehuzh());
			for (ZhanghContext zhCtx : zhanghList) {
				zhCtx.setKehzwm(info.getKehzwm());
				zhCtx.setYngyjg(yngyjgCtx.getYngyjg());
				zhCtx.setJigomc(yngyjgCtx.getJigomc());
			}
		} else {
			for (ZhanghContext zhCtx : zhanghList) {
				zhCtx.setKehzwm(info.getKehzwm());
				yngyjgCtx = kemuccGetter.query(zhanghCtx.getZhangh());
				zhCtx.setYngyjg(yngyjgCtx.getYngyjg());
				zhCtx.setJigomc(yngyjgCtx.getJigomc());
			}
		}

		KehuzhContext context = new KehuzhContext();
		context.setList(zhanghList);
		context.setKehhao(zhanghCtx.getKehhao());
		context.setKehzwm(info.getKehzwm());
		context.setShfobz(info.getShfobz());
		context.setKhzhjb(info.getKhzhjb());
		return context;
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
		Scan scanner = new Scan();
		scanner.addFamily(HBaseQueryConstants.FAMILIER_BYTE_F);
		Map<String, byte[]> qualifiers = HAkhzhField.getMxQualifiers();
		for (Map.Entry<String, byte[]> entry : qualifiers.entrySet()) {
			scanner.addColumn(HBaseQueryConstants.FAMILIER_BYTE_F, entry.getValue());
		}
		scanner.addColumn(HBaseQueryConstants.FAMILIER_BYTE_F, HAkhzhField.ZHHUXZ);// 过滤字段,需要添加
		scanner.setStartRow(buildStartRowKey(kehuzh, record.getKhzhlx()));
		scanner.setStopRow(buildStopRowKey(kehuzh, record.getKhzhlx()));

		SingleColumnValueFilter zhhuxzFilter = new SingleColumnValueFilter(HBaseQueryConstants.FAMILIER_BYTE_F, HAkhzhField.ZHHUXZ,
				CompareOp.EQUAL, Bytes.toBytes(record.getZhhuxz()));
		zhhuxzFilter.setFilterIfMissing(true);
		SingleColumnValueFilter huobdhFilter = new SingleColumnValueFilter(HBaseQueryConstants.FAMILIER_BYTE_F, HAkhzhField.HUOBDH,
				CompareOp.EQUAL, Bytes.toBytes(record.getHuobdh()));
		huobdhFilter.setFilterIfMissing(true);
		FilterList list = new FilterList(Operator.MUST_PASS_ALL);
		list.addFilter(zhhuxzFilter);
		list.addFilter(huobdhFilter);
		if (!FieldConstants.CHUIBZ_ALL.equals(record.getChuibz())) {
			SingleColumnValueFilter chuibzFilter = new SingleColumnValueFilter(HBaseQueryConstants.FAMILIER_BYTE_F, HAkhzhField.CHUIBZ,
					CompareOp.EQUAL, Bytes.toBytes(record.getChuibz()));
			chuibzFilter.setFilterIfMissing(true);
			list.addFilter(chuibzFilter);
		}
		scanner.setFilter(list);

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