package com.ceb.bank.query.lx;

import java.util.List;

import com.ceb.bank.context.LxContext;
import com.ceb.bank.context.RZhanghContext;
import com.ceb.hdqs.action.query.exception.BalanceBrokedException;
import com.ceb.hdqs.entity.PybjyEO;

/**
 * 账户余额校验接口
 * 
 * 
 */
public interface ILxVerifier {
	/**
	 * 多个账号校验时,需要在账号入口时进行重置
	 */
	public void reset();

	/**
	 * 同步连续性校验接口
	 * 
	 * @param zhangh
	 *            当前进行校验的账号
	 * @param list
	 *            明细集合
	 * @return
	 * @throws BalanceBrokedException
	 */
	public boolean check(String zhangh, List<LxContext> list) throws BalanceBrokedException;

	/**
	 * 异步连续性校验接口
	 * 
	 * @param rCtx
	 *            账号关联关系,客户号或证件查询时,保存客户账号和客户账号类型等属性信息
	 * @param list
	 *            明细列表
	 * @param record
	 *            查询条件
	 */
	public List<String> checkAsyn(RZhanghContext rCtx, List<? extends LxContext> list, PybjyEO record);
}