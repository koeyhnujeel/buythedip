package com.zunza.buythedip.external.binance.dto.klinestream;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class KlineDetails {
	@JsonProperty("i")
	private String interval;

	@JsonProperty("t")
	private long time;

	@JsonProperty("o")
	private BigDecimal open;

	@JsonProperty("h")
	private BigDecimal high;

	@JsonProperty("l")
	private BigDecimal low;

	@JsonProperty("c")
	private BigDecimal close;

	@JsonProperty("v")
	private BigDecimal volume;
}
