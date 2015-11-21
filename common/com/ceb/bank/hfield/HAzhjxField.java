package com.ceb.bank.hfield;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.hadoop.hbase.util.Bytes;

/**
 * AZHJX帐号解析表
 * 
 * <PRE>
 * ROWKEY = StringUtils.reverse(KEHHAO)+|+ZHANGH
 * </PRE>
 */
public class HAzhjxField {
	public static final byte[] ZHANGH = Bytes.toBytes("ZHANGH");// 0
	public static final byte[] ZHYYJG = Bytes.toBytes("ZHYYJG");// 1
	public static final byte[] HUOBDH = Bytes.toBytes("HUOBDH");// 2
	public static final byte[] YEWUDH = Bytes.toBytes("YEWUDH");// 3
	public static final byte[] ZHAOXH = Bytes.toBytes("ZHAOXH");// 4
	public static final byte[] JIANCW = Bytes.toBytes("JIANCW");// 5
	public static final byte[] ZHKJJG = Bytes.toBytes("ZHKJJG");// 6
	public static final byte[] KEMUCC = Bytes.toBytes("KEMUCC");// 7
	public static final byte[] KEHUZH = Bytes.toBytes("KEHUZH");// 8
	public static final byte[] KHZHLX = Bytes.toBytes("KHZHLX");// 9
	public static final byte[] KEHHAO = Bytes.toBytes("KEHHAO");// 10
	public static final byte[] KAIHRQ = Bytes.toBytes("KAIHRQ");// 11
	public static final byte[] KAIHGY = Bytes.toBytes("KAIHGY");// 12
	public static final byte[] KAIHJE = Bytes.toBytes("KAIHJE");// 13
	public static final byte[] KHGYLS = Bytes.toBytes("KHGYLS");// 14
	public static final byte[] XIOHJG = Bytes.toBytes("XIOHJG");// 15
	public static final byte[] XIOHRQ = Bytes.toBytes("XIOHRQ");// 16
	public static final byte[] LIXILX = Bytes.toBytes("LIXILX");// 17
	public static final byte[] XIOHGY = Bytes.toBytes("XIOHGY");// 18
	public static final byte[] XHGYLS = Bytes.toBytes("XHGYLS");// 19
	public static final byte[] DGDSBZ = Bytes.toBytes("DGDSBZ");// 20
	public static final byte[] SHJNCH = Bytes.toBytes("SHJNCH");// 21
	public static final byte[] JILUZT = Bytes.toBytes("JILUZT");// 22
	public static final byte[] RIZHXH = Bytes.toBytes("RIZHXH");// 23

	private static final ReentrantLock lock = new ReentrantLock();
	private static final Map<String, byte[]> MX_QUALIFIERS = new HashMap<String, byte[]>();

	public static Map<String, byte[]> getMxQualifiers() {
		if (MX_QUALIFIERS.isEmpty()) {
			try {
				lock.lock();
				if (MX_QUALIFIERS.isEmpty()) {
					MX_QUALIFIERS.put("kehhao", KEHHAO);
					MX_QUALIFIERS.put("kehuzh", KEHUZH);
					MX_QUALIFIERS.put("khzhlx", KHZHLX);
					MX_QUALIFIERS.put("zhangh", ZHANGH);//
					MX_QUALIFIERS.put("yewudh", YEWUDH);//
					MX_QUALIFIERS.put("huobdh", HUOBDH);//
					MX_QUALIFIERS.put("yngyjg", ZHYYJG);//
					MX_QUALIFIERS.put("kemucc", KEMUCC);//
				}
			} finally {
				lock.unlock();
			}
		}
		return MX_QUALIFIERS;
	}
}
