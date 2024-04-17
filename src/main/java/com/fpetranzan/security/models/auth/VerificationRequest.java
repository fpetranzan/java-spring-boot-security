package com.fpetranzan.security.models.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VerificationRequest {

	@Email(message = "Email does not have a valid format")
	@NotEmpty(message = "Email is mandatory")
	@NotBlank(message = "Email is mandatory")
	private String email;
	@Size(min = 8, message = "Password should be 8 characters long minimum")
	@NotEmpty(message = "Password is mandatory")
	@NotBlank(message = "Password is mandatory")
	private String password;
	@Size(min = 6, max = 6, message = "Password should be 8 characters long minimum")
	@NotEmpty(message = "Password is mandatory")
	@NotBlank(message = "Password is mandatory")
	private String code;
}
