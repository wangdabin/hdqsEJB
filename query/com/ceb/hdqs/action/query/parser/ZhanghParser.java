package com.ceb.hdqs.action.query.parser;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;

import com.ceb.bank.htable.AbstractQuery;
import com.ceb.hdqs.action.query.IconditionProcessor;
import com.ceb.hdqs.entity.PybjyEO;
import com.ceb.hdqs.entity.field.AghfhField;
import com.ceb.hdqs.entity.result.ZhangHParseResult;
import com.ceb.hdqs.query.utils.QueryConstants;
import com.ceb.hdqs.query.utils.QueryMethodUtils;

/**
 * 
 * 查询AGHFH和AGDFH确定该查询明细查询需要查询的明细表和查询出账号的属性信息
 * 
 * @author user
 * 
 */
public class ZhanghParser extends AbstractQuery<ZhangHParseResult> implements IconditionProcessor {
	private static final Log logger = LogFactory.getLog(ZhanghParser.class);

	private String tableName;

	public ZhanghParser(String tableName) {
		super(tableName);
		this.tableName = tableName;
	}

	/**
	 * 查询账号信息
	 * 
	 * @param account
	 * @return
	 * @throws IOException
	 */

	@Override
	public ZhangHParseResult parseCondition(PybjyEO record) throws Exception {
		logger.debug("Start query tableName: " + this.tableName);
		List<ZhangHParseResult> list = this.scan(buildParseScanner(record));
		if (list == null || list.isEmpty()) {
			return null;
		}
		ZhangHParseResult zhanghResult = list.get(0);
		if (zhanghResult == null) {
			return null;
		}
		// 如果图形前段回显了客户中文名，但是给客户中文名和解析出的客户中文名不一致，则直接返回空
		if (StringUtils.isNotBlank(record.getKhzwm()) && !QueryMethodUtils.checkKehuzwm(record.getKhzwm(), zhanghResult.getKehzwm())) {
			logger.debug("Unmatch,queried khzwm is " + zhanghResult.getKehzwm() + ",past is " + record.getKhzwm());
			return null;
		}
		zhanghResult.setRecord(record);
		zhanghResult.setPageLineNumber(record.getQueryNum());
		zhanghResult.setZhanghTotal(1);
		logger.debug(tableName + " query complete." + zhanghResult.parseToString());
		return zhanghResult;
	}

	@Override
	protected ZhangHParseResult parse(Result result) throws IOException {
		ZhangHParseResult zhParseResult = new ZhangHParseResult();
		zhParseResult.setHUOBDH(Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AghfhField.HUOBDH)));
		zhParseResult.setKehzwm(Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AghfhField.KEHZWM)));
		zhParseResult.setKehhao(Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AghfhField.KEHHAO)));
		zhParseResult.setZHANGH(Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AghfhField.ZHANGH)));
		// zhParseResult.setKEMUCC(Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE,
		// AGHFHField.KEMUCC)));
		zhParseResult.setZHYYNG(Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AghfhField.YNGYJG)));
		zhParseResult.setYEWUDH(Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AghfhField.YEWUDH)));
		zhParseResult.setCHUIBZ(Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AghfhField.CHUIBZ)));

		return zhParseResult;
	}

	@Override
	public Scan buildParseScanner(PybjyEO record) {
		String row = StringUtils.reverse(record.getZhangh());// 将账号翻转进行查询

		Get getter = new Get(Bytes.toBytes(row));
		getter.addFamily(QueryConstants.HBASE_TABLE_FAMILY_BYTE);
		getter.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AghfhField.HUOBDH);
		getter.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AghfhField.KEHZWM);
		getter.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AghfhField.KEHHAO);
		getter.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AghfhField.ZHANGH);
		// getter.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE,
		// AGHFHField.KEMUCC);
		getter.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AghfhField.YNGYJG);
		getter.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AghfhField.YEWUDH);
		getter.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AghfhField.CHUIBZ);
		Scan scan = new Scan(getter);
		return scan;
	}
}