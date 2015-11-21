package com.ceb.bank.query.scanner;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;

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

public class Akhzh0772Scanner extends AbstractQuery<ZhanghContext> {
	private static final Logger log = Logger.getLogger(Akhzh0772Scanner.class);
	private YngyjgGetter yngyjgGetter = new YngyjgGetter();
	private KemuccGetter kemuccGetter = new KemuccGetter();

	public Akhzh0772Scanner() {
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
		String buffer = buildRowkeyPrefix(record);
		Scan scanner = null;
		if (StringUtils.isBlank(record.getShunxh()) || record.getShunxh().equals("0000")) {
			scanner = new Scan();
			String startRow = buffer + FieldConstants.ROWKEY_SPLITTER + FieldConstants.MIN_CHAR;
			scanner.setStartRow(Bytes.toBytes(startRow));
			String stopRow = buffer + FieldConstants.ROWKEY_SPLITTER + FieldConstants.MAX_CHAR;
			scanner.setStopRow(Bytes.toBytes(stopRow));
			log.info("StartRow: " + startRow);
			log.info("StopRow: " + stopRow);
		} else {
			String rowkey = buffer + FieldConstants.ROWKEY_SPLITTER + record.getShunxh();
			Get get = new Get(Bytes.toBytes(rowkey));
			scanner = new Scan(get);
			log.info("rowkey: " + rowkey);
		}
		scanner.addFamily(HBaseQueryConstants.FAMILIER_BYTE_F);
		Map<String, byte[]> qualifiers = HAkhzhField.getMxQualifiers();
		for (Map.Entry<String, byte[]> entry : qualifiers.entrySet()) {
			scanner.addColumn(HBaseQueryConstants.FAMILIER_BYTE_F, entry.getValue());
		}
		return scanner;
	}

	private String buildRowkeyPrefix(PybjyEO record) {
		String kehuzh = StringUtils.reverse(record.getKehuzh());
		StringBuffer buffer = new StringBuffer();
		buffer.append(kehuzh).append(FieldConstants.ROWKEY_SPLITTER).append(record.getKhzhlx());
		return buffer.toString();
	}
}