package com.zunza.buythedip.cryptocurrency.client;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zunza.buythedip.cryptocurrency.dto.binance.ExchangeInfoDto;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class BinanceClient {

	private final WebClient webClient;
	private final ObjectMapper objectMapper;

	private static final String URL = "https://api.binance.com";

	public BinanceClient(
		WebClient.Builder webClientBuilder,
		ObjectMapper objectMapper
	) {
		this.objectMapper = objectMapper;
		this.webClient = webClientBuilder
			.baseUrl(URL)
			// .codecs(config -> config
			// 	.defaultCodecs()
			// 	.maxInMemorySize(19 * 1024 * 1024))
			.build();
	}

	public ExchangeInfoDto getExchangeInfo() {
		return webClient.get()
			.uri(uriBuilder -> uriBuilder
				.path("/api/v3/exchangeInfo")
				.queryParam("permissions", "SPOT")
				.build()
			)
			.retrieve()
			.bodyToMono(ExchangeInfoDto.class)
			.block();
	}

	public Mono<Double> getDailyOpenPrice(String symbol, LocalDate date) {
		Instant startInstant = date.atStartOfDay(ZoneOffset.UTC).toInstant();
		long startTime = startInstant.toEpochMilli();

		return webClient.get()
			.uri(uriBuilder -> uriBuilder
				.path("/api/v3/klines")
				.queryParam("symbol", symbol)
				.queryParam("interval", "1d")
				.queryParam("startTime", startTime)
				.queryParam("limit", 1)
				.build()
			)
			.retrieve()
			.bodyToMono(String.class)
			.map(res -> parseOpenPrice(res, symbol))
			.onErrorReturn(Double.NaN);
	}

	private Double parseOpenPrice(String res, String symbol) {
		try {
			JsonNode jsonNode = objectMapper.readTree(res);

			if (jsonNode == null || jsonNode.isEmpty()) {
				log.info("No kline data found for symbol: {}", symbol);
				return Double.NaN;
			}

			JsonNode kline = jsonNode.get(0);
			String openPriceStr = kline.get(1).asText();
			double openPrice = Double.parseDouble(openPriceStr);

			log.info("Successfully fetched open price: {} for symbol: {}", openPrice, symbol);
			return openPrice;

		} catch (Exception e) {
			log.error("Error parsing kline data for symbol: {}", symbol);
			log.error(e.getMessage());
			return Double.NaN;
		}
	}
}
