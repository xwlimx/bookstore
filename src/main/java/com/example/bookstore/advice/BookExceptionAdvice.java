package com.example.bookstore.advice;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.bookstore.exception.BaseException;
import com.example.bookstore.exception.BookExistException;
import com.example.bookstore.exception.InvalidAuthorNameException;
import com.example.bookstore.exception.InvalidIsbnException;
import com.example.bookstore.exception.InvalidTitleException;

@ControllerAdvice
public class BookExceptionAdvice {

	@ExceptionHandler({ InvalidAuthorNameException.class, //
			InvalidIsbnException.class, //
			InvalidTitleException.class })
	ResponseEntity<?> bookNotFoundHandler(BaseException e) {
		Map<String, Object> errorResponse = new HashMap<>();
		errorResponse.put("error", e.getMessage());
		errorResponse.put("status", NOT_FOUND.toString());

		return ResponseEntity //
				.status(NOT_FOUND) //
				.body(errorResponse);
	}

	@ExceptionHandler({ BookExistException.class, })
	ResponseEntity<?> createExistingBookHandler(BaseException e) {
		Map<String, Object> errorResponse = new HashMap<>();
		errorResponse.put("error", e.getMessage());
		errorResponse.put("status", BAD_REQUEST.toString());

		return ResponseEntity //
				.status(BAD_REQUEST) //
				.body(errorResponse);
	}

	@ExceptionHandler({ BindException.class, //
			MethodArgumentNotValidException.class })
	ResponseEntity<?> bindExceptionHandler(BindException e) {
		Map<String, String> errors = new HashMap<>();
		e.getFieldErrors().forEach(err -> {
			errors.put(err.getField(), err.getDefaultMessage());
		});
		Map<String, Object> errorResponse = new HashMap<>();
		errorResponse.put("error", errors);
		errorResponse.put("status", BAD_REQUEST.toString());

		return ResponseEntity //
				.badRequest() //
				.body(errorResponse);
	}
}
