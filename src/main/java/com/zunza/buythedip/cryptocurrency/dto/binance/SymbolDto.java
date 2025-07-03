package com.zunza.buythedip.cryptocurrency.dto.binance;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class SymbolDto {

	@JsonProperty("baseAsset")
	private String baseAsset;

	@JsonProperty("quoteAsset")
	private String quoteAsset;

	@JsonProperty("status")
	private String status;
}
