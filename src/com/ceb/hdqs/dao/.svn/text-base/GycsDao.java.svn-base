package com.ceb.hdqs.dao;

import java.util.List;
import javax.ejb.Local;

import com.ceb.hdqs.entity.GycsEO;

@Local
public interface GycsDao {
	public void save(GycsEO entity);

	public void save(List<GycsEO> list);

	public void delete(Long id);

	public void deleteAll();

	public GycsEO update(GycsEO entity);

	public void update(List<GycsEO> list);

	public GycsEO findById(Long id);

	public GycsEO findByGuiydh(String guiydh);

	public List<GycsEO> findAll();
}