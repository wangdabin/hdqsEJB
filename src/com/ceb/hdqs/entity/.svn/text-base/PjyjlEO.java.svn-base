package com.ceb.hdqs.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.ceb.hdqs.constants.HdqsConstants;

@Entity
@Table(name = "PJYJL")
public class PjyjlEO extends AbstractEO {
	private static final long serialVersionUID = 3023339693158013973L;

	private String jioyrq;// 交易日期,日期(yyyyMMdd),从换日表取值
	private String jiaoym;// 交易码
	private String yngyjg;// 营业机构号
	private String jio1gy;// 柜员号
	private String guiyls;// 柜员流水(柜员号+流水号)
	private String slbhao;// 受理编码 (交易日期+柜员号+流水号)
	private Boolean jioybz = Boolean.FALSE;// 校验标识,0前端不需要授权,1前端需要授权
	private String shoqgy;// 授权柜员
	private String qudaoo;// 渠道,系统标识码
	private String masterSyn = HdqsConstants.SYN_STATUS_INIT;// 配置文件中database.cluster.order中第一个集群同步状态,0:初始状态,1:正在运行,2:成功
	private String standbySyn = HdqsConstants.SYN_STATUS_INIT;// 配置文件中database.cluster.order中第二个集群同步状态,0:初始状态,1:正在运行,2:成功
	private Boolean fileQuery = Boolean.FALSE;// 是否文件查询
	private String queryStr;// 查询条件字符串
	private Boolean runSucc = Boolean.TRUE;// 交易运行状态0:失败;1:成功;
	private Long startTime;// 交易实际的开始时间
	private Long endTime;// 交易实际的结束时间

	public PjyjlEO() {
	}

	@Id
	@Column(name = "ID", nullable = false, precision = 16, scale = 0)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PJYJL_SEQ")
	@SequenceGenerator(name = "PJYJL_SEQ", sequenceName = "SEQ_PJYJL", initialValue = 0, allocationSize = 100)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
	 * 柜员流水(柜员号+流水号)
	 * 
	 * @return
	 */
	@Column(name = "GUIYLS", length = 12)
	public String getGuiyls() {
		return guiyls;
	}

	/**
	 * 柜员流水(柜员号+流水号)
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
	 * 校验标识,0前端不需要授权,1前端需要授权
	 * 
	 * @return
	 */
	@Column(name = "JIOYBZ", columnDefinition = "number(1)")
	public Boolean getJioybz() {
		return jioybz;
	}

	/**
	 * 校验标识,0前端不需要授权,1前端需要授权
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
	 * 查询条件字符串
	 * 
	 * @return
	 */
	@Column(name = "QUERYSTR", length = 1000)
	public String getQueryStr() {
		return queryStr;
	}

	/**
	 * 查询条件字符串
	 * 
	 * @param queryStr
	 */
	public void setQueryStr(String queryStr) {
		this.queryStr = queryStr;
	}

	/**
	 * 交易运行状态0:失败;1:成功;
	 * 
	 * @return
	 */
	@Column(name = "RUNSUCC", columnDefinition = "number(1)")
	public Boolean getRunSucc() {
		return runSucc;
	}

	/**
	 * 交易运行状态0:失败;1:成功;
	 * 
	 * @param runSucc
	 */
	public void setRunSucc(Boolean runSucc) {
		this.runSucc = runSucc;
	}

	/**
	 * 交易实际的开始时间
	 * 
	 * @return
	 */
	@Column(name = "STARTTIME", precision = 16, scale = 0)
	public Long getStartTime() {
		return startTime;
	}

	/**
	 * 交易实际的开始时间
	 * 
	 * @param startTime
	 */
	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	/**
	 * 交易实际的结束时间
	 * 
	 * @return
	 */
	@Column(name = "ENDTIME", precision = 16, scale = 0)
	public Long getEndTime() {
		return endTime;
	}

	/**
	 * 交易实际的结束时间
	 * 
	 * @param endTime
	 */
	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}
}