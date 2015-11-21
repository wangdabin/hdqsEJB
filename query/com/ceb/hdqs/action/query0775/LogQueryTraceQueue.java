package com.ceb.hdqs.action.query0775;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class LogQueryTraceQueue {
	private static final Log logger = LogFactory.getLog(LogQueryTraceQueue.class);

	private final ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<String>();

	public LogQueryTraceQueue() {
	}

	/**
	 * 添加
	 * 
	 * @param workItem
	 */
	public final void enqueue(List<String> map) {
		try {
			queue.addAll(map);
		} catch (NullPointerException e) {
			logger.info("放入queue的Value为空值");
		} catch (Exception e1) {
			logger.error(e1.getMessage(), e1);
		}
	}

	public List<String> loopSwitch() {
		List<String> map = new ArrayList<String>();
		if (!queue.isEmpty()) {
			map.addAll(queue);
			queue.clear();
		}
		return map;
	}
}