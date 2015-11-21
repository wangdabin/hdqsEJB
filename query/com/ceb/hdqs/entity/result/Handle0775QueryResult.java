package com.ceb.hdqs.entity.result;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.ceb.hdqs.entity.PjyjlEO;

public class Handle0775QueryResult implements Serializable {
	private static final long serialVersionUID = 1401556884846563950L;

	private List<PjyjlEO> records = new ArrayList<PjyjlEO>();// 查询结果明细
	private long itemCount;

	public List<PjyjlEO> getRecords() {
		return records;
	}

	public void setRecords(List<PjyjlEO> records) {
		this.records = records;
	}

	public long getItemCount() {
		return itemCount;
	}

	public void setItemCount(long itemCount) {
		this.itemCount = itemCount;
	}
}