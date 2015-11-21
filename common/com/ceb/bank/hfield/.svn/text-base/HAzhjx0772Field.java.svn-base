package com.ceb.bank.hfield;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.hadoop.hbase.util.Bytes;

/**
 * AZHJX0772 账号解析表 查询0772的国债 存单 定存
 * 
 * <PRE>
 * ROWKEY = StringUtils.reverse(ZHANGH)+|+JILUZT
 * </PRE>
 */
public class HAzhjx0772Field {
	public static final byte[] ZHANGH = Bytes.toBytes("ZHANGH");
	public static final byte[] HUOBDH = Bytes.toBytes("HUOBDH");
	public static final byte[] YEWUDH = Bytes.toBytes("YEWUDH");
	public static final byte[] ZHYYJG = Bytes.toBytes("ZHYYJG");
	public static final byte[] KEHUZH = Bytes.toBytes("KEHUZH");
	public static final byte[] KHZHLX = Bytes.toBytes("KHZHLX");
	public static final byte[] KEMUCC = Bytes.toBytes("KEMUCC");
	public static final byte[] KEHHAO = Bytes.toBytes("KEHHAO");
	public static final byte[] JILUZT = Bytes.toBytes("JILUZT");
	private static final ReentrantLock lock = new ReentrantLock();
	private static final Map<String, byte[]> MX_QUALIFIERS = new HashMap<String, byte[]>();
	private static final Map<String, byte[]> KEMUCC_QUALIFIERS = new HashMap<String, byte[]>();

	public static Map<String, byte[]> getMxQualifiers() {
		if (MX_QUALIFIERS.isEmpty()) {
			try {
				lock.lock();
				if (MX_QUALIFIERS.isEmpty()) {
					MX_QUALIFIERS.put("kehhao", KEHHAO);
					MX_QUALIFIERS.put("zhangh", ZHANGH);
					MX_QUALIFIERS.put("yewudh", YEWUDH);
					MX_QUALIFIERS.put("huobdh", HUOBDH);
					MX_QUALIFIERS.put("yngyjg", ZHYYJG);
					MX_QUALIFIERS.put("kemucc", KEMUCC);
				}
			} finally {
				lock.unlock();
			}
		}
		return MX_QUALIFIERS;
	}

	public static Map<String, byte[]> getKemuccQualifiers() {
		if (KEMUCC_QUALIFIERS.isEmpty()) {
			try {
				lock.lock();
				if (KEMUCC_QUALIFIERS.isEmpty()) {
					KEMUCC_QUALIFIERS.put("yewudh", YEWUDH);
					KEMUCC_QUALIFIERS.put("huobdh", HUOBDH);
					KEMUCC_QUALIFIERS.put("yngyjg", ZHYYJG);
					KEMUCC_QUALIFIERS.put("kemucc", KEMUCC);
				}
			} finally {
				lock.unlock();
			}
		}
		return KEMUCC_QUALIFIERS;
	}
}
