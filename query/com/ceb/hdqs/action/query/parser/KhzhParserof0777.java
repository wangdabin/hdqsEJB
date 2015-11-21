package com.ceb.hdqs.action.query.parser;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;

import com.ceb.hdqs.action.query.exception.InvalidRecordParameterException;
import com.ceb.hdqs.entity.PybjyEO;
import com.ceb.hdqs.query.utils.QueryConfUtils;
import com.ceb.hdqs.query.utils.QueryConstants;

/**
 * 对公查询输入条件为客户账号时，解析该客户账号的类 首先
 * 
 * @author user
 * 
 */
public class KhzhParserof0777 extends KhzhParserAsyn {
	private static final Log LOG = LogFactory.getLog(KhzhParserof0777.class);

	public KhzhParserof0777() {
	}

	@Override
	public Scan buildParseScanner(PybjyEO record) throws InvalidRecordParameterException {
		String kehuzh = StringUtils.reverse(record.getKehuzh());

		Scan scan = new Scan();
		byte[] startRow = buildStartRowByKHZH(kehuzh);
		byte[] stopRow = buildStopRowByKHZH(kehuzh);
		scan.setStartRow(startRow);
		scan.setStopRow(stopRow);
		try {
			LOG.debug(scan.toJSON());
		} catch (IOException e) {
			LOG.debug(e.getMessage(), e);
		}
		return scan;
	}

	/**
	 * 生成查询AKHZH的stopRowKey
	 * 
	 * @param kehuzh
	 * @return
	 */
	private byte[] buildStopRowByKHZH(String kehuzh) {
		Configuration conf = QueryConfUtils.getActiveConfig();
		String rowKeySplit = conf.get(QueryConstants.ROWKEY_SPLITTER, "|");
		StringBuilder stopRow = new StringBuilder();
		stopRow.append(kehuzh).append(rowKeySplit).append(QueryConstants.MAX_CHAR);
		return Bytes.toBytes(stopRow.toString());
	}

	/**
	 * 生成查询AKHZH的startrowkey
	 * 
	 * @param kehuzh
	 * @return
	 */
	private byte[] buildStartRowByKHZH(String kehuzh) {
		Configuration conf = QueryConfUtils.getActiveConfig();
		String rowKeySplit = conf.get(QueryConstants.ROWKEY_SPLITTER, "|");
		StringBuilder startRow = new StringBuilder();
		startRow.append(kehuzh).append(rowKeySplit).append(QueryConstants.MIN_NUM);
		return Bytes.toBytes(startRow.toString());
	}

}
