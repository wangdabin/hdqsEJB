package com.ceb.hdqs.service.cache;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.ceb.hdqs.entity.PjyjlEO;

/**
 * 单例模式
 * 
 * 
 */
public class Handle0775Cache extends AbstractCache {
	private static final Logger log = Logger.getLogger(Handle0775Cache.class);
	private static final Handle0775Cache instance = new Handle0775Cache();

	private final ConcurrentHashMap<String, C0775RowkeyItem> cache = new ConcurrentHashMap<String, C0775RowkeyItem>();

	private Handle0775Cache() {

	}

	public static Handle0775Cache getInstance() {
		return instance;
	}

	public void process() {
		Set<String> keySet = new HashSet<String>(cache.keySet());

		long currentT = System.currentTimeMillis();
		for (String key : keySet) {
			C0775RowkeyItem obj = cache.get(key);
			if (isExpired(currentT, obj.getLastModifiedT())) {
				log.info(key + "  expired,remove it.");

				Map<Integer, List<PjyjlEO>> itemCache = obj.getCache();
				Set<Integer> itemSet = new HashSet<Integer>(itemCache.keySet());
				for (Integer bishu : itemSet) {
					itemCache.get(bishu).clear();
				}
				itemCache.clear();

				Map<Integer, File> fCache = obj.getfCache();
				Set<Integer> fSet = new HashSet<Integer>(fCache.keySet());
				for (Integer bishu : fSet) {
					File file = fCache.get(bishu);
					file.delete();
				}
				fCache.clear();
				cache.remove(key);
			}
		}
	}

	public void forceProcess() {
		for (String key : cache.keySet()) {
			C0775RowkeyItem obj = cache.get(key);
			Map<Integer, List<PjyjlEO>> itemCache = obj.getCache();
			Set<Integer> itemSet = new HashSet<Integer>(itemCache.keySet());
			for (Integer bishu : itemSet) {
				itemCache.get(bishu).clear();
			}
			itemCache.clear();

			Map<Integer, File> fCache = obj.getfCache();
			Set<Integer> fSet = new HashSet<Integer>(fCache.keySet());
			for (Integer bishu : fSet) {
				File file = fCache.get(bishu);
				file.delete();
			}
			fCache.clear();
		}
		cache.clear();
	}

	public boolean containsKey(String key) {
		return cache.containsKey(key);
	}

	public C0775RowkeyItem get(String key) {
		return cache.get(key);
	}

	public void put(String key, C0775RowkeyItem value) {
		cache.put(key, value);
	}
}