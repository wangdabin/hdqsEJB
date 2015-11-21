package com.ceb.hdqs.action.synchronize;

import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.util.Bytes;

import com.ceb.hdqs.entity.PybjyEO;
import com.ceb.hdqs.entity.field.PybjyField;
import com.ceb.hdqs.query.utils.QueryConstants;

/**
 * 异步交易记录生成Putter的工具类
 * 
 * @author user
 * 
 */
public class PybjyPutUtils implements IKeyValueUtils<PybjyEO> {

	@Override
	public PybjyEO getEntity(String[] valueSplit) throws Exception {
		return null;
	}

	@Override
	public List<KeyValue> getPutter(byte[] rowkey, PybjyEO entity) throws Exception {
		List<KeyValue> kvs = new ArrayList<KeyValue>();
		// kvs.add(new KeyValue(rowkey, Constants.HBASE_TABLE_FAMILY_BYTE,
		// Bytes.toBytes("ZHANGH"),timestamp,Bytes.toBytes(entity.getZHANGH())));
		kvs.add(new KeyValue(rowkey, QueryConstants.HBASE_TABLE_FAMILY_BYTE, PybjyField.JIOYRQ, Bytes.toBytes(entity
				.getJioyrq() == null ? "" : entity.getJioyrq())));
		kvs.add(new KeyValue(rowkey, QueryConstants.HBASE_TABLE_FAMILY_BYTE, PybjyField.JIAOYM, Bytes.toBytes(entity
				.getJiaoym() == null ? "" : entity.getJiaoym())));
		kvs.add(new KeyValue(rowkey, QueryConstants.HBASE_TABLE_FAMILY_BYTE, PybjyField.YNGYJG, Bytes.toBytes(entity
				.getYngyjg() == null ? "" : entity.getYngyjg())));
		kvs.add(new KeyValue(rowkey, QueryConstants.HBASE_TABLE_FAMILY_BYTE, PybjyField.JIO1GY, Bytes.toBytes(entity
				.getJio1gy() == null ? "" : entity.getJio1gy())));
		kvs.add(new KeyValue(rowkey, QueryConstants.HBASE_TABLE_FAMILY_BYTE, PybjyField.GUIYLS, Bytes.toBytes(entity
				.getGuiyls() == null ? "" : entity.getGuiyls())));
		kvs.add(new KeyValue(rowkey, QueryConstants.HBASE_TABLE_FAMILY_BYTE, PybjyField.SLBHAO, Bytes.toBytes(entity
				.getSlbhao() == null ? "" : entity.getSlbhao())));
		kvs.add(new KeyValue(rowkey, QueryConstants.HBASE_TABLE_FAMILY_BYTE, PybjyField.RUNSTATUS, Bytes.toBytes(entity
				.getRunStatus())));
		kvs.add(new KeyValue(rowkey, QueryConstants.HBASE_TABLE_FAMILY_BYTE, PybjyField.NOTIFYSTATUS, Bytes
				.toBytes(entity.getNotifyStatus())));
		kvs.add(new KeyValue(rowkey, QueryConstants.HBASE_TABLE_FAMILY_BYTE, PybjyField.CHAXLX, Bytes.toBytes(entity
				.getChaxlx() == null ? "" : entity.getChaxlx())));
		kvs.add(new KeyValue(rowkey, QueryConstants.HBASE_TABLE_FAMILY_BYTE, PybjyField.CHAXZL, Bytes.toBytes(entity
				.getChaxzl() == null ? "" : entity.getChaxzl())));
		kvs.add(new KeyValue(rowkey, QueryConstants.HBASE_TABLE_FAMILY_BYTE, PybjyField.ZHANGH, Bytes.toBytes(entity
				.getZhangh() == null ? "" : entity.getZhangh())));
		kvs.add(new KeyValue(rowkey, QueryConstants.HBASE_TABLE_FAMILY_BYTE, PybjyField.KEHUZH, Bytes.toBytes(entity
				.getKehuzh() == null ? "" : entity.getKehuzh())));
		kvs.add(new KeyValue(rowkey, QueryConstants.HBASE_TABLE_FAMILY_BYTE, PybjyField.SHUNXH, Bytes.toBytes(entity
				.getShunxh() == null ? "" : entity.getShunxh())));
		kvs.add(new KeyValue(rowkey, QueryConstants.HBASE_TABLE_FAMILY_BYTE, PybjyField.KEHUHAO, Bytes.toBytes(entity
				.getKehuhao() == null ? "" : entity.getKehuhao())));
		kvs.add(new KeyValue(rowkey, QueryConstants.HBASE_TABLE_FAMILY_BYTE, PybjyField.ZHJNZL, Bytes.toBytes(entity
				.getZhjnzl() == null ? "" : entity.getZhjnzl())));
		kvs.add(new KeyValue(rowkey, QueryConstants.HBASE_TABLE_FAMILY_BYTE, PybjyField.ZHJHAO, Bytes.toBytes(entity
				.getZhjhao() == null ? "" : entity.getZhjhao())));
		kvs.add(new KeyValue(rowkey, QueryConstants.HBASE_TABLE_FAMILY_BYTE, PybjyField.KHZWM, Bytes.toBytes(entity
				.getKhzwm() == null ? "" : entity.getKhzwm())));
		kvs.add(new KeyValue(rowkey, QueryConstants.HBASE_TABLE_FAMILY_BYTE, PybjyField.STARTDATE, Bytes.toBytes(entity
				.getStartDate() == null ? "" : entity.getStartDate())));
		kvs.add(new KeyValue(rowkey, QueryConstants.HBASE_TABLE_FAMILY_BYTE, PybjyField.ENDDATE, Bytes.toBytes(entity
				.getEndDate() == null ? "" : entity.getEndDate())));
		kvs.add(new KeyValue(rowkey, QueryConstants.HBASE_TABLE_FAMILY_BYTE, PybjyField.FILEQUERY, Bytes.toBytes(entity
				.getFileQuery().booleanValue())));
		kvs.add(new KeyValue(rowkey, QueryConstants.HBASE_TABLE_FAMILY_BYTE, PybjyField.RECVFILE, Bytes.toBytes(entity
				.getRecvFile() == null ? "" : entity.getRecvFile())));
		kvs.add(new KeyValue(rowkey, QueryConstants.HBASE_TABLE_FAMILY_BYTE, PybjyField.PDFFILE, Bytes.toBytes(entity
				.getPdfFile() == null ? "" : entity.getPdfFile())));
		kvs.add(new KeyValue(rowkey, QueryConstants.HBASE_TABLE_FAMILY_BYTE, PybjyField.XLSFILE, Bytes.toBytes(entity
				.getXlsFile() == null ? "" : entity.getXlsFile())));
		kvs.add(new KeyValue(rowkey, QueryConstants.HBASE_TABLE_FAMILY_BYTE, PybjyField.COMMNUM, Bytes.toBytes(entity
				.getCommNum())));
		kvs.add(new KeyValue(rowkey, QueryConstants.HBASE_TABLE_FAMILY_BYTE, PybjyField.ITEMCOUNT, Bytes.toBytes(entity
				.getItemCount().longValue())));
		kvs.add(new KeyValue(rowkey, QueryConstants.HBASE_TABLE_FAMILY_BYTE, PybjyField.KHZHLX, Bytes.toBytes(entity
				.getKhzhlx() == null ? "" : entity.getKhzhlx())));
		kvs.add(new KeyValue(rowkey, QueryConstants.HBASE_TABLE_FAMILY_BYTE, PybjyField.ZHHUXZ, Bytes.toBytes(entity
				.getZhaoxz() == null ? "" : entity.getZhaoxz())));
		kvs.add(new KeyValue(rowkey, QueryConstants.HBASE_TABLE_FAMILY_BYTE, PybjyField.ZHAOXZ, Bytes.toBytes(entity
				.getZhaoxz() == null ? "" : entity.getZhaoxz())));
		kvs.add(new KeyValue(rowkey, QueryConstants.HBASE_TABLE_FAMILY_BYTE, PybjyField.HUOBDH, Bytes.toBytes(entity
				.getHuobdh() == null ? "" : entity.getHuobdh())));
		kvs.add(new KeyValue(rowkey, QueryConstants.HBASE_TABLE_FAMILY_BYTE, PybjyField.CHUIBZ, Bytes.toBytes(entity
				.getChuibz() == null ? "" : entity.getChuibz())));
		kvs.add(new KeyValue(rowkey, QueryConstants.HBASE_TABLE_FAMILY_BYTE, PybjyField.SHFOBZ, Bytes.toBytes(entity
				.getShfobz())));
		kvs.add(new KeyValue(rowkey, QueryConstants.HBASE_TABLE_FAMILY_BYTE, PybjyField.PRINTSTYLE, Bytes
				.toBytes(entity.getPrintStyle() == null ? "" : entity.getPrintStyle())));
		kvs.add(new KeyValue(rowkey, QueryConstants.HBASE_TABLE_FAMILY_BYTE, PybjyField.JIOYBZ, Bytes.toBytes(entity
				.getJioybz().booleanValue())));
		kvs.add(new KeyValue(rowkey, QueryConstants.HBASE_TABLE_FAMILY_BYTE, PybjyField.SHOQGY, Bytes.toBytes(entity
				.getShoqgy() == null ? "" : entity.getShoqgy())));
		kvs.add(new KeyValue(rowkey, QueryConstants.HBASE_TABLE_FAMILY_BYTE, PybjyField.QUDAOO, Bytes.toBytes(entity
				.getQudaoo() == null ? "" : entity.getQudaoo())));
		kvs.add(new KeyValue(rowkey, QueryConstants.HBASE_TABLE_FAMILY_BYTE, PybjyField.GUIYJB, Bytes.toBytes(entity
				.getGuiyjb())));
		kvs.add(new KeyValue(rowkey, QueryConstants.HBASE_TABLE_FAMILY_BYTE, PybjyField.BEIZXX, Bytes.toBytes(entity
				.getBeizxx() == null ? "" : entity.getBeizxx())));
		kvs.add(new KeyValue(rowkey, QueryConstants.HBASE_TABLE_FAMILY_BYTE, PybjyField.STARTTIME, Bytes.toBytes(entity
				.getStartTime().longValue())));
		kvs.add(new KeyValue(rowkey, QueryConstants.HBASE_TABLE_FAMILY_BYTE, PybjyField.ENDTIME, Bytes.toBytes(entity
				.getEndTime().longValue())));

		return kvs;
	}
}