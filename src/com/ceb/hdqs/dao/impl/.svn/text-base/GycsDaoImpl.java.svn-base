package com.ceb.hdqs.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.ceb.hdqs.dao.GycsDao;
import com.ceb.hdqs.entity.GycsEO;

@Stateless
public class GycsDaoImpl implements GycsDao {

	@PersistenceContext
	private EntityManager entityManager;

	public void save(GycsEO entity) {
		entityManager.persist(entity);
	}

	public void save(List<GycsEO> list) {
		for (int i = 0, size = list.size(); i < size; i++) {
			save(list.get(i));
			if ((i + 1) % 100 == 0) {
				entityManager.flush();
				entityManager.clear();
			}
		}
	}

	public void delete(Long id) {
		GycsEO entity = entityManager.getReference(GycsEO.class, id);
		entityManager.remove(entity);
	}

	public void deleteAll() {
		String updateSql = "delete from PGYCS where 1=1";
		entityManager.createNativeQuery(updateSql).executeUpdate();
	}

	public GycsEO update(GycsEO entity) {
		GycsEO result = entityManager.merge(entity);
		return result;
	}

	public void update(List<GycsEO> list) {
		for (int i = 0, size = list.size(); i < size; i++) {
			update(list.get(i));
			if ((i + 1) % 100 == 0) {
				entityManager.flush();
				entityManager.clear();
			}
		}
	}

	public GycsEO findById(Long id) {
		GycsEO instance = entityManager.find(GycsEO.class, id);
		return instance;
	}

	@SuppressWarnings("unchecked")
	public GycsEO findByGuiydh(String guiydh) {
		final String queryString = "select model from GycsEO model where model.guiydh= :guiydh";
		Query query = entityManager.createQuery(queryString);
		query.setParameter("guiydh", guiydh);
		List<GycsEO> list = query.getResultList();
		if (list == null || list.isEmpty()) {
			return null;
		}
		return list.get(0);
	}

	@SuppressWarnings("unchecked")
	public List<GycsEO> findAll() {
		final String queryString = "select model from GycsEO model";
		Query query = entityManager.createQuery(queryString);
		return query.getResultList();
	}
}