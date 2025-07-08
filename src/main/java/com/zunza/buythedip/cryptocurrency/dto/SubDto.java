package com.zunza.buythedip.cryptocurrency.dto;

import java.util.List;

import lombok.Getter;

@Getter
public class SubDto {
	private String method;
	private List<String> params;
	private String id;

	public SubDto(String method, List<String> params, String id) {
		this.method = method;
		this.params = params;
		this.id = id;
	}
}
