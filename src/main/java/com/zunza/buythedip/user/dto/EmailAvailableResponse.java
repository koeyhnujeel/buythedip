package com.zunza.buythedip.user.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class EmailAvailableResponse {
	private boolean isAvailable;

	public static EmailAvailableResponse createFrom(boolean isAvailable) {
		return EmailAvailableResponse.builder()
			.isAvailable(isAvailable)
			.build();
	}
}
