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
public class Handle0770Cache extends AbstractCache {
	private static final Logger log = Logger.getLogger(Handle0770Cache.class);
	private static final Handle0770Cache instance = new Handle0770Cache();

	private final ConcurrentHashMap<String, C0770RowkeyItem> cache = new ConcurrentHashMap<String, C0770RowkeyItem>();

	private Handle0770Cache() {

	}

	public static Handle0770Cache getInstance() {
		return instance;
	}

	public void process() {
		Set<String> keySet = new HashSet<String>(cache.keySet());

		long currentT = System.currentTimeMillis();
		for (String key : keySet) {
			C0770RowkeyItem obj = cache.get(key);
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
			C0770RowkeyItem obj = cache.get(key);

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

	public C0770RowkeyItem get(String key) {
		return cache.get(key);
	}

	public void put(String key, C0770RowkeyItem value) {
		cache.put(key, value);
	}
}
