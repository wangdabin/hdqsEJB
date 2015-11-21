package com.ceb.hdqs.dao;

import java.util.List;

import javax.ejb.Local;

import com.ceb.hdqs.entity.SqbmEO;

@Local
public interface SqbmDao {
	public void save(SqbmEO entity);

	public void delete(Long id);

	public SqbmEO update(SqbmEO entity);

	public SqbmEO findById(Long id);

	/**
	 * 根据授权编码和渠道进行查询,记录状态为0在用
	 * 
	 * @param szkmbm
	 *            授权编码
	 * @param qudaoo
	 *            渠道
	 * @return
	 */
	public SqbmEO findByAuthorizedCode(String szkmbm, String qudaoo, String jiluzt);

	/**
	 * 根据授权编码和渠道进行查询
	 * 
	 * @param szkmbm
	 * @param qudaoo
	 * @return
	 */
	public SqbmEO findBySzkmbmAndQudaoo(String szkmbm, String qudaoo);

	public List<SqbmEO> findByProperty(final String queryString, int startIdx, int count);

	public List<SqbmEO> findAll();

	public long getCountByProperty(final String querySql);
}