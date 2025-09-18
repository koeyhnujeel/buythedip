package com.zunza.buythedip.auth.oauth2.dto;

import java.util.Map;

import com.zunza.buythedip.user.constant.OAuth2Provider;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class KakaoOAuth2Response implements OAuth2Response {
	private final Map<String, Object> attribute;

	@Override
	public OAuth2Provider getProvider() {
		return OAuth2Provider.KAKAO;
	}

	@Override
	public String getProviderId() {
		return attribute.get("id").toString();
	}

	@Override
	public String getEmail() {
		Map<String, Object> kakaoAccount = (Map<String, Object>) attribute.get("kakao_account");
		return kakaoAccount.get("email").toString();
	}
}
