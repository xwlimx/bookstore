package com.example.bookstore.exception;

public class BookExistException extends BaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3138743479537164895L;

	public BookExistException(String isbn) {
		super("Existing book by isbn " + isbn);
	}

}
