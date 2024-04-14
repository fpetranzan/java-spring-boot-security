package com.fpetranzan.security.exceptions;

public class WrongPasswordMatchException extends RuntimeException {

	public WrongPasswordMatchException(String message) {
		super(message);
	}
}