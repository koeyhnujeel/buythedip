package com.zunza.buythedip.cryptocurrency.dto.binance;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;


@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TradeDto {

	@JsonProperty("s")
	private String symbol;

	@JsonProperty("p")
	private double price;

	@JsonProperty("q")
	private double quantity;

	@JsonProperty("T")
	private long tradeTime;
}
