package com.fpetranzan.security.controllers;

import com.fpetranzan.security.models.auth.ChangePasswordRequest;
import com.fpetranzan.security.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@PostMapping("/change-password")
	public void changePassword(@RequestBody @Valid ChangePasswordRequest request, Principal connectedUser) {
		userService.changePassword(request, connectedUser);
	}
}
