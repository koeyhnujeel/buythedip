package com.zunza.buythedip.infrastructure.gemini.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Builder;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PartDto {

	private String text;

	@Builder
	private PartDto(String text) {
		this.text = text;
	}

	public static PartDto from(String text) {
		return PartDto.builder()
			.text(text)
			.build();
	}
}
