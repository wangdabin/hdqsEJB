package com.ceb.hdqs.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.ceb.hdqs.dao.JyrqDao;
import com.ceb.hdqs.entity.JyrqEO;

@Stateless
public class JyrqDaoImpl implements JyrqDao {
	@PersistenceContext
	private EntityManager entityManager;

	public void save(JyrqEO entity) {
		entityManager.persist(entity);
	}

	public void delete(Long id) {
		JyrqEO entity = entityManager.getReference(JyrqEO.class, id);
		entityManager.remove(entity);
	}

	public JyrqEO update(JyrqEO entity) {
		JyrqEO result = entityManager.merge(entity);
		return result;
	}

	public JyrqEO findById(Long id) {
		JyrqEO instance = entityManager.find(JyrqEO.class, id);
		return instance;
	}

	@SuppressWarnings("unchecked")
	public List<JyrqEO> findAll() {
		final String queryString = "select model from JyrqEO model";
		Query query = entityManager.createQuery(queryString);

		return query.getResultList();
	}
}