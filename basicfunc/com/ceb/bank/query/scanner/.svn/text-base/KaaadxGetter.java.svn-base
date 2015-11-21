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
import com.ceb.bank.context.KaaadxContext;
import com.ceb.bank.hfield.HVyktdField;
import com.ceb.bank.htable.AbstractQuery;
import com.ceb.hdqs.action.query.exception.ConditionNotExistException;
import com.ceb.hdqs.action.query.exception.UnCorporationCardException;
import com.ceb.hdqs.query.utils.QueryMethodUtils;

public class KaaadxGetter extends AbstractQuery<KaaadxContext> {
	private static final Logger log = Logger.getLogger(KaaadxGetter.class);
	/**
	 * 卡对象 0-个人卡 1-单位卡
	 */
	public static final String KAAADX_GEREN = "0";
	public static final String KAAADX_DANWEI = "1";

	public KaaadxGetter() {
		super(HBaseQueryConstants.TABLE_VYKTD);
	}

	/**
	 * 验证是否是单位卡
	 * 
	 * @param kahaoo
	 * @return
	 * @throws Exception
	 */
	public KaaadxContext query(String kahaoo) throws Exception {
		KaaadxContext context = doGet(buildGet(kahaoo));

		if (context == null) {
			throw new ConditionNotExistException("卡号" + kahaoo + "不存在!");
		} else if (!KAAADX_DANWEI.equals(context.getKaaadx())) {
			throw new UnCorporationCardException("输入的卡号非单位卡!");
		}

		return context;
	}

	@Override
	protected KaaadxContext parse(Result result) throws IOException {
		KaaadxContext bean = new KaaadxContext();
		Map<String, byte[]> qualifiers = HVyktdField.getKaaadxQualifiers();
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
		if (StringUtils.isNotBlank(bean.getYngyjg())) {
			bean.setJigomc(QueryMethodUtils.getJGMC(bean.getYngyjg()));
		} else {
			bean.setJigomc("");
		}
		return bean;
	}

	private Get buildGet(String kahaoo) {
		Get getter = new Get(buildRowKey(kahaoo));
		getter.addFamily(HBaseQueryConstants.FAMILIER_BYTE_F);
		Map<String, byte[]> qualifiers = HVyktdField.getKaaadxQualifiers();
		for (Map.Entry<String, byte[]> entry : qualifiers.entrySet()) {
			getter.addColumn(HBaseQueryConstants.FAMILIER_BYTE_F, entry.getValue());
		}
		return getter;
	}

	private byte[] buildRowKey(String kahaoo) {
		String rowkey = StringUtils.reverse(kahaoo);
		return Bytes.toBytes(rowkey);
	}
}