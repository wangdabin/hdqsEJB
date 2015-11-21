package com.ceb.bank.context;

import java.io.Serializable;

/**
 * 同步查询时进行分页缓存,对应查询起始笔数是一个RowkeyContext集合.
 * 
 */
public class RowkeyContext implements Serializable, Comparable<RowkeyContext> {
	private static final long serialVersionUID = -8738214684611573217L;

	private byte[] rowkey;
	private int pageCount;

	public byte[] getRowkey() {
		return rowkey;
	}

	public void setRowkey(byte[] rowkey) {
		this.rowkey = rowkey;
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + rowkey.toString().hashCode();
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
		RowkeyContext other = (RowkeyContext) obj;
		return rowkey.toString().equals(other.getRowkey().toString());
	}

	@Override
	public String toString() {
		return "RowkeyContext [rowkey=" + rowkey.toString() + ", pageCount=" + pageCount + "]";
	}

	@Override
	public int compareTo(RowkeyContext o) {
		return rowkey.toString().compareTo(o.getRowkey().toString());
	}
}