package com.fpetranzan.security.controllers;

import com.fpetranzan.security.exceptions.InvalidAuthTokenException;
import com.fpetranzan.security.exceptions.UserNotFoundForAuthException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@ControllerAdvice
public class AuthenticationControllerExceptionHandler {

	@ExceptionHandler(InvalidAuthTokenException.class)
	public ResponseEntity<String> handleInvalidAuthTokenException(InvalidAuthTokenException ex, WebRequest request) {
		return ResponseEntity.status(BAD_REQUEST).body(ex.getMessage());
	}

	@ExceptionHandler(UserNotFoundForAuthException.class)
	public ResponseEntity<String> handleUserNotFoundForAuthException(UserNotFoundForAuthException ex, WebRequest request) {
		return ResponseEntity.status(UNAUTHORIZED).body(ex.getMessage());
	}
}
