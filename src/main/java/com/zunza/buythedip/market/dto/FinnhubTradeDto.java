package com.zunza.buythedip.market.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class FinnhubTradeDto {

	@JsonProperty("data")
	private List<PriceUpdateDto> data;

	@JsonProperty("type")
	private String type;
}
