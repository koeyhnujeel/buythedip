package com.zunza.buythedip.external.binance.stream.manager;

import java.util.List;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.client.WebSocketClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zunza.buythedip.crypto.repository.CryptoRepository;
import com.zunza.buythedip.crypto.service.CryptoService;
import com.zunza.buythedip.external.binance.stream.handler.KlineStreamHandler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class KlineStreamManager {
	private final ObjectMapper objectMapper;
	private final WebSocketClient webSocketClient;
	private final CryptoService cryptoService;
	private final CryptoRepository cryptoRepository;

	private static final String KLINE_STREAM_URL = "wss://data-stream.binance.vision/stream";
	private static final String STREAM_SUFFIX = "usdt@kline_";
	private static final List<String> KLINE_INTERVALS = List.of("1m", "3m", "5m", "15m", "30m", "1h", "4h", "1d", "1w", "1M");
	private static final int CHUNK_SIZE = 203;

	@EventListener(ApplicationReadyEvent.class)
	public void startStreaming() {
		List<String> symbols = cryptoRepository.findAll().stream()
			.map(crypto -> crypto.getSymbol().toLowerCase() + STREAM_SUFFIX)
			.toList();

		if (symbols.isEmpty()) {
			log.warn("심볼이 존재하지 않습니다.");
			return;
		}

		KLINE_INTERVALS.forEach(interval -> {
			KlineStreamHandler handler = new KlineStreamHandler(
				interval,
				symbols,
				CHUNK_SIZE,
				objectMapper,
				cryptoService
			);

			webSocketClient.execute(handler, KLINE_STREAM_URL);
		});
	}
}
