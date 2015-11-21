package com.ceb.hdqs.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.ceb.hdqs.dao.SqcsDao;
import com.ceb.hdqs.entity.SqcsEO;

@Stateless
public class SqcsDaoImpl implements SqcsDao {

	@PersistenceContext
	private EntityManager entityManager;

	public void save(SqcsEO entity) {
		entityManager.persist(entity);
	}

	public void delete(Long id) {
		SqcsEO entity = entityManager.getReference(SqcsEO.class, id);
		entityManager.remove(entity);
	}

	public SqcsEO update(SqcsEO entity) {
		SqcsEO result = entityManager.merge(entity);
		return result;
	}

	public SqcsEO findById(Long id) {
		SqcsEO instance = entityManager.find(SqcsEO.class, id);
		return instance;
	}

	@SuppressWarnings("unchecked")
	public SqcsEO findByGuiydh(String guiydh) {
		final String queryString = "select model from SqcsEO model where model.guiydh= :guiydh";
		Query query = entityManager.createQuery(queryString);
		query.setParameter("guiydh", guiydh);
		List<SqcsEO> list = query.getResultList();
		if (list == null || list.isEmpty()) {
			return null;
		}
		return list.get(0);
	}

	@SuppressWarnings("unchecked")
	public List<SqcsEO> findByProperty(String queryString, int startIdx, int count) {
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
	public List<SqcsEO> findAll(int startIdx, int count) {
		final String queryString = "select model from SqcsEO model order by model.id asc";
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

	public long getCountByProperty(final String querySql) {
		Query query = entityManager.createQuery(querySql);
		long count = ((Long) query.getResultList().get(0)).longValue();
		return count;
	}
}