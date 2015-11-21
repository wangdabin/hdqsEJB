package com.ceb.hdqs.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

import com.ceb.hdqs.constants.HdqsConstants;

@Entity
@Table(name = "PSQBM")
public class SqbmEO extends AbstractEO {
	private static final long serialVersionUID = 611354759674534840L;

	private String szkmbm;// 授权编码EAU0000001长度为10 ，以EAU开头，后面为数字
	private int guiyjb;// 授权柜员级别
	private String beizxx;// 授权原因
	private String qudaoo;// 渠道
	private String weihrq;// 维护日期
	private String weihgy;// 维护柜员
	private String beiy04;// 备用字段1
	private String weihxm;// 维护柜员中文名称
	private String jiluzt = HdqsConstants.STATUS_NORMAL;// 记录状态,状态是0代表正常,状态是4代表删除不可用
	private int version;

	public SqbmEO() {

	}

	@Id
	@Column(name = "ID", nullable = false, precision = 16, scale = 0)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "SZKMBM", length = 10)
	public String getSzkmbm() {
		return this.szkmbm;
	}

	public void setSzkmbm(String szkmbm) {
		this.szkmbm = szkmbm;
	}

	@Column(name = "GUIYJB", precision = 9, scale = 0)
	public int getGuiyjb() {
		return guiyjb;
	}

	public void setGuiyjb(int guiyjb) {
		this.guiyjb = guiyjb;
	}

	@Column(name = "BEIZXX", length = 128)
	public String getBeizxx() {
		return this.beizxx;
	}

	public void setBeizxx(String beizxx) {
		this.beizxx = beizxx;
	}

	@Column(name = "QUDAOO", length = 3)
	public String getQudaoo() {
		return this.qudaoo;
	}

	public void setQudaoo(String qudaoo) {
		this.qudaoo = qudaoo;
	}

	@Column(name = "WEIHRQ", length = 8)
	public String getWeihrq() {
		return this.weihrq;
	}

	public void setWeihrq(String weihrq) {
		this.weihrq = weihrq;
	}

	@Column(name = "WEIHGY", length = 6)
	public String getWeihgy() {
		return this.weihgy;
	}

	public void setWeihgy(String weihgy) {
		this.weihgy = weihgy;
	}

	@Column(name = "BEIY04", length = 4)
	public String getBeiy04() {
		return this.beiy04;
	}

	public void setBeiy04(String beiy04) {
		this.beiy04 = beiy04;
	}

	@Column(name = "BEIYZF", length = 22)
	public String getWeihxm() {
		return weihxm;
	}

	public void setWeihxm(String weihxm) {
		this.weihxm = weihxm;
	}

	@Column(name = "JILUZT", length = 1)
	public String getJiluzt() {
		return this.jiluzt;
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