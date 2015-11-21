package com.ceb.bank.query.scanner;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.FilterList.Operator;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;

import com.ceb.bank.constants.FieldConstants;
import com.ceb.bank.constants.HBaseQueryConstants;
import com.ceb.bank.constants.KemuccEnum;
import com.ceb.bank.context.KehuhaoContext;
import com.ceb.bank.context.KehuzhContext;
import com.ceb.bank.context.YngyjgContext;
import com.ceb.bank.context.ZhanghContext;
import com.ceb.bank.hfield.HAzhjxField;
import com.ceb.bank.htable.AbstractQuery;
import com.ceb.hdqs.constants.ExchangeCode;
import com.ceb.hdqs.constants.HdqsConstants;
import com.ceb.hdqs.entity.PybjyEO;
import com.ceb.hdqs.query.entity.CustomerInfo;
import com.ceb.hdqs.query.utils.QueryMethodUtils;

public class AzhjxScanner extends AbstractQuery<ZhanghContext> {
	private static final Logger log = Logger.getLogger(AzhjxScanner.class);
	private ChuibzGetter chuibzGetter = new ChuibzGetter();
	private YngyjgGetter yngyjgGetter = new YngyjgGetter();

	public AzhjxScanner(String tableName) {
		super(HBaseQueryConstants.TABLE_AZHJX);
	}

	public KehuhaoContext query(PybjyEO record) throws Exception {
		log.info("开始解析客户号: " + record.getKehuhao());
		KehuhaoContext ctx = new KehuhaoContext();

		CustomerInfo customerInfo = QueryMethodUtils.getCustomerChineseName(record.getKehuhao());
		// 如果图形前段回显了客户中文名，但是给客户中文名和解析出的客户中文名不一致，则直接返回空
		if (StringUtils.isNotBlank(record.getKhzwm()) && !QueryMethodUtils.checkKehuzwm(record.getKhzwm(), customerInfo.getKehzwm())) {
			log.info("回显中文名：" + record.getKhzwm() + "与查询的中文名：" + customerInfo.getKehzwm() + "不一致.");
			return null;
		}

		List<ZhanghContext> list = scan(buildScan(record));
		Map<String, List<ZhanghContext>> map = new HashMap<String, List<ZhanghContext>>();
		for (ZhanghContext zhanghCtx : list) {
			zhanghCtx.setKehzwm(customerInfo.getKehzwm());
			if (map.containsKey(zhanghCtx.getKehuzh())) {
				List<ZhanghContext> zhanghList = map.get(zhanghCtx.getKehuzh());
				zhanghList.add(zhanghCtx);
			} else {
				List<ZhanghContext> zhanghList = new ArrayList<ZhanghContext>();
				zhanghList.add(zhanghCtx);
				map.put(zhanghCtx.getKehuzh(), zhanghList);
			}
		}
		// init
		for (Entry<String, List<ZhanghContext>> entry : map.entrySet()) {
			// String key = entry.getKey();
			List<ZhanghContext> zhanghList = entry.getValue();
			KehuzhContext kehuzhCtx = new KehuzhContext();
			kehuzhCtx.setKehhao(record.getKehuhao());
			kehuzhCtx.setKehzwm(customerInfo.getKehzwm());
			kehuzhCtx.setShfobz(customerInfo.getShfobz());
			kehuzhCtx.setKhzhjb(customerInfo.getKhzhjb());
			kehuzhCtx.getList().addAll(zhanghList);
			ctx.getList().add(kehuzhCtx);
		}
		return ctx;
	}

	@Override
	protected ZhanghContext parse(Result result) throws IOException {
		ZhanghContext bean = new ZhanghContext();
		Map<String, byte[]> qualifiers = HAzhjxField.getMxQualifiers();
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
		if (StringUtils.isBlank(bean.getZhangh()) || StringUtils.isBlank(bean.getKemucc())) {
			log.info("发现账号为空或者KEMUCC为空的记录!");
			return null;
		}
		ZhanghContext tmpCtx = chuibzGetter.query(bean.getZhangh());
		if (StringUtils.isBlank(bean.getKehuzh())) {
			bean.setKehuzh(tmpCtx.getKehuzh());
		}
		if (StringUtils.isBlank(bean.getKhzhlx())) {
			bean.setKhzhlx(tmpCtx.getKhzhlx());
		}
		bean.setKemucc(tmpCtx.getKemucc());
		if (HdqsConstants.KHZHLX_CARD.equals(bean.getKhzhlx())) {
			log.info("Query yngyjg from VYKTD.");
			YngyjgContext yngyjgCtx = yngyjgGetter.query(bean.getKehuzh());
			bean.setYngyjg(yngyjgCtx.getYngyjg());
			bean.setJigomc(yngyjgCtx.getJigomc());
		} else {
			bean.setJigomc(QueryMethodUtils.getJGMC(bean.getYngyjg()));
		}

		return bean;
	}

	private Scan buildScan(PybjyEO record) {
		String kehuhao = StringUtils.reverse(record.getKehuhao());
		Scan scanner = new Scan();
		scanner.addFamily(HBaseQueryConstants.FAMILIER_BYTE_F);
		Map<String, byte[]> qualifiers = HAzhjxField.getMxQualifiers();
		for (Map.Entry<String, byte[]> entry : qualifiers.entrySet()) {
			scanner.addColumn(HBaseQueryConstants.FAMILIER_BYTE_F, entry.getValue());
		}
		scanner.setStartRow(buildStartRowKey(kehuhao));
		scanner.setStopRow(buildStopRowKey(kehuhao));

		FilterList list = new FilterList(Operator.MUST_PASS_ALL);
		if (ExchangeCode.EXCHANGECODE_0777.equals(record.getJiaoym())) {
			// 设置账号性质(账号性质 0活期,2定期,3全部)
			FilterList orList = new FilterList(Operator.MUST_PASS_ONE);
			SingleColumnValueFilter dgdqFilter = new SingleColumnValueFilter(HBaseQueryConstants.FAMILIER_BYTE_F, HAzhjxField.KEMUCC,
					CompareOp.EQUAL, Bytes.toBytes(KemuccEnum.KEMUCC_DGDQ));
			SingleColumnValueFilter dghqFilter = new SingleColumnValueFilter(HBaseQueryConstants.FAMILIER_BYTE_F, HAzhjxField.KEMUCC,
					CompareOp.EQUAL, Bytes.toBytes(KemuccEnum.KEMUCC_DGHQ));
			orList.addFilter(dgdqFilter);
			orList.addFilter(dghqFilter);
			list.addFilter(orList);
		} else if (ExchangeCode.EXCHANGECODE_0778.equals(record.getJiaoym())) {
			// 如果是0778查询，则根据0778输入的货币种类、账号性质进行查询
			// 设置账号性质(活期、定期、全部)
			if (FieldConstants.ZHAOXZ_DIGQ.equals(record.getZhaoxz())) {
				SingleColumnValueFilter dsdqFilter = new SingleColumnValueFilter(HBaseQueryConstants.FAMILIER_BYTE_F, HAzhjxField.KEMUCC,
						CompareOp.EQUAL, Bytes.toBytes(KemuccEnum.KEMUCC_DSDQ));
				list.addFilter(dsdqFilter);
			} else if (FieldConstants.ZHAOXZ_HUOQ.equals(record.getZhaoxz())) {
				SingleColumnValueFilter dshqFilter = new SingleColumnValueFilter(HBaseQueryConstants.FAMILIER_BYTE_F, HAzhjxField.KEMUCC,
						CompareOp.EQUAL, Bytes.toBytes(KemuccEnum.KEMUCC_DSHQ));
				list.addFilter(dshqFilter);
			} else {
				FilterList orList = new FilterList(Operator.MUST_PASS_ONE);
				SingleColumnValueFilter dsdqFilter = new SingleColumnValueFilter(HBaseQueryConstants.FAMILIER_BYTE_F, HAzhjxField.KEMUCC,
						CompareOp.EQUAL, Bytes.toBytes(KemuccEnum.KEMUCC_DSDQ));
				SingleColumnValueFilter dshqFilter = new SingleColumnValueFilter(HBaseQueryConstants.FAMILIER_BYTE_F, HAzhjxField.KEMUCC,
						CompareOp.EQUAL, Bytes.toBytes(KemuccEnum.KEMUCC_DSHQ));
				orList.addFilter(dsdqFilter);
				orList.addFilter(dshqFilter);
				list.addFilter(orList);
			}
			// 设置货币代号的过滤条件
			if (!FieldConstants.HUOBDH_ALL.equals(record.getHuobdh())) {
				log.info("需要查询的货币代号是：" + record.getHuobdh());
				SingleColumnValueFilter huobFilter = new SingleColumnValueFilter(HBaseQueryConstants.FAMILIER_BYTE_F, HAzhjxField.HUOBDH,
						CompareOp.EQUAL, Bytes.toBytes(record.getHuobdh()));
				list.addFilter(huobFilter);
			}
		} else {
			throw new InvalidParameterException("客户号解析中传入的查询码错误：" + record.getJiaoym());
		}
		scanner.setFilter(list);
		return scanner;
	}

	private byte[] buildStartRowKey(String kehuhao) {
		String rowKey = kehuhao + FieldConstants.ROWKEY_SPLITTER + FieldConstants.MIN_CHAR;
		return Bytes.toBytes(rowKey);
	}

	private byte[] buildStopRowKey(String kehuhao) {
		String rowKey = kehuhao + FieldConstants.ROWKEY_SPLITTER + FieldConstants.MAX_CHAR;
		return Bytes.toBytes(rowKey);
	}
}
