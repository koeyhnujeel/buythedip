package com.zunza.buythedip.auth.oauth2.dto;

import java.util.Map;

import com.zunza.buythedip.user.constant.OAuth2Provider;

import lombok.RequiredArgsConstructor;

public class NaverOAuth2Response implements OAuth2Response {
	private final Map<String, Object> response;

	public NaverOAuth2Response(
		Map<String, Object> attribute
	) {
		this.response = (Map<String, Object>) attribute.get("response");
	}

	@Override
	public OAuth2Provider getProvider() {
		return OAuth2Provider.NAVER;
	}

	@Override
	public String getProviderId() {
		return response.get("id").toString();
	}

	@Override
	public String getEmail() {
		return response.get("email").toString();
	}
}
