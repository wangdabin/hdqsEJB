package com.ceb.hdqs.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.ceb.hdqs.dao.GylsDao;
import com.ceb.hdqs.entity.GylsEO;

@Stateless
public class GylsDaoImpl implements GylsDao {

	@PersistenceContext
	private EntityManager entityManager;

	public GylsEO save(GylsEO entity) {
		entityManager.persist(entity);
		return entity;
	}

	public void delete(long id) {
		GylsEO entity = entityManager.getReference(GylsEO.class, id);
		entityManager.remove(entity);
	}

	public GylsEO update(GylsEO entity) {
		GylsEO result = entityManager.merge(entity);
		return result;
	}

//	public void reset(String createdT) {
//		final String updateSql = "UPDATE PGYLS SET EXDATE= ?1, SEQ=0, VERSION= ?2 WHERE 1=1";
//		Query query = entityManager.createNativeQuery(updateSql);
//		query.setParameter(1, createdT);
//		query.setParameter(2, 1);
//		query.executeUpdate();
//	}

	public GylsEO findById(long id) {
		GylsEO instance = entityManager.find(GylsEO.class, id);
		return instance;
	}

	@SuppressWarnings("unchecked")
	public GylsEO findByGuiyuanAndCreatedT(String guiyuan, String exDate) {
		final String queryString = "select model from GylsEO model where model.guiyuan= :guiyuan and model.exDate= :exDate";
		Query query = entityManager.createQuery(queryString);
		query.setParameter("guiyuan", guiyuan);
		query.setParameter("exDate", exDate);

		List<GylsEO> list = query.getResultList();
		if (list == null || list.isEmpty()) {
			return null;
		}
		return list.get(0);
	}

//	@SuppressWarnings("unchecked")
//	public List<GylsEO> findExpiredRecord(long expiredT) {
//		final String queryString = "select model from GylsEO model where model.shjnch< :expiredT";
//		Query query = entityManager.createQuery(queryString);
//		query.setParameter("expiredT", expiredT);
//
//		return query.getResultList();
//	}

	@SuppressWarnings("unchecked")
	public List<GylsEO> findAll() {
		final String queryString = "select model from GylsEO model";
		Query query = entityManager.createQuery(queryString);
		return query.getResultList();
	}
}