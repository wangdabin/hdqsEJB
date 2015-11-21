package com.ceb.hdqs.service;

import java.util.List;
import java.util.Map;

import javax.ejb.Local;

import com.ceb.hdqs.entity.PybjyEO;

@Local
public interface IYbjyService {
	public PybjyEO add(PybjyEO entity);

	public void batchAddRecords(List<PybjyEO> list);

	public void update(PybjyEO entity);

	public void batchUpdateRecords(List<PybjyEO> list);

	public void updateNotifyFlag(String handleNo);

	public void resetMasterSynStatus();

	public void resetStandbySynStatus();

	public void resetRunStatus();

	/**
	 * 根据受理编号获取交易记录对象
	 * 
	 * @param handleNo受理编号
	 * @return交易记录对象
	 */
	public List<PybjyEO> findByHandleNo(String handleNo) throws Exception;

	public PybjyEO findById(Long id);

	public boolean isFinish();

	public void synchronizeRecords() throws HdqsServiceException;

	public void rmSynchronizedRecords() throws HdqsServiceException;

	public long getUnCompleteCountsByHandleNo(String handleNo);

	public Map<String, List<PybjyEO>> queryUnhandleRecords();

	public Map<String, List<PybjyEO>> queryUnhandleRecordsBySlbhao();
}