package com.ceb.hdqs.entity.field;

import org.apache.hadoop.hbase.util.Bytes;

public class BhqtzField {
	public static final byte[] LEIXIN_TZ = Bytes.toBytes("0");
	public static final byte[] HUOBDH = Bytes.toBytes("HUOBDH");
	public static final byte[] YNGYJG = Bytes.toBytes("YNGYJG");
	public static final byte[] YEWUDH = Bytes.toBytes("YEWUDH");
	public static final byte[] KEHHAO = Bytes.toBytes("KEHHAO");

	public static final byte[] LEIXIN = Bytes.toBytes("LEIXIN");// 透支类型VARCHAR2(1),
	public static final byte[] ZHANGH = Bytes.toBytes("ZHANGH");// 活期帐号VARCHAR2(21),
	public static final byte[] TOUZZH = Bytes.toBytes("TOUZZH");// 透支帐号VARCHAR2(20),
	public static final byte[] ZHA1ZH = Bytes.toBytes("ZHA1ZH");// 应收未收帐号VARCHAR2(21),
}
