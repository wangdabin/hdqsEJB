package com.ceb.hdqs.query.entity;

import org.apache.commons.lang.StringUtils;

import com.ceb.hdqs.query.utils.QueryConstants;
import com.ceb.hdqs.query.utils.QueryMethodUtils;

public class PageHeader {

	public String newLine = "\\r\\n";
	public long printDate ;
	public String guiyuan = "";
	public String zhangh = "";
	public long totalNum = 0;
	public String logoUrl = "";
	public String pageCount = "";
	public String hbdh = "";
	public String chbz = "";
	private String khzwm = "";
	private String cxqsrq = "";
	private String cxzzrq = "";
	private String fkjgHao = "";
	private String fkjgName = "";
	private String khzh = "";
	private String kehhao = "";// 客户号
	private String tips = "";// 备注信息

	public String getGuiyuan() {
		return guiyuan;
	}

	public void setGuiyuan(String guiyuan) {
		this.guiyuan = guiyuan;
	}

	public long getPrintDate() {
		return printDate;
	}

	public void setPrintDate(long printDate) {
		this.printDate = printDate;
	}

	public long getTotalNum() {
		return totalNum;
	}

//	public void setTotalNum(ZhangHParseResult parseResult) {
//		if (parseResult.getRecord().getIsAsyn().booleanValue()) {// 如果是异步查询
//			this.totalNum += parseResult.getQueriedCount();
//		} else {// 如果是同步查询
//			this.totalNum = parseResult.getRecord().getItemCount();
//		}
//
//	}

	public String getZhangh() {
		return zhangh;
	}

	public void setZhangh(String zhangh) {
		this.zhangh = zhangh;
	}

	public void setTotalNum(long totalNum) {
		this.totalNum = totalNum;
	}

	public String getLogoUrl() {
		return logoUrl;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

	public String getPageCount() {
		return pageCount;
	}

	public void setPageCount(String pageCount) {
		this.pageCount = pageCount;
	}

	public String getHbdh() {
		return hbdh;
	}

	public void setHbdh(String hbdh) {
		this.hbdh = hbdh;
	}

	/**
	 * 获取Excel页头内容
	 * @param exCode
	 * @return
	 */
	public String[] getHeaderArray(String exCode) {
		String[] xlsTitle = new String[] { "" };
		if (QueryConstants.CORPORATE_BATCH_QUERY_CODE.equals(exCode)) {
			//2013-11-06  添加对公Excel对账单的 货币代号   ,去掉   "钞汇标志：" + QueryMethodUtils.chuibzFormat(getChbz()), 
			xlsTitle = new String[] { 
					"客户中文名:" + getKhzwm(),
					"客户号:" + getKehhao(), 
					"账号:" + getZhangh(),
					"货币代号:"+QueryMethodUtils.huobdhFormat(getHbdh()),
					"查询起始日期:" + getCxqsrq(),
					"查询终止日期:" + getCxzzrq(), 
					"打印时间:" + QueryMethodUtils.formatDateAndTime(getPrintDate()) };
		} else if (QueryConstants.PRIVATE_BATCH_QUERY_CODE.equals(exCode)) {
			xlsTitle = new String[] {
					"客户中文名:" + getKhzwm(),
					"客户号:" + getKehhao(),
					"客户账号:" + getKhzh(), 
					"系统账号:" + getZhangh(),
					"发卡/折机构：" + getFkjgHao() + "		" + getFkjgName(),
					"查询起始日期:" + getCxqsrq(),
					"查询终止日期:" + getCxzzrq(), 
					"打印时间:" + QueryMethodUtils.formatDateAndTime(getPrintDate()),
					"钞汇标志：" + QueryMethodUtils.chuibzFormat(getChbz()),
					"货币代号：" + QueryMethodUtils.huobdhFormat(getHbdh()) };
		}
		return xlsTitle;
	}

	public String getChbz() {
		return StringUtils.isBlank(chbz) ? "":chbz;
	}

	public void setChbz(String chbz) {
		this.chbz = chbz;
	}

	public String getNewLine() {
		return newLine;
	}

	public void setNewLine(String newLine) {
		this.newLine = newLine;
	}

	public String getKhzwm() {
		return StringUtils.isBlank(khzwm) ? "":khzwm;
	}

	public void setKhzwm(String khzwm) {
		this.khzwm = khzwm;
	}

	public String getCxqsrq() {
		return StringUtils.isBlank(cxqsrq)? "" :cxqsrq;
	}

	public void setCxqsrq(String cxqsrq) {
		this.cxqsrq = cxqsrq;
	}

	public String getCxzzrq() {
		return StringUtils.isBlank(cxzzrq) ? "":cxzzrq;
	}

	public void setCxzzrq(String cxzzrq) {
		this.cxzzrq = cxzzrq;
	}

	public String getFkjgHao() {
		return StringUtils.isBlank(fkjgHao) ? "":fkjgHao;
	}

	public void setFkjgHao(String fkjgHao) {
		this.fkjgHao = fkjgHao;
	}

	public String getFkjgName() {
		return StringUtils.isBlank(fkjgName) ? "" :fkjgName;
	}

	public void setFkjgName(String fkjgName) {
		this.fkjgName = fkjgName;
	}

	public String getKhzh() {
		return StringUtils.isBlank(khzh) ? "": khzh;
	}

	public void setKhzh(String khzh) {
		this.khzh = khzh;
	}

	public String getKehhao() {
		return StringUtils.isBlank(kehhao) ? "":kehhao;
	}

	public void setKehhao(String kehhao) {
		this.kehhao = kehhao;
	}

	public String getTips() {
		return tips;
	}

	public void setTips(String tips) {
		this.tips = tips;
	}

}
