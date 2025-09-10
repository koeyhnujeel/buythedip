package com.zunza.buythedip.external.binance.stream.handler

import com.fasterxml.jackson.databind.ObjectMapper
import com.zunza.buythedip.crypto.repository.CryptoRepository
import com.zunza.buythedip.external.binance.dto.SubscribeRequest
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.client.WebSocketClient
import org.springframework.web.socket.handler.TextWebSocketHandler

private val logger = KotlinLogging.logger {  }

@Component
class TradeWebSocketHandler(
    private val objectMapper: ObjectMapper,
    private var webSocketClient: WebSocketClient,
    private val cryptoRepository: CryptoRepository
) : TextWebSocketHandler() {
    companion object {
        private const val TRADE_STREAM_URL = "wss://data-stream.binance.vision/stream"
        private const val STREAM_SUFFIX = "usdt@aggTrade"
    }

    @EventListener(ApplicationReadyEvent::class)
    fun connect() {
        try {
            webSocketClient.execute(this, TRADE_STREAM_URL)
            logger.info { "바이낸스 웹소켓과 연결됐습니다. Type: [@aggTrade]" }
        } catch (e: Exception) {
            logger.warn { "바이낸스 웹소켓 연결에 실패했습니다. Type: [@aggTrade]" }
            logger.warn { "$e.message\n${e.stackTraceToString()}" }
        }
    }

    override fun afterConnectionEstablished(session: WebSocketSession) {
        val symbols = cryptoRepository.findAll()
            .map { it.symbol.lowercase() + STREAM_SUFFIX }

        val payload = objectMapper.writeValueAsString(
            SubscribeRequest(
                "SUBSCRIBE",
                symbols,
                session.id
            )
        )

        session.sendMessage(TextMessage(payload))
    }

    override fun handleTextMessage(
        session: WebSocketSession,
        message: TextMessage
    ) {
        logger.info { "[TRADE] ${message.payload.toByteArray(Charsets.UTF_8).size}bytes" }
    }
}
