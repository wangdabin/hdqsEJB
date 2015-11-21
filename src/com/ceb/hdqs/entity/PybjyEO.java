package com.ceb.hdqs.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.ceb.bank.constants.FieldConstants;
import com.ceb.hdqs.constants.HdqsConstants;

@Entity
@Table(name = "PYBJY")
public class PybjyEO extends AbstractEO {
	private static final long serialVersionUID = 6516222739737006006L;

	private Boolean isAsyn = Boolean.FALSE;// HBase查询使用
	private String jioyrq;// 交易日期,日期(yyyyMMdd),从换日表取值
	private String jiaoym;// 交易码
	private String yngyjg;// 营业机构号
	private String jio1gy;// 柜员号
	private String guiyls;// 柜员流水(柜员号+流水号)
	private String slbhao;// 受理编码 (交易日期+柜员号+流水号)
	private String runStatus = HdqsConstants.RUNNING_STATUS_WAITING;// 查询运行状态,-1:失败,0:等待,1:正在运行,2:成功,3:不连续
	private String notifyStatus = HdqsConstants.NOTIFY_STATUS_WAITING;// 通知标识,0:等待,1:成功
	private String chaxlx = "0";// 查询类型，判断非客户查询使用,0-客户查询,1-内部查询,2-监管查询
	private String chaxzl;// 查询条件种类 1-账号,2-对公一号通或客户账号,3-客户号,4-身份证号
	private String zhangh;// 账号accountHao
	private String kehuzh;// 客户帐号(或对公一号通)account
	private String shunxh;// 客户账号顺序号
	private String kehuhao;// 客户号customerHao
	private String zhjnzl;// 证件种类1身份证, 2护照, 3军人证, 4武警证, 5港澳居民来往内地通行证,// 6户口簿,
	// 9台湾居民来往大陆通行证, a外国人永久居留证
	private String zhjhao;// 证件号码(18)
	private String khzwm;// 中文名
	private String startDate;// 起始日期
	private String endDate;// 终止日期
	private Boolean fileQuery = Boolean.FALSE;// 是否文件查询
	private String recvFile;// 受理文件名
	private String pdfFile;
	private String xlsFile;
	private int commNum;// 本条件生成文件页数
	// private int lastNum;
	// private String handleInfo;
	private Long itemCount = 0L;// 查询明细条数
	private String khzhlx;// 客户帐号类型1-卡 2-活期一本通3-定期一本通 4-定期存折5-存单6-国债
	private String zhhuxz;// 帐户性质0001-普通活期 0011-小额圈存 0017-购汇保证金专户
	private String zhaoxz;// 账号性质 0活期,2定期,3全部
	private String huobdh;// 查询币种,货币代号(货币种类)01-人民币 12-英镑 13-港币 14-美元 00-全部
	private String chuibz;// 钞汇标志0-钞户1-汇户2-钞户和汇户
	private Integer startNum = 1;// 起始笔数
	private Integer queryNum = 20;// 查询笔数
	private String shfobz = HdqsConstants.SHFOBZ_DOWNLOAD;// 是否打印,0-两者都不；1-文件打印；2-文件导出
	private String printStyle = FieldConstants.PRINT_STYLE_KEHU;// 打印格式,只有0777、0778需要非客户对账单，其它全部都是客户对账单
	// 0-客户对账单pdf,1-非客户对账单
	private String masterSyn = HdqsConstants.SYN_STATUS_INIT;// 配置文件中database.cluster.order中第一个集群同步状态,0:初始状态,1:正在运行,2:成功
	private String standbySyn = HdqsConstants.SYN_STATUS_INIT;// 配置文件中database.cluster.order中第二个集群同步状态,0:初始状态,1:正在运行,2:成功
	private Boolean needAuth = Boolean.TRUE;
	private Boolean jioybz = Boolean.TRUE;// 检验标识,0前端不需要授权,1前端需要授权
	private String shoqgy;// 授权柜员
	private String qudaoo;// 渠道,系统标识码
	private int guiyjb = 0;// 授权柜员级别
	private String beizxx;// 授权原因
	private String errMsg;
	private Long startTime;// 交易查询的开始时间
	private Long endTime;// 交易查询的结束时间
	private Boolean authorize;

	public PybjyEO() {
	}

	@Id
	@Column(name = "ID", nullable = false, precision = 16, scale = 0)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PYBJY_SEQ")
	@SequenceGenerator(name = "PYBJY_SEQ", sequenceName = "SEQ_PYBJY", initialValue = 0, allocationSize = 100)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "ISASYN", columnDefinition = "number(1)")
	public Boolean getIsAsyn() {
		return isAsyn;
	}

	public void setIsAsyn(Boolean isAsyn) {
		this.isAsyn = isAsyn;
	}

	@Column(name = "JIOYRQ", length = 8)
	public String getJioyrq() {
		return jioyrq;
	}

	public void setJioyrq(String jioyrq) {
		this.jioyrq = jioyrq;
	}

	@Column(name = "JIAOYM", length = 6)
	public String getJiaoym() {
		return jiaoym;
	}

	public void setJiaoym(String jiaoym) {
		this.jiaoym = jiaoym;
	}

	@Column(name = "YNGYJG", length = 4)
	public String getYngyjg() {
		return yngyjg;
	}

	public void setYngyjg(String yngyjg) {
		this.yngyjg = yngyjg;
	}

	@Column(name = "JIO1GY", length = 6)
	public String getJio1gy() {
		return jio1gy;
	}

	public void setJio1gy(String jio1gy) {
		this.jio1gy = jio1gy;
	}

	/**
	 * 柜员流水号 (柜员号+流水号)
	 * 
	 * @return
	 */
	@Column(name = "GUIYLS", length = 12)
	public String getGuiyls() {
		return guiyls;
	}

	/**
	 * 柜员流水号 (柜员号+流水号)
	 * 
	 * @param guiyls
	 */
	public void setGuiyls(String guiyls) {
		this.guiyls = guiyls;
	}

	/**
	 * 受理编码 (交易日期+柜员号+流水号)
	 * 
	 * @return
	 */
	@Column(name = "SLBHAO", length = 20)
	public String getSlbhao() {
		return slbhao;
	}

	/**
	 * 受理编码 (交易日期+柜员号+流水号)
	 * 
	 * @param slbhao
	 */
	public void setSlbhao(String slbhao) {
		this.slbhao = slbhao;
	}

	/**
	 * 运行状态,-1:失败,0:等待,1:正在运行,2:成功,3:不连续
	 * 
	 * @return
	 */
	@Column(name = "RUNSTATUS", length = 2)
	public String getRunStatus() {
		return runStatus;
	}

	/**
	 * 运行状态,-1:失败,0:等待,1:正在运行,2:成功,3:不连续
	 * 
	 * @param runStatus
	 */
	public void setRunStatus(String runStatus) {
		this.runStatus = runStatus;
	}

	/**
	 * 通知标识,0:等待,1:成功
	 * 
	 * @return
	 */
	@Column(name = "NOTIFYSTATUS", length = 1)
	public String getNotifyStatus() {
		return notifyStatus;
	}

	/**
	 * 通知标识,0:等待,1:成功
	 * 
	 * @param notifyStatus
	 */
	public void setNotifyStatus(String notifyStatus) {
		this.notifyStatus = notifyStatus;
	}

	/**
	 * 查询类型，判断非客户查询使用,0-客户查询,1-内部查询,2-监管查询
	 * 
	 * @return
	 */
	@Column(name = "QUERYTYPE", length = 1)
	public String getChaxlx() {
		return chaxlx;
	}

	/**
	 * 查询类型，判断非客户查询使用,0-客户查询,1-内部查询,2-监管查询
	 * 
	 * @param chaxlx
	 */
	public void setChaxlx(String chaxlx) {
		this.chaxlx = chaxlx;
	}

	/**
	 * 查询条件种类 1-账号,2-对公一号通或客户账号,3-客户号,4-身份证号
	 * 
	 * @return
	 */
	@Column(name = "QUERYKIND", length = 1)
	public String getChaxzl() {
		return chaxzl;
	}

	/**
	 * 查询条件种类 1-账号,2-对公一号通或客户账号,3-客户号,4-身份证号
	 * 
	 * @param chaxzl
	 */
	public void setChaxzl(String chaxzl) {
		this.chaxzl = chaxzl;
	}

	@Column(name = "ACCOUNTHAO", length = 30)
	public String getZhangh() {
		return zhangh;
	}

	public void setZhangh(String zhangh) {
		this.zhangh = zhangh;
	}

	@Column(name = "ACCOUNT", length = 30)
	public String getKehuzh() {
		return kehuzh;
	}

	public void setKehuzh(String kehuzh) {
		this.kehuzh = kehuzh;
	}

	@Column(name = "ACCOUNTSEQ", length = 8)
	public String getShunxh() {
		return shunxh;
	}

	public void setShunxh(String shunxh) {
		this.shunxh = shunxh;
	}

	@Column(name = "CUSTOMERHAO", length = 30)
	public String getKehuhao() {
		return kehuhao;
	}

	public void setKehuhao(String kehuhao) {
		this.kehuhao = kehuhao;
	}

	/**
	 * 证件种类1身份证, 2护照, 3军人证, 4武警证, 5港澳居民来往内地通行证,6户口簿, 9台湾居民来往大陆通行证, a外国人永久居留证
	 * 
	 * @return
	 */
	@Column(name = "CERTIKIND", length = 1)
	public String getZhjnzl() {
		return zhjnzl;
	}

	/**
	 * 证件种类1身份证, 2护照, 3军人证, 4武警证, 5港澳居民来往内地通行证,6户口簿, 9台湾居民来往大陆通行证, a外国人永久居留证
	 * 
	 * @param zhjnzl
	 */
	public void setZhjnzl(String zhjnzl) {
		this.zhjnzl = zhjnzl;
	}

	@Column(name = "CERTINUM", length = 20)
	public String getZhjhao() {
		return zhjhao;
	}

	public void setZhjhao(String zhjhao) {
		this.zhjhao = zhjhao;
	}

	@Column(name = "CHINANAME", length = 80)
	public String getKhzwm() {
		return khzwm;
	}

	public void setKhzwm(String khzwm) {
		this.khzwm = khzwm;
	}

	/**
	 * 界面查询起始日期
	 * 
	 * @return
	 */
	@Column(name = "STARTDATE", length = 8)
	public String getStartDate() {
		return startDate;
	}

	/**
	 * 界面查询起始日期
	 * 
	 * @param startDate
	 */
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	/**
	 * 界面查询终止日期
	 * 
	 * @return
	 */
	@Column(name = "ENDDATE", length = 8)
	public String getEndDate() {
		return endDate;
	}

	/**
	 * 界面查询终止日期
	 * 
	 * @param endDate
	 */
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	/**
	 * 是否文件查询
	 * 
	 * @return
	 */
	@Column(name = "FILEQUERY", columnDefinition = "number(1)")
	public Boolean getFileQuery() {
		return fileQuery;
	}

	/**
	 * 是否文件查询
	 * 
	 * @param fileQuery
	 */
	public void setFileQuery(Boolean fileQuery) {
		this.fileQuery = fileQuery;
	}

	/**
	 * 受理文件名
	 * 
	 * @return
	 */
	@Column(name = "RECVFILE", length = 256)
	public String getRecvFile() {
		return recvFile;
	}

	/**
	 * 受理文件名
	 * 
	 * @param recvFile
	 */
	public void setRecvFile(String recvFile) {
		this.recvFile = recvFile;
	}

	@Column(name = "PDFFILE", length = 256)
	public String getPdfFile() {
		return pdfFile;
	}

	public void setPdfFile(String pdfFile) {
		this.pdfFile = pdfFile;
	}

	@Column(name = "XLSFILE", length = 256)
	public String getXlsFile() {
		return xlsFile;
	}

	public void setXlsFile(String xlsFile) {
		this.xlsFile = xlsFile;
	}

	/**
	 * 本条件生成文件页数
	 * 
	 * @return
	 */
	@Column(name = "COMMNUM", precision = 9, scale = 0)
	public int getCommNum() {
		return commNum;
	}

	/**
	 * 本条件生成文件页数
	 * 
	 * @param commNum
	 */
	public void setCommNum(int commNum) {
		this.commNum = commNum;
	}

	// /**
	// * 目前已废弃该字段
	// *
	// * @return
	// */
	// @Column(name = "LASTNUM", precision = 9, scale = 0)
	// public int getLastNum() {
	// return lastNum;
	// }
	//
	// /**
	// * 目前已废弃该字段
	// *
	// * @param lastNum
	// */
	// public void setLastNum(int lastNum) {
	// this.lastNum = lastNum;
	// }
	//
	// /**
	// * 目前已废弃该字段
	// *
	// * @return
	// */
	// @Column(name = "HANDLEINFO", length = 256)
	// public String getHandleInfo() {
	// return handleInfo;
	// }
	//
	// /**
	// * 目前已废弃该字段
	// *
	// * @param handleInfo
	// */
	// public void setHandleInfo(String handleInfo) {
	// this.handleInfo = handleInfo;
	// }

	/**
	 * 明细条数
	 * 
	 * @return
	 */
	@Column(name = "ITEMCOUNT", precision = 16, scale = 0)
	public Long getItemCount() {
		return itemCount;
	}

	/**
	 * 明细条数
	 * 
	 * @param itemCount
	 */
	public void setItemCount(Long itemCount) {
		this.itemCount = itemCount;
	}

	/**
	 * 客户帐号类型1-卡 2-活期一本通3-定期一本通 4-定期存折5-存单6-国债
	 * 
	 * @return
	 */
	@Column(name = "KHZHLX", length = 1)
	public String getKhzhlx() {
		return khzhlx;
	}

	/**
	 * 客户帐号类型1-卡 2-活期一本通3-定期一本通 4-定期存折5-存单6-国债
	 * 
	 * @param khzhlx
	 */
	public void setKhzhlx(String khzhlx) {
		this.khzhlx = khzhlx;
	}

	/**
	 * 帐户性质0001-普通活期 0011-小额圈存 0017-购汇保证金专户
	 * 
	 * @return
	 */
	@Column(name = "ZHHUXZ", length = 4)
	public String getZhhuxz() {
		return zhhuxz;
	}

	/**
	 * 帐户性质0001-普通活期 0011-小额圈存 0017-购汇保证金专户
	 * 
	 * @param zhhuxz
	 */
	public void setZhhuxz(String zhhuxz) {
		this.zhhuxz = zhhuxz;
	}

	/**
	 * 账号性质 0活期,2定期,3全部
	 * 
	 * @return
	 */
	@Column(name = "ZHAOXZ", length = 1)
	public String getZhaoxz() {
		return zhaoxz;
	}

	/**
	 * 账号性质 0活期,2定期,3全部
	 * 
	 * @param zhaoxz
	 */
	public void setZhaoxz(String zhaoxz) {
		this.zhaoxz = zhaoxz;
	}

	/**
	 * 查询币种,货币代号(货币种类)01-人民币 12-英镑 13-港币 14-美元 00-全部
	 * 
	 * @return
	 */
	@Column(name = "HUOBDH", length = 2)
	public String getHuobdh() {
		return huobdh;
	}

	/**
	 * 查询币种,货币代号(货币种类)01-人民币 12-英镑 13-港币 14-美元 00-全部
	 * 
	 * @param huobdh
	 */
	public void setHuobdh(String huobdh) {
		this.huobdh = huobdh;
	}

	/**
	 * 钞汇标志0-钞户1-汇户2-钞户和汇户
	 * 
	 * @return
	 */
	@Column(name = "CHUIBZ", length = 1)
	public String getChuibz() {
		return chuibz;
	}

	/**
	 * 钞汇标志0-钞户1-汇户2-钞户和汇户
	 * 
	 * @param chuibz
	 */
	public void setChuibz(String chuibz) {
		this.chuibz = chuibz;
	}

	/**
	 * 起始笔数
	 * 
	 * @return
	 */
	@Transient
	public Integer getStartNum() {
		return startNum;
	}

	/**
	 * 起始笔数
	 * 
	 * @param startNum
	 */
	public void setStartNum(Integer startNum) {
		this.startNum = startNum;
	}

	/**
	 * 查询笔数
	 * 
	 * @return
	 */
	@Transient
	public Integer getQueryNum() {
		return queryNum;
	}

	/**
	 * 查询笔数
	 * 
	 * @param queryNum
	 */
	public void setQueryNum(Integer queryNum) {
		this.queryNum = queryNum;
	}

	/**
	 * 是否打印,0-两者都不；1-文件打印；2-文件导出
	 * 
	 * @return
	 */
	@Column(name = "SHFOBZ", length = 1)
	public String getShfobz() {
		return shfobz;
	}

	/**
	 * 是否打印,0-两者都不；1-文件打印；2-文件导出
	 * 
	 * @param shfobz
	 */
	public void setShfobz(String shfobz) {
		this.shfobz = shfobz;
	}

	/**
	 * 打印格式,只有0777、0778需要非客户对账单，其它全部都是客户对账单 0-客户对账单pdf,1-非客户对账单
	 * 
	 * @return
	 */
	@Column(name = "PRINTSTYLE", length = 1)
	public String getPrintStyle() {
		return printStyle;
	}

	/**
	 * 打印格式,只有0777、0778需要非客户对账单，其它全部都是客户对账单 0-客户对账单pdf,1-非客户对账单
	 * 
	 * @param printStyle
	 */
	public void setPrintStyle(String printStyle) {
		this.printStyle = printStyle;
	}

	/**
	 * 配置文件中database.cluster.order中第一个集群同步状态,0:初始状态,1:正在运行,2:成功
	 * 
	 * @return
	 */
	@Column(name = "MASTERSYN", length = 1)
	public String getMasterSyn() {
		return masterSyn;
	}

	/**
	 * 配置文件中database.cluster.order中第一个集群同步状态,0:初始状态,1:正在运行,2:成功
	 * 
	 * @param masterSyn
	 */
	public void setMasterSyn(String masterSyn) {
		this.masterSyn = masterSyn;
	}

	/**
	 * 配置文件中database.cluster.order中第二个集群同步状态,0:初始状态,1:正在运行,2:成功
	 * 
	 * @return
	 */
	@Column(name = "STANDBYSYN", length = 1)
	public String getStandbySyn() {
		return standbySyn;
	}

	/**
	 * 配置文件中database.cluster.order中第二个集群同步状态,0:初始状态,1:正在运行,2:成功
	 * 
	 * @param standbySyn
	 */
	public void setStandbySyn(String standbySyn) {
		this.standbySyn = standbySyn;
	}

	@Column(name = "NEEDAUTH", columnDefinition = "number(1)")
	public Boolean getNeedAuth() {
		return needAuth;
	}

	public void setNeedAuth(Boolean needAuth) {
		this.needAuth = needAuth;
	}

	/**
	 * 检验标识,0前端不需要授权,1前端需要授权
	 * 
	 * @return
	 */
	@Column(name = "JIOYBZ", columnDefinition = "number(1)")
	public Boolean getJioybz() {
		return jioybz;
	}

	/**
	 * 检验标识,0前端不需要授权,1前端需要授权
	 * 
	 * @param jioybz
	 */
	public void setJioybz(Boolean jioybz) {
		this.jioybz = jioybz;
	}

	@Column(name = "SHOQGY", length = 8)
	public String getShoqgy() {
		return shoqgy;
	}

	public void setShoqgy(String shoqgy) {
		this.shoqgy = shoqgy;
	}

	@Column(name = "QUDAOO", length = 3)
	public String getQudaoo() {
		return qudaoo;
	}

	public void setQudaoo(String qudaoo) {
		this.qudaoo = qudaoo;
	}

	/**
	 * 授权柜员级别
	 * 
	 * @return
	 */
	@Column(name = "GUIYJB", precision = 9, scale = 0)
	public int getGuiyjb() {
		return guiyjb;
	}

	/**
	 * 授权柜员级别
	 * 
	 * @param guiyjb
	 */
	public void setGuiyjb(int guiyjb) {
		this.guiyjb = guiyjb;
	}

	/**
	 * 授权原因
	 * 
	 * @return
	 */
	@Column(name = "BEIZXX", length = 128)
	public String getBeizxx() {
		return this.beizxx;
	}

	/**
	 * 授权原因
	 * 
	 * @param beizxx
	 */
	public void setBeizxx(String beizxx) {
		this.beizxx = beizxx;
	}

	/**
	 * 处理过程中出现的异常信息
	 * 
	 * @return
	 */
	@Column(name = "ERRMSG", length = 500)
	public String getErrMsg() {
		return errMsg;
	}

	/**
	 * 处理过程中出现的异常信息
	 * 
	 * @param errMsg
	 */
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	/**
	 * 交易查询的开始时间
	 * 
	 * @return
	 */
	@Column(name = "STARTTIME", precision = 16, scale = 0)
	public Long getStartTime() {
		return startTime;
	}

	/**
	 * 交易查询的开始时间
	 * 
	 * @param startTime
	 */
	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	/**
	 * 交易查询的结束时间
	 * 
	 * @return
	 */
	@Column(name = "ENDTIME", precision = 16, scale = 0)
	public Long getEndTime() {
		return endTime;
	}

	/**
	 * 交易查询的结束时间
	 * 
	 * @param endTime
	 */
	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}

	@Transient
	@Deprecated
	public Boolean getAuthorize() {
		return authorize;
	}

	@Deprecated
	public void setAuthorize(Boolean authorize) {
		this.authorize = authorize;
	}

}