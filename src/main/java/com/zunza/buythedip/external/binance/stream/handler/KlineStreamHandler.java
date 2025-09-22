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
	private final String interval;
	private final List<String> symbols;
	private final int chunkSize;
	private final ObjectMapper objectMapper;
	private final CryptoService cryptoService;

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		for (int i = 0; i < symbols.size(); i += chunkSize) {
			List<String> chunk = symbols.subList(i, Math.min(i + chunkSize, symbols.size()));

			List<String> subscribeParams = chunk.stream()
				.map(symbol -> symbol + interval)
				.toList();

			SubscribeRequest request = new SubscribeRequest(
				"SUBSCRIBE",
				subscribeParams,
				String.valueOf(i / chunkSize + 1)
			);

			String payload = objectMapper.writeValueAsString(request);
			session.sendMessage(new TextMessage(payload));
			Thread.sleep(200);
		}
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
