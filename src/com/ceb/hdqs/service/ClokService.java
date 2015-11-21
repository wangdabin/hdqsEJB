package com.ceb.hdqs.service;

import javax.ejb.Remote;

@Remote
public interface ClokService {
	/**
	 * 获取分布式锁，存在竞争
	 */
	public boolean acquireLock(String cronName);

	/**
	 * 获取锁成功时，需要释放锁
	 */
	public void releaseLock(String cronName);
}