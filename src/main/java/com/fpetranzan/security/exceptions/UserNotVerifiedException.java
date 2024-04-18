package com.fpetranzan.security.exceptions;

public class UserNotVerifiedException extends RuntimeException {

	public UserNotVerifiedException(String message) {
		super(message);
	}
}