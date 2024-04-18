package com.fpetranzan.security.models.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.IM_USED;
import static org.springframework.http.HttpStatus.NOT_ACCEPTABLE;
import static org.springframework.http.HttpStatus.NOT_FOUND;

public enum BusinessErrorCodes {

	USER_EXISTS(226, IM_USED, "User already exists"),
	PASSWORD_ERROR(400, BAD_REQUEST, "Error in the password entered"),
	INVALID_TOKEN(400, BAD_REQUEST, "Invalid access token"),
	BAD_CREDENTIALS(403, FORBIDDEN, "Email and / or Password is incorrect"),
	USER_NOT_FOUND(404, NOT_FOUND, "User not found"),
	NO_USER_FOR_TOKEN(406, NOT_ACCEPTABLE, "User not found for auth token"),
	USER_NOT_VERIFIED(406, NOT_ACCEPTABLE, "User not verify");

	@Getter
	private final int code;
	@Getter
	private final String description;
	@Getter
	private final HttpStatus httpStatus;

	BusinessErrorCodes(int code, HttpStatus status, String description) {
		this.code = code;
		this.description = description;
		this.httpStatus = status;
	}
}