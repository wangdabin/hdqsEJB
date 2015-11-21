package com.ceb.hdqs.dao;

import java.util.List;
import javax.ejb.Local;

import com.ceb.hdqs.entity.JgcsEO;

@Local
public interface JgcsDao {
	public void save(JgcsEO entity);

	public void save(List<JgcsEO> list);

	public void delete(Long id);

	public void deleteAll();

	public JgcsEO update(JgcsEO entity);

	public void update(List<JgcsEO> list);

	public JgcsEO findById(Long id);

	/**
	 * 根据营业机构号查询
	 * 
	 * @param yngyjg
	 *            营业机构号
	 * @return
	 */
	public JgcsEO findByYngyjg(String yngyjg);

	/**
	 * 根据帐务上级(zngwsj)查询下级机构
	 * 
	 * @param yngyjg
	 * @return
	 */
	public List<JgcsEO> findChildren(String yngyjg);

	public List<JgcsEO> findAll();
}