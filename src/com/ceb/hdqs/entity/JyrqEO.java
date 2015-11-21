package com.ceb.hdqs.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.ceb.hdqs.constants.HdqsConstants;

/**
 * 日期参数表
 */
@Entity
@Table(name = "PJYRQ")
public class JyrqEO extends AbstractEO {
	private static final long serialVersionUID = 2302217527114829262L;

	private String ssjyrq;// 上上次交易日 VARCHAR2(8)
	private String scjyrq;// 上次交易日VARCHAR2(8)
	private String jioyrq;// 交易日期 VARCHAR2(8)
	private String xcjyrq;// 下次交易日VARCHAR2(8)
	private String zzhirq;// 年终决算日VARCHAR2(8)
	private Long shjnch;// 时间戳NUMBER(16,0)
	private String jiluzt = HdqsConstants.STATUS_NORMAL;// 记录状态VARCHAR2(1)

	public JyrqEO() {

	}

	@Id
	@Column(name = "ID", nullable = false, precision = 16, scale = 0)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "SSJYRQ", length = 8)
	public String getSsjyrq() {
		return this.ssjyrq;
	}

	public void setSsjyrq(String ssjyrq) {
		this.ssjyrq = ssjyrq;
	}

	@Column(name = "SCJYRQ", length = 8)
	public String getScjyrq() {
		return this.scjyrq;
	}

	public void setScjyrq(String scjyrq) {
		this.scjyrq = scjyrq;
	}

	@Column(name = "JIOYRQ", length = 8)
	public String getJioyrq() {
		return this.jioyrq;
	}

	public void setJioyrq(String jioyrq) {
		this.jioyrq = jioyrq;
	}

	@Column(name = "XCJYRQ", length = 8)
	public String getXcjyrq() {
		return this.xcjyrq;
	}

	public void setXcjyrq(String xcjyrq) {
		this.xcjyrq = xcjyrq;
	}

	@Column(name = "ZZHIRQ", length = 8)
	public String getZzhirq() {
		return this.zzhirq;
	}

	public void setZzhirq(String zzhirq) {
		this.zzhirq = zzhirq;
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