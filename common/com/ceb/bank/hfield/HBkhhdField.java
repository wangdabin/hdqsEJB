package com.ceb.bank.hfield;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.hadoop.hbase.util.Bytes;

public class HBkhhdField {
	public static final byte[] KHHDLX = Bytes.toBytes("KHHDLX");
	public static final byte[] YNGYJG = Bytes.toBytes("YNGYJG");
	public static final byte[] JIOYRQ = Bytes.toBytes("JIOYRQ");
	public static final byte[] GUIYDH = Bytes.toBytes("GUIYDH");
	public static final byte[] GUIYLS = Bytes.toBytes("GUIYLS");
	public static final byte[] DSYHBH = Bytes.toBytes("DSYHBH");
	public static final byte[] JIO1JE = Bytes.toBytes("JIO1JE");
	public static final byte[] JIAOYM = Bytes.toBytes("JIAOYM");
	public static final byte[] HUOBDH = Bytes.toBytes("HUOBDH");
	public static final byte[] PNGZHH = Bytes.toBytes("PNGZHH");
	public static final byte[] ZHYONR = Bytes.toBytes("ZHYONR");
	public static final byte[] QIXIRQ = Bytes.toBytes("QIXIRQ");
	public static final byte[] SHFBZ1 = Bytes.toBytes("SHFBZ1");
	public static final byte[] SHKHBM = Bytes.toBytes("SHKHBM");
	public static final byte[] SHKHMC = Bytes.toBytes("SHKHMC");
	public static final byte[] SKRZHH = Bytes.toBytes("SKRZHH");
	public static final byte[] SKRZWM = Bytes.toBytes("SKRZWM");
	public static final byte[] SKRYWM = Bytes.toBytes("SKRYWM");
	public static final byte[] SHFBZ2 = Bytes.toBytes("SHFBZ2");
	public static final byte[] FUKHBM = Bytes.toBytes("FUKHBM");
	public static final byte[] FUKHMC = Bytes.toBytes("FUKHMC");
	public static final byte[] FKRZHH = Bytes.toBytes("FKRZHH");
	public static final byte[] FKRZWM = Bytes.toBytes("FKRZWM");
	public static final byte[] FKRYWM = Bytes.toBytes("FKRYWM");
	public static final byte[] DAYNBZ = Bytes.toBytes("DAYNBZ");
	public static final byte[] FASCSH = Bytes.toBytes("FASCSH");
	public static final byte[] QUDAOO = Bytes.toBytes("QUDAOO");
	public static final byte[] BEIY01 = Bytes.toBytes("BEIY01");
	public static final byte[] BEIY04 = Bytes.toBytes("BEIY04");
	public static final byte[] BEIY40 = Bytes.toBytes("BEIY40");
	public static final byte[] SHJNCH = Bytes.toBytes("SHJNCH");
	public static final byte[] JILUZT = Bytes.toBytes("JILUZT");
	public static final byte[] RIZHXH = Bytes.toBytes("RIZHXH");
	
	
	private static final ReentrantLock lock = new ReentrantLock();
	private static final Map<String, byte[]> MX_QUALIFIERS = new HashMap<String, byte[]>();
	
	
	public static Map<String, byte[]> getMxQualifiers() {
		if (MX_QUALIFIERS.isEmpty()) {
			try {
				lock.lock();
				if (MX_QUALIFIERS.isEmpty()) {
					
					MX_QUALIFIERS.put("khhdlx", KHHDLX);
					MX_QUALIFIERS.put("fkrzhh", FKRZHH);
					MX_QUALIFIERS.put("fkrzwm", FKRZWM);
					MX_QUALIFIERS.put("fukhbm", FUKHBM);
					MX_QUALIFIERS.put("fukhmc", FUKHMC);
					MX_QUALIFIERS.put("skrzhh", SKRZHH);
					MX_QUALIFIERS.put("skrzwm", SKRZWM);
					MX_QUALIFIERS.put("shkhbm", SHKHBM);
					MX_QUALIFIERS.put("shkhmc", SHKHMC);
					MX_QUALIFIERS.put("huobdh", HUOBDH);
					MX_QUALIFIERS.put("jio1je", JIO1JE);
					MX_QUALIFIERS.put("pngzhh", PNGZHH);
					MX_QUALIFIERS.put("zhyonr", ZHYONR);
					MX_QUALIFIERS.put("jioyrq", JIOYRQ);
					MX_QUALIFIERS.put("guiydh", GUIYDH);
					MX_QUALIFIERS.put("guiyls", GUIYLS);
					MX_QUALIFIERS.put("jiaoym", JIAOYM);
					MX_QUALIFIERS.put("daynbz", DAYNBZ);
					MX_QUALIFIERS.put("rizhxh", RIZHXH);
					
				}
			} finally {
				lock.unlock();
			}
		}
		return MX_QUALIFIERS;
	}


}
