package com.ceb.hdqs.service;

import javax.ejb.Remote;

import com.ceb.hdqs.entity.GycsEO;
import com.ceb.hdqs.vo.GycsVO;

@Remote
public interface GycsService {
	public void saveOrUpdate(GycsVO entity);

	public void save(GycsEO entity);

	public void update(GycsEO entity);

	public GycsEO findById(Long id);

	public GycsEO findByGuiydh(String guiydh);

	public void removeAll();
}
