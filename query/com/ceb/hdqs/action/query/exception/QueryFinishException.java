package com.ceb.hdqs.action.query.exception;

/**
 * 当查询完成后，客户段再次发起相同查询时，抛出该异常
 * 
 * @author user
 * 
 */
public class QueryFinishException extends Exception {
	private static final long serialVersionUID = -2445723007346369252L;

	public QueryFinishException(String errorMsg) {
		super(errorMsg);
	}
}
