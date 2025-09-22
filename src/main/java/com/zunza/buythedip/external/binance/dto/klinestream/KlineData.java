package com.zunza.buythedip.external.binance.dto.klinestream;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class KlineData {
	@JsonProperty("s")
	private String symbol;

	@JsonProperty("k")
	private KlineDetails details;
}
