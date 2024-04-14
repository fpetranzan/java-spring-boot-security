package com.fpetranzan.security.controllers;

import com.fpetranzan.security.auth.ChangePasswordRequest;
import com.fpetranzan.security.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.Principal;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@PostMapping("/change-password")
	public ResponseEntity<ChangePasswordRequest> changePassword(@RequestBody ChangePasswordRequest request, Principal connectedUser) throws IOException {
		userService.changePassword(request, connectedUser);
		return ResponseEntity.ok().build();
	}
}
