package com.ceb.hdqs.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name = "PXTPZ")
public class XtpzEO extends AbstractEO {
	private static final long serialVersionUID = 2002789025884566894L;

	private String name;
	private String value;
	private String weihrq;
	private String comments;
	private int version;

	public XtpzEO() {

	}

	@Id
	@Column(name = "ID", nullable = false, precision = 16, scale = 0)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "NAME", length = 50)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "VALUE", length = 50)
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Column(name = "WEIHRQ", length = 16)
	public String getWeihrq() {
		return weihrq;
	}

	public void setWeihrq(String weihrq) {
		this.weihrq = weihrq;
	}

	@Column(name = "COMMENTS", length = 180)
	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	@Version
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}
}