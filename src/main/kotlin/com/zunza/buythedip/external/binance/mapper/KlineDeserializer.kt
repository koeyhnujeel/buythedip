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
            open = (node[1] as String).toDouble(),
            high = (node[2] as String).toDouble(),
            low = (node[3] as String).toDouble(),
            close = (node[4] as String).toDouble(),
            volume = (node[5] as String).toDouble(),
            closeTime = (node[6] as Number).toLong(),
            quoteAssetVolume = (node[7] as String).toDouble(),
            numberOfTrades = (node[8] as Number).toLong(),
            takerBuyBaseVolume = (node[9] as String).toDouble(),
            takerBuyQuoteVolume = (node[10] as String).toDouble(),
            ignore = (node[11] as String).toDouble()
        )
    }
}
