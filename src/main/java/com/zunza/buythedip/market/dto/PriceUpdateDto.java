package com.zunza.buythedip.market.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PriceUpdateDto {

	@JsonProperty("s")
	private String symbol;

	@JsonProperty("p")
	private double price;

	public void modifySymbol(String newSymbol) {
		this.symbol = newSymbol;
	}
}
