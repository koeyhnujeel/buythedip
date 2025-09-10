package com.zunza.buythedip.external.binance.stream.handler

import com.fasterxml.jackson.databind.ObjectMapper
import com.zunza.buythedip.external.binance.dto.SubscribeRequest
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler

private val logger = KotlinLogging.logger {  }

class KlineWebSocketHandler(
    private val interval: String,
    private val symbols: List<String>,
    private val chunkSize: Int,
    private val objectMapper: ObjectMapper
) : TextWebSocketHandler() {

    override fun afterConnectionEstablished(session: WebSocketSession) {
        val symbolChunks = symbols.chunked(chunkSize)

        symbolChunks.forEachIndexed { index, chunk ->
            val subscribeParams = chunk.map { it + interval }
            val payload = objectMapper.writeValueAsString(
                SubscribeRequest(
                    method = "SUBSCRIBE",
                    params = subscribeParams,
                    id = (index + 1).toString()
                )
            )

            session.sendMessage(TextMessage(payload))
            logger.info{"[SESSION ID: ${session.id}] Subscription request sent for ${chunk.size} symbols." +
                    " (Chunk ${index + 1}/${symbolChunks.size}, Interval: $interval)"}

            Thread.sleep(200)
        }
    }

    override fun handleTextMessage(
        session: WebSocketSession,
        message: TextMessage
    ) {
        logger.info{"[KLINE - $interval] ${message.payload.toByteArray(Charsets.UTF_8).size}bytes"}
    }
}
