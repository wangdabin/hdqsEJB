package com.ceb.hdqs.action.synchronize;

import java.util.List;

import org.apache.hadoop.hbase.KeyValue;

/**
 * 生成HFILE的Key Value 接口
 * 
 * @author user
 * 
 * @param <T>
 *            具体的类型
 */
public interface IKeyValueUtils<T> {

	/**
	 * 生成入库对象，将字符数组转换成对象
	 * 
	 * @param valueSplit
	 * @return
	 * @throws Exception
	 */
	public T getEntity(String[] valueSplit) throws Exception;

	/**
	 * 生成入库的KeyValue对象
	 * 
	 * @param rowkey
	 * @param agdfh
	 * @return
	 * @throws Exception
	 */
	List<KeyValue> getPutter(byte[] rowkey, T entity) throws Exception;

}
