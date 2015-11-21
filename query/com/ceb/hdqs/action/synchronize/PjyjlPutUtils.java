package com.ceb.hdqs.action.synchronize;

import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.util.Bytes;

import com.ceb.hdqs.entity.PjyjlEO;
import com.ceb.hdqs.entity.field.PjyjlField;
import com.ceb.hdqs.query.utils.QueryConstants;

public class PjyjlPutUtils implements IKeyValueUtils<PjyjlEO> {

	@Override
	public PjyjlEO getEntity(String[] valueSplit) throws Exception {
		return null;
	}

	@Override
	public List<KeyValue> getPutter(byte[] rowkey, PjyjlEO entity) throws Exception {
		List<KeyValue> kvs = new ArrayList<KeyValue>();

		kvs.add(new KeyValue(rowkey, QueryConstants.HBASE_TABLE_FAMILY_BYTE, PjyjlField.JIOYRQ, Bytes.toBytes(entity.getJioyrq() == null ? "" : entity.getJioyrq())));
		kvs.add(new KeyValue(rowkey, QueryConstants.HBASE_TABLE_FAMILY_BYTE, PjyjlField.JIAOYM, Bytes.toBytes(entity.getJiaoym() == null ? "" : entity.getJiaoym())));
		kvs.add(new KeyValue(rowkey, QueryConstants.HBASE_TABLE_FAMILY_BYTE, PjyjlField.YNGYJG, Bytes.toBytes(entity.getYngyjg() == null ? "" : entity.getYngyjg())));
		kvs.add(new KeyValue(rowkey, QueryConstants.HBASE_TABLE_FAMILY_BYTE, PjyjlField.JIO1GY, Bytes.toBytes(entity.getJio1gy() == null ? "" : entity.getJio1gy())));
		kvs.add(new KeyValue(rowkey, QueryConstants.HBASE_TABLE_FAMILY_BYTE, PjyjlField.GUIYLS, Bytes.toBytes(entity.getGuiyls() == null ? "" : entity.getGuiyls())));
		kvs.add(new KeyValue(rowkey, QueryConstants.HBASE_TABLE_FAMILY_BYTE, PjyjlField.SLBHAO, Bytes.toBytes(entity.getSlbhao() == null ? "" : entity.getSlbhao())));
		kvs.add(new KeyValue(rowkey, QueryConstants.HBASE_TABLE_FAMILY_BYTE, PjyjlField.JIOYBZ, Bytes.toBytes(entity.getJioybz().booleanValue())));
		kvs.add(new KeyValue(rowkey, QueryConstants.HBASE_TABLE_FAMILY_BYTE, PjyjlField.SHOQGY, Bytes.toBytes(entity.getShoqgy() == null ? "" : entity.getShoqgy())));
		kvs.add(new KeyValue(rowkey, QueryConstants.HBASE_TABLE_FAMILY_BYTE, PjyjlField.QUDAOO, Bytes.toBytes(entity.getQudaoo() == null ? "" : entity.getQudaoo())));
		kvs.add(new KeyValue(rowkey, QueryConstants.HBASE_TABLE_FAMILY_BYTE, PjyjlField.FILEQUERY, Bytes.toBytes(entity.getFileQuery().booleanValue())));
		kvs.add(new KeyValue(rowkey, QueryConstants.HBASE_TABLE_FAMILY_BYTE, PjyjlField.QUERYSTR, Bytes.toBytes(entity.getQueryStr() == null ? "" : entity.getQueryStr())));
		kvs.add(new KeyValue(rowkey, QueryConstants.HBASE_TABLE_FAMILY_BYTE, PjyjlField.RUNSUCC, Bytes.toBytes(entity.getRunSucc().booleanValue())));
		kvs.add(new KeyValue(rowkey, QueryConstants.HBASE_TABLE_FAMILY_BYTE, PjyjlField.STARTTIME, Bytes.toBytes(entity.getStartTime().longValue())));
		kvs.add(new KeyValue(rowkey, QueryConstants.HBASE_TABLE_FAMILY_BYTE, PjyjlField.ENDTIME, Bytes.toBytes(entity.getEndTime().longValue())));

		return kvs;
	}
}