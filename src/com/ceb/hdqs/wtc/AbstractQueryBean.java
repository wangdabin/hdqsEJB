package com.ceb.hdqs.wtc;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.naming.NamingException;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import weblogic.wtc.jatmi.Reply;
import weblogic.wtc.jatmi.TPServiceInformation;
import weblogic.wtc.jatmi.TypedCArray;

import com.ceb.hdqs.action.query.parser.BkhzjParser;
import com.ceb.hdqs.config.ConfigLoader;
import com.ceb.hdqs.config.RegisterTable;
import com.ceb.hdqs.constants.HdqsConstants;
import com.ceb.hdqs.entity.GylsEO;
import com.ceb.hdqs.entity.PjyjlEO;
import com.ceb.hdqs.entity.PybjyEO;
import com.ceb.hdqs.po.Authorize;
import com.ceb.hdqs.query.entity.AuthorizeLevel;
import com.ceb.hdqs.query.utils.QueryConstants;
import com.ceb.hdqs.service.AuthorizeService;
import com.ceb.hdqs.service.GycsService;
import com.ceb.hdqs.service.GylsService;
import com.ceb.hdqs.service.JgcsService;
import com.ceb.hdqs.service.JyjlService;
import com.ceb.hdqs.service.SqcsService;
import com.ceb.hdqs.service.YbjyService;
import com.ceb.hdqs.service.cache.AbstractRowkeyItem;
import com.ceb.hdqs.sop.SopIntf;
import com.ceb.hdqs.utils.DateTimeFormatUtils;
import com.ceb.hdqs.utils.HdqsUtils;
import com.ceb.hdqs.utils.JNDIUtils;
import com.ceb.hdqs.wtc.form.AbstractForm;

public abstract class AbstractQueryBean {
	private Logger log;
	// 授权相关
	private static final String AUTHORIZE_CODE = "WAU0001";
	protected static final String NEED_AUTHORIZE_SIGN = "1";
	// 错误提示
	protected static final String ERR_NUM = "-1";
	protected static final String ERR_CODE = "EGG0521";

	private GylsService gylsService;
	private JyjlService jyjlService;
	private YbjyService ybjyService;
	private AuthorizeService authorizeService;
	private SqcsService sqcsService;
	private JgcsService jgcsService;
	private GycsService gycsService;
	private int fieldCount;// 字段数
	private String exCode;// 交易码
	private String fileRecv;// 接收报文
	private String fileSent;// 发送结果报文
	private String fileAsyn;// 发送受理编号报文
	private String fileTable;// Form报文
	private String fileSubmit;// 0774和0778增删改时进行提示使用
	private String filePrint;// 打印文件信息报文
	// 异步文件下载
	private String fileDownLoad;// 异步文件下载报文
	private String fileDLTable;// 异步文件下载Form报文

	public AbstractQueryBean() {
		try {
			this.gylsService = (GylsService) JNDIUtils.lookup(GylsService.class);
			this.jyjlService = (JyjlService) JNDIUtils.lookup(JyjlService.class);
			this.ybjyService = (YbjyService) JNDIUtils.lookup(YbjyService.class);
			this.authorizeService = (AuthorizeService) JNDIUtils.lookup(AuthorizeService.class);
			this.sqcsService = (SqcsService) JNDIUtils.lookup(SqcsService.class);
			this.jgcsService = (JgcsService) JNDIUtils.lookup(JgcsService.class);
			this.gycsService = (GycsService) JNDIUtils.lookup(GycsService.class);
		} catch (NamingException e) {
			getLogger().error(e.getMessage(), e);
		}
	}

	public abstract Reply service(TPServiceInformation tpServiceInfo) throws Exception;

	public Logger getLogger() {
		return log;
	}

	public void setLogger(Logger log) {
		this.log = log;
	}

	protected SopIntf getSopFromWTC(TPServiceInformation tpServiceInfo) {
		TypedCArray serviceData = (TypedCArray) tpServiceInfo.getServiceData();
		byte[] recvBytes = serviceData.carray;
		getLogger().debug("recvBytes length:" + recvBytes.length);
		SopIntf recvSop = new SopIntf();
		recvSop.clear();
		if (StringUtils.isBlank(this.getFileRecv())) {
			recvSop.convertPktToPoolServer(recvBytes, recvBytes.length);
		} else {
			recvSop.convertPktToPoolServer(recvBytes, recvBytes.length, this.getFileRecv());
		}
		return recvSop;
	}

	protected Reply getExceptionBuffer(TPServiceInformation tpServiceInfo, SopIntf sendSop, String errMsg) {
		setErrorMessageForSop(sendSop, errMsg);
		return getReply(tpServiceInfo, sendSop);
	}

	/**
	 * 返回授权报文
	 * 
	 * @param tpServiceInfo
	 * @param sendSop
	 * @param errMsg
	 * @return
	 */
	protected Reply getAuthorizeBuffer(TPServiceInformation tpServiceInfo, SopIntf sendSop, String message) {
		// 输入公共信息头
		sendSop.put(null, "JIAOYM", this.getExCode());
		long currentT = System.currentTimeMillis();
		sendSop.put(null, "JIOYRQ", DateTimeFormatUtils.formatDate(currentT, "yyyymmdd"));
		sendSop.put(null, "JIOYSJ", Integer.valueOf(DateTimeFormatUtils.formatDate(currentT, "HHmmss")));
		Short FDCWJYXH = 0;
		sendSop.put(null, "CWJYXH", FDCWJYXH);
		sendSop.put(null, SopIntf.TPU_RETCODE, AUTHORIZE_CODE);
		sendSop.put(null, "PTCUWH", ERR_NUM);
		sendSop.put(null, "PTCWDH", AUTHORIZE_CODE);
		// message=""普通新增|+|3|+|"
		sendSop.put(null, "PTCWXX", message);
		return getReply(tpServiceInfo, sendSop);
	}

	protected Reply handleAsynchronized(TPServiceInformation tpServiceInfor, SopIntf sendSop, PybjyEO record) {
		sendSop.put(null, "JIAOYM", this.getExCode());
		long currentT = System.currentTimeMillis();
		sendSop.put(null, "JIOYRQ", DateTimeFormatUtils.formatDate(currentT, "yyyymmdd"));
		sendSop.put(null, "JIOYSJ", Integer.valueOf(DateTimeFormatUtils.formatDate(currentT, "HHmmss")));
		sendSop.put(null, "GUIYLS", record.getGuiyls());
		Short FDCWJYXH = 0;
		sendSop.put(null, "CWJYXH", FDCWJYXH);
		sendSop.put(null, SopIntf.TPU_RETCODE, SopIntf.SOP_SUCC);
		sendSop.put(getFileAsyn(), "SLBHAO", record.getSlbhao());
		String[] inmap = new String[] { getFileAsyn() };
		byte[] sendData = new byte[1024];
		TypedCArray replyBuffer = getTypedCArray(sendData, inmap, sendSop);
		tpServiceInfor.setReplyBuffer(replyBuffer);
		return tpServiceInfor;
	}

	private Reply getReply(TPServiceInformation tpServiceInfo, SopIntf sendSop) {
		byte[] sendData = new byte[1024];
		String[] inmap = null;
		if (StringUtils.isBlank(this.getFileSent())) {
			inmap = new String[] {};
		} else {
			inmap = new String[] { this.getFileSent() };
		}

		TypedCArray replyBuffer = getTypedCArray(sendData, inmap, sendSop);
		tpServiceInfo.setReplyBuffer(replyBuffer);
		return tpServiceInfo;
	}

	/*
	 * * 获取报文buffer对象
	 */
	protected TypedCArray getTypedCArray(byte[] sendData, String[] inmap, SopIntf sendSop) {
		int res = sendSop.convertPoolToPktServer(sendData, inmap);
		TypedCArray replyBuffer = new TypedCArray(res);
		byte[] replyArray = new byte[res];
		// String regKey = "needvalid." + getExCode() + ".mac";
		// String regValue = ConfigLoader.getInstance().getProperty(regKey);
		// if (HdqsUtils.isNotBlank(regValue) && regValue.trim().equals("true"))
		// {
		// int macIndex = 18;
		// byte[] macData = new byte[res - macIndex];
		// System.arraycopy(sendData, 0, replyArray, 0, res);
		// System.arraycopy(replyArray, macIndex, macData, 0, res - macIndex);
		// String mac = MACUtils.createMAC(macData);
		// System.arraycopy(mac.getBytes(), 0, replyArray, 2, macIndex - 2);
		// } else {
		System.arraycopy(sendData, 0, replyArray, 0, res);
		// }
		replyBuffer.carray = replyArray;
		return replyBuffer;
	}

	protected void getAsynTPServiceInfoAsResult(SopIntf sendSop, PybjyEO record) {
		sendSop.put(null, "JIAOYM", exCode);
		long currentT = System.currentTimeMillis();
		sendSop.put(null, "JIOYRQ", DateTimeFormatUtils.formatDate(currentT, "yyyymmdd"));
		sendSop.put(null, "JIOYSJ", Integer.valueOf(DateTimeFormatUtils.formatDate(currentT, "HHmmss")));
		sendSop.put(null, "GUIYLS", record.getGuiyls());
		Short FDCWJYXH = 0;
		sendSop.put(null, "CWJYXH", FDCWJYXH);
		sendSop.put(null, SopIntf.TPU_RETCODE, SopIntf.SOP_SUCC);
		sendSop.put(getFileSent(), "SLBHAO", record.getSlbhao());
	}

	/**
	 * 设置报文错误信息
	 * 
	 * @param sendSop
	 * @param errMsg
	 * @param exchangeCode
	 * @return
	 */
	protected void setErrorMessageForSop(SopIntf sendSop, String errMsg) {
		// 输入公共信息头
		sendSop.put(null, "JIAOYM", this.getExCode());
		long currentT = System.currentTimeMillis();
		sendSop.put(null, "JIOYRQ", DateTimeFormatUtils.formatDate(currentT, "yyyymmdd"));
		sendSop.put(null, "JIOYSJ", Integer.valueOf(DateTimeFormatUtils.formatDate(currentT, "HHmmss")));
		Short FDCWJYXH = 0;
		sendSop.put(null, "CWJYXH", FDCWJYXH);
		sendSop.put(null, SopIntf.TPU_RETCODE, ERR_CODE);
		// 错误信息输入
		// sendSop.put(null, "PTCUWH", ERR_NUM);
		sendSop.put(null, "PTCWDH", ERR_CODE);
		sendSop.put(null, "PTCWXX", errMsg);
	}

	protected void printRecvHeaderPkt(SopIntf recSop) {
		if (!getLogger().isDebugEnabled()) {
			return;
		}

		if (StringUtils.isNotBlank(this.getExCode())) {
			getLogger().debug("~~~~~~~~~~~~~~~~" + this.getExCode() + "~~~~~~~~~~~~~~~~");
		}
		// #输入系统头
		getLogger().debug("数据包长度:" + recSop.getStr(null, "SHJBCD"));
		// getLogger().debug("报文MAC:" + recSop.getStr(null, "BAWMAC"));
		// getLogger().debug("MAC机构号:" + recSop.getStr(null, "MACJGH"));
		// getLogger().debug("PIN种子:" + recSop.getStr(null, "PINZHZ"));
		// getLogger().debug("渠道来源-前置保留:" + recSop.getStr(null, "CHNLNO"));
		// getLogger().debug("渠道去向:" + recSop.getStr(null, "QUDQUX"));
		// getLogger().debug("加密标志-是否是加密交易标志:" + recSop.getStr(null, "MACFLG"));
		// getLogger().debug("PIN标志-是否包中有PIN:" + recSop.getStr(null, "PINFLG"));
		// getLogger().debug("组合标志-是否是组合交易标志:" + recSop.getStr(null, "COMFLG"));
		// getLogger().debug("主机服务:" + recSop.getStr(null, "ZHUJFW"));
		// getLogger().debug("信息结束标志:" + recSop.getStr(null, "XXJSBZ"));
		// getLogger().debug("数据包顺序号:" + recSop.getStr(null, "SJBSXH"));
		// getLogger().debug("检验标志:" + recSop.getStr(null, "JIOYBZ"));
		// getLogger().debug("密钥版本号:" + recSop.getStr(null, "MIYBBH"));
		// getLogger().debug("渠道:" + recSop.getStr(null, "QUDAOO"));
		// getLogger().debug("密码偏移量:" + recSop.getStr(null, "MIMPYL"));
		// getLogger().debug("帐号偏移量:" + recSop.getStr(null, "ZHHPYL"));
		// #输入公共头
		getLogger().debug("终端号:" + recSop.getStr(null, "ZHNGDH"));
		// getLogger().debug("城市代码:" + recSop.getStr(null, "CHSHDM"));
		// getLogger().debug("营业机构号:" + recSop.getStr(null, "YNGYJG"));
		// getLogger().debug("交易柜员:" + recSop.getStr(null, "JIO1GY"));
		// #输入交易头
		getLogger().debug("交易码:" + recSop.getStr(null, "JIAOYM"));
		// getLogger().debug("交易子码:" + recSop.getStr(null, "JIOYZM"));
		// getLogger().debug("交易模式:" + recSop.getStr(null, "JIOYMS"));
		// getLogger().debug("交易序号:" + recSop.getStr(null, "JIOYXH"));
		// getLogger().debug("交易数据包长度:" + recSop.getStr(null, "JYSJCD"));
		// getLogger().debug("偏移量1:" + recSop.getStr(null, "PNYIL1"));
		// getLogger().debug("偏移量2:" + recSop.getStr(null, "PNYIL2"));
		getLogger().debug("前台流水号:" + recSop.getStr(null, "QANTLS"));
		getLogger().debug("前台日期:" + recSop.getStr(null, "QANTRQ"));
		// getLogger().debug("授权柜员:" + recSop.getStr(null, "SHOQGY"));
		// getLogger().debug("授权密码:" + recSop.getStr(null, "SHOQMM"));
		// getLogger().debug("有无卡标识:" + recSop.getStr(null, "YWKABZ"));
		// getLogger().debug("操作员序号:" + recSop.getStr(null, "CZYNXH"));
		getLogger().debug("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
	}

	protected void printRecvFormPkt(AbstractForm form, PybjyEO record) {
		getLogger().info(form.toLog(record));
	}

	protected void printRecvFormPkt(AbstractForm form, PjyjlEO record) {
		getLogger().info(form.toLog(record));
	}

	/**
	 * 设置交易报文系统头
	 * 
	 * @param recSop
	 * @param sendSop
	 */
	protected void setSystemHeadForSop(SopIntf recSop, SopIntf sendSop) {
		sendSop.put(null, "BAWMAC", recSop.getStr(null, "BAWMAC"));
		sendSop.put(null, "MACJGH", recSop.getStr(null, "MACJGH"));
		sendSop.put(null, "PINZHZ", recSop.getStr(null, "PINZHZ"));
		sendSop.put(null, "CHNLNO", recSop.getStr(null, "CHNLNO"));
		sendSop.put(null, "QUDQUX", recSop.getStr(null, "QUDQUX"));
		sendSop.put(null, "MACFLG", recSop.getStr(null, "MACFLG"));
		sendSop.put(null, "PINFLG", recSop.getStr(null, "PINFLG"));
		sendSop.put(null, "COMFLG", recSop.getStr(null, "COMFLG"));
		sendSop.put(null, "ZHUJFW", recSop.getStr(null, "ZHUJFW"));
		sendSop.put(null, "XXJSBZ", recSop.getStr(null, "XXJSBZ"));
		sendSop.put(null, "SJBSXH", recSop.getStr(null, "SJBSXH"));
		sendSop.put(null, "JIOYBZ", recSop.getStr(null, "JIOYBZ"));
		sendSop.put(null, "SHOQGY", recSop.getStr(null, "SHOQGY"));
		String macNum = recSop.getStr(null, "MIYBBH");
		if (macNum == null || macNum.trim().length() == 0) {
			macNum = "0";
		}
		sendSop.put(null, "MIYBBH", recSop.getStr(null, "MIYBBH"));
		sendSop.put(null, "QUDAOO", recSop.getStr(null, "QUDAOO"));
		sendSop.put(null, "MIMPYL", recSop.getStr(null, "MIMPYL"));
		sendSop.put(null, "ZHHPYL", recSop.getStr(null, "ZHHPYL"));
		sendSop.put(null, SopIntf.TPU_RETCODE, SopIntf.SOP_SUCC);
	}

	/**
	 * 从基本数字类型获取字节流
	 * 
	 * @param val
	 * @return
	 */
	protected byte[] getBytesFromzNumberType(Object val) {
		ByteArrayOutputStream bArrayOutputStream = new ByteArrayOutputStream();
		DataOutputStream dataOutputStream = new DataOutputStream(bArrayOutputStream);
		try {
			if (val instanceof Integer)
				dataOutputStream.writeInt((Integer) val);
			else if (val instanceof Short) {
				dataOutputStream.writeShort((Short) val);
			}
		} catch (IOException e) {
			getLogger().error(e.getMessage(), e);
		}
		byte[] bytes = bArrayOutputStream.toByteArray();
		return bytes;
	}

	public PybjyEO buildAsyQueryRecord(SopIntf recvSop) {
		PybjyEO record = new PybjyEO();
		record.setJiaoym(this.getExCode());
		record.setYngyjg(recvSop.getStr(null, "YNGYJG").trim());
		record.setJio1gy(recvSop.getStr(null, "JIO1GY").trim());
		String jioybz = recvSop.getStr(null, "JIOYBZ").trim();
		if (HdqsUtils.isNotBlank(jioybz) && jioybz.equals(NEED_AUTHORIZE_SIGN)) {
			record.setJioybz(Boolean.TRUE);
		} else {
			record.setJioybz(Boolean.FALSE);
		}
		record.setShoqgy(recvSop.getStr(null, "SHOQGY").trim());
		record.setQudaoo(recvSop.getStr(null, "QUDAOO").trim());
		//
		String chaxlx = recvSop.getStr(this.getFileRecv(), "CHAXLX").trim();
		if (!StringUtils.isBlank(chaxlx)) {
			record.setChaxlx(chaxlx);
		}
		//
		record.setChaxzl(recvSop.getStr(this.getFileRecv(), "CHAXZL").trim());
		if (record.getJiaoym().equals(QueryConstants.CORPORATE_BATCH_QUERY_CODE) && record.getChaxzl().equals("2"))
			record.setKhzhlx(HdqsConstants.KHZHLX_DUIGONGYIHAOTONG);
		else
			record.setKhzhlx(recvSop.getStr(this.getFileRecv(), "KHZHLX").trim());
		record.setZhangh(recvSop.getStr(this.getFileRecv(), "ZHANGH").trim());
		record.setShunxh(recvSop.getStr(this.getFileRecv(), "SHUNXH").trim());
		record.setKehuzh(recvSop.getStr(this.getFileRecv(), "KEHUZH").trim());
		//
		record.setKehuhao(recvSop.getStr(this.getFileRecv(), "KEHUHAO").trim()); // 客户号
		record.setZhjnzl(recvSop.getStr(this.getFileRecv(), "ZHJNZL").trim());
		record.setZhjhao(recvSop.getStr(this.getFileRecv(), "ZHJHAO").trim());
		//
		record.setKhzwm(recvSop.getStr(this.getFileRecv(), "KHZWM").trim());
		//
		record.setStartDate(recvSop.getStr(this.getFileRecv(), "QISHRQ").trim());
		record.setEndDate(recvSop.getStr(this.getFileRecv(), "ZZHIRQ").trim());
		//
		String fileQuery = recvSop.getStr(this.getFileRecv(), "WJANBZ").trim();
		if (HdqsUtils.isNotBlank(fileQuery) && HdqsUtils.isNumeric(fileQuery)) {
			int queryI = Integer.parseInt(fileQuery);
			if (queryI == 0) {
				record.setFileQuery(Boolean.FALSE);
			} else {
				record.setFileQuery(Boolean.TRUE);
			}
		}
		record.setRecvFile(recvSop.getStr(this.getFileRecv(), "WJANMC").trim());
		String startNum = recvSop.getStr(this.getFileRecv(), "QISHBS").trim();
		if (HdqsUtils.isNotBlank(startNum)) {
			record.setStartNum(Integer.parseInt(startNum));
		}
		String queryNum = recvSop.getStr(this.getFileRecv(), "CXUNBS").trim();
		if (HdqsUtils.isNotBlank(queryNum)) {
			record.setQueryNum(Integer.parseInt(queryNum));
		}
		String shfobz = recvSop.getStr(this.getFileRecv(), "SHFOBZ").trim();
		if (!StringUtils.isBlank(shfobz)) {
			record.setShfobz(shfobz);
		}
		String pstyle = recvSop.getStr(this.getFileRecv(), "PSTYLE").trim();
		if (!StringUtils.isBlank(pstyle)) {
			record.setPrintStyle(pstyle);
		}

		record.setHuobdh(recvSop.getStr(this.getFileRecv(), "HUOBDH").trim());
		record.setChuibz(recvSop.getStr(this.getFileRecv(), "CHUIBZ").trim());
		record.setZhhuxz(recvSop.getStr(this.getFileRecv(), "ZHHUXZ").trim());
		record.setZhaoxz(recvSop.getStr(this.getFileRecv(), "ZHAOXZ").trim());

		return record;
	}

	protected PjyjlEO buildExchangeRecord(PybjyEO entity) {
		PjyjlEO record = new PjyjlEO();
		record.setJiaoym(entity.getJiaoym());
		record.setYngyjg(entity.getYngyjg());
		record.setJio1gy(entity.getJio1gy());
		record.setJioybz(entity.getJioybz());
		record.setShoqgy(entity.getShoqgy());
		record.setQudaoo(entity.getQudaoo());
		GylsEO gylsEO = gylsService.genGuiyuanSerialNumber(entity.getJio1gy());
		String guiyls = gylsService.buildGuiyls(gylsEO);
		String slbhao = gylsService.buildSlbhao(gylsEO);
		record.setGuiyls(guiyls);
		record.setSlbhao(slbhao);
		entity.setGuiyls(guiyls);
		entity.setSlbhao(slbhao);
		return record;
	}

	protected PjyjlEO buildExchangeRecord(SopIntf recvSop, AbstractForm form) {
		PjyjlEO record = new PjyjlEO();
		record.setJiaoym(this.getExCode());
		record.setYngyjg(recvSop.getStr(null, "YNGYJG").trim());
		record.setJio1gy(recvSop.getStr(null, "JIO1GY").trim());
		String jioybz = recvSop.getStr(null, "JIOYBZ").trim();
		if (HdqsUtils.isNotBlank(jioybz) && jioybz.equals(NEED_AUTHORIZE_SIGN)) {
			record.setJioybz(Boolean.TRUE);
		} else {
			record.setJioybz(Boolean.FALSE);
		}
		record.setShoqgy(recvSop.getStr(null, "SHOQGY").trim());
		record.setQudaoo(recvSop.getStr(null, "QUDAOO").trim());
		GylsEO gylsEO = gylsService.genGuiyuanSerialNumber(record.getJio1gy());
		String guiyls = getGylsService().buildGuiyls(gylsEO);
		String slbhao = getGylsService().buildSlbhao(gylsEO);
		record.setGuiyls(guiyls);
		record.setSlbhao(slbhao);
		record.setQueryStr(form.toString());
		return record;
	}

	protected PjyjlEO saveExchangeRecord(PybjyEO record, AbstractForm form) {
		PjyjlEO exRecord = buildExchangeRecord(record);
		exRecord.setQueryStr(form.toString());
		return getJyjlService().save(exRecord);
	}

	protected PjyjlEO saveExchangeRecord(PjyjlEO record) {
		return this.getJyjlService().save(record);
	}

	protected void updateExchangeRunErrStatus(PjyjlEO record) {
		long currentT = System.currentTimeMillis();
		record.setRunSucc(Boolean.FALSE);
		record.setEndTime(currentT);

		this.getJyjlService().update(record);
	}

	protected void updateExchangeEndTime(PjyjlEO record) {
		long currentT = System.currentTimeMillis();
		record.setEndTime(currentT);

		this.getJyjlService().update(record);
	}

	protected File getFilePath(String fileName) {
		String uploadPath = ConfigLoader.getInstance().getProperty(RegisterTable.FTP_UPLOAD_PATH);
		File recvFile = new File(uploadPath, fileName);
		getLogger().info("上传文件:" + recvFile.getAbsolutePath());
		return moveUploadFile(recvFile);
	}

	protected File moveUploadFile(File recvFile) {
		try {
			String bakPath = ConfigLoader.getInstance().getProperty(RegisterTable.FTP_BACKUP_PATH);
			File destDir = new File(bakPath, this.getExCode());
			if (!destDir.exists()) {
				destDir.mkdirs();
			}
			File destFile = new File(destDir, recvFile.getName());
			if (destFile.exists()) {
				return destFile;
			}
			FileUtils.moveFileToDirectory(recvFile, destDir, true);
			getLogger().info("备份文件:" + destFile.getAbsolutePath());
			return destFile;
		} catch (IOException e) {
			getLogger().error("An error occured when file moved", e);
			return recvFile;
		}
	}

	/**
	 * list集合转换数组
	 * 
	 * @param list
	 * @return
	 */
	protected String[] getStrArrayFromList(List<String> list) {
		String[] strArray = list.toArray(new String[0]);
		return strArray;
	}

	/**
	 * 生成提示信息报文
	 * 
	 * @param tpServiceInfor
	 * @param sendData
	 * @param record
	 * @param sendSop
	 * @param currentT
	 * @param message
	 * @return
	 */
	protected Reply getTpServiceInfoAsResult(TPServiceInformation tpServiceInfor, PjyjlEO record, SopIntf sendSop, String message) {
		long currentT = System.currentTimeMillis();
		sendSop.put(null, "JIOYRQ", DateTimeFormatUtils.formatDate(currentT, "yyyymmdd"));
		sendSop.put(null, "JIOYSJ", Integer.valueOf(DateTimeFormatUtils.formatDate(currentT, "HHmmss")));
		sendSop.put(null, "GUIYLS", record.getGuiyls());
		Short FDCWJYXH = 0;
		sendSop.put(null, "CWJYXH", FDCWJYXH);
		sendSop.put(null, SopIntf.TPU_RETCODE, SopIntf.SOP_SUCC);
		sendSop.put(getFileSubmit(), "MESSAGE", message);
		String[] inmap = new String[] { getFileSubmit() };
		byte[] sendData = new byte[1024];
		tpServiceInfor.setReplyBuffer(getTypedCArray(sendData, inmap, sendSop));
		return tpServiceInfor;
	}

	/**
	 * 格式化金额
	 * 
	 * @param str
	 * @return
	 */
	protected static String formatCurrency(String result) {
		if (result.startsWith(".")) {
			result = "0" + result;
		} else if (result.startsWith("-.")) {
			result = "-0." + result.substring(2);
		}
		BigDecimal tmpResult = new BigDecimal(result);
		tmpResult = tmpResult.setScale(2, BigDecimal.ROUND_HALF_UP);
		return tmpResult.toString();
	}

	/**
	 * 设置打印路径
	 * 
	 * @param sendSop
	 */
	protected void setPrintFilePath(SopIntf sendSop, String filePath) {
		sendSop.put(getFilePrint(), "PTPATH", filePath);
	}

	/**
	 * 设置文件下载路径
	 * 
	 * @param sendSop
	 */
	protected void setFileDownLoadDir(SopIntf sendSop) {
		sendSop.put(getFilePrint(), "DNDIR", ConfigLoader.getInstance().getProperty(RegisterTable.GEN_FILE_PATH));
	}

	/**
	 * 总行查询时集群查询时不需要进行授权校验,只适用于对私交易。用于传递给异步处理
	 * 
	 * @param record
	 */
	protected void isZonghangAuthorize(PybjyEO record) {
		record.setNeedAuth(Boolean.TRUE);
		if (getExCode().equals("0771") || getExCode().equals("0772") || getExCode().equals("0778")) {
			if (jgcsService.isZonghangQuery(record.getYngyjg())) {
				record.setNeedAuth(Boolean.FALSE);
			}
		}
	}

	/**
	 * 前台第一次进行交易时，如果JIOYBZ=1,则返回时要提供授权，如果授权柜员字段非空，则说明前台已经授权，则返回时不需要再考虑授权
	 * 
	 * @param record
	 * @return
	 */
	protected boolean isFrontAuthorize(PybjyEO record) {
		boolean result = record.getJioybz();
		if (result && StringUtils.isNotBlank(record.getShoqgy())) {
			result = false;
		}
		return result;
	}

	/**
	 * 前台第一次进行交易时，如果JIOYBZ=1,则返回时要提供授权，如果授权柜员字段非空，则说明前台已经授权，则返回时不需要再考虑授权
	 * 
	 * @param form
	 * @return
	 */
	protected boolean isFrontAuthorize(PjyjlEO record) {
		boolean result = record.getJioybz();
		if (result && StringUtils.isNotBlank(record.getShoqgy())) {
			result = false;
		}
		return result;
	}

	protected Authorize isNeedCacheAuthorize(final Authorize cachedAuth, PybjyEO record) {
		Authorize authorize = new Authorize();
		if (StringUtils.isNotBlank(record.getShoqgy())) {
			return authorize;
		}
		if (getExCode().equals("0771") || getExCode().equals("0772") || getExCode().equals("0778")) {
			if (jgcsService.isZonghangQuery(record.getYngyjg())) {
				return authorize;
			}
		}
		authorize.setNeedAuth(cachedAuth.isNeedAuth());
		authorize.setGuiyjb(cachedAuth.getGuiyjb());
		authorize.setBeizxx(cachedAuth.getBeizxx());
		return authorize;
	}

	@Deprecated
	protected Authorize isNeedAuthorize(PybjyEO record, AuthorizeLevel level) {
		Authorize authorize = new Authorize();
		if (StringUtils.isNotBlank(record.getShoqgy())) {
			return authorize;
		}
		if (getExCode().equals("0771") || getExCode().equals("0772") || getExCode().equals("0778")) {
			if (jgcsService.isZonghangQuery(record.getYngyjg())) {
				return authorize;
			}
		}

		if (level.getKey().intValue() > 0) {
			authorize.setNeedAuth(true);
			authorize.setGuiyjb(level.getKey());
			authorize.setBeizxx(level.getValue());
		}
		return authorize;
	}

	protected Authorize isNeedAuthorize(PybjyEO record, Authorize level) {
		Authorize authorize = new Authorize();
		if (StringUtils.isNotBlank(record.getShoqgy())) {
			return authorize;
		}
		if (getExCode().equals("0771") || getExCode().equals("0772") || getExCode().equals("0778")) {
			if (jgcsService.isZonghangQuery(record.getYngyjg())) {
				return authorize;
			}
		}
		return level;
	}

	/**
	 * 当抛出AsynQueryException异常时，需要判断后台是否需要授权
	 * 
	 * @param record
	 */
	protected Authorize isAsyAuthorize(PybjyEO record) {
		Authorize authorize = new Authorize();
		if (StringUtils.isNotBlank(record.getShoqgy())) {
			return authorize;
		}
		if (record.getGuiyjb() > 0) {
			authorize.setNeedAuth(true);
			authorize.setGuiyjb(record.getGuiyjb());
			authorize.setBeizxx(record.getBeizxx());
			return authorize;
		}
		return authorize;
	}

	/**
	 * 进行异步交易时，需要判断是否进行上传文件查询
	 * 
	 * @param record
	 * @return
	 */
	protected Authorize isNeedAsynAuth(PybjyEO record) {
		Authorize authorize = new Authorize();
		if (StringUtils.isNotBlank(record.getShoqgy())) {
			return authorize;
		}
		if (record.getFileQuery()) {
			authorize.setNeedAuth(true);
			authorize.setGuiyjb(3);
			authorize.setBeizxx("上传文件查询");
			return authorize;
		}
		return authorize;
	}

	/**
	 * 获取同步文件操作的文件路径
	 * 
	 * @param record
	 * @param item
	 * @return
	 */
	protected String getFilePathForSyn(PybjyEO record, AbstractRowkeyItem item) {
		String filePath = record.getShfobz().equals(HdqsConstants.SHFOBZ_PRINT) ? item.getCommFilePath() : item.getPdfFilePath();

		filePath = asyGenerateFile(record, filePath);
		if (filePath != null) {
			if (record.getShfobz().equals(HdqsConstants.SHFOBZ_PRINT)) {
				item.setCommFilePath(filePath);
			} else {
				item.setPdfFilePath(filePath);
			}
			getLogger().info("路径: " + filePath);
			filePath = getRelativePath(filePath);
			getLogger().info("返回前端相对路径: " + filePath);
		}

		return filePath;
	}

	protected String asyGenerateFile(PybjyEO record, String filePath) {
		throw new UnsupportedOperationException("需要在子类中实现");
	}

	protected String getRelativePath(String filePath) {
		String replaceStr = ConfigLoader.getInstance().getProperty(RegisterTable.GEN_FILE_PATH);
		replaceStr = HdqsUtils.formatFilePath(replaceStr);
		if (!replaceStr.endsWith(HdqsUtils.FOLDER_SEPARATOR)) {
			replaceStr += HdqsUtils.FOLDER_SEPARATOR;
		}
		return filePath.substring(replaceStr.length());
	}

	public GylsService getGylsService() {
		return gylsService;
	}

	public JyjlService getJyjlService() {
		return jyjlService;
	}

	public YbjyService getYbjyService() {
		return ybjyService;
	}

	public AuthorizeService getAuthorizeService() {
		return authorizeService;
	}

	public SqcsService getSqcsService() {
		return sqcsService;
	}

	public JgcsService getJgcsService() {
		return jgcsService;
	}

	public GycsService getGycsService() {
		return gycsService;
	}

	public int getFieldCount() {
		return fieldCount;
	}

	public void setFieldCount(int fieldCount) {
		this.fieldCount = fieldCount;
	}

	public String getExCode() {
		return exCode;
	}

	public void setExCode(String exCode) {
		this.exCode = exCode;
	}

	public String getFileRecv() {
		return fileRecv;
	}

	public void setFileRecv(String fileRecv) {
		this.fileRecv = fileRecv;
	}

	public String getFileSent() {
		return fileSent;
	}

	public void setFileSent(String fileSent) {
		this.fileSent = fileSent;
	}

	public String getFileAsyn() {
		return fileAsyn;
	}

	public void setFileAsyn(String fileAsyn) {
		this.fileAsyn = fileAsyn;
	}

	public String getFileTable() {
		return fileTable;
	}

	public void setFileTable(String fileTable) {
		this.fileTable = fileTable;
	}

	public String getFileSubmit() {
		return fileSubmit;
	}

	public void setFileSubmit(String fileSubmit) {
		this.fileSubmit = fileSubmit;
	}

	public String getFilePrint() {
		return filePrint;
	}

	public void setFilePrint(String filePrint) {
		this.filePrint = filePrint;
	}

	public String getFileDownLoad() {
		return fileDownLoad;
	}

	public void setFileDownLoad(String fileDownLoad) {
		this.fileDownLoad = fileDownLoad;
	}

	public String getFileDLTable() {
		return fileDLTable;
	}

	public void setFileDLTable(String fileDLTable) {
		this.fileDLTable = fileDLTable;
	}

	public void splitRecord(List<PybjyEO> list, PybjyEO record) {
		if (record.getChaxzl().equals(HdqsConstants.CHAXZL_ZHJNZL)) {
			BkhzjParser arser = new BkhzjParser();
			Set<String> keHuHaoSet = arser.getkehuhaoResults(record);
			if (!keHuHaoSet.isEmpty()) {
				Iterator<String> iterator = keHuHaoSet.iterator();
				while (iterator.hasNext()) {
					String keHuHao = iterator.next();
					PybjyEO temRecord = new PybjyEO();
					try {
						BeanUtils.copyProperties(temRecord, record);
					} catch (IllegalAccessException e) {
						log.info(e.getMessage());
					} catch (InvocationTargetException e) {
						log.info(e.getMessage());
					}
					temRecord.setId(null);
					temRecord.setKehuhao(keHuHao);

					list.add(temRecord);
				}
			} else {
				list.add(record);
			}

		} else {
			list.add(record);
		}
	}

}