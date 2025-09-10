package com.zunza.buythedip.external.binance.client

import com.zunza.buythedip.external.binance.dto.ExchangeInfo
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient

@Component
class BinanceClient(
    webClientBuilder: WebClient.Builder
) {
    companion object {
        private const val BASE_URL = "https://data-api.binance.vision"
    }

    private val webClient = webClientBuilder
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .baseUrl(BASE_URL)
        .codecs { config -> config
            .defaultCodecs()
            .maxInMemorySize(19 * 1024 * 1024)
        }
        .build()

    fun getExchangeInfo(): ExchangeInfo {
        return webClient.get()
            .uri { uriBuilder -> uriBuilder
                .path("/api/v3/exchangeInfo")
                .queryParam("permissions", "SPOT")
                .build()
            }
            .retrieve()
            .bodyToMono(ExchangeInfo::class.java)
            .block()!!
    }
}
