package com.ceb.bank.hfield;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.hadoop.hbase.util.Bytes;

/**
 * AGHFH 对公活期主文件
 * 
 * <PRE>
 * ROWKEY = StringUtils.reverse(ZHANGH)
 * </PRE>
 */
public class HAdgfhField {

	public static final byte[] ZHANGH = Bytes.toBytes("ZHANGH");
	public static final byte[] YNGYJG = Bytes.toBytes("YNGYJG");
	public static final byte[] ZHNGJG = Bytes.toBytes("ZHNGJG");
	public static final byte[] HUOBDH = Bytes.toBytes("HUOBDH");
	public static final byte[] YEWUDH = Bytes.toBytes("YEWUDH");
	public static final byte[] ZHAOXH = Bytes.toBytes("ZHAOXH");
	public static final byte[] CHUIBZ = Bytes.toBytes("CHUIBZ");
	public static final byte[] KEHHAO = Bytes.toBytes("KEHHAO");
	public static final byte[] KEHZWM = Bytes.toBytes("KEHZWM");
	public static final byte[] XINXNR = Bytes.toBytes("XINXNR");
	public static final byte[] KMUHAO = Bytes.toBytes("KMUHAO");
	public static final byte[] YUEEXZ = Bytes.toBytes("YUEEXZ");
	public static final byte[] SCJXRQ = Bytes.toBytes("SCJXRQ");
	public static final byte[] SRZHYE = Bytes.toBytes("SRZHYE");
	public static final byte[] SRYEFX = Bytes.toBytes("SRYEFX");
	public static final byte[] BLIUYE = Bytes.toBytes("BLIUYE");
	public static final byte[] DJIEYE = Bytes.toBytes("DJIEYE");
	public static final byte[] KNGZYE = Bytes.toBytes("KNGZYE");
	public static final byte[] QNRIYE = Bytes.toBytes("QNRIYE");
	public static final byte[] ZHHUYE = Bytes.toBytes("ZHHUYE");
	public static final byte[] YUEEFX = Bytes.toBytes("YUEEFX");
	public static final byte[] JIXIFF = Bytes.toBytes("JIXIFF");
	public static final byte[] LILVBH = Bytes.toBytes("LILVBH");
	public static final byte[] NYUELL = Bytes.toBytes("NYUELL");
	public static final byte[] YOUHLL = Bytes.toBytes("YOUHLL");
	public static final byte[] FDNGLX = Bytes.toBytes("FDNGLX");
	public static final byte[] FUDNGZ = Bytes.toBytes("FUDNGZ");
	public static final byte[] LEIJLX = Bytes.toBytes("LEIJLX");
	public static final byte[] JISHUU = Bytes.toBytes("JISHUU");
	public static final byte[] YJAJLX = Bytes.toBytes("YJAJLX");
	public static final byte[] YJAJJS = Bytes.toBytes("YJAJJS");
	public static final byte[] TOUZBZ = Bytes.toBytes("TOUZBZ");
	public static final byte[] TOUZZH = Bytes.toBytes("TOUZZH");
	public static final byte[] TOUZED = Bytes.toBytes("TOUZED");
	public static final byte[] TOUZLL = Bytes.toBytes("TOUZLL");
	public static final byte[] TOUZJS = Bytes.toBytes("TOUZJS");
	public static final byte[] CKLXRM = Bytes.toBytes("CKLXRM");
	public static final byte[] KAIHRQ = Bytes.toBytes("KAIHRQ");
	public static final byte[] KAIHGY = Bytes.toBytes("KAIHGY");
	public static final byte[] WEIHRQ = Bytes.toBytes("WEIHRQ");
	public static final byte[] WEIHGY = Bytes.toBytes("WEIHGY");
	public static final byte[] XIOHRQ = Bytes.toBytes("XIOHRQ");
	public static final byte[] XIOHGY = Bytes.toBytes("XIOHGY");
	public static final byte[] ZHJYRQ = Bytes.toBytes("ZHJYRQ");
	public static final byte[] JIOYRQ = Bytes.toBytes("JIOYRQ");
	public static final byte[] ZHHUSX = Bytes.toBytes("ZHHUSX");
	public static final byte[] XIEYBH = Bytes.toBytes("XIEYBH");
	public static final byte[] GUOXBZ = Bytes.toBytes("GUOXBZ");
	public static final byte[] RIJYXE = Bytes.toBytes("RIJYXE");
	public static final byte[] SHMING = Bytes.toBytes("SHMING");
	public static final byte[] ZDKZBZ = Bytes.toBytes("ZDKZBZ");
	public static final byte[] JBZHBZ = Bytes.toBytes("JBZHBZ");
	public static final byte[] TNGJDM = Bytes.toBytes("TNGJDM");
	public static final byte[] TCTDBZ = Bytes.toBytes("TCTDBZ");
	public static final byte[] CZZPBZ = Bytes.toBytes("CZZPBZ");
	public static final byte[] ZHFUTJ = Bytes.toBytes("ZHFUTJ");
	public static final byte[] CHAXMM = Bytes.toBytes("CHAXMM");
	public static final byte[] JIOYMM = Bytes.toBytes("JIOYMM");
	public static final byte[] JIXIZQ = Bytes.toBytes("JIXIZQ");
	public static final byte[] KAIHSH = Bytes.toBytes("KAIHSH");
	public static final byte[] ZONGHS = Bytes.toBytes("ZONGHS");
	public static final byte[] SHJNCH = Bytes.toBytes("SHJNCH");
	public static final byte[] JILUZT = Bytes.toBytes("JILUZT");

	private static final ReentrantLock lock = new ReentrantLock();
	private static final Map<String, byte[]> ZHANGH_QUALIFIERS = new HashMap<String, byte[]>();

	public static Map<String, byte[]> getZhanghQualifiers() {
		if (ZHANGH_QUALIFIERS.isEmpty()) {
			try {
				lock.lock();
				if (ZHANGH_QUALIFIERS.isEmpty()) {
					ZHANGH_QUALIFIERS.put("kehhao", KEHHAO);
					ZHANGH_QUALIFIERS.put("kehzwm", KEHZWM);
					ZHANGH_QUALIFIERS.put("yewudh", YEWUDH);
					ZHANGH_QUALIFIERS.put("zhangh", ZHANGH);
					ZHANGH_QUALIFIERS.put("huobdh", HUOBDH);
					ZHANGH_QUALIFIERS.put("chuibz", CHUIBZ);
					ZHANGH_QUALIFIERS.put("yngyjg", YNGYJG);
				}
			} finally {
				lock.unlock();
			}
		}
		return ZHANGH_QUALIFIERS;
	}
}
