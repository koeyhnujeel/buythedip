package com.zunza.buythedip.external.binance.dto.tickerstream;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TickerStreamResponse {
	private TickerData data;
}
