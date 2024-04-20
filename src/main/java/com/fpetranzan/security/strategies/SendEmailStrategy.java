package com.fpetranzan.security.strategies;

import com.fpetranzan.security.models.email.EmailTemplateName;
import com.fpetranzan.security.models.user.User;
import jakarta.mail.MessagingException;

public interface SendEmailStrategy {

	void sendEmail(User user) throws MessagingException;

	EmailTemplateName emailType();

}
