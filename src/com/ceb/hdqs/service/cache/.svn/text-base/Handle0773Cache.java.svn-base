package com.ceb.hdqs.service.cache;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

/**
 * 单例模式
 * 
 * 
 */
@SuppressWarnings("rawtypes")
public class Handle0773Cache extends AbstractCache {
	private static final Logger log = Logger.getLogger(Handle0773Cache.class);
	private static final Handle0773Cache instance = new Handle0773Cache();

	private final ConcurrentHashMap<String, C0773RowkeyItem> cache = new ConcurrentHashMap<String, C0773RowkeyItem>();

	private Handle0773Cache() {

	}

	public static Handle0773Cache getInstance() {
		return instance;
	}

	public void process() {
		Set<String> keySet = new HashSet<String>(cache.keySet());

		long currentT = System.currentTimeMillis();
		for (String key : keySet) {
			C0773RowkeyItem obj = cache.get(key);
			if (isExpired(currentT, obj.getLastModifiedT())) {
				log.info(key + "  expired,remove it.");

				List ascList = obj.getAscList();
				ascList.clear();
				cache.remove(key);
			}
		}
	}

	public void forceProcess() {
		for (String key : cache.keySet()) {
			log.info(key + "  force remove.");

			C0773RowkeyItem obj = cache.get(key);
			List ascList = obj.getAscList();
			ascList.clear();
		}
		cache.clear();
	}

	public boolean containsKey(String key) {
		return cache.containsKey(key);
	}

	public C0773RowkeyItem get(String key) {
		return cache.get(key);
	}

	public void put(String key, C0773RowkeyItem value) {
		cache.put(key, value);
	}
}