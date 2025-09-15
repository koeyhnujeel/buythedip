package com.zunza.buythedip.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginResponse {
	private String nickname;
	private String accessToken;

	@Builder
	private LoginResponse(String nickname, String accessToken) {
		this.nickname = nickname;
		this.accessToken = accessToken;
	}

	public static LoginResponse createOf(
		String nickname,
		String accessToken
	) {
		return LoginResponse.builder()
			.nickname(nickname)
			.accessToken(accessToken)
			.build();
	}
}
