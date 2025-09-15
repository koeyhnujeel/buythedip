package com.zunza.buythedip.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class EmailAvailableResponse {
	private boolean isAvailable;

	@Builder
	private EmailAvailableResponse(boolean isAvailable) {
		this.isAvailable = isAvailable;
	}

	public static EmailAvailableResponse createFrom(boolean isAvailable) {
		return EmailAvailableResponse.builder()
			.isAvailable(isAvailable)
			.build();
	}
}
