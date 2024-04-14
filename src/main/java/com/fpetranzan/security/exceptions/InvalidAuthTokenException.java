package com.fpetranzan.security.exceptions;

public class InvalidAuthTokenException extends RuntimeException {

	public InvalidAuthTokenException(String message) {
		super(message);
	}
}
