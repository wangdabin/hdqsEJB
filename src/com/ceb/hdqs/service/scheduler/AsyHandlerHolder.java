package com.ceb.hdqs.service.scheduler;

import com.ceb.hdqs.action.asyn.AsynHandlerPool;
import com.ceb.hdqs.action.asyn.AsynQueryHandler;

public final class AsyHandlerHolder {
	private static Object lock = new Object();
	private volatile static AsyHandlerHolder instance = null;
	private AsynQueryHandler handler = null;

	private AsyHandlerHolder() {

	}

	public static AsyHandlerHolder getInstance() {
		if (instance == null) {
			synchronized (lock) {
				if (instance == null) {
					instance = new AsyHandlerHolder();
				}
			}
		}
		return instance;
	}

	public AsynQueryHandler getHandler() {
		return handler;
	}

	public void setHandler(AsynHandlerPool pool) {
		this.handler = new AsynQueryHandler(pool);
	}
}