package com.ceb.hdqs.service.cache;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import com.ceb.hdqs.entity.PjyjlEO;

public class C0775RowkeyItem extends AbstractRowkeyItem {
	/**
	 * 根据查询起始笔数缓存对应的记录集合
	 */
	private ConcurrentHashMap<Integer, List<PjyjlEO>> cache = new ConcurrentHashMap<Integer, List<PjyjlEO>>();
	private ConcurrentHashMap<Integer, File> fCache = new ConcurrentHashMap<Integer, File>();
	private int size;

	public ConcurrentHashMap<Integer, List<PjyjlEO>> getCache() {
		return cache;
	}

	public void setCache(ConcurrentHashMap<Integer, List<PjyjlEO>> cache) {
		this.cache = cache;
	}

	public ConcurrentHashMap<Integer, File> getfCache() {
		return fCache;
	}

	public void setfCache(ConcurrentHashMap<Integer, File> fCache) {
		this.fCache = fCache;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		Map<String, Integer> map = new TreeMap<String, Integer>();
		String[] y = map.keySet().toArray(new String[0]);
	}
}