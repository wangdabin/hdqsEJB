package com.ceb.hdqs.dao;

import java.util.List;

import javax.ejb.Local;

import com.ceb.hdqs.entity.ClokEO;

@Local
public interface ClokDao {
	public void save(ClokEO entity);

	public void delete(long id);

	public ClokEO update(ClokEO entity);

	public void acquireLock(String cronName, String hostname);

	public void forceLock(String cronName, String hostname);

	public ClokEO findById(long id);

	public ClokEO findByName(String cronName);

	public List<ClokEO> findAll();
}
