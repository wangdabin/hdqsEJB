package com.ceb.hdqs.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "PJGCS")
public class JgcsEO extends AbstractEO {
	private static final long serialVersionUID = 5868766708988856614L;
	public static final String YNGYJG_ZONGHANG = "1001";
	// JGYYJB机构营业级别0-总行,1-分行,2-支行,3-分理处
	public static final String JGYYJB_ZONGHANG = "0";
	public static final String JGYYJB_FENHANG = "1";
	public static final String JGYYJB_ZHIHANG = "2";
	public static final String JGYYJB_FENLICHU = "3";

	private String qszxdh;// 地区代号(原为：清算中心代号)
	private String yinhdm;// 银行代码
	private String yngyjg;// 营业机构号
	private String fkjgdm;// 发卡机构代码
	private String zkjgdm;// 制卡中心代码
	private String waigdm;// 外管代码
	private String jggljb;// 机构管理级别
	private String jgyyjb;// 机构营业级别in use
	private String jgpzjb;// 机构凭证级别
	private String jgzjjb;// 出帐周期
	private String gnlisj;// 管理上级
	private String zngwsj;// 帐务上级in use
	private String pngzsj;// 凭证上级
	private String yngxsj;// 省区代号(前两位)
	private String xinjsj;// 现金上级
	private String jhjgbz;// 稽核机构标志
	private String jigolx;// 机构类型
	private String dlhsbz;// 独立核算标志
	private String jigomc;// 机构中文名称
	private String jgywmc;// 机构英文名称
	private String wxsjbz;// 尾箱上缴标志
	private String ynhbic;// 银行SWIFT
	private String dizhii;// 地址
	private String dianhh;// 电话号码
	private String dbguah;// 电报挂号
	private String lnxirm;// 联系人
	private String qyngrq;// 启用日期
	private String sbshbm;// 设备名
	private String dyndlm;// 报表队列名
	private String tszfjh;// 同城实时支付交换号
	private String lianhh;// 联行行号
	private String rjiebz;// 日结标记
	private String gbcgbz;// 广播成功标志
	private String wlywbz;// 外围落地业务处理标志
	private String yatmbz;// 异地支行ATM联网标志
	private String yposbz;// 异地支行POS联网标志
	private String fhipdz;// 分行IP地址
	private String fhport;// 分行PORT号
	private String youzbm;// 邮政编码
	private String dzyjdz;// E-mail
	private String jgfwdz;// 机构服务器地址名称
	private String rhjsbz;// 国结挂靠标志
	private String dbqfbz;// 代办签发标志
	private Long shjnch;// 时间戳
	private String jiluzt;// 记录状态

	public JgcsEO() {

	}

	@Id
	@Column(name = "ID", nullable = false, precision = 16, scale = 0)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "QSZXDH", length = 4)
	public String getQszxdh() {
		return this.qszxdh;
	}

	public void setQszxdh(String qszxdh) {
		this.qszxdh = qszxdh;
	}

	@Column(name = "YINHDM", length = 2)
	public String getYinhdm() {
		return this.yinhdm;
	}

	public void setYinhdm(String yinhdm) {
		this.yinhdm = yinhdm;
	}

	@Column(name = "YNGYJG", unique = true, length = 4)
	public String getYngyjg() {
		return this.yngyjg;
	}

	public void setYngyjg(String yngyjg) {
		this.yngyjg = yngyjg;
	}

	@Column(name = "FKJGDM", length = 2)
	public String getFkjgdm() {
		return this.fkjgdm;
	}

	public void setFkjgdm(String fkjgdm) {
		this.fkjgdm = fkjgdm;
	}

	@Column(name = "ZKJGDM", nullable = false, length = 4)
	public String getZkjgdm() {
		return this.zkjgdm;
	}

	public void setZkjgdm(String zkjgdm) {
		this.zkjgdm = zkjgdm;
	}

	@Column(name = "WAIGDM", length = 12)
	public String getWaigdm() {
		return this.waigdm;
	}

	public void setWaigdm(String waigdm) {
		this.waigdm = waigdm;
	}

	@Column(name = "JGGLJB", length = 1)
	public String getJggljb() {
		return this.jggljb;
	}

	public void setJggljb(String jggljb) {
		this.jggljb = jggljb;
	}

	@Column(name = "JGYYJB", length = 1)
	public String getJgyyjb() {
		return this.jgyyjb;
	}

	public void setJgyyjb(String jgyyjb) {
		this.jgyyjb = jgyyjb;
	}

	@Column(name = "JGPZJB", length = 1)
	public String getJgpzjb() {
		return this.jgpzjb;
	}

	public void setJgpzjb(String jgpzjb) {
		this.jgpzjb = jgpzjb;
	}

	@Column(name = "JGZJJB", length = 1)
	public String getJgzjjb() {
		return this.jgzjjb;
	}

	public void setJgzjjb(String jgzjjb) {
		this.jgzjjb = jgzjjb;
	}

	@Column(name = "GNLISJ", length = 4)
	public String getGnlisj() {
		return this.gnlisj;
	}

	public void setGnlisj(String gnlisj) {
		this.gnlisj = gnlisj;
	}

	@Column(name = "ZNGWSJ", length = 4)
	public String getZngwsj() {
		return this.zngwsj;
	}

	public void setZngwsj(String zngwsj) {
		this.zngwsj = zngwsj;
	}

	@Column(name = "PNGZSJ", length = 4)
	public String getPngzsj() {
		return this.pngzsj;
	}

	public void setPngzsj(String pngzsj) {
		this.pngzsj = pngzsj;
	}

	@Column(name = "YNGXSJ", length = 4)
	public String getYngxsj() {
		return this.yngxsj;
	}

	public void setYngxsj(String yngxsj) {
		this.yngxsj = yngxsj;
	}

	@Column(name = "XINJSJ", nullable = false, length = 4)
	public String getXinjsj() {
		return this.xinjsj;
	}

	public void setXinjsj(String xinjsj) {
		this.xinjsj = xinjsj;
	}

	@Column(name = "JHJGBZ", length = 1)
	public String getJhjgbz() {
		return this.jhjgbz;
	}

	public void setJhjgbz(String jhjgbz) {
		this.jhjgbz = jhjgbz;
	}

	@Column(name = "JIGOLX", length = 1)
	public String getJigolx() {
		return this.jigolx;
	}

	public void setJigolx(String jigolx) {
		this.jigolx = jigolx;
	}

	@Column(name = "DLHSBZ", length = 1)
	public String getDlhsbz() {
		return this.dlhsbz;
	}

	public void setDlhsbz(String dlhsbz) {
		this.dlhsbz = dlhsbz;
	}

	@Column(name = "JIGOMC", length = 62)
	public String getJigomc() {
		return this.jigomc;
	}

	public void setJigomc(String jigomc) {
		this.jigomc = jigomc;
	}

	@Column(name = "JGYWMC", length = 62)
	public String getJgywmc() {
		return this.jgywmc;
	}

	public void setJgywmc(String jgywmc) {
		this.jgywmc = jgywmc;
	}

	@Column(name = "WXSJBZ", length = 1)
	public String getWxsjbz() {
		return this.wxsjbz;
	}

	public void setWxsjbz(String wxsjbz) {
		this.wxsjbz = wxsjbz;
	}

	@Column(name = "YNHBIC", length = 11)
	public String getYnhbic() {
		return this.ynhbic;
	}

	public void setYnhbic(String ynhbic) {
		this.ynhbic = ynhbic;
	}

	@Column(name = "DIZHII", length = 62)
	public String getDizhii() {
		return this.dizhii;
	}

	public void setDizhii(String dizhii) {
		this.dizhii = dizhii;
	}

	@Column(name = "DIANHH", length = 20)
	public String getDianhh() {
		return this.dianhh;
	}

	public void setDianhh(String dianhh) {
		this.dianhh = dianhh;
	}

	@Column(name = "DBGUAH", length = 10)
	public String getDbguah() {
		return this.dbguah;
	}

	public void setDbguah(String dbguah) {
		this.dbguah = dbguah;
	}

	@Column(name = "LNXIRM", length = 22)
	public String getLnxirm() {
		return this.lnxirm;
	}

	public void setLnxirm(String lnxirm) {
		this.lnxirm = lnxirm;
	}

	@Column(name = "QYNGRQ", length = 8)
	public String getQyngrq() {
		return this.qyngrq;
	}

	public void setQyngrq(String qyngrq) {
		this.qyngrq = qyngrq;
	}

	@Column(name = "SBSHBM", length = 6)
	public String getSbshbm() {
		return this.sbshbm;
	}

	public void setSbshbm(String sbshbm) {
		this.sbshbm = sbshbm;
	}

	@Column(name = "DYNDLM", length = 8)
	public String getDyndlm() {
		return this.dyndlm;
	}

	public void setDyndlm(String dyndlm) {
		this.dyndlm = dyndlm;
	}

	@Column(name = "TSZFJH", length = 9)
	public String getTszfjh() {
		return this.tszfjh;
	}

	public void setTszfjh(String tszfjh) {
		this.tszfjh = tszfjh;
	}

	@Column(name = "LIANHH", length = 11)
	public String getLianhh() {
		return this.lianhh;
	}

	public void setLianhh(String lianhh) {
		this.lianhh = lianhh;
	}

	@Column(name = "RJIEBZ", length = 1)
	public String getRjiebz() {
		return this.rjiebz;
	}

	public void setRjiebz(String rjiebz) {
		this.rjiebz = rjiebz;
	}

	@Column(name = "GBCGBZ", length = 1)
	public String getGbcgbz() {
		return this.gbcgbz;
	}

	public void setGbcgbz(String gbcgbz) {
		this.gbcgbz = gbcgbz;
	}

	@Column(name = "WLYWBZ", length = 1)
	public String getWlywbz() {
		return this.wlywbz;
	}

	public void setWlywbz(String wlywbz) {
		this.wlywbz = wlywbz;
	}

	@Column(name = "YATMBZ", length = 1)
	public String getYatmbz() {
		return this.yatmbz;
	}

	public void setYatmbz(String yatmbz) {
		this.yatmbz = yatmbz;
	}

	@Column(name = "YPOSBZ", length = 1)
	public String getYposbz() {
		return this.yposbz;
	}

	public void setYposbz(String yposbz) {
		this.yposbz = yposbz;
	}

	@Column(name = "FHIPDZ", length = 15)
	public String getFhipdz() {
		return this.fhipdz;
	}

	public void setFhipdz(String fhipdz) {
		this.fhipdz = fhipdz;
	}

	@Column(name = "FHPORT", length = 4)
	public String getFhport() {
		return this.fhport;
	}

	public void setFhport(String fhport) {
		this.fhport = fhport;
	}

	@Column(name = "YOUZBM", length = 6)
	public String getYouzbm() {
		return this.youzbm;
	}

	public void setYouzbm(String youzbm) {
		this.youzbm = youzbm;
	}

	@Column(name = "DZYJDZ", length = 42)
	public String getDzyjdz() {
		return this.dzyjdz;
	}

	public void setDzyjdz(String dzyjdz) {
		this.dzyjdz = dzyjdz;
	}

	@Column(name = "JGFWDZ", length = 40)
	public String getJgfwdz() {
		return this.jgfwdz;
	}

	public void setJgfwdz(String jgfwdz) {
		this.jgfwdz = jgfwdz;
	}

	@Column(name = "RHJSBZ", length = 1)
	public String getRhjsbz() {
		return this.rhjsbz;
	}

	public void setRhjsbz(String rhjsbz) {
		this.rhjsbz = rhjsbz;
	}

	@Column(name = "DBQFBZ", length = 1)
	public String getDbqfbz() {
		return this.dbqfbz;
	}

	public void setDbqfbz(String dbqfbz) {
		this.dbqfbz = dbqfbz;
	}

	@Column(name = "SHJNCH", precision = 16, scale = 0)
	public Long getShjnch() {
		return this.shjnch;
	}

	public void setShjnch(Long shjnch) {
		this.shjnch = shjnch;
	}

	@Column(name = "JILUZT", length = 1)
	public String getJiluzt() {
		return this.jiluzt;
	}

	public void setJiluzt(String jiluzt) {
		this.jiluzt = jiluzt;
	}
}