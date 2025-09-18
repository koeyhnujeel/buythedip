package com.zunza.buythedip.external.binance.stream.handler;

import java.util.List;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zunza.buythedip.crypto.service.CryptoService;
import com.zunza.buythedip.external.binance.dto.SubscribeRequest;
import com.zunza.buythedip.external.binance.dto.TickerStreamResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class TickerStreamHandler extends TextWebSocketHandler {
	private final List<String> symbols;
	private final int chunkSize;
	private final ObjectMapper objectMapper;
	private final CryptoService cryptoService;

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		for (int i = 0; i < symbols.size(); i += chunkSize) {
			List<String> chunk = symbols.subList(i, Math.min(i + chunkSize, symbols.size()));

			SubscribeRequest payloadObj = new SubscribeRequest(
				"SUBSCRIBE",
				chunk,
				String.valueOf((i / chunkSize) + 1)
			);

			String payload = objectMapper.writeValueAsString(payloadObj);
			session.sendMessage(new TextMessage(payload));
			Thread.sleep(200);
		}
	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		TickerStreamResponse response = objectMapper.readValue(
			message.getPayload(),
			TickerStreamResponse.class
		);

		if (response.getData() == null) {
			return;
		}

		cryptoService.publishTicker(response.getData());
	}
}
