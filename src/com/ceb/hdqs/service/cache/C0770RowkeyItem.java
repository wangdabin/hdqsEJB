package com.ceb.hdqs.service.cache;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.ceb.bank.context.RowkeyContext;

public class C0770RowkeyItem extends AbstractRowkeyItem {
	/**
	 * 页码与RowKey映射关系
	 */
	private ConcurrentHashMap<Integer, List<RowkeyContext>> cache = new ConcurrentHashMap<Integer, List<RowkeyContext>>();
	private Long bishuu = 0L;
	private String zhangh;
	private String huobdh;
	private String kehzwm;
	private String tableName;

	public ConcurrentHashMap<Integer, List<RowkeyContext>> getCache() {
		return cache;
	}

	public void setCache(ConcurrentHashMap<Integer, List<RowkeyContext>> cache) {
		this.cache = cache;
	}

	public Long getBishuu() {
		return bishuu;
	}

	public void setBishuu(Long bishuu) {
		this.bishuu = bishuu;
	}

	public String getZhangh() {
		return zhangh;
	}

	public void setZhangh(String zhangh) {
		this.zhangh = zhangh;
	}

	public String getHuobdh() {
		return huobdh;
	}

	public void setHuobdh(String huobdh) {
		this.huobdh = huobdh;
	}

	public String getKehzwm() {
		return kehzwm;
	}

	public void setKehzwm(String kehzwm) {
		this.kehzwm = kehzwm;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
}