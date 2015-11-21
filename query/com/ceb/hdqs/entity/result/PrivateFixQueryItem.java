package com.ceb.hdqs.entity.result;

import java.io.Serializable;

import com.ceb.hdqs.action.query0772.Handle0772ItemQuery;
import com.ceb.hdqs.query.entity.TransferItem;
import com.ceb.hdqs.query.utils.QueryMethodUtils;

/**
 * 对私定期明细查询{@link Handle0772ItemQuery} 的输出字段
 * 
 * @author user
 * 
 */
public class PrivateFixQueryItem extends TransferItem implements Serializable {
	private static final long serialVersionUID = 7495890302251155033L;
	private String ZHANGH;
	// private String jioyrq;// 交易日期
	private String JIOYRQ;
	private String JIOYSJ;// 交易时间
//	private String JIO1JE;// 交易金额
//	private String ZHHUYE;// 帐户余额
//	private String YUEEFX;// 余额方向
	private String HUOBDH;// 币种
	private String JIAOYM;// 交易码
//	private String JIEDBZ;// 借贷标记
	private String JIO1GY;// 交易柜员
	private String SHOQGY;// 授权柜员
	private String CHBUBZ;// 冲补标志
	private String GUIYLS;// 柜员流水号
	private String ZHYODM;// 摘要代码
	private String YNGYJG;// 营业机构号
	private String KHZHLX;// 客户帐号类型
	private String PNGZHH;// 凭证代号
	private String DUIFZH;// 对方帐号
	private String DUIFMC;// 对方户名
	private String DANWMC;
	private String CPZNXH;
	private String SHJNCH;// 时间戳
	private String XNZHBZ;
	private String PNZZLX;
	private String SHDHDM;

//	public String getYUEEFX() {
//		return YUEEFX;
//	}
//
//	public void setYUEEFX(String yUEEFX) {
//		YUEEFX = yUEEFX;
//	}

	public String getPNZZLX() {
		return PNZZLX;
	}

	public void setPNZZLX(String pNZZLX) {
		PNZZLX = pNZZLX;
	}

	public String getXNZHBZ() {
		return XNZHBZ;
	}

	public void setXNZHBZ(String xNZHBZ) {
		XNZHBZ = xNZHBZ;
	}

	public String getZHANGH() {
		return ZHANGH;
	}

	public void setZHANGH(String zHANGH) {
		ZHANGH = zHANGH;
	}

	public String getDANWMC() {
		return DANWMC;
	}

	public void setDANWMC(String dANWMC) {
		DANWMC = dANWMC;
	}

	public String getJIOYRQ() {
		return JIOYRQ;
	}

	public void setJIOYRQ(String jIOYRQ) {
		JIOYRQ = jIOYRQ;
	}

	public String getJIOYSJ() {
		return JIOYSJ;
	}

	public void setJIOYSJ(String jIOYSJ) {
		JIOYSJ = jIOYSJ;
	}

//	public String getJIO1JE() {
//		return JIO1JE;
//	}
//
//	public void setJIO1JE(String jIO1JE) {
//		JIO1JE = jIO1JE;
//	}
//
//	public String getZHHUYE() {
//		return ZHHUYE;
//	}
//
//	public void setZHHUYE(String zHHUYE) {
//		ZHHUYE = zHHUYE;
//	}

	public String getHUOBDH() {
		return HUOBDH;
	}

	public void setHUOBDH(String hUOBDH) {
		HUOBDH = hUOBDH;
	}

	public String getJIAOYM() {
		return JIAOYM;
	}

	public void setJIAOYM(String jIAOYM) {
		JIAOYM = jIAOYM;
	}

//	public String getJIEDBZ() {
//		return JIEDBZ;
//	}
//
//	public void setJIEDBZ(String jIEDBZ) {
//		JIEDBZ = jIEDBZ;
//	}

	public String getJIO1GY() {
		return JIO1GY;
	}

	public void setJIO1GY(String jIO1GY) {
		JIO1GY = jIO1GY;
	}

	public String getSHOQGY() {
		return SHOQGY;
	}

	public void setSHOQGY(String sHOQGY) {
		SHOQGY = sHOQGY;
	}

	public String getCHBUBZ() {
		return CHBUBZ;
	}

	public void setCHBUBZ(String cHBUBZ) {
		CHBUBZ = cHBUBZ;
	}

	public String getGUIYLS() {
		return GUIYLS;
	}

	public void setGUIYLS(String gUIYLS) {
		GUIYLS = gUIYLS;
	}

	public String getZHYODM() {
		return ZHYODM;
	}

	public void setZHYODM(String zHYODM) {
		ZHYODM = zHYODM;
	}

	public String getYNGYJG() {
		return YNGYJG;
	}

	public void setYNGYJG(String yNGYJG) {
		YNGYJG = yNGYJG;
	}

	public String getKHZHLX() {
		return KHZHLX;
	}

	public String getSHJNCH() {
		return SHJNCH;
	}

	public void setSHJNCH(String sHJNCH) {
		SHJNCH = sHJNCH;
	}

	public void setKHZHLX(String kHZHLX) {
		KHZHLX = kHZHLX;
	}

	public String getPNGZHH() {
		return PNGZHH;
	}

	public void setPNGZHH(String pNGZHH) {
		PNGZHH = pNGZHH;
	}

	public String getDUIFZH() {
		return DUIFZH;
	}

	public void setDUIFZH(String dUIFZH) {
		DUIFZH = dUIFZH;
	}

	public String getDUIFMC() {
		return DUIFMC;
	}

	public void setDUIFMC(String dUIFMC) {
		DUIFMC = dUIFMC;
	}

	public String getCPZNXH() {
		return CPZNXH;
	}

	public void setCPZNXH(String cPZNXH) {
		CPZNXH = cPZNXH;
	}

	// 帐号
	// 交易日期
	// 交易时间
	// 交易金额
	// 帐户余额
	// 货币代号
	// 交易码
	// 借贷标记
	// 交易柜员
	// 授权柜员
	// 冲补标志
	// 柜员流水号
	// 摘要
	// 营业机构号 客户帐号类型 凭证号 对方帐号 对方名称 单位名称 现转标志
	@Override
	public String parseToString(String huobdh) {
		StringBuilder item = new StringBuilder();
		item.append(JIOYRQ)
				.append("|")
				.append(JIOYSJ)
				.append("|")
				// 2013-11-06 添加对交易金额、账户余额、借贷标志、客户账号类型的格式化
				.append(QueryMethodUtils.decimalFormat(getJIO1JE(), huobdh)).append("|")
				.append(QueryMethodUtils.decimalFormat(getZHHUYE(), huobdh)).append("|")
				.append(JIAOYM == null ? "" : JIAOYM).append("|").append(QueryMethodUtils.jiedbzFormat(getJIEDBZ()))
				.append("|").append(JIO1GY == null ? "" : JIO1GY).append("|").append(SHOQGY == null ? "" : SHOQGY)
				.append("|").append(CHBUBZ == null ? "" : CHBUBZ).append("|").append(GUIYLS == null ? "" : GUIYLS)
				.append("|").append(ZHYODM == null ? "" : ZHYODM).append("|").append(YNGYJG == null ? "" : YNGYJG)
				.append("|").append(QueryMethodUtils.khzhlxFormat(KHZHLX)).append("|")
				.append(PNGZHH == null ? "" : PNGZHH).append("|").append(DUIFZH == null ? "" : DUIFZH).append("|")
				.append(DUIFMC == null ? "" : DUIFMC).append("|").append(DANWMC == null ? "" : DANWMC).append("|")
				.append(this.getXNZHBZ() == null ? "" : this.getXNZHBZ());
		return item.toString();
	}

	// 客户账号 交易日期 交易地点 存入金额 转出金额 账户余额 交易摘要 对方账号 对方名称
	@Override
	public String[] toArray(String printStyle, String huobdh) {
		String[] temp = null;
		if (printStyle != null && printStyle.equals(1)) {// 如果是非客户查询对账单
			temp = this.parseToString(huobdh).split("\\|");
		} else {
			temp = this.parseToString(huobdh).split("\\|");
		}

		return temp;
	}

	public String getSHDHDM() {
		return SHDHDM;
	}

	public void setSHDHDM(String sHDHDM) {
		SHDHDM = sHDHDM;
	}
}
