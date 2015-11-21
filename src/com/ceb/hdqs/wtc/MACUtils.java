package com.ceb.hdqs.wtc;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import weblogic.wtc.gwt.TuxedoConnection;
import weblogic.wtc.gwt.TuxedoConnectionFactory;
import weblogic.wtc.jatmi.Reply;
import weblogic.wtc.jatmi.TypedCArray;

import com.ceb.hdqs.config.ConfigLoader;
import com.ceb.hdqs.config.RegisterTable;
import com.ceb.hdqs.po.SecretKeyPO;
import com.ceb.hdqs.sop.SopIntf;
import com.union.HsmAPI.gdHsmAPI;

public final class MACUtils {
	private static final Logger log = Logger.getLogger(MACUtils.class);

	public static final String MAC_EXCEPTION_VALUE = "-1111111";
	public static final String MAC_SUCCESS_VALUE = "0";
	private static final String MAC_FIELD_NAME = "BAWMAC";
	private static final String MAC_UPDATE_KEY = "HDQS.00009260.zak";
	private static final String MAC_RECEIVE_KEY = "HDQS.00009260.zpk";
	private static final int MAC_INDEX = 18;

	/**
	 * 根据请求报文获取mac值
	 * 
	 * @param recvSop
	 * @return
	 */
	public static String getMAC(SopIntf recvSop) {
		return recvSop.getStr(null, MAC_FIELD_NAME).trim();
	}

	/**
	 * 根据mac进行验证
	 * 
	 * @param recvSop
	 * @return
	 */
	public static String validateMAC(SopIntf recvSop, byte[] recvBytes) {
		try {
			String mac = getMAC(recvSop);
			log.info("Get mac: " + mac + ",recv bytes length:" + recvBytes.length);
			String macCfgFile = ConfigLoader.getInstance().getProperty(RegisterTable.MAC_CONFIG_FILE_PATH);
			gdHsmAPI hsmAPI = new gdHsmAPI(macCfgFile);
			int macDataLen = recvBytes.length - MAC_INDEX;
			byte[] macData = new byte[macDataLen];
			System.arraycopy(recvBytes, MAC_INDEX, macData, 0, macDataLen);
			hsmAPI.Init();
			hsmAPI.UnionVerifyMac(MACUtils.MAC_UPDATE_KEY, macDataLen, macData, mac);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return MAC_EXCEPTION_VALUE;
		}
		return MAC_SUCCESS_VALUE;
	}

	/**
	 * 根据生成mac
	 * 
	 * @param recvSop
	 * @return
	 */
	public static String createMAC(byte[] macData) {
		try {
			String macCfgFile = ConfigLoader.getInstance().getProperty(RegisterTable.MAC_CONFIG_FILE_PATH);
			gdHsmAPI hsmAPI = new gdHsmAPI(macCfgFile);
			hsmAPI.Init();
			return hsmAPI.UnionGenMac(MACUtils.MAC_UPDATE_KEY, macData.length, macData);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return MAC_EXCEPTION_VALUE;
		}
	}

	/**
	 * 根据mac进行验证
	 * 
	 * @param recvSop
	 * @return
	 */
	public static String updateMAC() {
		String result = "success";
		try {
			SecretKeyPO skpo = MACUtils.getSecretKeyPO();
			String macCfgFile = ConfigLoader.getInstance().getProperty(RegisterTable.MAC_CONFIG_FILE_PATH);
			gdHsmAPI hsmAPI = new gdHsmAPI(macCfgFile);
			hsmAPI.Init();
			int flag = hsmAPI.UnionStoreKey(MACUtils.MAC_UPDATE_KEY, skpo.getMacmyc(), skpo.getMacjyz());
			if (flag != 0) {
				result = "failure";
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result = "failure";
		}
		return result;
	}

	private static final int MAC_BYTES_LENGTH = 210;

	private static SecretKeyPO getSecretKeyPO() {
		try {
			Context ctx = new InitialContext();
			TuxedoConnectionFactory tcf = (TuxedoConnectionFactory) ctx.lookup("tuxedo.services.TuxedoConnection");
			TuxedoConnection tuxedoConn = tcf.getTuxedoConnection();
			TypedCArray tf = new TypedCArray(MAC_BYTES_LENGTH);
			byte[] sendData = new byte[MAC_BYTES_LENGTH];
			sendData = convertPoolToMacSop();
			log.info("the length of sendData:" + sendData.length);
			tf.carray = sendData;

			Reply cdmaRtn = tuxedoConn.tpcall("PBMIDGW260", tf, 0);
			TypedCArray cdmaData = (TypedCArray) cdmaRtn.getReplyBuffer();
			tuxedoConn.tpterm();

			SopIntf sop = new SopIntf();
			sop.convertSopToPool(cdmaData.carray, cdmaData.carray.length);
			printRecvHeaderPkt(sop);
			SecretKeyPO skp = new SecretKeyPO();
			skp.setZpkmyn(sop.getStr("O900b2", "ZPKMYN"));
			skp.setPinmyc(sop.getStr("O900b2", "PINMYC"));
			skp.setPinjyz(sop.getStr("O900b2", "PINJYZ"));
			skp.setZakmyn(sop.getStr("O900b2", "ZAKMYN"));
			skp.setMacmyc(sop.getStr("O900b2", "MACMYC"));
			skp.setMacjyz(sop.getStr("O900b2", "MACJYZ"));
			return skp;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return null;
		}
	}

	private static byte[] convertPoolToMacSop() {
		SopIntf sop = buildSysHead();
		sop.put(null, "JIAOYM", "900b");
		// 交易数据
		sop.put("O07811", "ZPKMYN", MAC_RECEIVE_KEY);
		byte[] localbuffer = new byte[210];
		String[] inmap = new String[] { "O07811" };
		int res = sop.convertPoolToPkt(localbuffer, inmap);
		log.info("Mac bytes length:" + res);
		return localbuffer;

	}

	private static SopIntf buildSysHead() {
		SopIntf sop = new SopIntf();
		sop.put(null, "BAWMAC", "0000000000000000");
		sop.put(null, "MACJGH", "0000");
		sop.put(null, "PINZHZ", "0000000000000000");
		sop.put(null, "CHNLNO", "010");
		sop.put(null, "QUDQUX", "870");
		sop.put(null, "MACFLG", "0");
		sop.put(null, "PINFLG", "0");
		sop.put(null, "COMFLG", "0");
		sop.put(null, "ZHUJFW", "chuxuu_02");
		sop.put(null, "XXJSBZ", "8");
		sop.put(null, "SJBSXH", "12288");
		sop.put(null, "JIOYBZ", "0");
		// sop.put(null, "MIYBBH", "3472328296362356784");
		sop.put(null, "QUDAOO", "870");
		sop.put(null, "MIMPYL", "00000000000000000000");
		sop.put(null, "ZHHPYL", "00000000000000000000");
		// //输入公共头
		sop.put(null, "ZHNGDH", "xx");
		sop.put(null, "CHSHDM", "0000");
		sop.put(null, "YNGYJG", "1001");
		sop.put(null, "JIO1GY", "90130100");
		// //输入交易头
		sop.put(null, "JIOYZM", "00");
		// sop.put(null, "JIOYMS", FDJIOYMS);
		Integer FDJIOYXH = 8257536;
		sop.put(null, "JIOYXH", getBytesFromNumberType(FDJIOYXH));
		short FDCOMMLN = (short) 126;
		sop.put(null, "JYSJCD", getBytesFromNumberType(FDCOMMLN));
		short FD_COFF1 = (short) 0;
		sop.put(null, "PNYIL1", FD_COFF1);
		Short FD_COFF2 = (short) 0;
		sop.put(null, "PNYIL2", FD_COFF2);
		sop.put(null, "QANTLS", "000874164699");
		sop.put(null, "QANTRQ", "20130220");
		return sop;
	}

	static byte[] getBytesFromNumberType(Object val) {
		ByteArrayOutputStream bArrayOutputStream = new ByteArrayOutputStream();
		DataOutputStream dataOutputStream = new DataOutputStream(bArrayOutputStream);
		try {
			if (val instanceof Integer)
				dataOutputStream.writeInt((Integer) val);
			else if (val instanceof Short) {
				dataOutputStream.writeShort((Short) val);
			}
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
		return bArrayOutputStream.toByteArray();
	}

	private static void printRecvHeaderPkt(SopIntf recSop) {
		// #输入系统头
		log.info("数据包长度:" + recSop.getStr(null, "SHJBCD"));
		log.info("报文MAC:" + recSop.getStr(null, "BAWMAC"));
		log.info("MAC机构号:" + recSop.getStr(null, "MACJGH"));
		log.info("PIN种子:" + recSop.getStr(null, "PINZHZ"));
		log.info("渠道来源-前置保留:" + recSop.getStr(null, "CHNLNO"));
		log.info("渠道去向:" + recSop.getStr(null, "QUDQUX"));
		log.info("加密标志-是否是加密交易标志:" + recSop.getStr(null, "MACFLG"));
		log.info("PIN标志-是否包中有PIN:" + recSop.getStr(null, "PINFLG"));
		log.info("组合标志-是否是组合交易标志:" + recSop.getStr(null, "COMFLG"));
		log.info("主机服务:" + recSop.getStr(null, "ZHUJFW"));
		log.info("信息结束标志:" + recSop.getStr(null, "XXJSBZ"));
		log.info("数据包顺序号:" + recSop.getStr(null, "SJBSXH"));
		log.info("检验标志:" + recSop.getStr(null, "JIOYBZ"));
		log.info("密钥版本号:" + recSop.getStr(null, "MIYBBH"));
		log.info("系统标识码:" + recSop.getStr(null, "QUDAOO"));
		log.info("密码偏移量:" + recSop.getStr(null, "MIMPYL"));
		log.info("帐号偏移量:" + recSop.getStr(null, "ZHHPYL"));

		log.info("********************************");
	}
}