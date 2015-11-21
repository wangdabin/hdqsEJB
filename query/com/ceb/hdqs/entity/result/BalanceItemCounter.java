package com.ceb.hdqs.entity.result;

import com.ceb.hdqs.query.entity.TransferItem;

/**
 * 余额连续性校验 实体类
 * 
 * @author user
 * 
 */
public class BalanceItemCounter extends TransferItem {
	private static final long serialVersionUID = 8035386956726286768L;

	public BalanceItemCounter() {
	}

	public BalanceItemCounter(String JIO1JE, String ZHHUYE, String YUEEFX, String JIEDBZ, String JIOYRQ) {
		super(JIO1JE, ZHHUYE, YUEEFX, JIEDBZ, JIOYRQ);
	}

	@Override
	public String parseToString(String huobdh) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] toArray(String printStyle, String huobdh) {
		// TODO Auto-generated method stub
		return null;
	}

}
