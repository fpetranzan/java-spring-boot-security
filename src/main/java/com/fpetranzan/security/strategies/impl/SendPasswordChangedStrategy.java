package com.fpetranzan.security.strategies.impl;

import com.fpetranzan.security.models.email.EmailTemplateName;
import com.fpetranzan.security.models.user.User;
import com.fpetranzan.security.services.EmailService;
import com.fpetranzan.security.strategies.SendEmailStrategy;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class SendPasswordChangedStrategy implements SendEmailStrategy {

	private final EmailService emailService;
	@Value("${application.mailing.frontend.login-url}")
	private String loginUrl;

	@Override
	public void sendEmail(User user) throws MessagingException {
		final String from = "emailpasswordchanged.no-reply@springBootJwtSecurity.com";
		final String to = user.getEmail();

		Map<String, Object> properties = new HashMap<>();
		properties.put("username", user.getFullName());
		properties.put("login_url", loginUrl);

		emailService.sendEmail(from, to, emailType(), "Password changed", properties);
	}

	@Override
	public EmailTemplateName emailType() {
		return EmailTemplateName.PASSWORD_UPDATED;
	}
}
