package com.fpetranzan.security.strategies.impl;

import com.fpetranzan.security.models.email.EmailTemplateName;
import com.fpetranzan.security.models.token.ActivationToken;
import com.fpetranzan.security.models.user.User;
import com.fpetranzan.security.repositories.ActivationTokenRepository;
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
public class SendConfirmEmailStrategy implements SendEmailStrategy {

	private final EmailService emailService;

	@Override
	public void sendEmail(User user) throws MessagingException {
		final String from = "emailconfirmation.no-reply@springBootJwtSecurity.com";
		final String to = user.getEmail();

		Map<String, Object> properties = new HashMap<>();
		properties.put("username", user.getFullName());

		emailService.sendEmail(from, to, emailType(), "Account confirmed", properties);
	}

	@Override
	public EmailTemplateName emailType() {
		return EmailTemplateName.CONFIRM_ACCOUNT;
	}
}
