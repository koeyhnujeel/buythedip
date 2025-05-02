package com.zunza.buythedip.security;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zunza.buythedip.common.ApiResponse;
import com.zunza.buythedip.common.ErrorResponse;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtExceptionFilter extends OncePerRequestFilter {

	private final ObjectMapper objectMapper;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {

		try{
			filterChain.doFilter(request, response);
		} catch (ExpiredJwtException e) {
			ErrorResponse errorResponse = ErrorResponse.builder()
				.message("만료된 JWT 토큰 입니다.")
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
}
