package com.zunza.buythedip.common;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ApiResponse<T> {
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private T data;
	private int code;
	private boolean success;

	@Builder
	public ApiResponse(T data, int code) {
		this.data = data;
		this.code = code;
		this.success = code >= 200 && code < 300;
	}
}
