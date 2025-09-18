package com.zunza.buythedip.crypto.dto;

import java.math.BigDecimal;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TickerResponse {
	private String symbol;
	private BigDecimal currentPrice;
	private BigDecimal changePrice;
	private BigDecimal changeRate;

	public static TickerResponse createOf(
		String symbol,
		BigDecimal currentPrice,
		BigDecimal changePrice,
		BigDecimal changeRate
	) {
		return TickerResponse.builder()
			.symbol(symbol)
			.currentPrice(currentPrice)
			.changePrice(changePrice)
			.changeRate(changeRate)
			.build();
	}
}
