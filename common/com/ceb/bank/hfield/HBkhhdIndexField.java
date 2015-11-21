package com.ceb.bank.hfield;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.hadoop.hbase.util.Bytes;

public class HBkhhdIndexField {
	public static final byte[] MTINDEX = Bytes.toBytes("MTINDEX");
	
	private static final ReentrantLock lock = new ReentrantLock();
	private static final Map<String, byte[]> MX_QUALIFIERS = new HashMap<String, byte[]>();
	
	
	public static Map<String, byte[]> getMxQualifiers() {
		if (MX_QUALIFIERS.isEmpty()) {
			try {
				lock.lock();
				if (MX_QUALIFIERS.isEmpty()) {
					
					MX_QUALIFIERS.put("mtindex", MTINDEX);
					
				}
			} finally {
				lock.unlock();
			}
		}
		return MX_QUALIFIERS;
	}

}
