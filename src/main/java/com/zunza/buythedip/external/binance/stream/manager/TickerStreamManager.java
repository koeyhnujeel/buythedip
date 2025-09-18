package com.zunza.buythedip.external.binance.stream.manager;

import java.util.List;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.client.WebSocketClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zunza.buythedip.crypto.repository.CryptoRepository;
import com.zunza.buythedip.crypto.service.CryptoService;
import com.zunza.buythedip.external.binance.stream.handler.TickerStreamHandler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class TickerStreamManager {
	private final ObjectMapper objectMapper;
	private final WebSocketClient webSocketClient;
	private final CryptoService cryptoService;
	private final CryptoRepository cryptoRepository;

	private static final String TICKER_STREAM_URL = "wss://data-stream.binance.vision/stream";
	private static final String TICKER_STREAM_SUFFIX = "usdt@miniTicker";
	private static final int CHUNK_SIZE = 203;

	@EventListener(ApplicationReadyEvent.class)
	public void startStreaming() {
		List<String> symbols = cryptoRepository.findAll().stream()
			.map(crypto -> crypto.getSymbol().toLowerCase() + TICKER_STREAM_SUFFIX)
			.toList();

		if (symbols.isEmpty()) {
			log.warn("심볼이 존재하지 않습니다.");
			return;
		}

		TickerStreamHandler handler = new TickerStreamHandler(
			symbols,
			CHUNK_SIZE,
			objectMapper,
			cryptoService
		);

		webSocketClient.execute(handler, TICKER_STREAM_URL);
	}
}
