package com.ceb.hdqs.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.ceb.hdqs.dao.JgcsDao;
import com.ceb.hdqs.entity.JgcsEO;

@Stateless
public class JgcsDaoImpl implements JgcsDao {
	@PersistenceContext
	private EntityManager entityManager;

	public void save(JgcsEO entity) {
		entityManager.persist(entity);
	}

	public void save(List<JgcsEO> list) {
		for (int i = 0, size = list.size(); i < size; i++) {
			save(list.get(i));
			if ((i + 1) % 100 == 0) {
				entityManager.flush();
				entityManager.clear();
			}
		}
	}

	public void delete(Long id) {
		JgcsEO entity = entityManager.getReference(JgcsEO.class, id);
		entityManager.remove(entity);
	}

	public void deleteAll() {
		String updateSql = "delete from PJGCS";
		entityManager.createNativeQuery(updateSql).executeUpdate();
	}

	public JgcsEO update(JgcsEO entity) {
		JgcsEO result = entityManager.merge(entity);
		return result;
	}

	public void update(List<JgcsEO> list) {
		for (int i = 0, size = list.size(); i < size; i++) {
			update(list.get(i));
			if ((i + 1) % 100 == 0) {
				entityManager.flush();
				entityManager.clear();
			}
		}
	}

	public JgcsEO findById(Long id) {
		JgcsEO instance = entityManager.find(JgcsEO.class, id);
		return instance;
	}

	@SuppressWarnings("unchecked")
	public JgcsEO findByYngyjg(String yngyjg) {
		final String queryString = "select model from JgcsEO model where model.yngyjg= :yngyjg";
		Query query = entityManager.createQuery(queryString);
		query.setParameter("yngyjg", yngyjg);
		List<JgcsEO> list = query.getResultList();
		if (list == null || list.isEmpty()) {
			return null;
		}
		return list.get(0);
	}

	@SuppressWarnings("unchecked")
	public List<JgcsEO> findChildren(String yngyjg) {
		final String queryString = "select model from JgcsEO model where model.zngwsj= :zngwsj";
		Query query = entityManager.createQuery(queryString);
		query.setParameter("zngwsj", yngyjg);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<JgcsEO> findAll() {
		final String queryString = "select model from JgcsEO model";
		Query query = entityManager.createQuery(queryString);
		return query.getResultList();
	}
}