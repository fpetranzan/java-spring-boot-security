package com.fpetranzan.security.services;

import com.fpetranzan.security.exceptions.WrongPasswordMatchException;
import com.fpetranzan.security.models.auth.ChangePasswordRequest;
import com.fpetranzan.security.models.user.User;
import com.fpetranzan.security.repositories.UserRepository;
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
			throw new WrongPasswordMatchException("Wrong password");
		}
		if(!request.getNewPassword().equals(request.getConfirmationPassword())) {
			throw new WrongPasswordMatchException("Password are not the same");
		}
		if(request.getCurrentPassword().equals(request.getNewPassword())) {
			throw new WrongPasswordMatchException("The new password must not be the same");
		}

		user.setPassword(passwordEncoder.encode(request.getNewPassword()));

		userRepository.save(user);
	}
}
