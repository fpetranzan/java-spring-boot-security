package com.fpetranzan.security.services;

import com.fpetranzan.security.exceptions.InvalidAuthTokenException;
import com.fpetranzan.security.exceptions.UserAlreadyExistsException;
import com.fpetranzan.security.exceptions.UserNotFoundForAuthException;
import com.fpetranzan.security.exceptions.UserNotVerifiedException;
import com.fpetranzan.security.exceptions.WrongPasswordMatchException;
import com.fpetranzan.security.models.auth.AuthenticationRequest;
import com.fpetranzan.security.models.auth.AuthenticationResponse;
import com.fpetranzan.security.models.auth.ForgotPasswordRequest;
import com.fpetranzan.security.models.auth.RegisterRequest;
import com.fpetranzan.security.constants.AuthConstants;
import com.fpetranzan.security.models.auth.UpdatePasswordRequest;
import com.fpetranzan.security.models.auth.VerificationRequest;
import com.fpetranzan.security.models.email.EmailTemplateName;
import com.fpetranzan.security.models.token.ActivationToken;
import com.fpetranzan.security.models.token.Token;
import com.fpetranzan.security.repositories.ActivationTokenRepository;
import com.fpetranzan.security.repositories.TokenRepository;
import com.fpetranzan.security.models.token.TokenType;
import com.fpetranzan.security.models.user.Role;
import com.fpetranzan.security.models.user.User;
import com.fpetranzan.security.repositories.UserRepository;
import com.fpetranzan.security.strategies.SendEmailContext;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
	private final ActivationTokenRepository activationTokenRepository;
	private final SendEmailContext sendEmailContext;

	public void register(RegisterRequest request) throws MessagingException {
		userRepository.findByEmail(request.getEmail()).ifPresent(user -> {
			throw new UserAlreadyExistsException("The user already exists");
		});

		final User user = User.builder()
			.firstname(request.getFirstName())
			.lastname(request.getLastName())
			.email(request.getEmail())
			.password(passwordEncoder.encode(request.getPassword()))
			.role(Role.USER)
			.enabled(Boolean.FALSE)
			.mfaEnabled(request.isMfaEnabled())
			.build();

		if (request.isMfaEnabled()) {
			user.setSecret(tfaService.genereteNewSecret());
		}

		userRepository.save(user);
		sendEmailContext.sendMessage(EmailTemplateName.ACTIVATE_ACCOUNT, user);
	}

	public AuthenticationResponse activateAccount(Integer userId, String token) throws MessagingException {
		ActivationToken savedToken = activationTokenRepository.findByUserAndToken(userId, token).orElseThrow(() -> new InvalidAuthTokenException("Invalid token"));

		if (LocalDateTime.now().isAfter(savedToken.getExpiredAt())) {
			savedToken.setValidatedAt(LocalDateTime.of(1900, 1, 1, 0, 0, 0, 0));
			activationTokenRepository.save(savedToken);

			sendEmailContext.sendMessage(EmailTemplateName.ACTIVATE_ACCOUNT, savedToken.getUser());
			throw new InvalidAuthTokenException("Activation token has expired. A new token has been send to the same email");
		}
		savedToken.setValidatedAt(LocalDateTime.now());

		User user = userRepository.findById(savedToken.getUser().getId()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
		user.setEnabled(Boolean.TRUE);

		String qrCodeImage = "";
		String jwtToken = "";
		String jwtRefreshToken = "";

		if (user.isMfaEnabled()) {
			qrCodeImage = tfaService.genereteQrCodeImageUrl(user.getSecret());
		} else {
			jwtToken = jwtService.generateToken(user);
			jwtRefreshToken = jwtService.generateRefreshToken(user);
		}

		activationTokenRepository.save(savedToken);
		userRepository.save(user);
		saveUserToken(user, jwtToken);
		sendEmailContext.sendMessage(EmailTemplateName.CONFIRM_ACCOUNT, user);

		return AuthenticationResponse.builder()
			.accessToken(jwtToken)
			.refreshToken(jwtRefreshToken)
			.mfaEnabled(user.isMfaEnabled())
			.qrCodeImageUri(qrCodeImage)
			.build();
	}

	public AuthenticationResponse authenticate(AuthenticationRequest request) {
		User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new UsernameNotFoundException("User not found"));

		if (user.isEnabled()) {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

			if (user.isMfaEnabled()) {
				return AuthenticationResponse.builder()
					.accessToken("")
					.refreshToken("")
					.mfaEnabled(Boolean.TRUE)
					.build();
			}

			final String jwtToken = jwtService.generateToken(user);
			final String jwtRefreshToken = jwtService.generateRefreshToken(user);
			revokeAllUserToken(user);
			saveUserToken(user, jwtToken);

			return AuthenticationResponse.builder()
				.accessToken(jwtToken)
				.refreshToken(jwtRefreshToken)
				.mfaEnabled(Boolean.FALSE)
				.build();
		}

		throw new UserNotVerifiedException("User not verify, please check your inbox");
	}

	public AuthenticationResponse refreshToken(String jwtRefreshToken) {
		final String userEmail;

		if (jwtRefreshToken == null || !jwtRefreshToken.startsWith(AuthConstants.AUTHORIZATION_TYPE.BEARER)) {
			throw new InvalidAuthTokenException("No token found in the request");
		}

		jwtRefreshToken = jwtRefreshToken.substring(7);
		userEmail = jwtService.extractUsername(jwtRefreshToken);

		if (userEmail != null) {
			User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new UsernameNotFoundException("User not found"));

			if (jwtService.isTokenValid(jwtRefreshToken, user)) {
				String jwtToken = jwtService.generateToken(user);
				revokeAllUserToken(user);
				saveUserToken(user, jwtToken);
				return AuthenticationResponse.builder()
					.accessToken(jwtToken)
					.refreshToken(jwtRefreshToken)
					.mfaEnabled(Boolean.FALSE)
					.build();
			} else {
				throw new InvalidAuthTokenException("Invalid refresh token");
			}
		} else {
			throw new UserNotFoundForAuthException("User for token not found");
		}
	}

	public AuthenticationResponse verifyCode(VerificationRequest request) {
		User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new UsernameNotFoundException("User not found"));

		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

		if (tfaService.isOtpNotValid(user.getSecret(), request.getCode())) {
			throw new BadCredentialsException("Code is not correct");
		}

		final String jwtToken = jwtService.generateToken(user);
		final String jwtRefreshToken = jwtService.generateRefreshToken(user);
		revokeAllUserToken(user);
		saveUserToken(user, jwtToken);

		return AuthenticationResponse.builder()
			.accessToken(jwtToken)
			.refreshToken(jwtRefreshToken)
			.build();
	}

	public void forgotPassword(ForgotPasswordRequest request) throws MessagingException {
		User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new UsernameNotFoundException("User not found"));

		if (user.isEnabled()) {
			sendEmailContext.sendMessage(EmailTemplateName.FORGOT_PASSWORD, user);
			return;
		}

		throw new UserNotVerifiedException("User not verify, please check your inbox");
	}

	public void updatePassword(Integer userId, UpdatePasswordRequest request) throws MessagingException {
		ActivationToken savedToken = activationTokenRepository.findByUserAndToken(userId, request.getCode()).orElseThrow(() -> new InvalidAuthTokenException("Invalid token"));
		User user = savedToken.getUser();

		if (LocalDateTime.now().isAfter(savedToken.getExpiredAt())) {
			savedToken.setValidatedAt(LocalDateTime.of(1900, 1, 1, 0, 0, 0, 0));
			activationTokenRepository.save(savedToken);

			sendEmailContext.sendMessage(EmailTemplateName.FORGOT_PASSWORD, user);
			throw new InvalidAuthTokenException("Activation token has expired. A new token has been send to the same email");
		}

		if(!request.getNewPassword().equals(request.getConfirmationPassword())) {
			throw new WrongPasswordMatchException("Password are not the same");
		}

		savedToken.setValidatedAt(LocalDateTime.now());
		user.setPassword(passwordEncoder.encode(request.getNewPassword()));

		userRepository.save(user);
		activationTokenRepository.save(savedToken);

		sendEmailContext.sendMessage(EmailTemplateName.PASSWORD_UPDATED, user);
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
		if (user != null && !jwtToken.equals("")) {
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
}
