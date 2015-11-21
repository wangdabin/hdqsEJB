package com.ceb.hdqs.service.cache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.ceb.hdqs.entity.result.ReplacementCardItemResult;
import com.ceb.hdqs.entity.result.Handle0773QueryResult;

public class C0773RowkeyItem extends AbstractRowkeyItem {

	private Handle0773QueryResult result = new Handle0773QueryResult();
	private List<ReplacementCardItemResult> ascList = Collections.synchronizedList(new ArrayList<ReplacementCardItemResult>());

	public Handle0773QueryResult getResult() {
		return result;
	}

	public void setResult(Handle0773QueryResult result) {
		this.result = result;
	}

	public List<ReplacementCardItemResult> getAscList() {
		return ascList;
	}

	public void setAscList(List<ReplacementCardItemResult> ascList) {
		this.ascList = ascList;
	}
}