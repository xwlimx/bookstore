package com.example.bookstore.exception;

public class IncorrectIsbnException extends BaseException { 

	/**
	 * 
	 */
	private static final long serialVersionUID = -290330182253653518L;

	public IncorrectIsbnException(String isbn) {
		super("Could not find book by isbn " + isbn);
	}

}
