package com.ceb.hdqs.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.ceb.hdqs.config.PropertiesLoader;
import com.ceb.hdqs.constants.HdqsConstants;
import com.ceb.hdqs.dao.YbjyDao;
import com.ceb.hdqs.entity.PybjyEO;
import com.ceb.hdqs.service.HdqsServiceException;

@Stateless
public class YbjyDaoImpl implements YbjyDao {

	@PersistenceContext
	private EntityManager entityManager;

	public PybjyEO save(PybjyEO entity) {
		entityManager.persist(entity);
		return entity;
	}

	public void delete(long id) {
		PybjyEO entity = entityManager.getReference(PybjyEO.class, id);
		entityManager.remove(entity);
	}

	public PybjyEO update(PybjyEO entity) {
		PybjyEO result = entityManager.merge(entity);
		return result;
	}

	public void updateNotifyStatus(String handleNo) {
		String updateSql = "update PYBJY set NOTIFYSTATUS='" + HdqsConstants.NOTIFY_STATUS_SENT + "' where SLBHAO= ?1";
		Query query = entityManager.createNativeQuery(updateSql);
		query.setParameter(1, handleNo);
		query.executeUpdate();
	}

	public void updateMasterSynStatus(Long id, String status) {
		PybjyEO entity = this.findRecordById(id);
		entity.setMasterSyn(status);
		update(entity);
	}

	public void updateStandbySynStatus(Long id, String status) {
		PybjyEO entity = this.findRecordById(id);
		entity.setStandbySyn(status);
		update(entity);
	}

	public void batchUpdateMasterSynStatus(List<Long> list, String status) {
		for (Long id : list) {
			updateMasterSynStatus(id, status);
		}
	}

	public void batchUpdateStandbySynStatus(List<Long> list, String status) {
		for (Long id : list) {
			updateStandbySynStatus(id, status);
		}
	}

	public void resetMasterSynStatus() {
		String updateSql = "update PYBJY set MASTERSYN=" + HdqsConstants.SYN_STATUS_INIT + " where MASTERSYN=" + HdqsConstants.SYN_STATUS_RUNNING;
		Query query = entityManager.createNativeQuery(updateSql);
		query.executeUpdate();
	}

	public void resetStandbySynStatus() {
		String updateSql = "update PYBJY set STANDBYSYN=" + HdqsConstants.SYN_STATUS_INIT + " where STANDBYSYN=" + HdqsConstants.SYN_STATUS_RUNNING;
		Query query = entityManager.createNativeQuery(updateSql);
		query.executeUpdate();
	}

	public void resetRunStatus() {
		String updateSql = "update PYBJY set RUNSTATUS=" + HdqsConstants.RUNNING_STATUS_WAITING + ",COMMNUM=0,ITEMCOUNT=0 where RUNSTATUS=" + HdqsConstants.RUNNING_STATUS_RUNNING;
		Query query = entityManager.createNativeQuery(updateSql);
		query.executeUpdate();
	}

	public PybjyEO findRecordById(long id) {
		PybjyEO instance = entityManager.find(PybjyEO.class, id);
		return instance;
	}

	@SuppressWarnings("unchecked")
	public List<PybjyEO> findMasterRecordsBySlbhao(String slbhao, int startIdx, int count) {
		String queryString = "select model from PybjyEO model where model.slbhao= :slbhao and model.masterSyn<>'" + HdqsConstants.SYN_STATUS_SUCCESS + "'";
		Query query = entityManager.createQuery(queryString);
		query.setParameter("slbhao", slbhao);
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
	public List<PybjyEO> findSlaveRecordsBySlbhao(String slbhao, int startIdx, int count) {
		String queryString = "select model from PybjyEO model where model.slbhao= :slbhao and model.standbySyn<>'" + HdqsConstants.SYN_STATUS_SUCCESS + "'";
		Query query = entityManager.createQuery(queryString);
		query.setParameter("slbhao", slbhao);
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
	public List<PybjyEO> findUnhandleRecordsBySlbhao(String slbhao, int startIdx, int count) {
		String queryString = "select model from PybjyEO model where model.slbhao= :slbhao and model.runStatus='" + HdqsConstants.RUNNING_STATUS_WAITING + "'";
		Query query = entityManager.createQuery(queryString);
		query.setParameter("slbhao", slbhao);
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
	public List<PybjyEO> findRecordsBySlbhao(String slbhao, int startIdx, int count) {
		String queryString = "select model from PybjyEO model where model.slbhao= :slbhao";
		Query query = entityManager.createQuery(queryString);
		query.setParameter("slbhao", slbhao);
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
	public List<PybjyEO> findUnhandleRecords(int startIdx, int count) {
		String queryString = "select model from PybjyEO model where model.runStatus='" + HdqsConstants.RUNNING_STATUS_WAITING + "'";
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
	public List<String> findUnhandleSlbhaos(int startIdx, int count) {
		String queryString = "SELECT DISTINCT model.slbhao FROM PybjyEO model WHERE model.runStatus='" + HdqsConstants.RUNNING_STATUS_WAITING + "'";
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
	public List<PybjyEO> findExpiredRecords(long expiredT, int startIdx, int count) {
		final String queryString = "select model from PybjyEO model where model.endTime< :expiredT";
		Query query = entityManager.createQuery(queryString);
		query.setParameter("expiredT", expiredT);
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
	public List<PybjyEO> findNeedSynchronizeRecords(long expiredT, boolean isMaster, int startIdx, int count) {
		StringBuffer buffer = new StringBuffer("select model from PybjyEO model where model.runStatus NOT IN('" + HdqsConstants.RUNNING_STATUS_WAITING + "','"
				+ HdqsConstants.RUNNING_STATUS_RUNNING + "')");
		if (isMaster) {
			buffer.append(" and model.masterSyn=" + HdqsConstants.SYN_STATUS_INIT);
		} else {
			buffer.append(" and model.standbySyn=" + HdqsConstants.SYN_STATUS_INIT);
		}
		// 保证数据同步到Hbase
		// buffer.append(" and model.notifyStatus='" +
		// HdqsConstants.NOTIFY_STATUS_SENT + "'");
		buffer.append(" and model.endTime< :expiredT");

		Query query = entityManager.createQuery(buffer.toString());
		query.setParameter("expiredT", expiredT);
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
	public List<PybjyEO> findAll() {
		final String queryString = "select model from PybjyEO model";
		Query query = entityManager.createQuery(queryString);

		return query.getResultList();
	}

	public long getUnCompleteCountsBySlbhao(String slbhao) {
		String querySql = "select count(model) from PybjyEO model where model.slbhao= :slbhao and model.runStatus IN('" + HdqsConstants.RUNNING_STATUS_WAITING + "','"
				+ HdqsConstants.RUNNING_STATUS_RUNNING + "')";
		Query query = entityManager.createQuery(querySql);
		query.setParameter("slbhao", slbhao);
		long count = ((Long) query.getResultList().get(0)).longValue();
		return count;
	}

	public void rmSynchronizedRecords() throws HdqsServiceException {
		String[] namenodeArray = PropertiesLoader.getClusterOrder();
		String updateSql = null;
		if (namenodeArray.length == 1) {
			updateSql = "delete from PYBJY where MASTERSYN=" + HdqsConstants.SYN_STATUS_SUCCESS;
		} else {
			updateSql = "delete from PYBJY where MASTERSYN=" + HdqsConstants.SYN_STATUS_SUCCESS + " and STANDBYSYN=" + HdqsConstants.SYN_STATUS_SUCCESS;
		}

		entityManager.createNativeQuery(updateSql).executeUpdate();
	}
}