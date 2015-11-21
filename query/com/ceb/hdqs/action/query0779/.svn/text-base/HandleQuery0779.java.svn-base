package com.ceb.hdqs.action.query0779;

import java.io.IOException;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;

import com.ceb.bank.htable.AbstractQuery;
import com.ceb.hdqs.entity.PybjyEO;
import com.ceb.hdqs.entity.field.PybjyField;
import com.ceb.hdqs.query.utils.QueryConfUtils;
import com.ceb.hdqs.query.utils.QueryConstants;

/**
 * 根据受理编码取结果
 * 
 * @author user
 * 
 */
public class HandleQuery0779 extends AbstractQuery<PybjyEO> {

	public HandleQuery0779() {
		super(QueryConstants.HBASE_TABLE_NAME_ASYNQUERY);
	}

	public List<PybjyEO> query(String handleNo) throws Exception {
		Configuration conf = QueryConfUtils.getActiveConfig();
		String rowKeySplit = conf.get(QueryConstants.ROWKEY_SPLITTER, "|");

		Scan scan = new Scan();
		scan.addFamily(QueryConstants.HBASE_TABLE_FAMILY_BYTE);
		scan.setStartRow(Bytes.toBytes(handleNo + rowKeySplit + QueryConstants.MIN_NUM));
		scan.setStopRow(Bytes.toBytes(handleNo + rowKeySplit + QueryConstants.MAX_CHAR));

		List<PybjyEO> records = this.scan(scan);
		return records;
	}

	@Override
	protected PybjyEO parse(Result result) throws IOException {
		PybjyEO record = new PybjyEO();
		record.setJioyrq(Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, PybjyField.JIOYRQ)));

		record.setJiaoym(Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, PybjyField.JIAOYM)));
		record.setYngyjg(Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, PybjyField.YNGYJG)));
		record.setJio1gy(Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, PybjyField.JIO1GY)));
		record.setGuiyls(Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, PybjyField.GUIYLS)));
		record.setSlbhao(Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, PybjyField.SLBHAO)));
		record.setRunStatus(Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, PybjyField.RUNSTATUS)));
		record.setNotifyStatus(Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, PybjyField.NOTIFYSTATUS)));
		record.setChaxlx(Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, PybjyField.CHAXLX)));
		record.setChaxzl(Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, PybjyField.CHAXZL)));
		record.setZhangh(Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, PybjyField.ZHANGH)));
		record.setKehuzh(Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, PybjyField.KEHUZH)));
		record.setShunxh(Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, PybjyField.SHUNXH)));
		record.setKehuhao(Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, PybjyField.KEHUHAO)));
		record.setZhjnzl(Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, PybjyField.ZHJNZL)));
		record.setZhjhao(Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, PybjyField.ZHJHAO)));
		record.setKhzwm(Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, PybjyField.KHZWM)));
		record.setStartDate(Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, PybjyField.STARTDATE)));
		record.setEndDate(Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, PybjyField.ENDDATE)));
		record.setFileQuery(Bytes.toBoolean(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, PybjyField.FILEQUERY)));
		record.setRecvFile(Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, PybjyField.RECVFILE)));
		record.setPdfFile(Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, PybjyField.PDFFILE)));
		record.setXlsFile(Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, PybjyField.XLSFILE)));
		record.setCommNum(Bytes.toInt(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, PybjyField.COMMNUM)));
		record.setItemCount(Bytes.toLong(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, PybjyField.ITEMCOUNT)));
		record.setKhzhlx(Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, PybjyField.KHZHLX)));
		record.setZhhuxz(Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, PybjyField.ZHHUXZ)));
		record.setZhaoxz(Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, PybjyField.ZHAOXZ)));
		record.setHuobdh(Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, PybjyField.HUOBDH)));
		record.setChuibz(Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, PybjyField.CHUIBZ)));
		record.setShfobz(Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, PybjyField.SHFOBZ)));
		record.setPrintStyle(Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, PybjyField.PRINTSTYLE)));
		record.setJioybz(Bytes.toBoolean(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, PybjyField.JIOYBZ)));
		record.setShoqgy(Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, PybjyField.SHOQGY)));
		record.setQudaoo(Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, PybjyField.QUDAOO)));
		record.setGuiyjb(Bytes.toInt(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, PybjyField.GUIYJB)));
		record.setBeizxx(Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, PybjyField.BEIZXX)));
		record.setStartTime(Bytes.toLong(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, PybjyField.STARTTIME)));
		record.setEndTime(Bytes.toLong(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, PybjyField.ENDTIME)));

		return record;
	}
}
