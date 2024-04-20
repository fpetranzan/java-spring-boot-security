package com.fpetranzan.security.strategies.impl;

import com.fpetranzan.security.models.email.EmailTemplateName;
import com.fpetranzan.security.models.token.ActivationToken;
import com.fpetranzan.security.models.user.User;
import com.fpetranzan.security.repositories.ActivationTokenRepository;
import com.fpetranzan.security.services.AuthenticationService;
import com.fpetranzan.security.services.EmailService;
import com.fpetranzan.security.strategies.SendEmailStrategy;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class SendVerifyEmailStrategy implements SendEmailStrategy {

	private final EmailService emailService;
	private final ActivationTokenRepository activationTokenRepository;
	@Value("${application.mailing.frontend.activation-url}")
	private String activationUrl;

	@Override
	public void sendEmail(User user) throws MessagingException {
		final String from = "emailverify.no-reply@springBootJwtSecurity.com";
		final String to = user.getEmail();

		String activationToken = generateAndSaveActivationToken(user);

		Map<String, Object> properties = new HashMap<>();
		properties.put("username", user.getFullName());
		properties.put("confirmation_url", String.format(activationUrl, user.getId()));
		properties.put("activation_code", activationToken);

		emailService.sendEmail(from, to, emailType(), "Account activation", properties);
	}

	@Override
	public EmailTemplateName emailType() {
		return EmailTemplateName.ACTIVATE_ACCOUNT;
	}

	private String generateAndSaveActivationToken(User user) {
		String generatedToken = generateActivationToken(6);
		saveUserActivationToken(user, generatedToken);
		return generatedToken;
	}

	private String generateActivationToken(int length) {
		String characters = "0123456789";
		StringBuilder codeBuilder = new StringBuilder();
		SecureRandom secureRandom = new SecureRandom();

		for (int i = 0; i < length; i++) {
			int randomIndex = secureRandom.nextInt(characters.length());
			codeBuilder.append(characters.charAt(randomIndex));
		}

		return codeBuilder.toString();
	}

	private void saveUserActivationToken(User user, String generatedToken) {
		if (user != null && !generatedToken.equals("")) {
			final ActivationToken token = ActivationToken.builder()
				.user(user)
				.token(generatedToken)
				.createdAt(LocalDateTime.now())
				.expiredAt(LocalDateTime.now().plusMinutes(15))
				.build();

			activationTokenRepository.save(token);
		}
	}
}
