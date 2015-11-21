package com.ceb.bank.item;

import com.ceb.bank.context.LxContext;

/**
 * 保存明细对应的RowKey信息,生成PDF时需获取下一页第一条记录的RowKey
 */
public abstract class AbstractItem extends LxContext {
	private static final long serialVersionUID = 7229007387336030789L;

	private byte[] rowkey;

	public byte[] getRowkey() {
		return rowkey;
	}

	public void setRowkey(byte[] rowkey) {
		this.rowkey = rowkey;
	}
}