package com.zunza.buythedip.infrastructure.gemini.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Builder;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class GeminiResponseDto {
	private List<CandidateDto> candidates;

	@Builder
	private GeminiResponseDto(List<CandidateDto> candidates) {
		this.candidates = candidates;
	}

	public static GeminiResponseDto from(List<CandidateDto> candidates) {
		return GeminiResponseDto.builder()
			.candidates(candidates)
			.build();
	}

	public String getResult() {
		return candidates.get(0)
			.getContent()
			.getParts().get(0)
			.getText();
	}
}
