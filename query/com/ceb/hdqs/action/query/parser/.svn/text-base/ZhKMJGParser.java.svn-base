package com.ceb.hdqs.action.query.parser;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;

import com.ceb.bank.htable.AbstractQuery;
import com.ceb.hdqs.entity.field.AzhjxField;
import com.ceb.hdqs.entity.result.ZhangHParseResult;
import com.ceb.hdqs.query.utils.QueryConfUtils;
import com.ceb.hdqs.query.utils.QueryConstants;
import com.ceb.hdqs.query.utils.QueryMethodUtils;

/**
 * 查询每个账号的kemucc和账户营业机构
 * 
 * @author user
 * 
 */
public class ZhKMJGParser extends AbstractQuery<ZhangHParseResult> {

	public ZhKMJGParser() {
		super(QueryConstants.TABLE_NAME_AZHJX0772);
	}

	public void parseZhjx(ZhangHParseResult input) throws IOException {
		Scan scanner = buildParseScanner(input.getZHANGH());
		List<ZhangHParseResult> list = this.scan(scanner);
		if (list == null || list.isEmpty()) {
			return;
		}
		input.setKEMUCC(list.get(0).getKEMUCC());
		input.setZHYYNG(list.get(0).getZHYYNG());
		String fkjgmc = QueryMethodUtils.getJGMC(input.getZHYYNG());
		input.setJIGOMC(fkjgmc);
		input.setYEWUDH(list.get(0).getYEWUDH());
		input.setHUOBDH(list.get(0).getHUOBDH());
	}

	public Scan buildParseScanner(String zhangh) {
		String rowKeySplit = QueryConfUtils.getActiveConfig().get(QueryConstants.ROWKEY_SPLITTER, "|");

		Scan scanner = new Scan();
		scanner.addFamily(QueryConstants.HBASE_TABLE_FAMILY_BYTE);
		scanner.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AzhjxField.ZHYYJG);
		scanner.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AzhjxField.YEWUDH);
		scanner.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AzhjxField.KEMUCC);
		scanner.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AzhjxField.HUOBDH);

		String row = StringUtils.reverse(zhangh);
		scanner.setStartRow(Bytes.toBytes(row + rowKeySplit + QueryConstants.MIN_NUM));
		scanner.setStopRow(Bytes.toBytes(row + rowKeySplit + QueryConstants.MAX_CHAR));
		return scanner;
	}

	@Override
	protected ZhangHParseResult parse(Result result) throws IOException {
		ZhangHParseResult zhjx = new ZhangHParseResult();

		zhjx.setZHYYNG(Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AzhjxField.ZHYYJG)));
		zhjx.setYEWUDH(Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AzhjxField.YEWUDH)));
		zhjx.setKEMUCC(Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AzhjxField.KEMUCC)));
		zhjx.setHUOBDH(Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AzhjxField.HUOBDH)));
		zhjx.setJIGOMC(QueryMethodUtils.getJGMC(zhjx.getZHYYNG()));
		return zhjx;
	}
}