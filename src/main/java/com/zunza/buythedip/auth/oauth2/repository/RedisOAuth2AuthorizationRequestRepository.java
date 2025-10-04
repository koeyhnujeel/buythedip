package com.zunza.buythedip.auth.oauth2.repository;

import java.util.concurrent.TimeUnit;

import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zunza.buythedip.infrastructure.redis.service.RedisCacheService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RedisOAuth2AuthorizationRequestRepository implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {
	private final static String AUTH_REQUEST_PREFIX = "OAUTH2_AUTH_REQUEST:";
	private final RedisCacheService cacheService;
	private final ObjectMapper objectMapper;

	@Override
	public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
		try {
			String key = getKey(request.getParameter("state"));
			String value = cacheService.get(key);
			return objectMapper.readValue(value, OAuth2AuthorizationRequest.class);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void saveAuthorizationRequest(
		OAuth2AuthorizationRequest authorizationRequest,
		HttpServletRequest request,
		HttpServletResponse response
	) {
		try {
			String key = getKey(authorizationRequest.getState());
			String authRequest = objectMapper.writeValueAsString(authorizationRequest);
			cacheService.set(key, authRequest, 5L, TimeUnit.MINUTES);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public OAuth2AuthorizationRequest removeAuthorizationRequest(
		HttpServletRequest request,
		HttpServletResponse response
	) {
		try {
			String key = getKey(request.getParameter("state"));
			String value = cacheService.get(key);
			cacheService.delete(key);
			return objectMapper.readValue(value, OAuth2AuthorizationRequest.class);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	private String getKey(String state) {
		return AUTH_REQUEST_PREFIX + state;
	}
}
