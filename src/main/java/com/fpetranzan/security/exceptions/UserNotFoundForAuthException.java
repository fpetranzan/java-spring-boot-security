package com.fpetranzan.security.exceptions;

public class UserNotFoundForAuthException extends RuntimeException {

	public UserNotFoundForAuthException(String message) {
		super(message);
	}
}
