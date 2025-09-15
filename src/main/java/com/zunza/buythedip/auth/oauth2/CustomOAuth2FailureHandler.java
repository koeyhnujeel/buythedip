package com.zunza.buythedip.auth.oauth2;


import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.Charset;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomOAuth2FailureHandler extends SimpleUrlAuthenticationFailureHandler {

	@Override
	public void onAuthenticationFailure(
		HttpServletRequest request,
		HttpServletResponse response,
		AuthenticationException exception
	) throws IOException, ServletException {
		String errorMessage = URLEncoder.encode(exception.getMessage(), Charset.defaultCharset());
		String redirectUrl = "http://localhost:5173/login/error?message=" + errorMessage;

		response.sendRedirect(redirectUrl);
	}
}
