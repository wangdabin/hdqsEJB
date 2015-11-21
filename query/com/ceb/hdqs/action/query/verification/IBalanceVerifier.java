package com.ceb.hdqs.action.query.verification;

import java.util.List;

import com.ceb.hdqs.action.query.exception.BalanceBrokedException;
import com.ceb.hdqs.entity.result.ZhangHParseResult;
import com.ceb.hdqs.query.entity.BalanceVerifyEntity;
import com.ceb.hdqs.query.entity.TransferItem;

/**
 * 账户余额校验接口
 * 
 * @author user
 * 
 */
public interface IBalanceVerifier {
	public boolean verify(BalanceVerifyEntity recordInfo, ZhangHParseResult zhangHParseResult, List<? extends TransferItem> balanceMap
		) throws BalanceBrokedException;
}
