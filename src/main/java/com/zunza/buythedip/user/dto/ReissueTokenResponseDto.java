package com.zunza.buythedip.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ReissueTokenResponseDto {
	private String newAccessToken;

	@Builder
	private ReissueTokenResponseDto(String newAccessToken) {
		this.newAccessToken = newAccessToken;
	}

	public static ReissueTokenResponseDto from(String newAccessToken) {
		return ReissueTokenResponseDto.builder()
			.newAccessToken(newAccessToken)
			.build();
	}
}
