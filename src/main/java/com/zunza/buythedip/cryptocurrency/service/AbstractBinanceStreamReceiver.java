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

	protected static final String URL = "wss://data-stream.binance.vision/stream";

	protected AbstractBinanceStreamReceiver(WebSocketClient webSocketClient, ObjectMapper objectMapper) {
		this.webSocketClient = webSocketClient;
		this.objectMapper = objectMapper;
	}

	@PostConstruct
	public void connect() {
		try {
			log.info("[{}] Connecting to Binance WebSocket", getClass().getSimpleName());
			webSocketClient.execute(this, URL).get();
			log.info("[{}] Connected successfully", getClass().getSimpleName());
		} catch (Exception e) {
			log.error("[{}] Connection failed: {}", getClass().getSimpleName(), e.getMessage());
		}
	}
}
