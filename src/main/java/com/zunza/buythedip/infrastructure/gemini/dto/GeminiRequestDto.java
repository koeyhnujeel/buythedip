package com.zunza.buythedip.infrastructure.gemini.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GeminiRequestDto {
	private List<ContentDto> contents;

	@Builder
	private GeminiRequestDto(List<ContentDto> contents) {
		this.contents = contents;
	}

	public static GeminiRequestDto from(List<ContentDto> contents) {
		return GeminiRequestDto.builder()
			.contents(contents)
			.build();
	}
}
