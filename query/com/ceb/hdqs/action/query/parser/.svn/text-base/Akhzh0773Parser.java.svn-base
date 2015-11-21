package com.ceb.hdqs.action.query.parser;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;

import com.ceb.bank.htable.AbstractQuery;
import com.ceb.hdqs.entity.field.AkhzhField;
import com.ceb.hdqs.query.utils.QueryConstants;

public class Akhzh0773Parser extends AbstractQuery<String> {

	public Akhzh0773Parser() {
		super(QueryConstants.TABLE_NAME_AKHZH_0773);
	}

	@Override
	protected String parse(Result result) throws IOException {
		String chuibz = Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, AkhzhField.CHUIBZ));
		return chuibz;
	}

	/**
	 * 获取指定账号的钞汇标志
	 * 
	 * @param zhangh
	 *            需要查询钞汇标志的账号
	 * @return String 输入账号的钞汇标志，如果没有则返回 null
	 * @throws IOException
	 */
	public String getChuibz(String zhangh) throws IOException {
		Scan scan = new Scan();
		String startRow = StringUtils.reverse(zhangh) + "|" + QueryConstants.MIN_NUM;
		String stopRow = StringUtils.reverse(zhangh) + "|" + QueryConstants.MAX_CHAR;
		scan.setStartRow(Bytes.toBytes(startRow));
		scan.setStopRow(Bytes.toBytes(stopRow));
		List<String> list = this.scan(scan);
		if (list == null || list.isEmpty()) {
			return null;
		}
		return list.get(0);
	}
}