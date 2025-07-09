package com.zunza.buythedip.cryptocurrency.client;


import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zunza.buythedip.cryptocurrency.dto.binance.ExchangeInfoDto;
import com.zunza.buythedip.cryptocurrency.dto.binance.KlineDto;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class BinanceClient {

	private final WebClient webClient;
	private final ObjectMapper objectMapper;

	private static final String URL = "https://data-api.binance.vision";

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

	public Mono<Double> getDailyOpenPrice(String symbol) {
		return webClient.get()
			.uri(uriBuilder -> uriBuilder
				.path("/api/v3/klines")
				.queryParam("symbol", symbol)
				.queryParam("interval", "1d")
				.queryParam("limit", 1)
				.build()
			)
			.retrieve()
			.bodyToMono(String.class)
			.map(res -> parseOpenPrice(res, symbol))
			.onErrorReturn(Double.NaN);
	}

	public Mono<List<KlineDto>> getKlines(String symbol, String interval) {
		return webClient.get()
			.uri(uriBuilder -> uriBuilder
				.path("/api/v3/klines")
				.queryParam("symbol", symbol)
				.queryParam("interval", interval)
				.queryParam("limit", 100)
				.build()
			)
			.retrieve()
			.bodyToMono(new ParameterizedTypeReference<List<List<Object>>>() {})
			.map(res -> res.stream()
				.map(KlineDto::createKlineDtoForInit)
				.toList());
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
