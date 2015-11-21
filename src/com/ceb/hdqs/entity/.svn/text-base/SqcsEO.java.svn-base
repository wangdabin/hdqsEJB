package com.ceb.hdqs.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

import com.ceb.hdqs.constants.HdqsConstants;

@Entity
@Table(name = "PSQCS")
public class SqcsEO extends AbstractEO {
	private static final long serialVersionUID = 7797233768983456035L;

	private String guiydh;// 柜员代号
	private String guiyxm;// 柜员姓名
	private String gylxdm;// 柜员角色代码
	private String qyngrq;// 权限启用日期
	private String jshurq;// 权限结束日期
	private String weihrq;// 维护日期
	private String weihgy;// 维护柜员
	private String weihxm;// 维护姓名
	private String slbhao;// 受理编号
	private String jiluzt = HdqsConstants.STATUS_NORMAL;// 记录状态,0为在用
	private int version;

	public SqcsEO() {

	}

	@Id
	@Column(name = "ID", nullable = false, precision = 16, scale = 0)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "GUIYDH", length = 6)
	public String getGuiydh() {
		return guiydh;
	}

	public void setGuiydh(String guiydh) {
		this.guiydh = guiydh;
	}

	@Column(name = "GUIYXM", length = 22)
	public String getGuiyxm() {
		return guiyxm;
	}

	public void setGuiyxm(String guiyxm) {
		this.guiyxm = guiyxm;
	}

	@Column(name = "GYLXDM", length = 2)
	public String getGylxdm() {
		return gylxdm;
	}

	public void setGylxdm(String gylxdm) {
		this.gylxdm = gylxdm;
	}

	@Column(name = "QYNGRQ", length = 8)
	public String getQyngrq() {
		return qyngrq;
	}

	public void setQyngrq(String qyngrq) {
		this.qyngrq = qyngrq;
	}

	@Column(name = "JSHURQ", length = 8)
	public String getJshurq() {
		return jshurq;
	}

	public void setJshurq(String jshurq) {
		this.jshurq = jshurq;
	}

	@Column(name = "WEIHRQ", length = 8)
	public String getWeihrq() {
		return weihrq;
	}

	public void setWeihrq(String weihrq) {
		this.weihrq = weihrq;
	}

	@Column(name = "WEIHGY", length = 6)
	public String getWeihgy() {
		return weihgy;
	}

	public void setWeihgy(String weihgy) {
		this.weihgy = weihgy;
	}

	@Column(name = "WEIHXM", length = 22)
	public String getWeihxm() {
		return weihxm;
	}

	public void setWeihxm(String weihxm) {
		this.weihxm = weihxm;
	}

	@Column(name = "SLBHAO", length = 10)
	public String getSlbhao() {
		return slbhao;
	}

	public void setSlbhao(String slbhao) {
		this.slbhao = slbhao;
	}

	@Column(name = "JILUZT", length = 1)
	public String getJiluzt() {
		return jiluzt;
	}

	public void setJiluzt(String jiluzt) {
		this.jiluzt = jiluzt;
	}

	@Version
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}
}