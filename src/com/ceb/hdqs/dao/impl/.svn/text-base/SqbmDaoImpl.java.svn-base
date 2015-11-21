package com.ceb.hdqs.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.ceb.hdqs.dao.SqbmDao;
import com.ceb.hdqs.entity.SqbmEO;

@Stateless
public class SqbmDaoImpl implements SqbmDao {

	@PersistenceContext
	private EntityManager entityManager;

	public void save(SqbmEO entity) {
		entityManager.persist(entity);
	}

	public void delete(Long id) {
		SqbmEO entity = entityManager.getReference(SqbmEO.class, id);
		entityManager.remove(entity);
	}

	public SqbmEO update(SqbmEO entity) {
		SqbmEO result = entityManager.merge(entity);
		return result;
	}

	public SqbmEO findById(Long id) {
		SqbmEO instance = entityManager.find(SqbmEO.class, id);
		return instance;
	}

	@SuppressWarnings("unchecked")
	public SqbmEO findByAuthorizedCode(String szkmbm, String qudaoo, String jiluzt) {
		final String queryString = "select model from SqbmEO model where model.szkmbm= :szkmbm and model.qudaoo= :qudaoo and model.jiluzt= :jiluzt";
		Query query = entityManager.createQuery(queryString);
		query.setParameter("szkmbm", szkmbm);
		query.setParameter("qudaoo", qudaoo);
		query.setParameter("jiluzt", jiluzt);
		List<SqbmEO> list = query.getResultList();
		if (list == null || list.isEmpty()) {
			return null;
		}
		return list.get(0);
	}

	@SuppressWarnings("unchecked")
	public SqbmEO findBySzkmbmAndQudaoo(String szkmbm, String qudaoo) {
		final String queryString = "select model from SqbmEO model where model.szkmbm= :szkmbm and model.qudaoo= :qudaoo";
		Query query = entityManager.createQuery(queryString);
		query.setParameter("szkmbm", szkmbm);
		query.setParameter("qudaoo", qudaoo);
		List<SqbmEO> list = query.getResultList();
		if (list == null || list.isEmpty()) {
			return null;
		}
		return list.get(0);
	}

	@SuppressWarnings("unchecked")
	public List<SqbmEO> findByProperty(String queryString, int startIdx, int count) {
		Query query = entityManager.createQuery(queryString);
		int rowStartIdx = Math.max(0, startIdx);
		if (rowStartIdx > 0) {
			query.setFirstResult(rowStartIdx);
		}

		int rowCount = Math.max(0, count);
		if (rowCount > 0) {
			query.setMaxResults(rowCount);
		}
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<SqbmEO> findAll() {
		final String queryString = "select model from SqbmEO model order by model.id asc";
		Query query = entityManager.createQuery(queryString);
		return query.getResultList();
	}

	public long getCountByProperty(final String querySql) {
		Query query = entityManager.createQuery(querySql);
		long count = ((Long) query.getResultList().get(0)).longValue();
		return count;
	}
}