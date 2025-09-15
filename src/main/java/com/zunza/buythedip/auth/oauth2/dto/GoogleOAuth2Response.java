package com.zunza.buythedip.auth.oauth2.dto;

import java.util.Map;

import com.zunza.buythedip.user.constant.OAuth2Provider;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GoogleOAuth2Response implements OAuth2Response {
	private final Map<String, Object> attribute;

	@Override
	public OAuth2Provider getProvider() {
		return OAuth2Provider.GOOGLE;
	}

	@Override
	public String getProviderId() {
		return attribute.get("sub").toString();
	}

	@Override
	public String getEmail() {
		return attribute.get("email").toString();
	}
}
