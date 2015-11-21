package com.ceb.hdqs.action.query.parser;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ceb.hdqs.entity.result.KehzhParserResult;

/**
 * 客户账号解析使用的多线程队列
 * 
 * @author user
 * 
 */
public class KhzhParseTraceQueue {
	private static final Log logger = LogFactory.getLog(KhzhParseTraceQueue.class);

	private final ConcurrentMap<String, KehzhParserResult> queue = new ConcurrentHashMap<String, KehzhParserResult>();

	public KhzhParseTraceQueue() {
	}

	/**
	 * 添加
	 * 
	 * @param workItem
	 */
	public final void enqueue(String key, KehzhParserResult list) {
		try {
			queue.putIfAbsent(key, list);
		} catch (NullPointerException e) {
			logger.info("放入queue的Value为空值");
		} catch (Exception e1) {
			logger.error(e1.getMessage(), e1);
		}
	}

	public Map<String, KehzhParserResult> loopSwitch() {
		Map<String, KehzhParserResult> map = null;
		if (!queue.isEmpty()) {
			map = new HashMap<String, KehzhParserResult>();
			map.putAll(queue);
			queue.clear();
		}
		return map;
	}

}
