package com.ceb.hdqs.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.ceb.hdqs.dao.XtpzDao;
import com.ceb.hdqs.entity.XtpzEO;

@Stateless
public class XtpzDaoImpl implements XtpzDao {
	@PersistenceContext
	private EntityManager entityManager;

	public void save(XtpzEO entity) {
		entityManager.persist(entity);
	}

	public void delete(Long id) {
		XtpzEO entity = entityManager.getReference(XtpzEO.class, id);
		entityManager.remove(entity);
	}

	public XtpzEO update(XtpzEO entity) {
		XtpzEO result = entityManager.merge(entity);
		return result;
	}

	public XtpzEO findRecordById(long id) {
		XtpzEO instance = entityManager.find(XtpzEO.class, id);
		return instance;
	}

	@SuppressWarnings("unchecked")
	public XtpzEO findRecordByName(String name) {
		String queryString = "select model from XtpzEO model where model.name= :name";
		Query query = entityManager.createQuery(queryString);
		query.setParameter("name", name);
		List<XtpzEO> list = query.getResultList();
		if (list == null || list.isEmpty()) {
			return null;
		}
		return list.get(0);
	}

	@SuppressWarnings("unchecked")
	public List<XtpzEO> findAll() {
		final String queryString = "select model from XtpzEO model";
		Query query = entityManager.createQuery(queryString);

		return query.getResultList();
	}
}