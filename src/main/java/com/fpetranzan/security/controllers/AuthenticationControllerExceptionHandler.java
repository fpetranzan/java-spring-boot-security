package com.fpetranzan.security.controllers;

import com.fpetranzan.security.exceptions.InvalidAuthTokenException;
import com.fpetranzan.security.exceptions.UserAlreadyExistsException;
import com.fpetranzan.security.exceptions.UserNotFoundForAuthException;
import com.fpetranzan.security.exceptions.WrongPasswordMatchException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@ControllerAdvice
public class AuthenticationControllerExceptionHandler {

	@ExceptionHandler(UserAlreadyExistsException.class)
	public ResponseEntity<String> handleUserAlreadyExistsException(UserAlreadyExistsException ex, WebRequest request) {
		return ResponseEntity.status(UNAUTHORIZED).body(ex.getMessage());
	}

	@ExceptionHandler(UsernameNotFoundException.class)
	public ResponseEntity<String> handleUsernameNotFoundException(UsernameNotFoundException ex, WebRequest request) {
		return ResponseEntity.status(UNAUTHORIZED).body(ex.getMessage());
	}

	@ExceptionHandler(InvalidAuthTokenException.class)
	public ResponseEntity<String> handleInvalidAuthTokenException(InvalidAuthTokenException ex, WebRequest request) {
		return ResponseEntity.status(BAD_REQUEST).body(ex.getMessage());
	}

	@ExceptionHandler(UserNotFoundForAuthException.class)
	public ResponseEntity<String> handleUserNotFoundForAuthException(UserNotFoundForAuthException ex, WebRequest request) {
		return ResponseEntity.status(UNAUTHORIZED).body(ex.getMessage());
	}

	@ExceptionHandler(WrongPasswordMatchException.class)
	public ResponseEntity<String> handleWrongPasswordMatchException(WrongPasswordMatchException ex, WebRequest request) {
		return ResponseEntity.status(BAD_REQUEST).body(ex.getMessage());
	}

	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<String> handleBadCredentialsException(BadCredentialsException ex, WebRequest request) {
		return ResponseEntity.status(BAD_REQUEST).body(ex.getMessage());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, WebRequest request) {
		return ResponseEntity.status(BAD_REQUEST).body(ex.getFieldError().getDefaultMessage());
	}
}
