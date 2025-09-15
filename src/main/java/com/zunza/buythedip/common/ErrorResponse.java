package com.zunza.buythedip.common;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ErrorResponse {
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String message;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private List<String> messages;

	@Builder
	public ErrorResponse(String message, List<String> messages) {
		this.message = message;
		this.messages = messages;
	}
}
