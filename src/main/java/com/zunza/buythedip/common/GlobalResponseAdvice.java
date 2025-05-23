package com.zunza.buythedip.common;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import jakarta.servlet.http.HttpServletResponse;

@RestControllerAdvice
public class GlobalResponseAdvice implements ResponseBodyAdvice<Object> {

	@Autowired
	private HttpServletResponse httpServletResponse;

	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		return returnType.getContainingClass().isAnnotationPresent(RestController.class);
	}

	@Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
		Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
		ServerHttpResponse response) {

		int statusCode = httpServletResponse.getStatus();

		if (body == null) {
			return null;
		}

		if (body instanceof ApiResponse<?>) {
			return body;
		}

		return ApiResponse.builder()
			.data(body)
			.code(statusCode)
			.build();
	}

	@ExceptionHandler(CustomException.class)
	public ResponseEntity<ApiResponse<Object>> CustomExceptionHandler(CustomException e) {
		ErrorResponse errorResponse = ErrorResponse.builder()
			.message(e.getMessage())
			.build();

		ApiResponse<Object> apiResponse = ApiResponse.builder()
			.data(errorResponse)
			.code(e.getStatusCode())
			.build();

		return ResponseEntity.status(e.getStatusCode()).body(apiResponse);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiResponse<Object>> methodArgumentNotValidExceptionHandler(
		MethodArgumentNotValidException e) {

		List<String> errorMessages = e.getFieldErrors().stream()
			.map(DefaultMessageSourceResolvable::getDefaultMessage)
			.toList();

		String message = errorMessages.size() == 1 ? errorMessages.get(0) : null;
		List<String> messages = errorMessages.size() > 1 ? errorMessages : null;

		ErrorResponse errorResponse = ErrorResponse.builder()
			.message(message)
			.messages(messages)
			.build();

		ApiResponse<Object> apiResponse = ApiResponse.builder()
			.data(errorResponse)
			.code(e.getStatusCode().value())
			.build();

		return ResponseEntity.status(e.getStatusCode()).body(apiResponse);
	}

	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<ApiResponse<Object>> authenticationExceptionHandler(
		AuthenticationException e
	) {
		String message = "아이디 또는 비밀번호를 확인해 주세요.";
		ErrorResponse errorResponse = ErrorResponse.builder()
			.message(message)
			.build();

		int statusCode = HttpServletResponse.SC_UNAUTHORIZED;
		ApiResponse<Object> apiResponse = ApiResponse.builder()
			.data(errorResponse)
			.code(statusCode)
			.build();

		return ResponseEntity.status(statusCode).body(apiResponse);
	}
}
