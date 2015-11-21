package com.ceb.bank.context;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 0781单位卡查询时,因为每个账号的明细表都不一样,需要缓存.明细里面没有HUOBDH,CHUIBZ,联机查询需要注入到Item0781明细中
 */
public class RowkeyAndTblContext extends RowkeyContext {
	private static final long serialVersionUID = 1510577546641718282L;
	private String tableName;
	private String huobdh;// AKHZH表中查询字段
	private String chuibz;// AKHZH表中查询字段

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getHuobdh() {
		return huobdh;
	}

	public void setHuobdh(String huobdh) {
		this.huobdh = huobdh;
	}

	public String getChuibz() {
		return chuibz;
	}

	public void setChuibz(String chuibz) {
		this.chuibz = chuibz;
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	@Override
	public int compareTo(RowkeyContext o) {
		if (o instanceof RowkeyAndTblContext) {
			RowkeyAndTblContext tmp = (RowkeyAndTblContext) o;
			int result = this.getTableName().compareTo(tmp.getTableName());
			if (result != 0) {
				result = this.getRowkey().toString().compareTo(tmp.getRowkey().toString());
			}
			return result;
		} else {
			return super.compareTo(o);
		}
	}
}
