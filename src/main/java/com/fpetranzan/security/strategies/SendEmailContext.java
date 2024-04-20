package com.fpetranzan.security.strategies;

import com.fpetranzan.security.exceptions.NotFoundEmailStrategy;
import com.fpetranzan.security.models.email.EmailTemplateName;
import com.fpetranzan.security.models.user.User;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

@Component
@AllArgsConstructor
public class SendEmailContext {
	private final Map<EmailTemplateName, SendEmailStrategy> sendEmailByType;

	public void sendMessage(EmailTemplateName emailTemplateType, User user) throws NotFoundEmailStrategy, MessagingException {
		SendEmailStrategy emailStrategy = sendEmailByType.getOrDefault(emailTemplateType, null);
		if (Objects.isNull(emailStrategy)) {
			throw new NotFoundEmailStrategy("Email Template Type not found. type: " + emailTemplateType);
		}
		emailStrategy.sendEmail(user);
	}
}