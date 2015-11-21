package com.ceb.hdqs.dao;

import java.util.List;

import javax.ejb.Local;

import com.ceb.hdqs.entity.PybjyEO;
import com.ceb.hdqs.service.HdqsServiceException;

@Local
public interface YbjyDao {
	public PybjyEO save(PybjyEO entity);

	public void delete(long id);

	public PybjyEO update(PybjyEO entity);

	public void updateNotifyStatus(String slbhao);

	public void updateMasterSynStatus(Long id, String status);

	public void updateStandbySynStatus(Long id, String status);

	public void batchUpdateMasterSynStatus(List<Long> ids, String status);

	public void batchUpdateStandbySynStatus(List<Long> ids, String status);

	public void resetMasterSynStatus();

	public void resetStandbySynStatus();

	public void resetRunStatus();

	public PybjyEO findRecordById(long id);

	public List<PybjyEO> findMasterRecordsBySlbhao(String slbhao, int startIdx, int count);

	public List<PybjyEO> findSlaveRecordsBySlbhao(String slbhao, int startIdx, int count);

	public List<PybjyEO> findRecordsBySlbhao(String slbhao, int startIdx, int count);

	public List<PybjyEO> findUnhandleRecords(int startIdx, int count);

	public List<String> findUnhandleSlbhaos(int startIdx, int count);

	public List<PybjyEO> findExpiredRecords(long expiredT, int startIdx, int count);

	public List<PybjyEO> findNeedSynchronizeRecords(long expiredT, boolean isMaster, int startIdx, int count);

	public List<PybjyEO> findAll();

	public long getUnCompleteCountsBySlbhao(String slbhao);

	public void rmSynchronizedRecords() throws HdqsServiceException;
}