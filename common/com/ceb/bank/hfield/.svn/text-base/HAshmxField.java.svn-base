package com.ceb.bank.hfield;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.hadoop.hbase.util.Bytes;

/**
 * 
 * ASHMX 对私活期明细
 * 
 * <PRE>
 * ROWKEY = StringUtils.reverse(ZHANGH) + | + JILUZT + | + JIOYRQ +| + SHJNCH + | + GUIYLS + | + CPZNXH;
 * </PRE>
 */
public class HAshmxField {

	public static final byte[] ZHANGH = Bytes.toBytes("ZHANGH");
	public static final byte[] JIOYRQ = Bytes.toBytes("JIOYRQ");
	public static final byte[] ZHUJRQ = Bytes.toBytes("ZHUJRQ");
	public static final byte[] JIOYSJ = Bytes.toBytes("JIOYSJ");
	public static final byte[] JIAOYM = Bytes.toBytes("JIAOYM");
	public static final byte[] PNGZHH = Bytes.toBytes("PNGZHH");
	public static final byte[] JIEDBZ = Bytes.toBytes("JIEDBZ");
	public static final byte[] JIO1JE = Bytes.toBytes("JIO1JE");
	public static final byte[] ZHHUYE = Bytes.toBytes("ZHHUYE");
	public static final byte[] YUEEXZ = Bytes.toBytes("YUEEXZ");
	public static final byte[] YUEEFX = Bytes.toBytes("YUEEFX");
	public static final byte[] KEHUZH = Bytes.toBytes("KEHUZH");
	public static final byte[] KHZHLX = Bytes.toBytes("KHZHLX");
	public static final byte[] SHUNXH = Bytes.toBytes("SHUNXH");
	public static final byte[] YNGYJG = Bytes.toBytes("YNGYJG");
	public static final byte[] ZHNGJG = Bytes.toBytes("ZHNGJG");
	public static final byte[] ZHYYJG = Bytes.toBytes("ZHYYJG");
	public static final byte[] ZHKJJG = Bytes.toBytes("ZHKJJG");
	public static final byte[] SHDHDM = Bytes.toBytes("SHDHDM");
	public static final byte[] SHHUBH = Bytes.toBytes("SHHUBH");
	public static final byte[] DANWMC = Bytes.toBytes("DANWMC");
	public static final byte[] JIO1GY = Bytes.toBytes("JIO1GY");
	public static final byte[] SHOQGY = Bytes.toBytes("SHOQGY");
	public static final byte[] GUIYLS = Bytes.toBytes("GUIYLS");
	public static final byte[] YNGYLS = Bytes.toBytes("YNGYLS");
	public static final byte[] XNZHBZ = Bytes.toBytes("XNZHBZ");
	public static final byte[] ZHYODM = Bytes.toBytes("ZHYODM");
	public static final byte[] CPZNXH = Bytes.toBytes("CPZNXH");
	public static final byte[] CHBUBZ = Bytes.toBytes("CHBUBZ");
	public static final byte[] YNJYRQ = Bytes.toBytes("YNJYRQ");
	public static final byte[] DUIFZH = Bytes.toBytes("DUIFZH");
	public static final byte[] DUIFMC = Bytes.toBytes("DUIFMC");
	public static final byte[] SHJNCH = Bytes.toBytes("SHJNCH");
	public static final byte[] JILUZT = Bytes.toBytes("JILUZT");

	private static final ReentrantLock lock = new ReentrantLock();
	private static final Map<String, byte[]> LX_QUALIFIERS = new HashMap<String, byte[]>();
	private static final Map<String, byte[]> MX_QUALIFIERS = new HashMap<String, byte[]>();

	public static Map<String, byte[]> getLxQualifiers() {
		if (LX_QUALIFIERS.isEmpty()) {
			try {
				lock.lock();
				if (LX_QUALIFIERS.isEmpty()) {
					LX_QUALIFIERS.put("jio1je", JIO1JE);
					LX_QUALIFIERS.put("zhhuye", ZHHUYE);
					LX_QUALIFIERS.put("yueefx", YUEEFX);
					LX_QUALIFIERS.put("jiedbz", JIEDBZ);
					LX_QUALIFIERS.put("jioyrq", JIOYRQ);
					LX_QUALIFIERS.put("shjnch", SHJNCH);
				}
			} finally {
				lock.unlock();
			}
		}
		return LX_QUALIFIERS;
	}

	public static Map<String, byte[]> getMxQualifiers() {
		if (MX_QUALIFIERS.isEmpty()) {
			try {
				lock.lock();
				if (MX_QUALIFIERS.isEmpty()) {
					MX_QUALIFIERS.put("jio1je", JIO1JE);
					MX_QUALIFIERS.put("zhhuye", ZHHUYE);
					MX_QUALIFIERS.put("yueefx", YUEEFX);
					MX_QUALIFIERS.put("jiedbz", JIEDBZ);
					MX_QUALIFIERS.put("jioyrq", JIOYRQ);
					MX_QUALIFIERS.put("shjnch", SHJNCH);
					MX_QUALIFIERS.put("zhangh", ZHANGH);
					MX_QUALIFIERS.put("jioysj", JIOYSJ);
					MX_QUALIFIERS.put("jiaoym", JIAOYM);
					MX_QUALIFIERS.put("jio1gy", JIO1GY);
					MX_QUALIFIERS.put("shoqgy", SHOQGY);
					MX_QUALIFIERS.put("chbubz", CHBUBZ);
					MX_QUALIFIERS.put("guiyls", GUIYLS);
					MX_QUALIFIERS.put("zhyodm", ZHYODM);
					MX_QUALIFIERS.put("yngyjg", YNGYJG);
					MX_QUALIFIERS.put("khzhlx", KHZHLX);
					MX_QUALIFIERS.put("pngzhh", PNGZHH);
					MX_QUALIFIERS.put("duifzh", DUIFZH);
					MX_QUALIFIERS.put("duifmc", DUIFMC);
					MX_QUALIFIERS.put("xnzhbz", XNZHBZ);// 现转标志
					MX_QUALIFIERS.put("danwmc", DANWMC);
					MX_QUALIFIERS.put("shdhdm", SHDHDM);
				}
			} finally {
				lock.unlock();
			}
		}
		return MX_QUALIFIERS;
	}
}
