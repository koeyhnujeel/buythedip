package com.zunza.buythedip.external.binance.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.zunza.buythedip.external.binance.dto.deserializer.KlineDeserializer;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonDeserialize(using = KlineDeserializer.class)
public class KlineRestApiResponse {
	private Long openTime;
	private BigDecimal open;
	private BigDecimal high;
	private BigDecimal low;
	private BigDecimal close;
	private BigDecimal volume;
	private Long closeTime;
	private BigDecimal quoteAssetVolume;
	private Long numberOfTrades;
	private BigDecimal takerBuyBaseVolume;
	private BigDecimal takerBuyQuoteVolume;
	private BigDecimal ignore;
}
