package com.zunza.buythedip.external.binance.stream.manager

import com.fasterxml.jackson.databind.ObjectMapper
import com.zunza.buythedip.crypto.repository.CryptoRepository
import com.zunza.buythedip.external.binance.stream.handler.KlineWebSocketHandler
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import org.springframework.web.socket.client.WebSocketClient

private val logger = KotlinLogging.logger {  }

@Component
class KlineStreamManager(
    private val objectMapper: ObjectMapper,
    private val webSocketClient: WebSocketClient,
    private val cryptoRepository: CryptoRepository
) {
    companion object {
        private const val KLINE_STREAM_URL = "wss://data-stream.binance.vision/stream"
        private const val STREAM_SUFFIX = "usdt@kline_"
        private val KLINE_INTERVALS = listOf("1m", "3m", "5m", "15m", "30m", "1h", "4h", "1d", "1w", "1M")
        private const val SYMBOL_CHUNK_SIZE = 202
    }

    @EventListener(ApplicationReadyEvent::class)
    fun startStreaming() {
        val symbols = cryptoRepository.findAll()
            .map { it.symbol.lowercase() + STREAM_SUFFIX }

        if (symbols.isEmpty()) {
            logger.warn { "심볼이 존재하지 않습니다." }
            return
        }

        KLINE_INTERVALS.forEach { interval ->
            try {
                val handler = KlineWebSocketHandler(
                    interval = interval,
                    symbols = symbols,
                    chunkSize = SYMBOL_CHUNK_SIZE,
                    objectMapper = objectMapper
                )
                webSocketClient.execute(handler, KLINE_STREAM_URL)
            } catch (e: Exception) {
                logger.error { "[KLINE] 웹소켓 연결에 실패했습니다. INTERVAL: $interval. \nError: ${e.message}" }
                throw e
            }
        }
    }
}

