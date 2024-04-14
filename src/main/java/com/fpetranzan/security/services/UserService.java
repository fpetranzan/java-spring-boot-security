package com.fpetranzan.security.services;

import com.fpetranzan.security.auth.ChangePasswordRequest;
import com.fpetranzan.security.user.User;
import com.fpetranzan.security.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
@RequiredArgsConstructor
public class UserService {

	private final PasswordEncoder passwordEncoder;
	private final UserRepository userRepository;

	public void changePassword(ChangePasswordRequest request, Principal connectedUser) {
		User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

		if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
			throw new IllegalStateException("Wrong password");
		}
		if(!request.getNewPassword().equals(request.getConfirmationPassword())) {
			throw new IllegalStateException("Password are not the same");
		}

		user.setPassword(passwordEncoder.encode(request.getNewPassword()));

		userRepository.save(user);
	}
}
