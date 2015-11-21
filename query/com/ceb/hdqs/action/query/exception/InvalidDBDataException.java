package com.ceb.hdqs.action.query.exception;

/**
 * 数据库数据不一致的异常
 * 
 * @author user
 * 
 */
public class InvalidDBDataException extends Exception {
	private static final long serialVersionUID = 450827695910778327L;

	public InvalidDBDataException(String msg) {
		super(msg);
	}
}
