package com.ceb.bank.context;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 客户账号上下文
 */
public class KehuzhContext implements Serializable {
	private static final long serialVersionUID = -700462575516456923L;

	private String kehhao;
	private String kehzwm = "";// 根据kehhao查询
	private int shfobz;// 根据kehhao查询,对私授权使用
	private int khzhjb; // 根据kehhao查询,对私授权使用
	private List<ZhanghContext> list = new ArrayList<ZhanghContext>();

	public String getKehhao() {
		return kehhao;
	}

	public void setKehhao(String kehhao) {
		this.kehhao = kehhao;
	}

	public String getKehzwm() {
		return kehzwm;
	}

	public void setKehzwm(String kehzwm) {
		this.kehzwm = kehzwm;
	}

	public int getShfobz() {
		return shfobz;
	}

	public void setShfobz(int shfobz) {
		this.shfobz = shfobz;
	}

	public int getKhzhjb() {
		return khzhjb;
	}

	public void setKhzhjb(int khzhjb) {
		this.khzhjb = khzhjb;
	}

	public List<ZhanghContext> getList() {
		return list;
	}

	public void setList(List<ZhanghContext> list) {
		this.list = list;
	}
}