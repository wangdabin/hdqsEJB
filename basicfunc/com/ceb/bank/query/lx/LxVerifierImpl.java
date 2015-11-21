package com.ceb.bank.query.lx;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.ceb.bank.context.LxContext;
import com.ceb.bank.context.RZhanghContext;
import com.ceb.bank.utils.HdqsCommonUtils;
import com.ceb.hdqs.action.query.exception.BalanceBrokedException;
import com.ceb.hdqs.entity.PybjyEO;

/**
 * 账户余额连续性校验
 * 
 * 
 */
public class LxVerifierImpl implements ILxVerifier {

	private boolean isFirst = true;// 是否是账号的第一条记录

	private BigDecimal preBalance = null;
	private String preJioyrq = null;
	private BigDecimal jioyje = null;
	private BigDecimal currentBalance = null;
	private String curJioyrq = null;

	/**
	 * 多个账号校验时,需要在账号入口时进行重置
	 */
	@Override
	public void reset() {
		this.isFirst = true;
	}

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
	@Override
	public boolean check(String zhangh, List<LxContext> list) throws BalanceBrokedException {
		if (list == null || list.isEmpty() || list.size() == 1) {
			return true;
		}
		int yueefx = -1;
		int jiedbz = -1;
		for (int i = 0; i < list.size(); i++) {
			if (isFirst) {// 第一批的第一条，直接赋值，不进行验证
				currentBalance = new BigDecimal(list.get(i).getZhhuye());
				curJioyrq = list.get(i).getJioyrq();
				isFirst = false;
				continue;
			}
			preBalance = currentBalance;
			preJioyrq = curJioyrq;
			jioyje = new BigDecimal(list.get(i).getJio1je());
			currentBalance = new BigDecimal(list.get(i).getZhhuye());
			curJioyrq = list.get(i).getJioyrq();
			yueefx = Integer.parseInt(list.get(i).getYueefx().trim());
			jiedbz = Integer.parseInt(list.get(i).getJiedbz().trim());
			if (!verifyItem(yueefx, jiedbz)) {
				throw new BalanceBrokedException("账户" + zhangh + "在" + preJioyrq + "到" + curJioyrq + "之间余额不连续");
			}
		}

		return true;
	}

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
	@Override
	public List<String> checkAsyn(RZhanghContext rCtx, List<? extends LxContext> list, PybjyEO record) {
		List<String> blxList = new ArrayList<String>();
		if (list == null || list.isEmpty() || list.size() == 1) {
			return blxList;
		}
		int yueefx = -1;
		int jiedbz = -1;
		for (int i = 0; i < list.size(); i++) {
			if (isFirst) {// 第一批的第一条，直接赋值，不进行验证
				currentBalance = new BigDecimal(list.get(i).getZhhuye());
				curJioyrq = list.get(i).getJioyrq();
				isFirst = false;
				continue;
			}
			preBalance = currentBalance;
			preJioyrq = curJioyrq;
			jioyje = new BigDecimal(list.get(i).getJio1je());
			currentBalance = new BigDecimal(list.get(i).getZhhuye());
			curJioyrq = list.get(i).getJioyrq();
			yueefx = Integer.parseInt(list.get(i).getYueefx().trim());
			jiedbz = Integer.parseInt(list.get(i).getJiedbz().trim());
			if (!verifyItem(yueefx, jiedbz)) {
				String tip = HdqsCommonUtils.getBlxTips(record, preJioyrq, curJioyrq, rCtx);
				blxList.add(tip);
			}
		}

		return blxList;
	}

	private boolean verifyItem(int yueefx, int jiedbz) {
		// 处理的规则为： 账户的余额方向与发生额的借贷标志一致余额增加，否则减少
		BigDecimal handleValue;
		if (yueefx == jiedbz) {
			handleValue = preBalance.add(jioyje);
		} else {
			handleValue = preBalance.subtract(jioyje);
		}

		// 精确newBalance
		BigDecimal handleScale = handleValue.setScale(2, BigDecimal.ROUND_HALF_UP);
		BigDecimal currentScale = currentBalance.setScale(2, BigDecimal.ROUND_HALF_UP);
		// 计算精确后的余额和获取的余额的差
		double sub = Math.abs(handleScale.subtract(currentScale).doubleValue());
		return handleScale.compareTo(currentScale) == 0 || sub < 0.01;
	}
}