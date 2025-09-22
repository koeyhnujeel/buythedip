package com.zunza.buythedip.external.binance.dto.klinestream;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class KlineStreamResponse {
	private KlineData data;
}
