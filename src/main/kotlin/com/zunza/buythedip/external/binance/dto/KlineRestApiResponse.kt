package com.zunza.buythedip.external.binance.dto

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.zunza.buythedip.external.binance.mapper.KlineDeserializer
import java.math.BigDecimal

@JsonDeserialize(using = KlineDeserializer::class)
data class KlineRestApiResponse(
    val openTime: Long,
    val open: Double,
    val high: Double,
    val low: Double,
    val close: Double,
    val volume: Double,
    val closeTime: Long,
    val quoteAssetVolume: Double,
    val numberOfTrades: Long,
    val takerBuyBaseVolume: Double,
    val takerBuyQuoteVolume: Double,
    val ignore: Double
)
