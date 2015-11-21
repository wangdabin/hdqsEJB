package com.ceb.hdqs.service.cache;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.ceb.bank.context.RowkeyContext;

/**
 * 单例模式
 * 
 * 
 */
public class Handle0772Cache extends AbstractCache {
	private static final Logger log = Logger.getLogger(Handle0772Cache.class);
	private static final Handle0772Cache instance = new Handle0772Cache();

	private final ConcurrentHashMap<String, C0772RowkeyItem> cache = new ConcurrentHashMap<String, C0772RowkeyItem>();

	private Handle0772Cache() {

	}

	public static Handle0772Cache getInstance() {
		return instance;
	}

	public void process() {
		Set<String> keySet = new HashSet<String>(cache.keySet());

		long currentT = System.currentTimeMillis();
		for (String key : keySet) {
			C0772RowkeyItem obj = cache.get(key);
			if (isExpired(currentT, obj.getLastModifiedT())) {
				log.info(key + "  expired,remove it.");

				Map<Integer, List<RowkeyContext>> itemCache = obj.getCache();
				for (Integer page : itemCache.keySet()) {
					itemCache.get(page).clear();
				}
				itemCache.clear();
				cache.remove(key);
			}
		}
	}

	public void forceProcess() {
		for (String key : cache.keySet()) {
			log.info(key + "  force remove.");
			C0772RowkeyItem obj = cache.get(key);

			Map<Integer, List<RowkeyContext>> itemCache = obj.getCache();
			for (Integer page : itemCache.keySet()) {
				itemCache.get(page).clear();
			}
			itemCache.clear();
		}
		cache.clear();
	}

	public boolean containsKey(String key) {
		return cache.containsKey(key);
	}

	public C0772RowkeyItem get(String key) {
		return cache.get(key);
	}

	public void put(String key, C0772RowkeyItem value) {
		cache.put(key, value);
	}
}