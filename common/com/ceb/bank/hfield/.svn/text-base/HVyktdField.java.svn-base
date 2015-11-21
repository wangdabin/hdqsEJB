package com.ceb.bank.hfield;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.hadoop.hbase.util.Bytes;

/**
 * VYKTD 凭证信息文件A-卡
 * 
 * <PRE>
 * ROWKEY = StringUtils.reverse(KAHAOO)
 * </PRE>
 */
public class HVyktdField {
	public static final byte[] KEHHAO = Bytes.toBytes("KEHHAO");// 客户号
	public static final byte[] KAHAOO = Bytes.toBytes("KAHAOO");// 卡号
	public static final byte[] HQYBTH = Bytes.toBytes("HQYBTH");
	public static final byte[] DQYBTH = Bytes.toBytes("DQYBTH");
	public static final byte[] SHUNXH = Bytes.toBytes("SHUNXH");
	public static final byte[] YWZHBZ = Bytes.toBytes("YWZHBZ");
	public static final byte[] ZHNGJG = Bytes.toBytes("ZHNGJG");
	public static final byte[] YNGYJG = Bytes.toBytes("YNGYJG");// 营业机构
	public static final byte[] ZKJGDM = Bytes.toBytes("ZKJGDM");
	public static final byte[] FKJGDM = Bytes.toBytes("FKJGDM");
	public static final byte[] BIOWWX = Bytes.toBytes("BIOWWX");
	public static final byte[] JDJKBZ = Bytes.toBytes("JDJKBZ");
	public static final byte[] KAAAZL = Bytes.toBytes("KAAAZL");
	public static final byte[] KAAALX = Bytes.toBytes("KAAALX");
	public static final byte[] KAAADX = Bytes.toBytes("KAAADX");// 卡对象
	public static final byte[] ZHANGH = Bytes.toBytes("ZHANGH");
	public static final byte[] XNGMPY = Bytes.toBytes("XNGMPY");
	public static final byte[] KAAADJ = Bytes.toBytes("KAAADJ");
	public static final byte[] ZHFKBZ = Bytes.toBytes("ZHFKBZ");
	public static final byte[] ZHUKAH = Bytes.toBytes("ZHUKAH");
	public static final byte[] FAKAGY = Bytes.toBytes("FAKAGY");
	public static final byte[] FAKARQ = Bytes.toBytes("FAKARQ");
	public static final byte[] FAKAFS = Bytes.toBytes("FAKAFS");
	public static final byte[] HXIOGY = Bytes.toBytes("HXIOGY");
	public static final byte[] HXIORQ = Bytes.toBytes("HXIORQ");
	public static final byte[] JIOYMM = Bytes.toBytes("JIOYMM");
	public static final byte[] CHAXMM = Bytes.toBytes("CHAXMM");
	public static final byte[] CHFUCS = Bytes.toBytes("CHFUCS");
	public static final byte[] XLMMBZ = Bytes.toBytes("XLMMBZ");
	public static final byte[] HNKACS = Bytes.toBytes("HNKACS");
	public static final byte[] YOUXRQ = Bytes.toBytes("YOUXRQ");
	public static final byte[] DZSYZT = Bytes.toBytes("DZSYZT");
	public static final byte[] SFYMMF = Bytes.toBytes("SFYMMF");
	public static final byte[] FAKAQD = Bytes.toBytes("FAKAQD");
	public static final byte[] FKLXRM = Bytes.toBytes("FKLXRM");
	public static final byte[] JIFENN = Bytes.toBytes("JIFENN");
	public static final byte[] DFZWJG = Bytes.toBytes("DFZWJG");
	public static final byte[] YCVVBZ = Bytes.toBytes("YCVVBZ");
	public static final byte[] GLKZBZ = Bytes.toBytes("GLKZBZ");
	public static final byte[] RIZHXH = Bytes.toBytes("RIZHXH");
	public static final byte[] LJFKRQ = Bytes.toBytes("LJFKRQ");
	public static final byte[] KDEXXH = Bytes.toBytes("KDEXXH");
	public static final byte[] KDETXH = Bytes.toBytes("KDETXH");
	public static final byte[] YUXFRQ = Bytes.toBytes("YUXFRQ");
	public static final byte[] SFKNFF = Bytes.toBytes("SFKNFF");
	public static final byte[] SFKSXF = Bytes.toBytes("SFKSXF");
	public static final byte[] SFZDXQ = Bytes.toBytes("SFZDXQ");
	public static final byte[] SFZDXK = Bytes.toBytes("SFZDXK");
	public static final byte[] SFYDZD = Bytes.toBytes("SFYDZD");
	public static final byte[] GJKBZZ = Bytes.toBytes("GJKBZZ");
	public static final byte[] YIKTBZ = Bytes.toBytes("YIKTBZ");
	public static final byte[] XLKABZ = Bytes.toBytes("XLKABZ");
	public static final byte[] XNGMFH = Bytes.toBytes("XNGMFH");
	public static final byte[] KLXBHH = Bytes.toBytes("KLXBHH");
	public static final byte[] KXEXXH = Bytes.toBytes("KXEXXH");
	public static final byte[] KXETXH = Bytes.toBytes("KXETXH");
	public static final byte[] DQKZZE = Bytes.toBytes("DQKZZE");
	public static final byte[] YHBKZE = Bytes.toBytes("YHBKZE");
	public static final byte[] KYLJJE = Bytes.toBytes("KYLJJE");
	public static final byte[] KHQKCS = Bytes.toBytes("KHQKCS");
	public static final byte[] YJIUCS = Bytes.toBytes("YJIUCS");
	public static final byte[] YLIUBZ = Bytes.toBytes("YLIUBZ");
	public static final byte[] SLBHAO = Bytes.toBytes("SLBHAO");
	public static final byte[] BEIZHU = Bytes.toBytes("BEIZHU");
	public static final byte[] SHJNCH = Bytes.toBytes("SHJNCH");
	public static final byte[] JILUZT = Bytes.toBytes("JILUZT");
	private static final ReentrantLock lock = new ReentrantLock();
	private static final Map<String, byte[]> KAAADX_QUALIFIERS = new HashMap<String, byte[]>();

	public static Map<String, byte[]> getKaaadxQualifiers() {
		if (KAAADX_QUALIFIERS.isEmpty()) {
			try {
				lock.lock();
				if (KAAADX_QUALIFIERS.isEmpty()) {
					KAAADX_QUALIFIERS.put("yngyjg", YNGYJG);
					KAAADX_QUALIFIERS.put("kaaadx", KAAADX);
				}
			} finally {
				lock.unlock();
			}
		}
		return KAAADX_QUALIFIERS;
	}
}