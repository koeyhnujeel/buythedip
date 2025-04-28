package com.zunza.buythedip.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginSuccessResponseDto {
	private String nickname;
	private String accessToken;

	@Builder
	private LoginSuccessResponseDto(String nickname, String accessToken) {
		this.nickname = nickname;
		this.accessToken = accessToken;
	}

	public static LoginSuccessResponseDto of(String nickname, String accessToken) {
		return LoginSuccessResponseDto.builder()
			.nickname(nickname)
			.accessToken(accessToken)
			.build();
	}
}
