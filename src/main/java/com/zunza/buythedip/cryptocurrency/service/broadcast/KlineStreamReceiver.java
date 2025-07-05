package com.zunza.buythedip.cryptocurrency.service.broadcast;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class KlineStreamReceiver extends AbstractBinanceStreamReceiver {

	public KlineStreamReceiver(
		WebSocketClient webSocketClient,
		ObjectMapper objectMapper
	) {
		super(webSocketClient, objectMapper);
	}

	@Override
	public void connect() {
		super.connect();
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		super.afterConnectionEstablished(session);
	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		super.handleTextMessage(session, message);
	}
}
