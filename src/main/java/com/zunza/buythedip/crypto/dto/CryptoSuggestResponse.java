package com.zunza.buythedip.crypto.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CryptoSuggestResponse {
	private Long id;
	private String name;
	private String symbol;
	private String logo;
}
