package com.ceb.hdqs.action.query.parser;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import com.ceb.bank.htable.AbstractQuery;
import com.ceb.hdqs.entity.field.BdskhField;
import com.ceb.hdqs.query.entity.CustomerInfo;
import com.ceb.hdqs.query.utils.QueryConstants;

/**
 * 查询BDSKH表查询出对应的客户信息
 * 
 * @author user
 * 
 */
public class BdskhParser extends AbstractQuery<CustomerInfo> {

	public BdskhParser() {
		super(QueryConstants.TABLE_NAME_BDSKH);
	}

	@Override
	protected CustomerInfo parse(Result result) throws IOException {
		String gerzwm = "";
		String shfobz = "";
		String khzhjb = "";
		CustomerInfo info = new CustomerInfo();
		;
		byte[] zwmTmp = result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, BdskhField.GERZWM);
		if (zwmTmp != null && zwmTmp.length > 0) {
			gerzwm = Bytes.toString(zwmTmp);
		}

		byte[] sfbzTmp = result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, BdskhField.SHFOBZ);
		if (sfbzTmp != null && sfbzTmp.length > 0) {
			shfobz = Bytes.toString(sfbzTmp);
		}

		byte[] khjbTmp = result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, BdskhField.KHZHJB);
		if (khjbTmp != null && khjbTmp.length > 0) {
			khzhjb = Bytes.toString(khjbTmp);
		}

		info.setKehzwm(gerzwm);
		info.setKhzhjb(Integer.parseInt(StringUtils.isBlank(khzhjb) ? "0" : khzhjb));
		info.setShfobz(Integer.parseInt(StringUtils.isBlank(shfobz) ? "0" : shfobz));
		return info;
	}

	public CustomerInfo getCustomerInfo(String kehhao) throws IOException {
		Get get = new Get(Bytes.toBytes(StringUtils.reverse(kehhao)));
		get.addFamily(QueryConstants.HBASE_TABLE_FAMILY_BYTE);
		get.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, BdskhField.GERZWM);
		get.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, BdskhField.KHZHJB);
		get.addColumn(QueryConstants.HBASE_TABLE_FAMILY_BYTE, BdskhField.SHFOBZ);
		CustomerInfo info = doGet(get);
		if (info == null) {
			info = new CustomerInfo();
		}
		info.setKehhao(kehhao);
		return info;
	}
}
