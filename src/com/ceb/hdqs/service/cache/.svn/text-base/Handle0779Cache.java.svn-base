package com.ceb.hdqs.service.cache;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.ceb.hdqs.entity.PybjyEO;

/**
 * 单例模式
 * 
 * 
 */
public class Handle0779Cache extends AbstractCache {
	private static final Logger log = Logger.getLogger(Handle0779Cache.class);

	private static final Handle0779Cache instance = new Handle0779Cache();

	private final ConcurrentHashMap<String, C0779RowkeyItem> cache = new ConcurrentHashMap<String, C0779RowkeyItem>();

	private Handle0779Cache() {

	}

	public static Handle0779Cache getInstance() {
		return instance;
	}

	public void process() {
		Set<String> keySet = new HashSet<String>(cache.keySet());

		long currentT = System.currentTimeMillis();
		for (String key : keySet) {
			C0779RowkeyItem obj = cache.get(key);
			if (isExpired(currentT, obj.getLastModifiedT())) {
				log.info(key + "  expired,remove it.");

				List<PybjyEO> ascList = obj.getAscList();
				ascList.clear();
				cache.remove(key);
			}
		}
	}

	public void forceProcess() {
		for (String key : cache.keySet()) {
			log.info(key + "  force remove.");

			C0779RowkeyItem obj = cache.get(key);
			List<PybjyEO> ascList = obj.getAscList();
			ascList.clear();
		}
		cache.clear();
	}

	public boolean containsKey(String key) {
		return cache.containsKey(key);
	}

	public C0779RowkeyItem get(String key) {
		return cache.get(key);
	}

	public void put(String key, C0779RowkeyItem value) {
		cache.put(key, value);
	}
}