package com.ceb.hdqs.action.query.exception;

/**
 * 翻页过程中，如果不存在下一页，则抛出该异常
 * 
 * @author user
 * 
 */
public class NoNextPageException extends Exception {
	private static final long serialVersionUID = 8046752842871847446L;

	public NoNextPageException(String errorMsg) {
		super(errorMsg);
	}
}
