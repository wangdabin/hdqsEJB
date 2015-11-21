package com.ceb.hdqs.action.query.exception;

/**
 * 输入条件不存在时抛出此异常
 * 
 * @author user
 * 
 */
public class ConditionNotExistException extends Exception {
	private static final long serialVersionUID = -3588392604554562026L;

	public ConditionNotExistException(String errorMsg) {
		super(errorMsg);
	}

	public ConditionNotExistException(String errMsg, Throwable cause) {
		super(errMsg, cause);
	}
}
