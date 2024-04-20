package com.fpetranzan.security.models.auth;

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
public class UpdatePasswordRequest {

	@Size(min = 6, max = 6, message = "Password should be 8 characters long minimum")
	@NotEmpty(message = "Password is mandatory")
	@NotBlank(message = "Password is mandatory")
	private String code;
	@Size(min = 8, message = "Password should be 8 characters long minimum")
	@NotEmpty(message = "New Password is mandatory")
	@NotBlank(message = "New Password is mandatory")
	private String newPassword;
	@Size(min = 8, message = "Password should be 8 characters long minimum")
	@NotEmpty(message = "Confirmation Password is mandatory")
	@NotBlank(message = "Confirmation Password is mandatory")
	private String confirmationPassword;
}
