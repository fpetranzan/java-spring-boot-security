package com.fpetranzan.security.tests;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/management")
public class ManagementController {

	@GetMapping
	@PreAuthorize("hasAnyAuthority('admin:read','management:read')")
	public String get() {
		return "GET:: Management Controller";
	}

	@PutMapping
	@PreAuthorize("hasAnyAuthority('admin:update','management:update')")
	public String put() {
		return "PUT:: Management Controller";
	}

	@PostMapping
	@PreAuthorize("hasAnyAuthority('admin:create','management:create')")
	public String post() {
		return "POST:: Management Controller";
	}

	@DeleteMapping
	@PreAuthorize("hasAnyAuthority('admin:delete','management:delete')")
	public String delete() {
		return "DELETE:: Management Controller";
	}
}
