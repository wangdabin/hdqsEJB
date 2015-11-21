package com.ceb.hdqs.service.impl;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.commons.lang.StringUtils;

import com.ceb.hdqs.config.PropertiesLoader;
import com.ceb.hdqs.config.RegisterTable;
import com.ceb.hdqs.dao.ClokDao;
import com.ceb.hdqs.entity.ClokEO;
import com.ceb.hdqs.service.ClokService;
import com.ceb.hdqs.service.IClokService;

@Stateless(mappedName = "ClokService")
public class ClokServiceImpl implements IClokService, ClokService {
	@EJB
	ClokDao referDao;

	public boolean acquireLock(String cronName) {
		PropertiesLoader instance = PropertiesLoader.getInstance();
		String hostname = instance.getProperty(RegisterTable.LOCAL_CLOCK_NAME);
		try {
			referDao.acquireLock(cronName, hostname);
		} catch (Exception e) {
			// version冲突
			return false;
		}
		ClokEO lockObj = referDao.findByName(cronName);
		if (lockObj == null || StringUtils.isBlank(lockObj.getHostname())) {
			// 锁对象不存在
			return false;
		}
		if (hostname.equals(lockObj.getHostname())) {
			// 本机器获取到锁
			return true;
		}
		// 其它机器获取到锁，判断处理是否超时
		if (PropertiesLoader.overflowMinuteLimit(lockObj.getShjnch())) {
			return forceLock(cronName);
		}
		return false;
	}

	public boolean forceLock(String cronName) {
		PropertiesLoader instance = PropertiesLoader.getInstance();
		String hostname = instance.getProperty(RegisterTable.LOCAL_CLOCK_NAME);
		try {
			referDao.forceLock(cronName, hostname);
		} catch (Exception e) {
			// version冲突
			return false;
		}
		ClokEO lockObj = referDao.findByName(cronName);
		if (lockObj == null || StringUtils.isBlank(lockObj.getHostname())) {
			// 锁对象不存在
			return false;
		}
		if (hostname.equals(lockObj.getHostname())) {
			// 本机器获取到锁
			return true;
		}

		return false;
	}

	public void releaseLock(String cronName) {
		ClokEO lockObj = referDao.findByName(cronName);
		lockObj.setStatus(ClokEO.STATUS_INIT);
		lockObj.setShjnch(System.currentTimeMillis());
		referDao.update(lockObj);
	}
}