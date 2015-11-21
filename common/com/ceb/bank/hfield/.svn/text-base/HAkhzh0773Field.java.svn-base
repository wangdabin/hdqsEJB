package com.ceb.bank.hfield;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.hadoop.hbase.util.Bytes;

/**
 * AKHZH 对私客户帐号系统帐号对照表
 * 
 * <PRE>
 * ROWKEY = StringUtils.reverse(ZHUZZH) + | + SHJNCH + | + StringUtils.reverse(KEHUZH) + | + KHZHLX + | + SHUNXH
 * </PRE>
 */
public class HAkhzh0773Field {
	public static final byte[] KEHHAO = Bytes.toBytes("KEHHAO");// 0
	public static final byte[] KEHUZH = Bytes.toBytes("KEHUZH");// 1
	public static final byte[] KHZHLX = Bytes.toBytes("KHZHLX");// 2
	public static final byte[] ZHHUXZ = Bytes.toBytes("ZHHUXZ");// 3
	public static final byte[] HUOBDH = Bytes.toBytes("HUOBDH");// 4
	public static final byte[] CHUIBZ = Bytes.toBytes("CHUIBZ");// 5
	public static final byte[] SHUNXH = Bytes.toBytes("SHUNXH");// 6
	public static final byte[] ZHUZZH = Bytes.toBytes("ZHUZZH");// 7
	public static final byte[] SHJNCH = Bytes.toBytes("SHJNCH");// 8
	public static final byte[] JILUZT = Bytes.toBytes("JILUZT");// 9
	public static final byte[] RIZHXH = Bytes.toBytes("RIZHXH");// 10

	private static final ReentrantLock lock = new ReentrantLock();
	private static final Map<String, byte[]> MX_QUALIFIERS = new HashMap<String, byte[]>();

	public static Map<String, byte[]> getMxQualifiers() {
		if (MX_QUALIFIERS.isEmpty()) {
			try {
				lock.lock();
				if (MX_QUALIFIERS.isEmpty()) {
					MX_QUALIFIERS.put("kehuzh", KEHUZH);
					MX_QUALIFIERS.put("khzhlx", KHZHLX);
					MX_QUALIFIERS.put("chuibz", CHUIBZ);
				}
			} finally {
				lock.unlock();
			}
		}
		return MX_QUALIFIERS;
	}
}
