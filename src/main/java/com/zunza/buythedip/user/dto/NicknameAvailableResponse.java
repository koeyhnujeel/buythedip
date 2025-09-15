package com.zunza.buythedip.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class NicknameAvailableResponse {
	private boolean isAvailable;

	@Builder
	private NicknameAvailableResponse(boolean isAvailable) {
		this.isAvailable = isAvailable;
	}

	public static NicknameAvailableResponse createFrom(boolean isAvailable) {
		return NicknameAvailableResponse.builder()
			.isAvailable(isAvailable)
			.build();
	}
}
