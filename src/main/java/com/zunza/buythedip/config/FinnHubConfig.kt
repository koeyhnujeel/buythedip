package com.zunza.buythedip.config

import io.finnhub.api.apis.DefaultApi
import io.finnhub.api.infrastructure.ApiClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class FinnHubConfig {

    @Value("\${finnhub.secret}")
    lateinit var apiKey: String

    @Bean
    open fun finnHubClient(): DefaultApi {
        ApiClient.apiKey["token"] = apiKey
        return DefaultApi()
    }
}
