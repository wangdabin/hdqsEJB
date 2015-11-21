package com.ceb.bank.context;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ZhjhaoContext implements Serializable {
	private static final long serialVersionUID = -8356230204328239597L;

	private List<KehuhaoContext> list = new ArrayList<KehuhaoContext>();

	public List<KehuhaoContext> getList() {
		return list;
	}

	public void setList(List<KehuhaoContext> list) {
		this.list = list;
	}
}