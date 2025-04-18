package com.zunza.buythedip.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
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
			return ApiResponse.builder()
				.data(null)
				.code(statusCode)
				.build();
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
}
