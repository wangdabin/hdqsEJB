package com.ceb.hdqs.entity.result;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.ceb.hdqs.query.entity.EnumQueryKind;
import com.ceb.hdqs.query.entity.Page;
import com.ceb.hdqs.query.entity.SkyPair;
import com.ceb.hdqs.query.utils.QueryConstants;

/**
 * 客户号解析结果
 * 
 * @author user
 * 
 */
public class KehhaoParseResult extends ParseResult {

	/**
	 * 是否是本行员工
	 */
	private int shfbz;

	private int khzhjb;

	public Map<SkyPair<String, EnumQueryKind>, KehzhParserResult> getKhzhParseResult() {
		return khzhParseResult;
	}

	public void setKhzhParseResult(Map<SkyPair<String, EnumQueryKind>, KehzhParserResult> khzhParseResult) {
		this.khzhParseResult = khzhParseResult;
	}

	public int getKhzhjb() {
		return khzhjb;
	}

	public void setKhzhjb(int khzhjb) {
		this.khzhjb = khzhjb;
	}

	/**
	 * 客户号parse结果
	 */
	private Map<SkyPair<String, EnumQueryKind>, KehzhParserResult> khzhParseResult = new HashMap<SkyPair<String, EnumQueryKind>, KehzhParserResult>();

	public int getShfbz() {
		return shfbz;
	}

	/**
	 * 设置是否是本行员工
	 * 
	 * @param shfbz
	 */
	public void setShfbz(int shfbz) {
		this.shfbz = shfbz;
	}

	@Override
	public Page<? extends AbstractQueryResult> nextPage(long queryNum) {
		for (Entry<SkyPair<String, EnumQueryKind>, KehzhParserResult> khzhInfo : khzhParseResult.entrySet()) {
			try {
				return khzhInfo.getValue().nextPage(queryNum);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;

	}

	@Override
	public String parseToString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String parseToString(String huobdh) {
		// TODO Auto-generated method stub
		return null;
	}

	public int getFinished() {
		for (Entry<SkyPair<String, EnumQueryKind>, KehzhParserResult> khzhInfo : khzhParseResult.entrySet()) {
			if (khzhInfo.getValue().getFinished() != QueryConstants.ZHANGH_QUERY_FINISHED) {
				return QueryConstants.ZHANGH_QUERY_NO_FINISHED;
			}
		}
		return QueryConstants.ZHANGH_QUERY_FINISHED;
	}

	@Override
	public boolean isKuaJG() {
		if (khzhParseResult.isEmpty()) {
			return false;
		}
		for (Entry<SkyPair<String, EnumQueryKind>, KehzhParserResult> khzhInfo : khzhParseResult.entrySet()) {
			if (!khzhInfo.getValue().isKuaJG()) {
				return false;
			}
		}
		return true;
	}
}
