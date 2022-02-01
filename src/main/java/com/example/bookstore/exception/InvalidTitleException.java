package com.example.bookstore.exception;

public class InvalidTitleException extends BaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 916146440394541986L;

	public InvalidTitleException(String title) {
		super("Could not find book by title " + title);
	}
}
