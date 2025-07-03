package com.zunza.buythedip.cryptocurrency.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.zunza.buythedip.cryptocurrency.dto.cmc.CryptoCurrencyMetadataDto;

@Component
public class CoinMarketCapClient {

	private final WebClient webClient;

	private static final String URL = "https://pro-api.coinmarketcap.com";

	public CoinMarketCapClient(
		@Value("${coinMarketCap.key}")
		String apiKey,
		WebClient.Builder webClientBuilder
		) {
		this.webClient = webClientBuilder
			.baseUrl(URL)
			.defaultHeader("X-CMC_PRO_API_KEY", apiKey)
			.build();
	}

	public CryptoCurrencyMetadataDto getCryptoCurrencyMetadata(String symbolParam) {
		return webClient.get()
			.uri(uriBuilder -> uriBuilder
				.path("/v2/cryptocurrency/info")
				.queryParam("skip_invalid", "true")
				.queryParam("symbol", symbolParam)
				.build()
			)
			.retrieve()
			.bodyToMono(CryptoCurrencyMetadataDto.class)
			.block();
	}
}
