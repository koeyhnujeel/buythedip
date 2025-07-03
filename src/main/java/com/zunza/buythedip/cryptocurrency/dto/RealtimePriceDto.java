package com.zunza.buythedip.cryptocurrency.dto;


import lombok.Builder;
import lombok.Getter;

@Getter
public class RealtimePriceDto {
	private String symbol;
	private double currentPrice;
	private double changeRate;

	@Builder
	private RealtimePriceDto(String symbol, double currentPrice, double changeRate) {
		this.symbol = symbol;
		this.currentPrice = currentPrice;
		this.changeRate = changeRate;
	}

	public static RealtimePriceDto of(String symbol, double currentPrice, double changeRate) {
		return RealtimePriceDto.builder()
			.symbol(symbol)
			.currentPrice(currentPrice)
			.changeRate(changeRate)
			.build();

	}
}
