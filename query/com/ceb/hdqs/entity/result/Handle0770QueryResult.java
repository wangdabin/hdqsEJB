/*
 * (C) Copyright  2013  BeagleData Corporation, All Rights Reserved.            
 */
package com.ceb.hdqs.entity.result;

import java.io.Serializable;

import com.ceb.hdqs.query.utils.QueryMethodUtils;

/**
 * 对公查询结果，每个账户对应一个对象
 * 
 * @author user
 * 
 */
public class Handle0770QueryResult extends AbstractQueryResult implements Serializable {
	private static final long serialVersionUID = 8648182704548562182L;

	private Long totalNum;// 交易总笔数
	private String huobdh;// 货币代号
	private String kehzwm;// 客户中文名
	private String zhangh;

	public Long getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(Long totalNum) {
		this.totalNum = totalNum;
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

	public String getZhangh() {
		return zhangh;
	}

	public void setZhangh(String zhangh) {
		this.zhangh = zhangh;
	}

	@Override
	public String parseToString(String huobdh) {
		StringBuilder buffer = new StringBuilder();
		buffer.append(QueryMethodUtils.formatString(21, this.getZhangh())).append(QueryMethodUtils.formatString(20, this.getHuobdh()))
				.append(QueryMethodUtils.formatString(80, this.getKehzwm())).append(QueryMethodUtils.formatString(10, this.getTotalNum() + ""));

		return buffer.toString();
	}
}
