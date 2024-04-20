package com.fpetranzan.security.configs;

import com.fpetranzan.security.models.email.EmailTemplateName;
import com.fpetranzan.security.strategies.SendEmailStrategy;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Configuration
@AllArgsConstructor
public class StrategyConfig {
	private final List<SendEmailStrategy> emailStrategies;

	@Bean
	public Map<EmailTemplateName, SendEmailStrategy> sendEmailByType() {
		Map<EmailTemplateName, SendEmailStrategy> emailByType = new EnumMap<>(EmailTemplateName.class);
		emailStrategies.forEach(emailStrategy -> emailByType.put(emailStrategy.emailType(), emailStrategy));
		return emailByType;
	}
}
