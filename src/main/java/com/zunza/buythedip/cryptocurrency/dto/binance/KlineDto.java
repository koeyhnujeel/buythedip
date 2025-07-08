package com.zunza.buythedip.cryptocurrency.dto.binance;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.Builder;
import lombok.Getter;

@Getter
public class KlineDto {
	private String symbol;
	private String interval;
	private CandlestickData candlestick;
	private VolumeData volumeData;

	public static final String COLOR_BULLISH = "rgba(0, 150, 136, 0.8)";
	public static final String COLOR_BEARISH = "rgba(0, 150, 136, 0.8)";

	@Builder
	public KlineDto(String symbol, String interval, CandlestickData candlestick, VolumeData volumeData) {
		this.symbol = symbol;
		this.interval = interval;
		this.candlestick = candlestick;
		this.volumeData = volumeData;
	}

	public static KlineDto createKlineDtoForUpdate(JsonNode jsonNode) {
		String symbol = jsonNode.get("s").asText();
		String baseSymbol = symbol.replace("USDT", "");
		JsonNode klineNode = jsonNode.get("k");
		String interval = klineNode.get("i").asText();

		long time = klineNode.get("t").asLong() / 1000;
		double open = klineNode.get("o").asDouble();
		double high = klineNode.get("h").asDouble();
		double low = klineNode.get("l").asDouble();
		double close = klineNode.get("c").asDouble();
		double volume = klineNode.get("v").asDouble();
		String color = (close >= open) ? COLOR_BULLISH : COLOR_BEARISH;

		CandlestickData candlestickData = CandlestickData.builder()
			.time(time)
			.open(open)
			.high(high)
			.low(low)
			.close(close)
			.build();

		VolumeData volumeData = VolumeData.builder()
			.time(time)
			.value(volume)
			.color(color)
			.build();

		return KlineDto.builder()
			.symbol(baseSymbol)
			.interval(interval)
			.candlestick(candlestickData)
			.volumeData(volumeData)
			.build();
	}

	public static KlineDto createKlineDtoForInit(List<Object> list) {
		long time = ((Number)list.get(0)).longValue() / 1000;
		double open = Double.parseDouble(String.valueOf(list.get(1)));
		double high = Double.parseDouble(String.valueOf(list.get(2)));
		double low = Double.parseDouble(String.valueOf(list.get(3)));
		double close = Double.parseDouble(String.valueOf(list.get(4)));
		double volume = Double.parseDouble(String.valueOf(list.get(5)));
		String color = (open >= close) ? COLOR_BULLISH : COLOR_BEARISH;

		CandlestickData candle = CandlestickData.builder()
			.time(time)
			.open(open)
			.high(high)
			.low(low)
			.close(close)
			.build();

		VolumeData volumeData = VolumeData.builder()
			.time(time)
			.value(volume)
			.color(color)
			.build();

		return KlineDto.builder()
			.candlestick(candle)
			.volumeData(volumeData)
			.build();
	}

}
