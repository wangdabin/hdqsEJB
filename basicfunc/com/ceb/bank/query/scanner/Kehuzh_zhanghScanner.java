package com.ceb.bank.query.scanner;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;

import com.ceb.bank.constants.FieldConstants;
import com.ceb.bank.constants.HBaseQueryConstants;
import com.ceb.bank.context.KehuzhContext;
import com.ceb.bank.context.ZhanghContext;
import com.ceb.bank.hfield.HAzhjx0772Field;
import com.ceb.bank.htable.AbstractQuery;
import com.ceb.hdqs.entity.PybjyEO;
import com.ceb.hdqs.query.entity.CustomerInfo;
import com.ceb.hdqs.query.utils.QueryMethodUtils;

/**
 * 输入的客户账号是国债、存单、 储蓄存款存折时，这些客户账号就是账号 直接解析AZHJX0772，查询出该账号对应的信息
 * 
 * 
 */
public class Kehuzh_zhanghScanner extends AbstractQuery<ZhanghContext> {
	private static final Logger log = Logger.getLogger(Kehuzh_zhanghScanner.class);
	private ChuibzGetter chuibzGetter = new ChuibzGetter();

	public Kehuzh_zhanghScanner() {
		super(HBaseQueryConstants.TABLE_AZHJX0772);
	}

	public KehuzhContext query(PybjyEO record) throws IOException {
		List<ZhanghContext> zhanghList = scan(buildScan(record));
		if (zhanghList == null || zhanghList.isEmpty()) {
			return null;
		}
		ZhanghContext zhanghCtx = zhanghList.get(0);
		CustomerInfo info = QueryMethodUtils.getCustomerChineseName(zhanghCtx.getKehhao());
		// 如果图形前段回显了客户中文名，但是给客户中文名和解析出的客户中文名不一致，则直接返回空
		if (StringUtils.isNotBlank(record.getKhzwm()) && !QueryMethodUtils.checkKehuzwm(record.getKhzwm(), info.getKehzwm())) {
			log.info("查询出的客户中文名：" + info.getKehzwm() + "和回显中文名：" + record.getKhzwm());
			return null;
		}
		zhanghCtx.setKehzwm(info.getKehzwm());
		KehuzhContext context = new KehuzhContext();
		// 因为RowKey有JILUZT字段,需要考虑去重
		List<ZhanghContext> tmpList = new ArrayList<ZhanghContext>();
		tmpList.add(zhanghCtx);
		context.setList(tmpList);
		context.setKehhao(zhanghCtx.getKehhao());
		context.setKehzwm(info.getKehzwm());
		context.setShfobz(info.getShfobz());
		context.setKhzhjb(info.getKhzhjb());

		return context;
	}

	@Override
	protected ZhanghContext parse(Result result) throws IOException {
		ZhanghContext bean = new ZhanghContext();
		Map<String, byte[]> qualifiers = HAzhjx0772Field.getMxQualifiers();
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
		// 由于账号解析表中不存在钞汇标志,所以需要根据账号查询AKHZH表查询出钞汇标志
		ZhanghContext chuibzCtx = chuibzGetter.query(bean.getZhangh());
		if (StringUtils.isNotBlank(chuibzCtx.getChuibz())) {
			bean.setChuibz(chuibzCtx.getChuibz());
		}
		// 获取账户营业机构名称
				String jigomc = QueryMethodUtils.getJGMC(bean.getYngyjg());
				bean.setJigomc(jigomc);
		return bean;
	}

	/**
	 * 此处是国债、存单、 储蓄存款存折的客户账号就是账号
	 * 
	 * @param record
	 * @return
	 */
	private Scan buildScan(PybjyEO record) {
		String zhangh = StringUtils.reverse(record.getKehuzh());
		Scan scanner = new Scan();
		scanner.addFamily(HBaseQueryConstants.FAMILIER_BYTE_F);
		Map<String, byte[]> qualifiers = HAzhjx0772Field.getMxQualifiers();
		for (Map.Entry<String, byte[]> entry : qualifiers.entrySet()) {
			scanner.addColumn(HBaseQueryConstants.FAMILIER_BYTE_F, entry.getValue());
		}
		scanner.setStartRow(buildStartRowKey(zhangh));
		scanner.setStopRow(buildStopRowKey(zhangh));

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