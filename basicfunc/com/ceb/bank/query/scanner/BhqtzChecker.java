package com.ceb.bank.query.scanner;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;

import com.ceb.bank.constants.HBaseQueryConstants;
import com.ceb.bank.hfield.HBhqtzField;
import com.ceb.bank.htable.AbstractQuery;
import com.ceb.hdqs.entity.PybjyEO;

/**
 * 法人透支户, 在BHQTZ中只有一条记录,参考mm4404文档
 * 
 * 
 */
public class BhqtzChecker extends AbstractQuery<Boolean> {

	public BhqtzChecker() {
		super(HBaseQueryConstants.TABLE_BHQTZ);
	}

	public boolean check(PybjyEO record) throws IOException {
		List<Boolean> list = scan(buildScan(record));
		if (list == null || list.isEmpty()) {
			return false;
		}
		return true;
	}

	private Scan buildScan(PybjyEO record) {
		byte[] rowKey = Bytes.toBytes(StringUtils.reverse(record.getZhangh()));
		Get getter = new Get(rowKey);
		getter.addFamily(HBaseQueryConstants.FAMILIER_BYTE_F);
		getter.addColumn(HBaseQueryConstants.FAMILIER_BYTE_F, HBhqtzField.LEIXIN);

		Scan scan = new Scan(getter);
		// TODO 应该过滤JILUZT而不是LEIXIN,参考核心是不是错了
		SingleColumnValueFilter normalFilter = new SingleColumnValueFilter(HBaseQueryConstants.FAMILIER_BYTE_F, HBhqtzField.LEIXIN,
				CompareOp.EQUAL, HBhqtzField.LEIXIN_BYTE_LARGE);
		scan.setFilter(normalFilter);
		return scan;
	}

	@Override
	protected Boolean parse(Result result) throws IOException {
		return Boolean.TRUE;
	}
}