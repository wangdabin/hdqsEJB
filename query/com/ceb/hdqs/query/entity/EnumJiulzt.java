package com.ceb.hdqs.query.entity;

/**
 * 记录状态枚举
 * @author user
 *
 */
public enum EnumJiulzt {
	JILUZT_0("0-正常"),
	JILUZT_1("1-销户"),
	JILUZT_2("2-只收不付冻结"),
	JILUZT_3("3-封闭冻结"),
	JILUZT_4("4-删除"),
	JILUZT_5("5-未使用"),
	JILUZT_6("6-结清"),
	JILUZT_7("7-打印"),
	JILUZT_8("8-碰库"),
	JILUZT_9("9-不动户"),
	JILUZT_A("A-不动户转收益"),
	JILUZT_B("B-死亡户"),
	JILUZT_C("C-报案户"),
	JILUZT_D("D-请与开户行接洽"),
	JILUZT_E("E-不能在他行销记户"),
	JILUZT_F("F-准客户"),
	JILUZT_G("G-未复核"),
	JILUZT_R("R-被当日冲正"),
	JILUZT_S("S-被隔日冲正"),
	JILUZT_J("J-禁用"),
	JILUZT_Y("Y-预销户"),
	JILUZT_Z("Z-质押冻结"),
	JILUZT_T("T-凭证在途");
	
	public String jiluzt;
	private EnumJiulzt(String jiluzt){
		this.jiluzt = jiluzt;
	}
	
	public String getDisplay(){
		return this.jiluzt;
	}
}
