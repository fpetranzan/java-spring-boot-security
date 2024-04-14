package com.fpetranzan.security.configs;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import static com.fpetranzan.security.models.user.Role.ADMIN;
import static com.fpetranzan.security.models.user.Role.MANAGER;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

	private final JwtAuthenticationFilter jwtAuthFilter;
	private final AuthenticationProvider authenticationProvider;
	private final LogoutHandler logoutHandler;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.csrf(csrf -> csrf.disable())
			.authorizeHttpRequests(request ->
				request
					.requestMatchers("/api/v1/auth/**")
					.permitAll()

					.requestMatchers("/api/v1/management/**")
					.hasAnyRole(ADMIN.name(), MANAGER.name())

					.requestMatchers("/api/v1/admin/**")
					.hasRole(ADMIN.name())

					.anyRequest()
					.authenticated()
			)
			.sessionManagement(session ->
				session
					.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			)
			.authenticationProvider(authenticationProvider)
			.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
			.logout(logout ->
				logout
					.logoutUrl("/api/v1/auth/logout")
					.addLogoutHandler(logoutHandler)
					.logoutSuccessHandler(
						(request, response, authentication) -> SecurityContextHolder.clearContext())
			);

		return http.build();
	}
}
