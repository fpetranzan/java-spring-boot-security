package com.fpetranzan.security.controllers;

import com.fpetranzan.security.models.auth.AuthenticationRequest;
import com.fpetranzan.security.models.auth.AuthenticationResponse;
import com.fpetranzan.security.models.auth.ForgotPasswordRequest;
import com.fpetranzan.security.models.auth.RegisterRequest;
import com.fpetranzan.security.models.auth.UpdatePasswordRequest;
import com.fpetranzan.security.models.auth.VerificationRequest;
import com.fpetranzan.security.services.AuthenticationService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

	private final AuthenticationService authenticationService;

	@PostMapping("/register")
	public void register(@RequestBody @Valid RegisterRequest request) throws MessagingException {
		authenticationService.register(request);
	}

	@GetMapping("/{userId}/verify-account")
	public AuthenticationResponse activateAccount(@PathVariable Integer userId, @RequestParam String token) throws MessagingException {
		return authenticationService.activateAccount(userId, token);
	}

	@PostMapping("/authenticate")
	public AuthenticationResponse authenticate(@RequestBody @Valid AuthenticationRequest request) {
		return authenticationService.authenticate(request);
	}

	@PostMapping("/refresh-token")
	public AuthenticationResponse refreshToken(@RequestHeader("Authorization") String jwtRefreshToken) {
		return authenticationService.refreshToken(jwtRefreshToken);
	}

	@PostMapping("/verify")
	public AuthenticationResponse verifyCode(@RequestBody @Valid VerificationRequest request) {
		return authenticationService.verifyCode(request);
	}

	@GetMapping("/forgot-password")
	public void forgotPassword(@RequestBody @Valid ForgotPasswordRequest request) throws MessagingException {
		authenticationService.forgotPassword(request);
	}

	@PostMapping("/{userId}/update-password")
	public void updatePassword(@PathVariable Integer userId, @RequestBody @Valid UpdatePasswordRequest request) throws MessagingException {
		authenticationService.updatePassword(userId, request);
	}
}
