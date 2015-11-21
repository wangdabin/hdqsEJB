package com.ceb.hdqs.action.query.exception;

public class OutputFileExistException extends Exception {
	private static final long serialVersionUID = 3132402806442587927L;

	public OutputFileExistException(String errorMsg) {
		super(errorMsg);
	}
}
