package com.fpetranzan.security.tests;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

	@GetMapping
	@PreAuthorize("hasAuthority('admin:read')")
	public String get() {
		return "GET:: Admin Controller";
	}

	@PutMapping
	@PreAuthorize("hasAuthority('admin:update')")
	public String put() {
		return "PUT:: Admin Controller";
	}

	@PostMapping
	@PreAuthorize("hasAuthority('admin:create')")
	public String post() {
		return "POST:: Admin Controller";
	}

	@DeleteMapping
	@PreAuthorize("hasAuthority('admin:delete')")
	public String delete() {
		return "DELETE:: Admin Controller";
	}
}
