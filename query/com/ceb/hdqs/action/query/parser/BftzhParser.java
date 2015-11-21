package com.ceb.hdqs.action.query.parser;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;

import com.ceb.bank.htable.AbstractQuery;
import com.ceb.hdqs.action.query.exception.InvalidRecordParameterException;
import com.ceb.hdqs.entity.PybjyEO;
import com.ceb.hdqs.entity.field.BhqtzField;
import com.ceb.hdqs.query.utils.QueryConstants;

/**
 * 解析法人透支账户信息
 * 
 * @author user
 * 
 */
public class BftzhParser extends AbstractQuery<Boolean> {

	public BftzhParser() {
		super(QueryConstants.TABLE_NAME_BHQTZ);
	}

	public boolean parseCondition(PybjyEO record) throws Exception {
		List<Boolean> list = this.scan(buildParseScanner(record));
		if (list == null || list.isEmpty()) {
			return false;
		}
		return true;
	}

	public Scan buildParseScanner(PybjyEO record) throws InvalidRecordParameterException {
		byte[] rowKey = Bytes.toBytes(StringUtils.reverse(record.getZhangh()));

		Get getter = new Get(rowKey);
		getter.addFamily(QueryConstants.HBASE_TABLE_FAMILY_BYTE);
		Scan scan = new Scan(getter);
		SingleColumnValueFilter leixingFilter = new SingleColumnValueFilter(QueryConstants.HBASE_TABLE_FAMILY_BYTE, BhqtzField.LEIXIN, CompareOp.EQUAL, BhqtzField.LEIXIN_TZ);
		scan.setFilter(leixingFilter);
		return scan;
	}

	@Override
	protected Boolean parse(Result result) throws IOException {
		return Boolean.TRUE;
	}
}