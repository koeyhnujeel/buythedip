package com.zunza.buythedip.auth.jwt;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zunza.buythedip.common.ApiResponse;
import com.zunza.buythedip.common.ErrorResponse;

import io.jsonwebtoken.JwtException;
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
	protected void doFilterInternal(
		HttpServletRequest request,
		HttpServletResponse response,
		FilterChain filterChain
	) throws ServletException, IOException {

		try {
			filterChain.doFilter(request, response);
		} catch (JwtException e) {
			ErrorResponse errorResponse = ErrorResponse.builder()
				.message(e.getMessage())
				.build();

			ApiResponse<Object> apiResponse = ApiResponse.builder()
				.data(errorResponse)
				.code(HttpStatus.UNAUTHORIZED.value())
				.build();

			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			response.setCharacterEncoding("UTF-8");

			objectMapper.writeValue(response.getWriter(), apiResponse);
		}
	}
}
