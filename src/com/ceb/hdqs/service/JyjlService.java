package com.ceb.hdqs.service;

import java.util.List;

import javax.ejb.Remote;

import com.ceb.hdqs.entity.PjyjlEO;
import com.ceb.hdqs.wtc.form.Handle0775Form;

@Remote
public interface JyjlService {
	public PjyjlEO save(PjyjlEO entity);

	public void update(PjyjlEO entity);

	public void updateRunningStatus(Long id, Boolean runSucc);

	public void resetMasterSynStatus();

	public void resetStandbySynStatus();

	public int getCounts(Handle0775Form form, String excludeCode) throws HdqsServiceException;

	public int getCounts(Handle0775Form form, String excludeCode, List<String> list) throws HdqsServiceException;

	public List<PjyjlEO> findByProperty(Handle0775Form form, String excludeCode) throws HdqsServiceException;

	public List<PjyjlEO> findByProperty(Handle0775Form form, String excludeCode, List<String> list)
			throws HdqsServiceException;

	public List<PjyjlEO> findByProperty(Handle0775Form form, String excludeCode, int start, int limit)
			throws HdqsServiceException;

	public List<PjyjlEO> findAll();

	public boolean isFinish();

	public void synchronizeRecords() throws HdqsServiceException;

	public void rmSynchronizedRecords() throws HdqsServiceException;

}