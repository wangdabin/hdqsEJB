package com.ceb.hdqs.service.cache;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.ceb.bank.context.RowkeyAndTblContext;

public class C0781RowkeyItem extends AbstractRowkeyItem {
	/**
	 * 页码与RowKey映射关系
	 */
	private ConcurrentHashMap<Integer, List<RowkeyAndTblContext>> cache = new ConcurrentHashMap<Integer, List<RowkeyAndTblContext>>();

	private String kehhao;
	private String kehuzh;
	private String gerzwm;
	private Long bishuu = 0L;

	public ConcurrentHashMap<Integer, List<RowkeyAndTblContext>> getCache() {
		return cache;
	}

	public void setCache(ConcurrentHashMap<Integer, List<RowkeyAndTblContext>> cache) {
		this.cache = cache;
	}

	public String getKehhao() {
		return kehhao;
	}

	public void setKehhao(String kehhao) {
		this.kehhao = kehhao;
	}

	public String getKehuzh() {
		return kehuzh;
	}

	public void setKehuzh(String kehuzh) {
		this.kehuzh = kehuzh;
	}

	public String getGerzwm() {
		return gerzwm;
	}

	public void setGerzwm(String gerzwm) {
		this.gerzwm = gerzwm;
	}

	public Long getBishuu() {
		return bishuu;
	}

	public void setBishuu(Long bishuu) {
		this.bishuu = bishuu;
	}
}