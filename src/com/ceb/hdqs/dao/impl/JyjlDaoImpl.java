package com.ceb.hdqs.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.ceb.hdqs.config.PropertiesLoader;
import com.ceb.hdqs.constants.HdqsConstants;
import com.ceb.hdqs.dao.JyjlDao;
import com.ceb.hdqs.entity.PjyjlEO;
import com.ceb.hdqs.service.HdqsServiceException;

@Stateless
public class JyjlDaoImpl implements JyjlDao {

	@PersistenceContext
	private EntityManager entityManager;

	public PjyjlEO save(PjyjlEO entity) {
		entityManager.persist(entity);
		return entity;
	}

	public void delete(Long id) {
		PjyjlEO entity = entityManager.getReference(PjyjlEO.class, id);
		entityManager.remove(entity);
	}

	public PjyjlEO update(PjyjlEO entity) {
		PjyjlEO result = entityManager.merge(entity);
		return result;
	}

	public void updateRunningStatus(Long id, Boolean runSucc) {
		long currentT = System.currentTimeMillis();
		PjyjlEO entity = findRecordById(id);
		entity.setRunSucc(runSucc);
		entity.setEndTime(currentT);
		update(entity);
	}

	public void updateMasterSynStatus(Long id, String status) {
		PjyjlEO entity = findRecordById(id);
		entity.setMasterSyn(status);
		update(entity);
	}

	public void updateStandbySynStatus(Long id, String status) {
		PjyjlEO entity = findRecordById(id);
		entity.setStandbySyn(status);
		update(entity);
	}

	public void updateMasterSynStatus(List<Long> list, String status) {
		for (Long id : list) {
			updateMasterSynStatus(id, status);
		}
	}

	public void updateStandbySynStatus(List<Long> list, String status) {
		for (Long id : list) {
			updateStandbySynStatus(id, status);
		}
	}

	public void resetMasterSynStatus() {
		String updateSql = "update PJYJL set MASTERSYN=" + HdqsConstants.SYN_STATUS_INIT + " where MASTERSYN="
				+ HdqsConstants.SYN_STATUS_RUNNING;
		Query query = entityManager.createNativeQuery(updateSql);
		query.executeUpdate();
	}

	public void resetStandbySynStatus() {
		String updateSql = "update PJYJL set STANDBYSYN=" + HdqsConstants.SYN_STATUS_INIT + " where STANDBYSYN="
				+ HdqsConstants.SYN_STATUS_RUNNING;
		Query query = entityManager.createNativeQuery(updateSql);
		query.executeUpdate();
	}

	public PjyjlEO findRecordById(Long id) {
		PjyjlEO instance = entityManager.find(PjyjlEO.class, id);
		return instance;
	}

	public int getCounts(final String queryStr) {
		Query query = entityManager.createQuery("select count(model) " + queryStr);
		int count = ((Long) query.getResultList().get(0)).intValue();
		return count;
	}

	@SuppressWarnings("unchecked")
	public List<PjyjlEO> findByProperty(final String queryStr, int start, int limit) {
		Query query = entityManager.createQuery("select model " + queryStr);
		int rowStartIdx = Math.max(0, start);
		if (rowStartIdx > 0) {
			query.setFirstResult(rowStartIdx);
		}

		int rowCount = Math.max(0, limit);
		if (rowCount > 0) {
			query.setMaxResults(rowCount);
		}
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<PjyjlEO> findAll(int start, int limit) {
		final String queryString = "select model from PjyjlEO model";
		Query query = entityManager.createQuery(queryString);
		int rowStartIdx = Math.max(0, start);
		if (rowStartIdx > 0) {
			query.setFirstResult(rowStartIdx);
		}

		int rowCount = Math.max(0, limit);
		if (rowCount > 0) {
			query.setMaxResults(rowCount);
		}
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<PjyjlEO> findExpiredRecords(long expiredT, int start, int limit) {
		final String queryString = "select model from PjyjlEO model where model.endTime< :expiredT";
		Query query = entityManager.createQuery(queryString);
		query.setParameter("expiredT", expiredT);
		int rowStartIdx = Math.max(0, start);
		if (rowStartIdx > 0) {
			query.setFirstResult(rowStartIdx);
		}

		int rowCount = Math.max(0, limit);
		if (rowCount > 0) {
			query.setMaxResults(rowCount);
		}
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<PjyjlEO> findExpiredRecords(long expiredT, boolean isMaster, int start, int limit) {
		StringBuffer buffer = new StringBuffer("select model from PjyjlEO model where");
		if (isMaster) {
			buffer.append(" model.masterSyn=" + HdqsConstants.SYN_STATUS_INIT);
		} else {
			buffer.append(" model.standbySyn=" + HdqsConstants.SYN_STATUS_INIT);
		}
		buffer.append(" and model.endTime< :expiredT");

		Query query = entityManager.createQuery(buffer.toString());
		query.setParameter("expiredT", expiredT);
		int rowStartIdx = Math.max(0, start);
		if (rowStartIdx > 0) {
			query.setFirstResult(rowStartIdx);
		}

		int rowCount = Math.max(0, limit);
		if (rowCount > 0) {
			query.setMaxResults(rowCount);
		}
		return query.getResultList();
	}

	public void rmSynchronizedRecords() throws HdqsServiceException {
		String[] namenodeArray = PropertiesLoader.getClusterOrder();
		String updateSql = null;
		if (namenodeArray.length == 1) {
			updateSql = "delete from PJYJL where MASTERSYN=" + HdqsConstants.SYN_STATUS_SUCCESS;
		} else {
			updateSql = "delete from PJYJL where MASTERSYN=" + HdqsConstants.SYN_STATUS_SUCCESS + " and STANDBYSYN="
					+ HdqsConstants.SYN_STATUS_SUCCESS;
		}

		entityManager.createNativeQuery(updateSql).executeUpdate();
	}
}