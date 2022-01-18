package com.example.bookstore.exception;

public class IncorrectAuthorNameException extends BaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3489149501329388987L;

	public IncorrectAuthorNameException(String name) {
		super("Could not find book by author name " + name);
	}

}
