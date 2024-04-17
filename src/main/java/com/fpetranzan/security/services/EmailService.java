package com.fpetranzan.security.services;

import com.fpetranzan.security.models.email.EmailTemplateName;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Map;

import static com.fpetranzan.security.models.email.EmailTemplateName.CONFIRM_ACCOUNT;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.mail.javamail.MimeMessageHelper.MULTIPART_MODE_MIXED;

@Service
@RequiredArgsConstructor
public class EmailService {

	private final JavaMailSender mailSender;
	private final SpringTemplateEngine templateEngine;

	@Async
	public void sendEmail(String from, String to, EmailTemplateName emailTemplate, String subject, Map<String, Object> properties) throws MessagingException {
		String templateName;

		if (emailTemplate == null) {
			templateName = CONFIRM_ACCOUNT.name();
		} else {
			templateName = emailTemplate.name();
		}

		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, MULTIPART_MODE_MIXED, UTF_8.name());

		Context context = new Context();
		context.setVariables(properties);

		helper.setFrom(from);
		helper.setTo(to);
		helper.setSubject(subject);

		String template = templateEngine.process(templateName, context);

		helper.setText(template, Boolean.TRUE);

		mailSender.send(mimeMessage);
	}
}
