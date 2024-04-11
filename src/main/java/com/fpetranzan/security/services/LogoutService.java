package com.fpetranzan.security.services;

import com.fpetranzan.security.constants.AuthConstants;
import com.fpetranzan.security.token.Token;
import com.fpetranzan.security.token.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

	private final TokenRepository tokenRepository;

	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		final String jwt;

		if (authHeader == null || !authHeader.startsWith(AuthConstants.AUTHORIZATION_TYPE.BEARER)) {
			return;
		}

		jwt = authHeader.substring(7);
		Token storedToken = tokenRepository.findByToken(jwt).orElse(null);

		if(storedToken != null) {
			storedToken.setExpired(Boolean.TRUE);
			storedToken.setRevoked(Boolean.TRUE);

			tokenRepository.save(storedToken);
		}
	}
}
