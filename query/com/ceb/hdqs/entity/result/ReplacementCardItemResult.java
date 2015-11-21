package com.ceb.hdqs.entity.result;

import java.io.IOException;
import java.io.Serializable;

import com.ceb.hdqs.query.entity.Page;

/**
 * 换卡记录的每条记录存储类型
 * 
 * @author user
 * 
 */
public class ReplacementCardItemResult extends ParseResult implements Serializable {
	private static final long serialVersionUID = -993371828287059274L;
	private String zhuzzh;// 主账号
	private String kahaoo; // 客户帐号(老卡号)
	private String jiluzt; // 记录状态
	private String jdjkbz; // 借贷记卡标志
	private String kaaazl;// 卡种类
	private String fakarq; // 发卡日期

	private String pnzzlx;

	public String getPnzzlx() {
		return pnzzlx;
	}

	public void setPnzzlx(String pnzzlx) {
		this.pnzzlx = pnzzlx;
	}

	public String getZhuzzh() {
		return zhuzzh;
	}

	public void setZhuzzh(String zhuzzh) {
		this.zhuzzh = zhuzzh;
	}

	public String getKahaoo() {
		return kahaoo;
	}

	public void setKahaoo(String kahaoo) {
		this.kahaoo = kahaoo;
	}

	public String getJiluzt() {
		return jiluzt;
	}

	public void setJiluzt(String jiluzt) {
		this.jiluzt = jiluzt;
	}

	public String getJdjkbz() {
		return jdjkbz;
	}

	public void setJdjkbz(String jdjkbz) {
		this.jdjkbz = jdjkbz;
	}

	public String getKaaazl() {
		return kaaazl;
	}

	public void setKaaazl(String kaaazl) {
		this.kaaazl = kaaazl;
	}

	public String getFakarq() {
		return fakarq;
	}

	public void setFakarq(String fakarq) {
		this.fakarq = fakarq;
	}

	@Override
	public String toString() {
		return "ReplacementCardItemResult [zhuzzh=" + zhuzzh + ", kahaoo=" + kahaoo + ", jiluzt=" + jiluzt + ", jdjkbz=" + jdjkbz + ", kaaazl=" + kaaazl + ", fakarq=" + fakarq
				+ "]";
	}

	@Override
	public String parseToString() {
		StringBuilder item = new StringBuilder();
		item.append(zhuzzh).append("|").append(kahaoo).append("|").append(jiluzt).append("|").append(jdjkbz).append("|").append(kaaazl).append("|").append(fakarq);
		return item.toString();
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

}
