package com.ceb.bank.result;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ceb.bank.context.RowkeyContext;
import com.ceb.hdqs.po.Authorize;

/**
 * 0770查询返回结果
 * 
 */
public class Query0770Result {
	/*
	 * 存储查询任务的授权级别和说明
	 */
	private Authorize authorize = new Authorize();
	private Map<Integer, List<RowkeyContext>> pageCache = new HashMap<Integer, List<RowkeyContext>>();// 分页映射信息
	private Long bishuu = 0L;
	private String zhangh;
	private String huobdh;
	private String kehzwm;
	private String tableName;

	public Authorize getAuthorize() {
		return authorize;
	}

	public void setAuthorize(Authorize authorize) {
		this.authorize = authorize;
	}

	public Map<Integer, List<RowkeyContext>> getPageCache() {
		return pageCache;
	}

	public void setPageCache(Map<Integer, List<RowkeyContext>> pageCache) {
		this.pageCache = pageCache;
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