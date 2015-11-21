package com.ceb.hdqs.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.ceb.hdqs.action.query0779.HandleQuery0779;
import com.ceb.hdqs.action.synchronize.PybjySynchronizeProcessor;
import com.ceb.hdqs.config.ConfigLoader;
import com.ceb.hdqs.config.PropertiesLoader;
import com.ceb.hdqs.config.RegisterTable;
import com.ceb.hdqs.constants.HdqsConstants;
import com.ceb.hdqs.dao.YbjyDao;
import com.ceb.hdqs.entity.PybjyEO;
import com.ceb.hdqs.query.utils.QueryConfUtils;
import com.ceb.hdqs.service.HdqsServiceException;
import com.ceb.hdqs.service.IJyrqService;
import com.ceb.hdqs.service.IYbjyService;
import com.ceb.hdqs.service.YbjyComprator;
import com.ceb.hdqs.service.YbjyService;

@Stateless(mappedName = "YbjyService")
public class YbjyServiceImpl implements IYbjyService, YbjyService {
	private static final Logger log = Logger.getLogger(YbjyServiceImpl.class);
	private volatile static boolean isFinish = true;
	private static final int ERR_MSG_SIZE = 500;

	@EJB
	YbjyDao referDao;
	@EJB
	IJyrqService jyrqService;

	public PybjyEO add(PybjyEO entity) {
		String createdT = jyrqService.queryJyrq();
		long currentT = System.currentTimeMillis();

		handleAddRecord(createdT, currentT, entity);
		return referDao.save(entity);
	}

	/**
	 * fatal user error> org.apache.openjpa.persistence.ArgumentException:
	 * Attempt to assign id "com.ceb.hdqs.entity.AsyQueryRecord-0" to new
	 * instance
	 * "com.ceb.hdqs.entity.AsyQueryRecord-com.ceb.hdqs.entity.AsyQueryRecord-0"
	 * failed; there is already an object in the L1 cache with this id. You must
	 * delete this object (in a previous transaction or the current one) before
	 * reusing its id. This error can also occur when a horizontally or
	 * vertically mapped classes uses auto-increment application identity and
	 * does not use a hierarchy of application identity classes.
	 * EntityBean没有配置序列
	 */
	public void batchAddRecords(List<PybjyEO> list) {
		String createdT = jyrqService.queryJyrq();
		long currentT = System.currentTimeMillis();
		for (PybjyEO entity : list) {
			handleAddRecord(createdT, currentT, entity);
			referDao.save(entity);
		}
	}

	private void handleAddRecord(String createdT, long currentT, PybjyEO entity) {
		entity.setJioyrq(createdT);
		entity.setStartTime(currentT);
		entity.setEndTime(currentT);
		entity.setIsAsyn(Boolean.TRUE);
		entity.setShfobz(HdqsConstants.SHFOBZ_DOWNLOAD);
		entity.setItemCount(0L);// 防止在后台是否异步处理中有遍历计数赋值
		entity.setCommNum(0);
	}

	public void update(PybjyEO entity) {
		long currentT = System.currentTimeMillis();
		handleUpdateRecord(entity, currentT);
		referDao.update(entity);
	}

	public void batchUpdateRecords(List<PybjyEO> list) {
		long currentT = System.currentTimeMillis();
		for (PybjyEO record : list) {
			handleUpdateRecord(record, currentT);
			referDao.update(record);
		}
	}

	private void handleUpdateRecord(PybjyEO entity, long currentT) {
		String errMsg = entity.getErrMsg();
		if (StringUtils.isNotBlank(errMsg)) {
			if (errMsg.length() > ERR_MSG_SIZE) {
				errMsg = errMsg.substring(0, ERR_MSG_SIZE - 3) + "...";
			}
			entity.setErrMsg(errMsg);
		}

		entity.setEndTime(currentT);
	}

	public void updateNotifyFlag(String handleNo) {
		referDao.updateNotifyStatus(handleNo);
	}

	public void resetMasterSynStatus() {
		referDao.resetMasterSynStatus();
	}

	public void resetStandbySynStatus() {
		referDao.resetStandbySynStatus();
	}

	public void resetRunStatus() {
		referDao.resetRunStatus();
	}

	public List<PybjyEO> findByHandleNo(String handleNo) throws Exception {
		// load data from hbase
		HandleQuery0779 asynQuery = new HandleQuery0779();
		List<PybjyEO> list1 = asynQuery.query(handleNo);

		// 根据当前活动的master,来查询本地相应未同步的记录
		List<PybjyEO> list2 = null;
		String[] namenodeArray = PropertiesLoader.getClusterOrder();
		if (namenodeArray.length == 1) {
			list2 = referDao.findMasterRecordsBySlbhao(handleNo, 0, 0);
		} else {
			// master is active
			if (namenodeArray[0].trim().equals(QueryConfUtils.getActiveClusterFlag())) {
				list2 = referDao.findMasterRecordsBySlbhao(handleNo, 0, 0);
			} else {
				list2 = referDao.findSlaveRecordsBySlbhao(handleNo, 0, 0);
			}
		}

		List<PybjyEO> list = new ArrayList<PybjyEO>();
		if (list1 != null && list1.size() > 0)
			list.addAll(list1);
		if (list2 != null && list2.size() > 0)
			list.addAll(list2);
		Collections.sort(list, new YbjyComprator());
		return list;
	}

	public PybjyEO findById(Long id) {
		return referDao.findRecordById(id);
	}

	public boolean isFinish() {
		return isFinish;
	}

	public void synchronizeRecords() throws HdqsServiceException {
		isFinish = false;
		String prop = ConfigLoader.getInstance().getProperty(RegisterTable.DB_SYNCHRONIZE_HOURS_BEFORE_THRESHOLD);
		long interval = TimeUnit.HOURS.toMillis(1);
		if (StringUtils.isNotBlank(prop)) {
			interval = TimeUnit.HOURS.toMillis(Integer.valueOf(prop));
		}
		String batchSize = ConfigLoader.getInstance().getProperty(RegisterTable.SYN_HANDLE_BATCH_SIZE, "100");

		long expiredT = System.currentTimeMillis() - interval;
		List<PybjyEO> masteList = referDao.findNeedSynchronizeRecords(expiredT, true, 0, Integer.parseInt(batchSize));

		String[] namenodeArray = PropertiesLoader.getClusterOrder();
		if (namenodeArray.length == 1) {
			synRecordsToActive(masteList);
		} else {
			List<PybjyEO> slaveList = referDao.findNeedSynchronizeRecords(expiredT, false, 0, Integer.parseInt(batchSize));
			// master is active
			if (namenodeArray[0].trim().equals(QueryConfUtils.getActiveClusterFlag())) {
				synRecordsToActive(masteList);
				synRecordsToStandby(slaveList);
			} else {
				synRecordsToStandby(masteList);
				synRecordsToActive(slaveList);
			}
		}
		isFinish = true;
	}

	private void synRecordsToActive(List<PybjyEO> list) {
		if (list == null || list.isEmpty()) {
			log.debug("Synchronize PYBJY to " + QueryConfUtils.getActiveClusterFlag() + " ,size 0.");
			return;
		}
		List<Long> idList = new ArrayList<Long>();
		for (PybjyEO record : list) {
			idList.add(record.getId());
		}
		log.info("Start Synchronize PYBJY to " + QueryConfUtils.getActiveClusterFlag() + " ,size " + list.size());
		referDao.batchUpdateMasterSynStatus(idList, HdqsConstants.SYN_STATUS_RUNNING);
		PybjySynchronizeProcessor processor = new PybjySynchronizeProcessor();
		try {
			List<Long> failList = processor.synchronizeToActive(list);
			List<Long> succList = new ArrayList<Long>();
			for (Long id : idList) {
				if (failList.contains(id)) {
					continue;
				} else {
					succList.add(id);
				}
			}
			referDao.batchUpdateMasterSynStatus(failList, HdqsConstants.SYN_STATUS_INIT);
			referDao.batchUpdateMasterSynStatus(succList, HdqsConstants.SYN_STATUS_SUCCESS);
			// 刘治军添加 20131118 16:06
			log.info("Synchronize PYBJY to " + QueryConfUtils.getActiveClusterFlag() + " success.");
		} catch (Exception e) {
			// 刘治军添加 20131118 16:06 如果发生异常，则将集合中的所有的记录状态更改成未同步状态.
			referDao.batchUpdateMasterSynStatus(idList, HdqsConstants.SYN_STATUS_INIT);
			log.error(e.getMessage(), e);
			log.info("Synchronize PYBJY to " + QueryConfUtils.getActiveClusterFlag() + " fail.");
		}
	}

	private void synRecordsToStandby(List<PybjyEO> list) {
		if (list == null || list.isEmpty()) {
			log.debug("Synchronize PYBJY to " + QueryConfUtils.getStandbyClusterFlag() + " ,size 0.");
			return;
		}
		List<Long> idList = new ArrayList<Long>();
		for (PybjyEO record : list) {
			idList.add(record.getId());
		}
		log.info("Start Synchronize PYBJY to " + QueryConfUtils.getStandbyClusterFlag() + " ,size " + list.size());
		referDao.batchUpdateStandbySynStatus(idList, HdqsConstants.SYN_STATUS_RUNNING);
		PybjySynchronizeProcessor processor = new PybjySynchronizeProcessor();
		try {
			List<Long> failList = processor.synchronizeToStandby(list);
			List<Long> succList = new ArrayList<Long>();
			for (Long id : idList) {
				if (failList.contains(id)) {
					continue;
				} else {
					succList.add(id);
				}
			}
			referDao.batchUpdateStandbySynStatus(failList, HdqsConstants.SYN_STATUS_INIT);
			referDao.batchUpdateStandbySynStatus(succList, HdqsConstants.SYN_STATUS_SUCCESS);
			log.info("Synchronize PYBJY to " + QueryConfUtils.getStandbyClusterFlag() + " success.");
		} catch (Exception e) {
			// 刘治军添加 20131118 16:06 如果发生异常，则将集合中的所有的记录状态更改成未同步状态.
			referDao.batchUpdateStandbySynStatus(idList, HdqsConstants.SYN_STATUS_INIT);
			log.error(e.getMessage(), e);
			log.info("Synchronize PYBJY to " + QueryConfUtils.getStandbyClusterFlag() + " fail.");
		}

	}

	public void rmSynchronizedRecords() throws HdqsServiceException {
		referDao.rmSynchronizedRecords();
	}

	public long getUnCompleteCountsByHandleNo(String handleNo) {
		return referDao.getUnCompleteCountsBySlbhao(handleNo);
	}

	// query record for asynchronize handler
	public Map<String, List<PybjyEO>> queryUnhandleRecords() {
		String prop = ConfigLoader.getInstance().getProperty(RegisterTable.ASY_HANDLE_BATCH_SIZE, "1");

		List<PybjyEO> list = referDao.findUnhandleRecords(0, Integer.parseInt(prop));
		if (list == null || list.isEmpty()) {
			return Collections.emptyMap();
		}
		Map<String, List<PybjyEO>> map = new HashMap<String, List<PybjyEO>>();
		int size = list.size();
		String currentHandleNo = null;
		String lastHandleNo = list.get(size - 1).getSlbhao();
		for (PybjyEO record : list) {
			if (record.getSlbhao().equals(lastHandleNo)) {
				List<PybjyEO> lastList = referDao.findRecordsBySlbhao(lastHandleNo, 0, 0);
				map.put(lastHandleNo, lastList);
				break;
			}
			if (!record.getSlbhao().equals(currentHandleNo)) {
				currentHandleNo = record.getSlbhao();
				List<PybjyEO> tmpList = referDao.findRecordsBySlbhao(currentHandleNo, 0, 0);
				map.put(currentHandleNo, tmpList);
			}
		}
		return map;
	}

	public Map<String, List<PybjyEO>> queryUnhandleRecordsBySlbhao() {
		String prop = ConfigLoader.getInstance().getProperty(RegisterTable.ASY_HANDLE_BATCH_SIZE, "1");

		List<String> list = referDao.findUnhandleSlbhaos(0, Integer.parseInt(prop));
		if (list == null || list.isEmpty()) {
			return Collections.emptyMap();
		}
		Map<String, List<PybjyEO>> map = new HashMap<String, List<PybjyEO>>();
		for (String slbhao : list) {
			List<PybjyEO> itemList = referDao.findRecordsBySlbhao(slbhao, 0, 0);
			map.put(slbhao, itemList);
		}
		return map;
	}
}