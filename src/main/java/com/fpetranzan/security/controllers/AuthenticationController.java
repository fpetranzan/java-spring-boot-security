package com.fpetranzan.security.controllers;

import com.fpetranzan.security.models.auth.AuthenticationRequest;
import com.fpetranzan.security.models.auth.AuthenticationResponse;
import com.fpetranzan.security.models.auth.RegisterRequest;
import com.fpetranzan.security.models.auth.VerificationRequest;
import com.fpetranzan.security.services.AuthenticationService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
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

	@GetMapping("/activate-account")
	public AuthenticationResponse activateAccount(@RequestParam String token) throws MessagingException {
		return authenticationService.activateAccount(token);
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
}
