package com.ceb.hdqs.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.ceb.hdqs.action.synchronize.PjyjlSynchronizeProcessor;
import com.ceb.hdqs.config.ConfigLoader;
import com.ceb.hdqs.config.PropertiesLoader;
import com.ceb.hdqs.config.RegisterTable;
import com.ceb.hdqs.constants.HdqsConstants;
import com.ceb.hdqs.dao.JyjlDao;
import com.ceb.hdqs.entity.PjyjlEO;
import com.ceb.hdqs.query.utils.QueryConfUtils;
import com.ceb.hdqs.service.HdqsServiceException;
import com.ceb.hdqs.service.IJyjlService;
import com.ceb.hdqs.service.IJyrqService;
import com.ceb.hdqs.service.JyjlService;
import com.ceb.hdqs.utils.HdqsUtils;
import com.ceb.hdqs.wtc.form.Handle0775Form;

@Stateless(mappedName = "JyjlService")
public class JyjlServiceImpl implements IJyjlService, JyjlService {
	private static final Logger log = Logger.getLogger(JyjlServiceImpl.class);
	private volatile static boolean isFinish = true;
	@EJB
	JyjlDao referDao;
	@EJB
	IJyrqService jyrqService;

	public PjyjlEO save(PjyjlEO entity) {
		String jyrq = jyrqService.queryJyrq();

		long currentT = System.currentTimeMillis();
		entity.setJioyrq(jyrq);
		entity.setStartTime(currentT);
		entity.setEndTime(currentT);

		return referDao.save(entity);
	}

	public void update(PjyjlEO entity) {
		entity.setEndTime(System.currentTimeMillis());
		referDao.update(entity);
	}

	public void updateRunningStatus(Long id, Boolean runSucc) {
		referDao.updateRunningStatus(id, runSucc);
	}

	public void resetMasterSynStatus() {
		referDao.resetMasterSynStatus();
	}

	public void resetStandbySynStatus() {
		referDao.resetStandbySynStatus();
	}

	public int getCounts(Handle0775Form form, String excludeCode) throws HdqsServiceException {
		String buff = buildBuffer(form, excludeCode, null);
		int count = referDao.getCounts(buff);
		return count;
	}

	public int getCounts(Handle0775Form form, String excludeCode, List<String> list) throws HdqsServiceException {
		String buff = buildBuffer(form, excludeCode, list);
		int count = referDao.getCounts(buff);
		return count;
	}

	public List<PjyjlEO> findByProperty(Handle0775Form form, String excludeCode) throws HdqsServiceException {
		String buff = buildBuffer(form, excludeCode, null);
		List<PjyjlEO> list = referDao.findByProperty(buff, 0, 0);
		if (list == null) {
			return Collections.emptyList();
		}
		return list;
	}

	public List<PjyjlEO> findByProperty(Handle0775Form form, String excludeCode, List<String> list) throws HdqsServiceException {
		String buff = buildBuffer(form, excludeCode, list);
		List<PjyjlEO> result = referDao.findByProperty(buff, 0, 0);
		if (result == null) {
			return Collections.emptyList();
		}
		return result;
	}

	public List<PjyjlEO> findByProperty(Handle0775Form form, String excludeCode, int start, int limit) throws HdqsServiceException {
		String buff = buildBuffer(form, excludeCode, null);
		List<PjyjlEO> list = referDao.findByProperty(buff, start, limit);
		if (list == null) {
			return Collections.emptyList();
		}
		return list;
	}

	private String buildBuffer(Handle0775Form form, String excludeCode, List<String> list) throws HdqsServiceException {
		String buff;
		String[] namenodeArray = PropertiesLoader.getClusterOrder();
		if (namenodeArray.length == 1) {
			buff = buildQueryStr(form, excludeCode, true, list);
		} else {
			// master is active
			if (namenodeArray[0].trim().equals(QueryConfUtils.getActiveClusterFlag())) {
				buff = buildQueryStr(form, excludeCode, true, list);
			} else {
				buff = buildQueryStr(form, excludeCode, false, list);
			}
		}
		return buff;
	}

	private String buildQueryStr(Handle0775Form form, String excludeCode, boolean isMaster, List<String> list) {
		StringBuffer buff = new StringBuffer("from PjyjlEO model where 1=1");

		if (list != null && !list.isEmpty()) {
			StringBuffer buffer = new StringBuffer();
			for (int i = 0, size = list.size(); i < size; i++) {
				buffer.append("'" + list.get(i) + "',");
			}
			buffer.deleteCharAt(buffer.length() - 1);
			log.info(buffer.toString());

			buff.append(" and model.yngyjg in (" + buffer.toString() + ")");
		} else if (HdqsUtils.isNotBlank(form.getYngyjg()))
			buff.append(" and model.yngyjg='" + form.getYngyjg() + "'");
		if (HdqsUtils.isNotBlank(form.getJio1gy()))
			buff.append(" and model.jio1gy='" + form.getJio1gy() + "'");
		if (HdqsUtils.isNotBlank(form.getGuiyls()))
			buff.append(" and model.guiyls='" + form.getGuiyls() + "'");
		if (HdqsUtils.isNotBlank(form.getJiaoym())) {
			buff.append(" and model.jiaoym='" + form.getJiaoym() + "'");
		} else if (HdqsUtils.isNotBlank(excludeCode)) {
			buff.append(" and model.jiaoym<>'" + excludeCode + "'");
		}
		buff.append(" and model.jioyrq>='" + form.getQishrq() + "'");
		buff.append(" and model.jioyrq<='" + form.getZzhirq() + "'");
		if (isMaster) {
			buff.append(" and model.masterSyn<>" + HdqsConstants.SYN_STATUS_SUCCESS);
		} else {
			buff.append(" and model.standbySyn<>" + HdqsConstants.SYN_STATUS_SUCCESS);
		}
		return buff.toString();
	}

	public List<PjyjlEO> findAll() {
		List<PjyjlEO> list = referDao.findAll(0, 0);
		if (list == null) {
			return Collections.emptyList();
		}
		return list;
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
		String[] namenodeArray = PropertiesLoader.getClusterOrder();
		List<PjyjlEO> masteList = referDao.findExpiredRecords(expiredT, true, 0, Integer.parseInt(batchSize));
		if (namenodeArray.length == 1) {
			synRecordsToActive(masteList);
		} else {
			List<PjyjlEO> standbyList = referDao.findExpiredRecords(expiredT, false, 0, Integer.parseInt(batchSize));
			// master is active
			if (namenodeArray[0].trim().equals(QueryConfUtils.getActiveClusterFlag())) {
				synRecordsToActive(masteList);
				synRecordsToStandby(standbyList);
			} else {
				synRecordsToStandby(masteList);
				synRecordsToActive(standbyList);
			}
		}
		isFinish = true;
	}

	private void synRecordsToActive(List<PjyjlEO> list) {
		if (list == null || list.isEmpty()) {
			log.debug("Synchronize PJYJL to " + QueryConfUtils.getActiveClusterFlag() + " ,size 0.");
			return;
		}
		List<Long> idList = new ArrayList<Long>();
		for (PjyjlEO record : list) {
			idList.add(record.getId());
		}
		log.info("Start Synchronize PJYJL to " + QueryConfUtils.getActiveClusterFlag() + " ,size " + list.size());
		referDao.updateMasterSynStatus(idList, HdqsConstants.SYN_STATUS_RUNNING);
		PjyjlSynchronizeProcessor processor = new PjyjlSynchronizeProcessor();
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
			referDao.updateMasterSynStatus(failList, HdqsConstants.SYN_STATUS_INIT);
			referDao.updateMasterSynStatus(succList, HdqsConstants.SYN_STATUS_SUCCESS);
			log.info("Synchronize PJYJL to " + QueryConfUtils.getActiveClusterFlag() + " success.");
		} catch (Exception e) {
			// 刘治军添加 20131118 16:06 如果发生异常，则将集合中的所有的记录状态更改成未同步状态.
			referDao.updateMasterSynStatus(idList, HdqsConstants.SYN_STATUS_INIT);
			log.error(e.getMessage(), e);
			log.info("Synchronize PJYJL to " + QueryConfUtils.getActiveClusterFlag() + " fail.");
		}
	}

	private void synRecordsToStandby(List<PjyjlEO> list) {
		if (list == null || list.isEmpty()) {
			log.debug("Synchronize PJYJL to " + QueryConfUtils.getStandbyClusterFlag() + " ,size 0.");
			return;
		}
		List<Long> idList = new ArrayList<Long>();
		for (PjyjlEO record : list) {
			idList.add(record.getId());
		}
		log.info("Start Synchronize PJYJL to " + QueryConfUtils.getStandbyClusterFlag() + " ,size " + list.size());
		referDao.updateStandbySynStatus(idList, HdqsConstants.SYN_STATUS_RUNNING);
		PjyjlSynchronizeProcessor processor = new PjyjlSynchronizeProcessor();
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
			referDao.updateStandbySynStatus(failList, HdqsConstants.SYN_STATUS_INIT);
			referDao.updateStandbySynStatus(succList, HdqsConstants.SYN_STATUS_SUCCESS);
			log.info("Synchroniz PJYJL to " + QueryConfUtils.getStandbyClusterFlag() + " success.");
		} catch (Exception e) {
			// 刘治军添加 20131118 16:06 如果发生异常，则将集合中的所有的记录状态更改成未同步状态.
			referDao.updateStandbySynStatus(idList, HdqsConstants.SYN_STATUS_INIT);
			log.error(e.getMessage(), e);
			log.info("Synchroniz PJYJL to " + QueryConfUtils.getStandbyClusterFlag() + " fail.");
		}

	}

	public void rmSynchronizedRecords() throws HdqsServiceException {
		referDao.rmSynchronizedRecords();
	}
}