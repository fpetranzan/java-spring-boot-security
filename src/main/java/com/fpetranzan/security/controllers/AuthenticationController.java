package com.fpetranzan.security.controllers;

import com.fpetranzan.security.models.auth.AuthenticationRequest;
import com.fpetranzan.security.models.auth.AuthenticationResponse;
import com.fpetranzan.security.models.auth.RegisterRequest;
import com.fpetranzan.security.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
	public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
		return ResponseEntity.ok(authenticationService.register(request));
	}

	@PostMapping("/authenticate")
	public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
		return ResponseEntity.ok(authenticationService.authenticate(request));
	}

	@PostMapping("/refresh-token")
	public AuthenticationResponse refreshToken(@RequestHeader("Authorization") String token) {
		return authenticationService.refreshToken(token);
	}
}
