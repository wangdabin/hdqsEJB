package com.ceb.hdqs.dao;

import java.util.List;

import javax.ejb.Local;

import com.ceb.hdqs.entity.SqcsEO;

@Local
public interface SqcsDao {
	public void save(SqcsEO entity);

	public void delete(Long id);

	public SqcsEO update(SqcsEO entity);

	public SqcsEO findById(Long id);

	public SqcsEO findByGuiydh(String guiydh);

	public List<SqcsEO> findByProperty(String queryString, int startIdx, int count);

	public List<SqcsEO> findAll(int startIdx, int count);

	public long getCountByProperty(final String querySql);
}