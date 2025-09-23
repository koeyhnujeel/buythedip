package com.zunza.buythedip.crypto.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.zunza.buythedip.external.binance.dto.KlineRestApiResponse;
import com.zunza.buythedip.external.binance.dto.klinestream.KlineData;
import com.zunza.buythedip.external.binance.dto.klinestream.KlineDetails;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ChartResponse {
	private static final String COLOR_BULLISH = "rgba(244, 67, 54, 1.0)";
	private static final String COLOR_BEARISH = "rgba(76, 175, 80, 1.0)";

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String symbol;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String interval;
	private Candle candle;
	private Volume volume;

	public static ChartResponse createHistoryResponse(KlineRestApiResponse response, int scale) {
		long time = response.getOpenTime() / 1000;
		BigDecimal open = response.getOpen().setScale(scale);
		BigDecimal high = response.getHigh().setScale(scale);
		BigDecimal low = response.getLow().setScale(scale);
		BigDecimal close = response.getClose().setScale(scale);
		BigDecimal value = response.getVolume().stripTrailingZeros();
		String color = open.compareTo(close) >= 0 ? COLOR_BULLISH : COLOR_BEARISH;

		Candle candle = new Candle(time, open, high, low, close);
		Volume volume = new Volume(time, value, color);

		return ChartResponse.builder()
			.candle(candle)
			.volume(volume)
			.build();
	}

	public static ChartResponse createUpdateResponse(KlineData data, int scale) {
		KlineDetails details = data.getDetails();

		long time = details.getTime() / 1000;
		BigDecimal open = details.getOpen().setScale(scale);
		BigDecimal high = details.getHigh().setScale(scale);
		BigDecimal low = details.getLow().setScale(scale);
		BigDecimal close = details.getClose().setScale(scale);
		BigDecimal value = details.getVolume().stripTrailingZeros();
		String color = open.compareTo(close) >= 0 ? COLOR_BULLISH : COLOR_BEARISH;

		String symbol = data.getSymbol().replace("USDT", "");
		String interval = details.getInterval();

		Candle candle = new Candle(time, open, high, low, close);
		Volume volume = new Volume(time, value, color);

		return ChartResponse.builder()
			.symbol(symbol)
			.interval(interval)
			.candle(candle)
			.volume(volume)
			.build();
	}

	@Getter
	@AllArgsConstructor
	public static class Candle {
		private long time;
		private BigDecimal open;
		private BigDecimal high;
		private BigDecimal low;
		private BigDecimal close;
	}

	@Getter
	@AllArgsConstructor
	public static class Volume {
		private long time;
		private BigDecimal value;
		private String color;
	}
}
