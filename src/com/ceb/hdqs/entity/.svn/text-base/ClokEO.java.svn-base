package com.ceb.hdqs.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name = "PCLOK")
public class ClokEO extends AbstractEO {
	private static final long serialVersionUID = -2704499909324234772L;
	public static final String STATUS_INIT = "0";// 释放
	public static final String STATUS_LOCK = "1";// 锁定

	private String name;// cron名称
	private String hostname;// 服务器名称
	private String status;// 锁状态,0:释放,1:锁定
	private Long shjnch;// 时间戳
	private int version;

	public ClokEO() {

	}

	@Id
	@Column(name = "ID", nullable = false, precision = 16, scale = 0)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "NAME", length = 35)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "HOSTNAME", length = 35)
	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	@Column(name = "STATUS", length = 1)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name = "SHJNCH", precision = 16, scale = 0)
	public Long getShjnch() {
		return this.shjnch;
	}

	public void setShjnch(Long shjnch) {
		this.shjnch = shjnch;
	}

	@Version
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}
}