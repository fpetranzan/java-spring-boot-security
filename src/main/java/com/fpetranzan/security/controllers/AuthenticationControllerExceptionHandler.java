package com.fpetranzan.security.controllers;

import com.fpetranzan.security.exceptions.InvalidAuthTokenException;
import com.fpetranzan.security.exceptions.UserAlreadyExistsException;
import com.fpetranzan.security.exceptions.UserNotFoundForAuthException;
import com.fpetranzan.security.exceptions.UserNotVerifiedException;
import com.fpetranzan.security.exceptions.WrongPasswordMatchException;
import com.fpetranzan.security.models.exception.ExceptionResponse;
import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.HashSet;
import java.util.Set;

import static com.fpetranzan.security.models.exception.BusinessErrorCodes.BAD_CREDENTIALS;
import static com.fpetranzan.security.models.exception.BusinessErrorCodes.NO_USER_FOR_TOKEN;
import static com.fpetranzan.security.models.exception.BusinessErrorCodes.PASSWORD_ERROR;
import static com.fpetranzan.security.models.exception.BusinessErrorCodes.USER_EXISTS;
import static com.fpetranzan.security.models.exception.BusinessErrorCodes.USER_NOT_FOUND;
import static com.fpetranzan.security.models.exception.BusinessErrorCodes.USER_NOT_VERIFIED;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@ControllerAdvice
public class AuthenticationControllerExceptionHandler {

	@ExceptionHandler(UserAlreadyExistsException.class)
	public ResponseEntity<ExceptionResponse> handleUserAlreadyExistsException(UserAlreadyExistsException ex, WebRequest request) {
		return ResponseEntity
			.status(UNAUTHORIZED)
			.body(
				ExceptionResponse.builder()
					.businessErrorCode(USER_EXISTS.getCode())
					.businessErrorDescription(USER_EXISTS.getDescription())
					.error(ex.getMessage())
					.build()
			);
	}

	@ExceptionHandler(UsernameNotFoundException.class)
	public ResponseEntity<ExceptionResponse> handleUsernameNotFoundException(UsernameNotFoundException ex, WebRequest request) {
		return ResponseEntity
			.status(NOT_FOUND)
			.body(
				ExceptionResponse.builder()
					.businessErrorCode(USER_NOT_FOUND.getCode())
					.businessErrorDescription(USER_NOT_FOUND.getDescription())
					.error(ex.getMessage())
					.build()
			);
	}

	@ExceptionHandler(UserNotFoundForAuthException.class)
	public ResponseEntity<ExceptionResponse> handleUserNotFoundForAuthException(UserNotFoundForAuthException ex, WebRequest request) {
		return ResponseEntity
			.status(NOT_FOUND)
			.body(
				ExceptionResponse.builder()
					.businessErrorCode(NO_USER_FOR_TOKEN.getCode())
					.businessErrorDescription(NO_USER_FOR_TOKEN.getDescription())
					.error(ex.getMessage())
					.build()
			);
	}

	@ExceptionHandler(UserNotVerifiedException.class)
	public ResponseEntity<ExceptionResponse> handleUserNotVerifiedException(UserNotVerifiedException ex, WebRequest request) {
		return ResponseEntity
			.status(UNAUTHORIZED)
			.body(
				ExceptionResponse.builder()
					.businessErrorCode(USER_NOT_VERIFIED.getCode())
					.businessErrorDescription(USER_NOT_VERIFIED.getDescription())
					.error(ex.getMessage())
					.build()
			);
	}

	@ExceptionHandler(InvalidAuthTokenException.class)
	public ResponseEntity<ExceptionResponse> handleInvalidAuthTokenException(InvalidAuthTokenException ex, WebRequest request) {
		return ResponseEntity
			.status(BAD_REQUEST)
			.body(
				ExceptionResponse.builder()
					.businessErrorCode(PASSWORD_ERROR.getCode())
					.businessErrorDescription(PASSWORD_ERROR.getDescription())
					.error(ex.getMessage())
					.build()
			);
	}

	@ExceptionHandler(WrongPasswordMatchException.class)
	public ResponseEntity<ExceptionResponse> handleWrongPasswordMatchException(WrongPasswordMatchException ex, WebRequest request) {
		return ResponseEntity
			.status(UNAUTHORIZED)
			.body(
				ExceptionResponse.builder()
					.businessErrorCode(PASSWORD_ERROR.getCode())
					.businessErrorDescription(PASSWORD_ERROR.getDescription())
					.error(ex.getMessage())
					.build()
			);
	}

	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<ExceptionResponse> handleBadCredentialsException(BadCredentialsException ex) {
		return ResponseEntity
			.status(UNAUTHORIZED)
			.body(
				ExceptionResponse.builder()
					.businessErrorCode(BAD_CREDENTIALS.getCode())
					.businessErrorDescription(BAD_CREDENTIALS.getDescription())
					.error(ex.getMessage())
					.build()
			);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ExceptionResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
		Set<String> errors = new HashSet<>();
		ex.getBindingResult().getAllErrors()
			.forEach(error -> {
				var errorMessage = error.getDefaultMessage();
				errors.add(errorMessage);
			});

		return ResponseEntity
			.status(BAD_REQUEST)
			.body(
				ExceptionResponse.builder()
					.validationErrors(errors)
					.build()
			);
	}

	@ExceptionHandler(MessagingException.class)
	public ResponseEntity<ExceptionResponse> handleMessagingException(MessagingException ex) {
		return ResponseEntity
			.status(INTERNAL_SERVER_ERROR)
			.body(
				ExceptionResponse.builder()
					.error(ex.getMessage())
					.build()
			);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ExceptionResponse> handleException(Exception ex) {
		ex.printStackTrace();
		return ResponseEntity
			.status(INTERNAL_SERVER_ERROR)
			.body(
				ExceptionResponse.builder()
					.businessErrorDescription("Internal error, please contact the admin")
					.error(ex.getMessage())
					.build()
			);
	}
}
