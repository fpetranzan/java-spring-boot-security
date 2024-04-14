package com.fpetranzan.security.controllers;

import com.fpetranzan.security.auth.AuthenticationRequest;
import com.fpetranzan.security.auth.AuthenticationResponse;
import com.fpetranzan.security.auth.ChangePasswordRequest;
import com.fpetranzan.security.auth.RegisterRequest;
import com.fpetranzan.security.service.UserService;
import com.fpetranzan.security.services.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.Principal;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

	private final AuthenticationService authenticationService;
	private final UserService userService;

	@PostMapping("/register")
	public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
		return ResponseEntity.ok(authenticationService.register(request));
	}

	@PostMapping("/authenticate")
	public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
		return ResponseEntity.ok(authenticationService.authenticate(request));
	}

	@PostMapping("/refresh-token")
	public void refresh(HttpServletRequest request, HttpServletResponse response) throws IOException {
		authenticationService.refreshToken(request, response);
	}

	@PostMapping("/change-password")
	public ResponseEntity<ChangePasswordRequest> changePassword(@RequestBody ChangePasswordRequest request, Principal connectedUser) throws IOException {
		userService.changePassword(request, connectedUser);
		return ResponseEntity.ok().build();
	}
}
