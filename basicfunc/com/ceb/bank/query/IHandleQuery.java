package com.ceb.bank.query;

import com.ceb.hdqs.entity.PybjyEO;

public interface IHandleQuery<T> {
	/**
	 * 加载起始笔数与(rowKey,tableName)之间对应关系
	 * 
	 * @param record
	 * @return
	 * @throws Exception
	 */
	public T load(PybjyEO record) throws Exception;
}
