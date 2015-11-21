package com.ceb.hdqs.dao;

import java.util.List;

import javax.ejb.Local;

import com.ceb.hdqs.entity.GylsEO;

@Local
public interface GylsDao {
	public GylsEO save(GylsEO entity);

	public void delete(long id);

	public GylsEO update(GylsEO entity);

	public GylsEO findById(long id);

	public GylsEO findByGuiyuanAndCreatedT(String guiyuan, String createdT);

	public List<GylsEO> findAll();
}