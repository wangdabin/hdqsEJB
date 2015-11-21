package com.ceb.hdqs.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.ceb.hdqs.dao.ClokDao;
import com.ceb.hdqs.entity.ClokEO;

@Stateless
public class ClokDaoImpl implements ClokDao {
	@PersistenceContext
	private EntityManager entityManager;

	public void save(ClokEO entity) {
		entityManager.persist(entity);
	}

	public void delete(long id) {
		ClokEO entity = entityManager.getReference(ClokEO.class, id);
		entityManager.remove(entity);
	}

	public ClokEO update(ClokEO entity) {
		ClokEO result = entityManager.merge(entity);
		return result;
	}

	public void acquireLock(String cronName, String hostname) {
		String updateSql = "update PCLOK set HOSTNAME= ?1,STATUS= ?2,SHJNCH= ?3 where NAME= ?4 and STATUS= ?5";
		Query query = entityManager.createNativeQuery(updateSql);
		query.setParameter(1, hostname);
		query.setParameter(2, ClokEO.STATUS_LOCK);
		query.setParameter(3, System.currentTimeMillis());
		query.setParameter(4, cronName);
		query.setParameter(5, ClokEO.STATUS_INIT);
		query.executeUpdate();
	}

	public void forceLock(String cronName, String hostname) {
		String updateSql = "update PCLOK set HOSTNAME= ?1,STATUS= ?2,SHJNCH= ?3 where NAME= ?4 ";
		Query query = entityManager.createNativeQuery(updateSql);
		query.setParameter(1, hostname);
		query.setParameter(2, ClokEO.STATUS_LOCK);
		query.setParameter(3, System.currentTimeMillis());
		query.setParameter(4, cronName);
		query.executeUpdate();
	}

	public ClokEO findById(long id) {
		ClokEO instance = entityManager.find(ClokEO.class, id);
		return instance;
	}

	@SuppressWarnings("unchecked")
	public ClokEO findByName(String cronName) {
		final String queryString = "select model from ClokEO model where model.name= :name";
		Query query = entityManager.createQuery(queryString);
		query.setParameter("name", cronName);

		List<ClokEO> list = query.getResultList();
		if (list == null || list.isEmpty()) {
			return null;
		}
		return list.get(0);
	}

	@SuppressWarnings("unchecked")
	public List<ClokEO> findAll() {
		final String queryString = "select model from ClokEO model";
		Query query = entityManager.createQuery(queryString);
		return query.getResultList();
	}
}