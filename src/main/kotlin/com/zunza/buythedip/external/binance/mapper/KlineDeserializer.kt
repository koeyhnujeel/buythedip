package com.zunza.buythedip.external.binance.mapper

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.zunza.buythedip.external.binance.dto.KlineRestApiResponse

class KlineDeserializer : JsonDeserializer<KlineRestApiResponse>() {
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): KlineRestApiResponse {
        val node = p.readValueAs(Array<Any>::class.java)
        return KlineRestApiResponse(
            openTime = (node[0] as Number).toLong(),
            open = (node[1] as String).toBigDecimal(),
            high = (node[2] as String).toBigDecimal(),
            low = (node[3] as String).toBigDecimal(),
            close = (node[4] as String).toBigDecimal(),
            volume = (node[5] as String).toBigDecimal(),
            closeTime = (node[6] as Number).toLong(),
            quoteAssetVolume = (node[7] as String).toBigDecimal(),
            numberOfTrades = (node[8] as Number).toLong(),
            takerBuyBaseVolume = (node[9] as String).toBigDecimal(),
            takerBuyQuoteVolume = (node[10] as String).toBigDecimal(),
            ignore = (node[11] as String).toBigDecimal()
        )
    }
}
