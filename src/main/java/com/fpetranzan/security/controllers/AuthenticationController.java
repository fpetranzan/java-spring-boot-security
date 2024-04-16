package com.fpetranzan.security.controllers;

import com.fpetranzan.security.models.auth.AuthenticationRequest;
import com.fpetranzan.security.models.auth.AuthenticationResponse;
import com.fpetranzan.security.models.auth.RegisterRequest;
import com.fpetranzan.security.models.auth.VerificationRequest;
import com.fpetranzan.security.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

	private final AuthenticationService authenticationService;

	@PostMapping("/register")
	public AuthenticationResponse register(@RequestBody RegisterRequest request) {
		return authenticationService.register(request);
	}

	@PostMapping("/authenticate")
	public AuthenticationResponse authenticate(@RequestBody AuthenticationRequest request) {
		return authenticationService.authenticate(request);
	}

	@PostMapping("/refresh-token")
	public AuthenticationResponse refreshToken(@RequestHeader("Authorization") String jwtRefreshToken) {
		return authenticationService.refreshToken(jwtRefreshToken);
	}

	@PostMapping("/verify")
	public AuthenticationResponse verifyCode(@RequestBody VerificationRequest request) {
		return authenticationService.verifyCode(request);
	}
}
