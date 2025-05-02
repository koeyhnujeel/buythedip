package com.zunza.buythedip.security;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zunza.buythedip.common.ApiResponse;
import com.zunza.buythedip.common.ErrorResponse;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

	private final ObjectMapper objectMapper;

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException authException) throws IOException, ServletException {

		ErrorResponse errorResponse = ErrorResponse.builder()
			.message("로그인이 필요한 서비스 입니다.")
			.build();

		ApiResponse<Object> apiResponse = ApiResponse.builder()
			.data(errorResponse)
			.code(HttpServletResponse.SC_UNAUTHORIZED)
			.build();

		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		objectMapper.writeValue(response.getWriter(), apiResponse);
	}
}
