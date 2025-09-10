package com.zunza.buythedip.external.binance.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class ExchangeInfo(
    val symbols: List<Symbol>
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Symbol(
    val baseAsset: String,
    val quoteAsset: String,
    val status: String
)
