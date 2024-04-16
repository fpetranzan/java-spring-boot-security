package com.fpetranzan.security.services;

import com.fpetranzan.security.exceptions.InvalidAuthTokenException;
import com.fpetranzan.security.exceptions.UserAlreadyExistsException;
import com.fpetranzan.security.exceptions.UserNotFoundForAuthException;
import com.fpetranzan.security.models.auth.AuthenticationRequest;
import com.fpetranzan.security.models.auth.AuthenticationResponse;
import com.fpetranzan.security.models.auth.RegisterRequest;
import com.fpetranzan.security.constants.AuthConstants;
import com.fpetranzan.security.models.token.Token;
import com.fpetranzan.security.repositories.TokenRepository;
import com.fpetranzan.security.models.token.TokenType;
import com.fpetranzan.security.models.user.Role;
import com.fpetranzan.security.models.user.User;
import com.fpetranzan.security.repositories.UserRepository;
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
	private final TwoFactorAuthenticationService tfaService;

	public AuthenticationResponse register(RegisterRequest request) {
		userRepository.findByEmail(request.getEmail()).ifPresent(user -> {
			throw new UserAlreadyExistsException("The user already exists");
		});

		String qrCodeImage = "";
		String jwtToken = "";
		String jwtRefreshToken = "";
		final User user = User.builder()
			.firstname(request.getFirstName())
			.lastname(request.getLastName())
			.email(request.getEmail())
			.password(passwordEncoder.encode(request.getPassword()))
			.role(Role.USER)
			.mfaEnabled(request.isMfaEnabled())
			.build();

		if (request.isMfaEnabled()) {
			user.setSecret(tfaService.genereteNewSecret());
			qrCodeImage = tfaService.genereteQrCodeImageUrl(user.getSecret());
		} else {
			jwtToken = jwtService.generateToken(user);
			jwtRefreshToken = jwtService.generateRefreshToken(user);
		}

		userRepository.save(user);
		saveUserToken(user, jwtToken);

		return AuthenticationResponse.builder()
			.accessToken(jwtToken)
			.refreshToken(jwtRefreshToken)
			.mfaEnabled(request.isMfaEnabled())
			.qrCodeImageUri(qrCodeImage)
			.build();
	}

	public AuthenticationResponse authenticate(AuthenticationRequest request) {
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

		User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
		final String jwtToken = jwtService.generateToken(user);
		final String jwtRefreshToken = jwtService.generateRefreshToken(user);
		revokeAllUserToken(user);
		saveUserToken(user, jwtToken);

		return AuthenticationResponse.builder()
			.accessToken(jwtToken)
			.refreshToken(jwtRefreshToken)
			.build();
	}

	public AuthenticationResponse refreshToken(String token) {
		final String userEmail;

		if (token == null || !token.startsWith(AuthConstants.AUTHORIZATION_TYPE.BEARER)) {
			throw new InvalidAuthTokenException("No token found in the request");
		}

		token = token.substring(7);
		userEmail = jwtService.extractUsername(token);

		if (userEmail != null) {
			User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new UsernameNotFoundException("User not found"));

			if (jwtService.isTokenValid(token, user)) {
				String accessToken = jwtService.generateToken(user);
				revokeAllUserToken(user);
				saveUserToken(user, accessToken);
				return AuthenticationResponse.builder()
					.accessToken(accessToken)
					.refreshToken(token)
					.build();
			} else {
				throw new InvalidAuthTokenException("Invalid refresh token");
			}
		} else {
			throw new UserNotFoundForAuthException("User for token not found");
		}
	}

	private void revokeAllUserToken(User user) {
		List<Token> validUserTokens = tokenRepository.findAllValidTokensByUser(user.getId());

		if (validUserTokens.isEmpty()) {
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
