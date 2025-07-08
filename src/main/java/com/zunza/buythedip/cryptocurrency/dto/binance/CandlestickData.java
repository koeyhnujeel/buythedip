package com.zunza.buythedip.cryptocurrency.dto.binance;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CandlestickData {
	private long time;
	private double open;
	private double high;
	private double low;
	private double close;

	@Builder
	public CandlestickData(long time, double open, double high, double low, double close) {
		this.time = time;
		this.open = open;
		this.high = high;
		this.low = low;
		this.close = close;
	}
}
