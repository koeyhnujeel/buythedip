package com.zunza.buythedip.external.coinmarketcap.client

import com.zunza.buythedip.external.coinmarketcap.dto.Metadata
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import kotlin.jvm.java

@Component
class CoinMarketCapClient(
    @Value("\${coinMarketCap.key}") private val apiKey: String,
    webClientBuilder: WebClient.Builder
) {
    companion object {
        private const val BASE_URL = "https://pro-api.coinmarketcap.com"
    }

    private val webClient = webClientBuilder
        .baseUrl(BASE_URL)
        .defaultHeader("X-CMC_PRO_API_KEY", apiKey)
        .codecs { config -> config
            .defaultCodecs()
            .maxInMemorySize(19 * 1024 * 1024)
        }
        .build();

    fun getInfo(symbols: String): Metadata {
        return webClient.get()
            .uri { uriBuilder -> uriBuilder
                .path("/v2/cryptocurrency/info")
                .queryParam("skip_invalid", "true")
                .queryParam("symbol", symbols)
                .build()
            }
            .retrieve()
            .bodyToMono(Metadata::class.java)
            .block()!!
    }
}
