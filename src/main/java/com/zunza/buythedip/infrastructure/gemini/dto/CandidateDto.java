package com.zunza.buythedip.infrastructure.gemini.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Builder;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CandidateDto {
	private ContentDto content;

	@Builder
	private CandidateDto(ContentDto content) {
		this.content = content;
	}

	public static CandidateDto from(ContentDto content) {
		return CandidateDto.builder()
			.content(content)
			.build();
	}
}
