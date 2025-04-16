package com.zunza.buythedip.infrastructure.gemini.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Builder;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContentDto {
	private List<PartDto> parts;

	@Builder
	private ContentDto(List<PartDto> parts) {
		this.parts = parts;
	}

	public static ContentDto from(List<PartDto> parts) {
		return ContentDto.builder()
			.parts(parts)
			.build();
	}
}
