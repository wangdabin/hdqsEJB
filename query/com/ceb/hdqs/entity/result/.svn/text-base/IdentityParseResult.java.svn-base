package com.ceb.hdqs.entity.result;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.ceb.hdqs.query.entity.EIdentityType;
import com.ceb.hdqs.query.entity.Page;
import com.ceb.hdqs.query.utils.QueryConstants;

/**
 * 客户证件解析结果
 * 
 * @author user
 * 
 */
public class IdentityParseResult extends ParseResult {

	private String identityNum;
	private EIdentityType identityType;
   private Map<String, KehhaoParseResult> identityParseResult = new HashMap<String, KehhaoParseResult>();
    
	public Map<String, KehhaoParseResult> getIdentityParseResult() {
	return identityParseResult;
}

public void setIdentityParseResult(Map<String, KehhaoParseResult> identityParseResult) {
	this.identityParseResult = identityParseResult;
}

	public String getIdentityNum() {
		return identityNum;
	}

	public void setIdentityNum(String identityNum) {
		this.identityNum = identityNum;
	}

	public EIdentityType getIdentityType() {
		return identityType;
	}

	public void setIdentityType(EIdentityType identityType) {
		this.identityType = identityType;
	}


	@Override
	public String parseToString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<? extends AbstractQueryResult> nextPage(long queryNum) throws IOException, Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String parseToString(String huobdh) {
		// TODO Auto-generated method stub
		return null;
	}

	public int getFinished() {
		for (Entry<String, KehhaoParseResult> kehhaoInfo : identityParseResult.entrySet()) {
			if (kehhaoInfo.getValue().getFinished() != QueryConstants.ZHANGH_QUERY_FINISHED) {
				return QueryConstants.ZHANGH_QUERY_NO_FINISHED;
			}
		}
		return QueryConstants.ZHANGH_QUERY_FINISHED;
	}

	@Override
	public boolean isKuaJG() {
		if (identityParseResult.isEmpty())
			return false;
		for (Entry<String, KehhaoParseResult> khhaoParseResult : identityParseResult.entrySet()) {
			if (!khhaoParseResult.getValue().isKuaJG()) {
				return false;
			}
		}
		return true;
	}

}
