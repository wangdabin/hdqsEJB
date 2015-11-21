package com.ceb.hdqs.service;

import javax.ejb.Local;

import com.ceb.hdqs.entity.GycsEO;
import com.ceb.hdqs.vo.GycsVO;

@Local
public interface IGycsService {
	public void saveOrUpdate(GycsVO entity);

	public void save(GycsEO entity);

	public void update(GycsEO entity);

	public GycsEO findById(Long id);

	public GycsEO findByGuiydh(String guiydh);

	public void removeAll();
}
