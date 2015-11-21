package com.ceb.bank.query.scanner;

import java.util.List;

import com.ceb.bank.context.LxContext;
import com.ceb.bank.query.lx.ILxVerifier;
import com.ceb.hdqs.action.query.exception.BalanceBrokedException;
import com.ceb.hdqs.entity.PybjyEO;
import com.ceb.hdqs.query.utils.QueryConstants;

public abstract class AbstractLxScanner extends AbstractCacheScanner<LxContext> {

	/**
	 * 调用getHTable()方式前,必须调用setTableName(String tableName)
	 * 
	 * @param conf
	 */
	public AbstractLxScanner() {
		super();
	}

	public AbstractLxScanner(String tableName) {
		super(tableName);
	}

	protected void verifyLx(PybjyEO record, ILxVerifier verifier, List<LxContext> lxList) throws BalanceBrokedException {
		try {
			verifier.check(record.getZhangh(), lxList);
		} catch (Exception e) {
			String emsg = e.getMessage() + QueryConstants.ERROR_MSG_SUFFIX;
			throw new BalanceBrokedException(emsg);
		}
	}
}
