package com.ceb.hdqs.dao;

import java.util.List;

import javax.ejb.Local;

import com.ceb.hdqs.entity.PjyjlEO;
import com.ceb.hdqs.service.HdqsServiceException;

@Local
public interface JyjlDao {
	public PjyjlEO save(PjyjlEO entity);

	public void delete(Long id);

	public PjyjlEO update(PjyjlEO entity);

	public void updateRunningStatus(Long id, Boolean runSucc);

	public void updateMasterSynStatus(Long id, String status);

	public void updateStandbySynStatus(Long id, String status);

	public void updateMasterSynStatus(List<Long> ids, String status);

	public void updateStandbySynStatus(List<Long> ids, String status);

	public void resetMasterSynStatus();

	public void resetStandbySynStatus();

	public PjyjlEO findRecordById(Long id);

	public int getCounts(final String queryStr);

	public List<PjyjlEO> findByProperty(final String queryStr, int start, int limit);

	public List<PjyjlEO> findAll(int start, int limit);

	public List<PjyjlEO> findExpiredRecords(long expiredT, int start, int limit);

	public List<PjyjlEO> findExpiredRecords(long expiredT, boolean isMaster, int start, int limit);

	public void rmSynchronizedRecords() throws HdqsServiceException;
}