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
public class Handle0771Cache extends AbstractCache {
	private static final Logger log = Logger.getLogger(Handle0771Cache.class);
	private static final Handle0771Cache instance = new Handle0771Cache();

	private final ConcurrentHashMap<String, C0771RowkeyItem> cache = new ConcurrentHashMap<String, C0771RowkeyItem>();

	private Handle0771Cache() {

	}

	public static Handle0771Cache getInstance() {
		return instance;
	}

	public void process() {
		Set<String> keySet = new HashSet<String>(cache.keySet());

		long currentT = System.currentTimeMillis();
		for (String key : keySet) {
			C0771RowkeyItem obj = cache.get(key);
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
			C0771RowkeyItem obj = cache.get(key);

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

	public C0771RowkeyItem get(String key) {
		return cache.get(key);
	}

	public void put(String key, C0771RowkeyItem value) {
		cache.put(key, value);
	}
}