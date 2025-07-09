package com.zunza.buythedip.cryptocurrency.service;

import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractBinanceStreamReceiver extends TextWebSocketHandler {

	protected final WebSocketClient webSocketClient;
	protected final ObjectMapper objectMapper;
	protected String url;

	protected AbstractBinanceStreamReceiver(
		WebSocketClient webSocketClient,
		ObjectMapper objectMapper,
		String url
	) {
		this.webSocketClient = webSocketClient;
		this.objectMapper = objectMapper;
		this.url = url;
	}

	@PostConstruct
	public void connect() {
		try {
			log.info("[{}] Connecting to Binance WebSocket", getClass().getSimpleName());
			webSocketClient.execute(this, url).get();
			log.info("[{}] Connected successfully", getClass().getSimpleName());
		} catch (Exception e) {
			log.error("[{}] Connection failed: {}", getClass().getSimpleName(), e.getMessage());
		}
	}
}
