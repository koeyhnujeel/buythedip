package com.zunza.buythedip.external.binance.dto

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.zunza.buythedip.external.binance.mapper.KlineDeserializer
import java.math.BigDecimal

@JsonDeserialize(using = KlineDeserializer::class)
data class KlineRestApiResponse(
    val openTime: Long,
    val open: BigDecimal,
    val high: BigDecimal,
    val low: BigDecimal,
    val close: BigDecimal,
    val volume: BigDecimal,
    val closeTime: Long,
    val quoteAssetVolume: BigDecimal,
    val numberOfTrades: Long,
    val takerBuyBaseVolume: BigDecimal,
    val takerBuyQuoteVolume: BigDecimal,
    val ignore: BigDecimal
)
