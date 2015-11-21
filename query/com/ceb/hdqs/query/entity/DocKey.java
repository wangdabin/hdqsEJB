package com.ceb.hdqs.query.entity;

/**
 * 统计文档信息的key
 * 
 * @author user
 * 
 */
public class DocKey extends SkyPair<Long, String> {

	public DocKey(Long id, String zhangh) {
		this.key = (id == null ? new Long(0) : id);
		this.value = zhangh;
	}

	public DocKey() {
	}

	public Long getId() {
		return key;
	}

	public String getZhangh() {
		return value;
	}

}
