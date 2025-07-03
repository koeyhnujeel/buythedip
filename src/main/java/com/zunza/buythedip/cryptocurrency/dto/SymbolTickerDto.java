package com.zunza.buythedip.cryptocurrency.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SymbolTickerDto {
	private String symbol;
	private double currentPrice;
	private double changePrice;
	private double changeRate;

	@Builder
	public SymbolTickerDto(String symbol, double currentPrice, double changePrice, double changeRate) {
		this.symbol = symbol;
		this.currentPrice = currentPrice;
		this.changePrice = changePrice;
		this.changeRate = changeRate;
	}

	public static SymbolTickerDto of(String symbol, double currentPrice, double changePrice, double changeRate) {
		return SymbolTickerDto.builder()
			.symbol(symbol)
			.currentPrice(currentPrice)
			.changePrice(changePrice)
			.changeRate(changeRate)
			.build();
	}
}
