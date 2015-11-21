package com.ceb.hdqs.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "PGYLS")
public class GylsEO extends AbstractEO {
	private static final long serialVersionUID = -8603844715752043518L;

	private String guiyuan;
	private String exDate;// 日期(yyyyMMdd),从换日表取值
	private int seq;

	public GylsEO() {
	}

	@Id
	@Column(name = "ID", nullable = false, precision = 16, scale = 0)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GUIYUAN_SEQ")
	@SequenceGenerator(name = "GUIYUAN_SEQ", sequenceName = "SEQ_PGYLS", initialValue = 0, allocationSize = 100)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "GUIYUAN", length = 6)
	public String getGuiyuan() {
		return this.guiyuan;
	}

	public void setGuiyuan(String guiyuan) {
		this.guiyuan = guiyuan;
	}

	@Column(name = "EXDATE", length = 8)
	public String getExDate() {
		return exDate;
	}

	public void setExDate(String exDate) {
		this.exDate = exDate;
	}

	@Column(name = "SEQ", precision = 9, scale = 0)
	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}
}