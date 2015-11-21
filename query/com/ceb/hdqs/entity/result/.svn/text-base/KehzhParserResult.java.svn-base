package com.ceb.hdqs.entity.result;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.ceb.hdqs.query.entity.EnumQueryKind;
import com.ceb.hdqs.query.entity.Page;
import com.ceb.hdqs.query.entity.SkyPair;
import com.ceb.hdqs.query.utils.QueryConstants;

/**
 * 客户账号ParseResult，输入条件为客户账号的情况下，解析出该对象，同时如果输入的查询条件都是客户号和证据则解析出的对应的查询条件，同样会封装成
 * 该对象，利用该对象的nextPage方法进行业务逻辑控制查询。
 * 
 * @author user
 * 
 */
public class KehzhParserResult extends ParseResult implements Serializable {
	private static final long serialVersionUID = -6433278375514901146L;

	private String kehuzh;
	private String khzhlx;
	private String huobdh;
	private String chohbz;
	private int shfobz;
	private int khzhjb;
	private String kaaadx;
	private Map<SkyPair<String, EnumQueryKind>, ZhangHParseResult> zhanghParseResult = new HashMap<SkyPair<String, EnumQueryKind>, ZhangHParseResult>();

	public int getKhzhjb() {
		return khzhjb;
	}

	public void setKhzhjb(int khzhjb) {
		this.khzhjb = khzhjb;
	}

	public int getShfobz() {
		return shfobz;
	}

	public void setShfobz(int shfobz) {
		this.shfobz = shfobz;
	}

	public String getHuobdh() {
		return huobdh;
	}

	public void setHuobdh(String huobdh) {
		this.huobdh = huobdh;
	}

	public String getChohbz() {
		return chohbz;
	}

	public void setChohbz(String chohbz) {
		this.chohbz = chohbz;
	}

	public String getKhzhlx() {
		return khzhlx;
	}

	public void setKhzhlx(String khzhlx) {
		this.khzhlx = khzhlx;
	}

	public KehzhParserResult() {
//		this.queryTpye = QueryConstants.ASYNCHRONIZE_INPUT_FIELD_KHZHAO;
	}

	/**
	 * 获取输入的开户账号解析出的账号信息
	 * 
	 * @return Map<String,List<ZhangHParseResult>> zhanghParseResult
	 */
	public Map<SkyPair<String, EnumQueryKind>, ZhangHParseResult> getZhanghParseResult() {
		return this.zhanghParseResult;
	}

	/**
	 * 设置输入的开户账号解析出的账号信息
	 * 
	 * @param Map
	 *            <String,List<ZhangHParseResult>> zhanghParseResult
	 */
	public void setZhanghParseResult(Map<SkyPair<String, EnumQueryKind>, ZhangHParseResult> zhanghParseResult) {
		this.zhanghParseResult = zhanghParseResult;
	}

	public String getKehuzh() {
		return kehuzh;
	}

	public void setKehuzh(String kehuzh) {
		this.kehuzh = kehuzh;
	}

	public String getKaaadx() {
		return kaaadx;
	}

	public void setKaaadx(String kaaadx) {
		this.kaaadx = kaaadx;
	}

	@Override
	public long getItemCount() {
		long totalNum = 0;
		for (Entry<SkyPair<String, EnumQueryKind>, ZhangHParseResult> res : zhanghParseResult.entrySet()) {
			totalNum += res.getValue().getItemCount();
		}
		return totalNum;
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
		for (Entry<SkyPair<String, EnumQueryKind>, ZhangHParseResult> zhanghInfo : zhanghParseResult.entrySet()) {
			if (zhanghInfo.getValue().getFinished() != QueryConstants.ZHANGH_QUERY_FINISHED) {
				return QueryConstants.ZHANGH_QUERY_NO_FINISHED;
			}
		}
		return QueryConstants.ZHANGH_QUERY_FINISHED;
	}

	@Override
	public Page<? extends AbstractQueryResult> nextPage(long queryNum) throws IOException, Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isKuaJG() {
		if (zhanghParseResult.isEmpty()) {
			return false;
		}
		for (Entry<SkyPair<String, EnumQueryKind>, ZhangHParseResult> zhanghInfo : zhanghParseResult.entrySet()) {
			if (!zhanghInfo.getValue().isKuaJG()) {
				return false;
			}
		}
		return true;
	}
}
