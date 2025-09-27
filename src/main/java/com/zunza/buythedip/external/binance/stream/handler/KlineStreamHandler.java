package com.zunza.buythedip.external.binance.stream.handler;

import java.util.List;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zunza.buythedip.crypto.service.CryptoService;
import com.zunza.buythedip.external.binance.dto.klinestream.KlineStreamResponse;
import com.zunza.buythedip.external.binance.dto.SubscribeRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class KlineStreamHandler extends TextWebSocketHandler {
	private final List<String> params;
	private final ObjectMapper objectMapper;
	private final CryptoService cryptoService;

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		SubscribeRequest request = new SubscribeRequest(
			"SUBSCRIBE",
			params,
			session.getId()
		);

		String payload = objectMapper.writeValueAsString(request);
		session.sendMessage(new TextMessage(payload));
		Thread.sleep(200);
	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		KlineStreamResponse response = objectMapper.readValue(
			message.getPayload(),
			KlineStreamResponse.class
		);

		if (response.getData() == null) {
			return;
		}

		cryptoService.publishKline(response.getData());
	}
}
