package com.ceb.hdqs.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "PGYCS")
public class GycsEO extends AbstractEO {
	private static final long serialVersionUID = 5626291441514610837L;
	// GUIYJB柜员级别1-一级柜员,2-二级柜员,3-三级柜员,4-四级柜员,5-五级柜员,6-六级柜员
	public static final String GUIYJB_ONE = "1";
	public static final String GUIYJB_TWO = "2";
	public static final String GUIYJB_THREE = "3";
	public static final String GUIYJB_FOUR = "4";
	public static final String GUIYJB_FIVE = "5";
	public static final String GUIYJB_SIX = "6";

	private String yngyjg;// 营业机构号
	private String zhngjg;// 帐务机构号
	private String guiydh;// 柜员代号
	private String ygngbh;// 员工编号
	private String guiysx;// 柜员属性
	private String guiyxm;// 柜员姓名
	private String gylxdm;// 柜员角色代码
	private String jioymm;// 密码
	private String mimarq;// 密码修改日期
	private String guiyzt;// 柜员签到状态
	private String qyngrq;// 启用日期
	private String dqjyls;// 当前交易流水号
	private String guiylx;// 柜员类型
	private String bumenn;// 所属部门
	private String guiyjb;// 级别
	private String jyzblb;// 交易组别列表
	private String sqzblb;// 授权组别列表
	private String jnshjy;// 删减交易
	private String weixdh;// 现金尾箱号
	private String biowwx;// 表外尾箱号
	private Double gyxjxe;// 柜员现金借方限额
	private Double gyxdxe;// 柜员现金贷方限额
	private Double gyzjxe;// 柜员转帐借方限额
	private Double gyzdxe;// 柜员转账贷方限额
	private Double xjwxxe;// 现金尾箱限额
	private String shoqkh;// 授权卡号
	private Long qdaosj;// 签到时间
	private Long qtuisj;// 签退时间
	private String gyrjbz;// 日结标记
	private String zhngdh;// 终端号
	private String weihrq;// 维护日期
	private String gyqybz;// 柜员启用标志
	private String ywkabz;// 柜员有无卡标志
	private String czynxh;// 柜员卡序号
	private Long shjnch;// 时间戳
	private String jiluzt;// 记录状态

	public GycsEO() {
	}

	@Id
	@Column(name = "ID", nullable = false, precision = 16, scale = 0)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "YNGYJG", nullable = false, length = 4)
	public String getYngyjg() {
		return this.yngyjg;
	}

	public void setYngyjg(String yngyjg) {
		this.yngyjg = yngyjg;
	}

	@Column(name = "ZHNGJG", nullable = false, length = 4)
	public String getZhngjg() {
		return this.zhngjg;
	}

	public void setZhngjg(String zhngjg) {
		this.zhngjg = zhngjg;
	}

	@Column(name = "GUIYDH", unique = true, nullable = false, length = 6)
	public String getGuiydh() {
		return this.guiydh;
	}

	public void setGuiydh(String guiydh) {
		this.guiydh = guiydh;
	}

	@Column(name = "YGNGBH", length = 10)
	public String getYgngbh() {
		return this.ygngbh;
	}

	public void setYgngbh(String ygngbh) {
		this.ygngbh = ygngbh;
	}

	@Column(name = "GUIYSX", nullable = false, length = 1)
	public String getGuiysx() {
		return this.guiysx;
	}

	public void setGuiysx(String guiysx) {
		this.guiysx = guiysx;
	}

	@Column(name = "GUIYXM", length = 22)
	public String getGuiyxm() {
		return this.guiyxm;
	}

	public void setGuiyxm(String guiyxm) {
		this.guiyxm = guiyxm;
	}

	@Column(name = "GYLXDM", length = 2)
	public String getGylxdm() {
		return this.gylxdm;
	}

	public void setGylxdm(String gylxdm) {
		this.gylxdm = gylxdm;
	}

	@Column(name = "JIOYMM", length = 16)
	public String getJioymm() {
		return this.jioymm;
	}

	public void setJioymm(String jioymm) {
		this.jioymm = jioymm;
	}

	@Column(name = "MIMARQ", length = 8)
	public String getMimarq() {
		return this.mimarq;
	}

	public void setMimarq(String mimarq) {
		this.mimarq = mimarq;
	}

	@Column(name = "GUIYZT", length = 1)
	public String getGuiyzt() {
		return this.guiyzt;
	}

	public void setGuiyzt(String guiyzt) {
		this.guiyzt = guiyzt;
	}

	@Column(name = "QYNGRQ", length = 8)
	public String getQyngrq() {
		return this.qyngrq;
	}

	public void setQyngrq(String qyngrq) {
		this.qyngrq = qyngrq;
	}

	@Column(name = "DQJYLS", length = 6)
	public String getDqjyls() {
		return this.dqjyls;
	}

	public void setDqjyls(String dqjyls) {
		this.dqjyls = dqjyls;
	}

	@Column(name = "GUIYLX", length = 1)
	public String getGuiylx() {
		return this.guiylx;
	}

	public void setGuiylx(String guiylx) {
		this.guiylx = guiylx;
	}

	@Column(name = "BUMENN", length = 2)
	public String getBumenn() {
		return this.bumenn;
	}

	public void setBumenn(String bumenn) {
		this.bumenn = bumenn;
	}

	@Column(name = "GUIYJB", length = 1)
	public String getGuiyjb() {
		return this.guiyjb;
	}

	public void setGuiyjb(String guiyjb) {
		this.guiyjb = guiyjb;
	}

	@Column(name = "JYZBLB", length = 120)
	public String getJyzblb() {
		return this.jyzblb;
	}

	public void setJyzblb(String jyzblb) {
		this.jyzblb = jyzblb;
	}

	@Column(name = "SQZBLB", length = 120)
	public String getSqzblb() {
		return this.sqzblb;
	}

	public void setSqzblb(String sqzblb) {
		this.sqzblb = sqzblb;
	}

	@Column(name = "JNSHJY", length = 256)
	public String getJnshjy() {
		return this.jnshjy;
	}

	public void setJnshjy(String jnshjy) {
		this.jnshjy = jnshjy;
	}

	@Column(name = "WEIXDH", length = 4)
	public String getWeixdh() {
		return this.weixdh;
	}

	public void setWeixdh(String weixdh) {
		this.weixdh = weixdh;
	}

	@Column(name = "BIOWWX", length = 4)
	public String getBiowwx() {
		return this.biowwx;
	}

	public void setBiowwx(String biowwx) {
		this.biowwx = biowwx;
	}

	@Column(name = "GYXJXE", precision = 13, scale = 2)
	public Double getGyxjxe() {
		return this.gyxjxe;
	}

	public void setGyxjxe(Double gyxjxe) {
		this.gyxjxe = gyxjxe;
	}

	@Column(name = "GYXDXE", precision = 13, scale = 2)
	public Double getGyxdxe() {
		return this.gyxdxe;
	}

	public void setGyxdxe(Double gyxdxe) {
		this.gyxdxe = gyxdxe;
	}

	@Column(name = "GYZJXE", precision = 13, scale = 2)
	public Double getGyzjxe() {
		return this.gyzjxe;
	}

	public void setGyzjxe(Double gyzjxe) {
		this.gyzjxe = gyzjxe;
	}

	@Column(name = "GYZDXE", precision = 13, scale = 2)
	public Double getGyzdxe() {
		return this.gyzdxe;
	}

	public void setGyzdxe(Double gyzdxe) {
		this.gyzdxe = gyzdxe;
	}

	@Column(name = "XJWXXE", precision = 13, scale = 2)
	public Double getXjwxxe() {
		return this.xjwxxe;
	}

	public void setXjwxxe(Double xjwxxe) {
		this.xjwxxe = xjwxxe;
	}

	@Column(name = "SHOQKH", length = 10)
	public String getShoqkh() {
		return this.shoqkh;
	}

	public void setShoqkh(String shoqkh) {
		this.shoqkh = shoqkh;
	}

	@Column(name = "QDAOSJ", precision = 10, scale = 0)
	public Long getQdaosj() {
		return this.qdaosj;
	}

	public void setQdaosj(Long qdaosj) {
		this.qdaosj = qdaosj;
	}

	@Column(name = "QTUISJ", precision = 10, scale = 0)
	public Long getQtuisj() {
		return this.qtuisj;
	}

	public void setQtuisj(Long qtuisj) {
		this.qtuisj = qtuisj;
	}

	@Column(name = "GYRJBZ", nullable = false, length = 1)
	public String getGyrjbz() {
		return this.gyrjbz;
	}

	public void setGyrjbz(String gyrjbz) {
		this.gyrjbz = gyrjbz;
	}

	@Column(name = "ZHNGDH", length = 5)
	public String getZhngdh() {
		return this.zhngdh;
	}

	public void setZhngdh(String zhngdh) {
		this.zhngdh = zhngdh;
	}

	@Column(name = "WEIHRQ", nullable = false, length = 8)
	public String getWeihrq() {
		return this.weihrq;
	}

	public void setWeihrq(String weihrq) {
		this.weihrq = weihrq;
	}

	@Column(name = "GYQYBZ", length = 1)
	public String getGyqybz() {
		return this.gyqybz;
	}

	public void setGyqybz(String gyqybz) {
		this.gyqybz = gyqybz;
	}

	@Column(name = "YWKABZ", length = 1)
	public String getYwkabz() {
		return this.ywkabz;
	}

	public void setYwkabz(String ywkabz) {
		this.ywkabz = ywkabz;
	}

	@Column(name = "CZYNXH", length = 2)
	public String getCzynxh() {
		return this.czynxh;
	}

	public void setCzynxh(String czynxh) {
		this.czynxh = czynxh;
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