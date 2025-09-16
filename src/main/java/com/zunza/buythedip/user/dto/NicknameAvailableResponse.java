package com.zunza.buythedip.user.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NicknameAvailableResponse {
	private boolean isAvailable;

	public static NicknameAvailableResponse createFrom(boolean isAvailable) {
		return NicknameAvailableResponse.builder()
			.isAvailable(isAvailable)
			.build();
	}
}
