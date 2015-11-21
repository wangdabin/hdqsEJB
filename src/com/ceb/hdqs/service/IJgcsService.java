package com.ceb.hdqs.service;

import java.util.List;

import javax.ejb.Local;

import com.ceb.hdqs.entity.JgcsEO;
import com.ceb.hdqs.vo.JgcsVO;

@Local
public interface IJgcsService {
	public void initCache();

	public void refreshCache();

	public void saveOrUpdate(JgcsVO entity);

	public void save(JgcsEO entity);

	public void update(JgcsEO entity);

	public JgcsEO findById(Long id);

	public JgcsEO findByYngyjg(String yngyjg);

	/**
	 * 查询辖内机构,包含本机构
	 * 
	 * @param yngyjg
	 * @return
	 * @throws HdqsServiceException
	 */
	public List<String> findChildren(String yngyjg) throws HdqsServiceException;

	public List<JgcsEO> findAll();

	public void removeAll();

	/**
	 * 判断是否是上级机构查询,含同机构查询
	 * 
	 * @param parentJG
	 * @param childJG
	 * @return
	 * @throws HdqsServiceException
	 */
	public boolean isParentQuery(String parentJG, String childJG);

	public boolean isZonghangQuery(String yngyjg);
}
