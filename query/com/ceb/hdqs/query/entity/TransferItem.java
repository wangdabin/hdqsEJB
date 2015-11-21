package com.ceb.hdqs.query.entity;

import java.io.Serializable;

import com.ceb.hdqs.entity.result.AbstractQueryResult;

/**
 * 统计查询明细条数的实体类,用于验证余额连续性
 * 
 * @author user
 * 
 */
public abstract class TransferItem extends AbstractQueryResult implements Serializable {

	private static final long serialVersionUID = 2544910247760615167L;

	private String JIO1JE;// 交易金额
	private String ZHHUYE;// 帐户余额
	private String YUEEFX;// 余额方向
	private String JIEDBZ;// 借贷标记
	private String SHJNCH;// 时间戳
	private String JIOYRQ;

	public TransferItem() {
	}

	public TransferItem(String JIO1JE, String ZHHUYE, String YUEEFX, String JIEDBZ, String JIOYRQ) {
		this.JIO1JE = JIO1JE;
		this.ZHHUYE = ZHHUYE;
		this.YUEEFX = YUEEFX;
		this.JIEDBZ = JIEDBZ;
		this.JIOYRQ = JIOYRQ;
	}

	public String getJIO1JE() {
		return JIO1JE;
	}

	public void setJIO1JE(String jIO1JE) {
		JIO1JE = jIO1JE;
	}

	public String getZHHUYE() {
		return ZHHUYE;
	}

	public void setZHHUYE(String zHHUYE) {
		ZHHUYE = zHHUYE;
	}

	public String getYUEEFX() {
		return YUEEFX;
	}

	public void setYUEEFX(String yUEEFX) {
		YUEEFX = yUEEFX;
	}

	public String getJIEDBZ() {
		return JIEDBZ;
	}

	public void setJIEDBZ(String jIEDBZ) {
		JIEDBZ = jIEDBZ;
	}

	public String getSHJNCH() {
		return SHJNCH;
	}

	public void setSHJNCH(String sHJNCH) {
		SHJNCH = sHJNCH;
	}

	// 实现equals 方法和hashcode方法
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((SHJNCH == null) ? 0 : SHJNCH.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TransferItem other = (TransferItem) obj;
		if (SHJNCH == null) {
			if (other.SHJNCH != null)
				return false;
		} else if (!SHJNCH.equals(other.SHJNCH))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ItemCounter [JIO1JE=" + JIO1JE + ", ZHHUYE=" + ZHHUYE + ", YUEEFX=" + YUEEFX + ", JIEDBZ=" + JIEDBZ
				+ ", SHJNCH=" + SHJNCH + "]";
	}

	public String getJIOYRQ() {
		return JIOYRQ;
	}

	public void setJIOYRQ(String jIOYRQ) {
		JIOYRQ = jIOYRQ;
	}

}
