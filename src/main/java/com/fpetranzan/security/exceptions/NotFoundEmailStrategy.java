package com.fpetranzan.security.exceptions;

public class NotFoundEmailStrategy extends RuntimeException {

	public NotFoundEmailStrategy(String message) {
		super(message);
	}
}
