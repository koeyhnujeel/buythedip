package com.zunza.buythedip.external.binance.client;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.zunza.buythedip.external.binance.dto.KlineRestApiResponse;

import reactor.core.publisher.Mono;

@Component
public class BinanceClient {

	private static final String BASE_URL = "https://data-api.binance.vision";

	private final WebClient webClient;

	public BinanceClient(WebClient.Builder webClientBuilder) {
		this.webClient = webClientBuilder
			.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
			.baseUrl(BASE_URL)
			// .codecs(config -> config
			// 	.defaultCodecs()
			// 	.maxInMemorySize(19 * 1024 * 1024)
			// )
			.build();
	}

	public Mono<List<KlineRestApiResponse>> getKline(String symbol, String interval, int limit) {
		return webClient.get()
			.uri(uriBuilder -> uriBuilder
				.path("/api/v3/klines")
				.queryParam("symbol", symbol)
				.queryParam("interval", interval)
				.queryParam("limit", limit)
				.build()
			)
			.retrieve()
			.bodyToMono(new ParameterizedTypeReference<>() {
			});
	}
}
