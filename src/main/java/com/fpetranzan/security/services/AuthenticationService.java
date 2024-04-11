package com.fpetranzan.security.services;

import com.fpetranzan.security.auth.AuthenticationRequest;
import com.fpetranzan.security.auth.AuthenticationResponse;
import com.fpetranzan.security.auth.RegisterRequest;
import com.fpetranzan.security.token.Token;
import com.fpetranzan.security.token.TokenRepository;
import com.fpetranzan.security.token.TokenType;
import com.fpetranzan.security.user.Role;
import com.fpetranzan.security.user.User;
import com.fpetranzan.security.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

	private final UserRepository userRepository;
	private final TokenRepository tokenRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;

	public AuthenticationResponse register(RegisterRequest request) {
		final User user = User.builder()
			.lastname(request.getLastName())
			.email(request.getEmail())
			.password(passwordEncoder.encode(request.getPassword()))
			.role(Role.USER)
			.build();
		userRepository.save(user);

		final String jwtToken = jwtService.generateToken(user);
		saveUserToken(user, jwtToken);

		return AuthenticationResponse.builder()
			.accessToken(jwtToken)
			.build();
	}

	public AuthenticationResponse authenticate(AuthenticationRequest request) {
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

		User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
		final String jwtToken = jwtService.generateToken(user);
		revokeAllUserToken(user);
		saveUserToken(user, jwtToken);

		return AuthenticationResponse.builder()
			.accessToken(jwtToken)
			.build();
	}

	private void revokeAllUserToken(User user) {
		List<Token> validUserTokens = tokenRepository.findAllValidTokensByUser(user.getId());

		if(validUserTokens.isEmpty()) {
			return;
		}

		validUserTokens.forEach(token -> {
			token.setExpired(Boolean.TRUE);
			token.setRevoked(Boolean.TRUE);
		});
		tokenRepository.saveAll(validUserTokens);
	}

	private void saveUserToken(User user, String jwtToken) {
		final Token token = Token.builder()
			.user(user)
			.token(jwtToken)
			.tokenType(TokenType.BEARER)
			.expired(Boolean.FALSE)
			.revoked(Boolean.FALSE)
			.build();
		tokenRepository.save(token);
	}
}
