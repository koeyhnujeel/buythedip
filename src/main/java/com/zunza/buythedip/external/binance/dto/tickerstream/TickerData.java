package com.zunza.buythedip.external.binance.dto.tickerstream;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TickerData {
	@JsonProperty("s")
	private String symbol;

	@JsonProperty("c")
	private BigDecimal closePrice;
}
