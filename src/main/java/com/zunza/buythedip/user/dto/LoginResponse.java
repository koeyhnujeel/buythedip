package com.zunza.buythedip.user.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class LoginResponse {
	private String nickname;
	private String accessToken;

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
