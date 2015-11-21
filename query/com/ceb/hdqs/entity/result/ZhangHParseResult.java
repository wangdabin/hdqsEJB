package com.ceb.hdqs.entity.result;

import java.io.IOException;

import com.ceb.hdqs.query.entity.Page;

/**
 * 账号解析出该账号的信息
 * 
 * @author user
 * 
 */
public class ZhangHParseResult extends ParseResult {

	private String HUOBDH;
	private String KEMUCC;
	private String ZHANGH;
	private String YEWUDH;
	private String kehuzh;
	private String KHZHLX;
	private String CHUIBZ;
	private int zhanghPageCount;
	private long zhanghItemCount;
	private boolean isFtzh = false;

	private byte[] lastRowkey;

	public boolean isFtzh() {
		return isFtzh;
	}

	public void setFtzh(boolean isFtzh) {
		this.isFtzh = isFtzh;
	}

	public byte[] getLastRowkey() {
		return lastRowkey;
	}

	public void setLastRowkey(byte[] lastRowkey) {
		this.lastRowkey = lastRowkey;
	}

	public ZhangHParseResult() {
	};

	public ZhangHParseResult(String zhangh, String kemucc, String ynyejg, String yewudh) {
		this.ZHANGH = zhangh;
		this.KEMUCC = kemucc;
		this.ZHYYNG = ynyejg;
		this.YEWUDH = yewudh;
	}

	public String getYEWUDH() {
		return YEWUDH;
	}

	public String getZHYYNG() {
		return ZHYYNG;
	}

	public void setZHYYNG(String yNGYJG) {
		ZHYYNG = yNGYJG;
	}

	public void setYEWUDH(String yEWUDH) {
		YEWUDH = yEWUDH;
	}

	/**
	 * 获取账号
	 * 
	 * @return
	 */
	public String getZHANGH() {
		return ZHANGH;
	}

	/**
	 * 设置账号
	 * 
	 * @param zHANGH
	 */
	public void setZHANGH(String zHANGH) {
		ZHANGH = zHANGH;
	}

	/**
	 * 获取科目存储属性
	 * 
	 * @return
	 */
	public String getKEMUCC() {
		return KEMUCC;
	}

	/**
	 * 设置科目存储属性
	 * 
	 * @param kEMUCC
	 */
	public void setKEMUCC(String kEMUCC) {
		KEMUCC = kEMUCC;
	}

	/**
	 * 获取货币代号
	 * 
	 * @return
	 */
	public String getHUOBDH() {
		return HUOBDH;
	}

	/**
	 * 设置货币代号
	 * 
	 * @param hUOBDH
	 */
	public void setHUOBDH(String hUOBDH) {
		HUOBDH = hUOBDH;
	}

	@Override
	public String parseToString() {
		StringBuilder result = new StringBuilder();
		result.append("账号:").append(ZHANGH).append("     ").append("客户中文名:").append(getKehzwm()).append("     ").append("货币代号:")
				.append(HUOBDH).append("     ").append("开始日期:").append(record.getStartDate()).append("     ").append("结束日期:")
				.append(record.getEndDate());

		return result.toString();
	}

	@Override
	public String toString() {
		return "ZhangHParseResult [HUOBDH=" + HUOBDH + ", KEMUCC=" + KEMUCC + ", ZHANGH=" + ZHANGH + ", YNGYJG=" + ZHYYNG + ", YEWUDH="
				+ YEWUDH + ", itemCount=" + itemCount + "]";
	}

	public Page<? extends AbstractQueryResult> nextPage(long queryNum) throws IOException, Exception {
		// this.getRecord().setQueryNum((int) queryNum);
		// return this.getQueryExectuer().nextPage(this,null);
		return null;
	}

	public String getKehuzh() {
		return kehuzh;
	}

	public void setKehuzh(String kehuzh) {
		this.kehuzh = kehuzh;
	}

	public String getKHZHLX() {
		return KHZHLX;
	}

	public void setKHZHLX(String kHZHLX) {
		KHZHLX = kHZHLX;
	}

	public String getCHUIBZ() {
		return CHUIBZ;
	}

	public void setCHUIBZ(String cHUIBZ) {
		CHUIBZ = cHUIBZ;
	}

	@Override
	public String parseToString(String huobdh) {
		// TODO Auto-generated method stub
		return null;
	}

	public int getZhanghPageCount() {
		return zhanghPageCount;
	}

	public void setZhanghPageCount(int zhanghPageCount) {
		this.zhanghPageCount = zhanghPageCount;
	}

	public long getZhanghItemCount() {
		return zhanghItemCount;
	}

	public void setZhanghItemCount(long zhanghItemCount) {
		this.zhanghItemCount = zhanghItemCount;
	}

	@Override
	public boolean isKuaJG() {
		// TODO Auto-generated method stub
		return super.isKuaJG();
	}
}
