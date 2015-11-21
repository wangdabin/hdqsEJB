package com.ceb.hdqs.entity.result;

import java.util.List;


/**
 * 换卡记录查询返回结果类型
 * 
 * @author user
 * 
 */
public class Handle0773QueryResult extends AbstractQueryResult {

	private String KEHUZH; // 客户号
	private String GERZWM; // 客户中文名
	private String remark; // 备注信息
	private ReplacementCardParseResult<ReplacementCardItemResult> parseResult;

	/**
	 * 查询结果，每个账号对应一个List集合
	 */
	private List<ReplacementCardItemResult> replacementCardResult;

	public String getKEHUZH() {
		return KEHUZH;
	}

	public void setKEHUZH(String kEHUZH) {
		KEHUZH = kEHUZH;
	}

	public String getGERZWM() {
		return GERZWM;
	}

	public void setGERZWM(String gERZWM) {
		GERZWM = gERZWM;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public List<ReplacementCardItemResult> getReplacementCardResult() {
		return replacementCardResult;
	}

	public void setReplacementCardResult(List<ReplacementCardItemResult> itemDetail) {
		this.replacementCardResult = itemDetail;
	}

	@Override
	public String parseToString(String huobdh) {
		// TODO Auto-generated method stub
		return null;
	}

	public ReplacementCardParseResult<ReplacementCardItemResult> getParseResult() {
		return parseResult;
	}

	public void setParseResult(ReplacementCardParseResult<ReplacementCardItemResult> parseResult) {
		this.parseResult = parseResult;
	}
}