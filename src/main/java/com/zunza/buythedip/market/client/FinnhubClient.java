package com.zunza.buythedip.market.client;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zunza.buythedip.market.service.MarketDataService;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class FinnhubClient extends TextWebSocketHandler {

	private final WebSocketClient webSocketClient;
	private final MarketDataService marketDataService;
	private final ObjectMapper objectMapper;

	@Value("${finnhub.websocket-url}")
	private String finnhubUrl;

	@Value("${finnhub.secret}")
	private String token;

	@PostConstruct
	public void connect() {
		try {
			log.info("start connecting...");
			String connectUrl = finnhubUrl + "?token=" + token;
			webSocketClient.execute(this, connectUrl).get(5, TimeUnit.SECONDS);
			log.info("Finnhub WebSocket server Connected");
		} catch (Exception e) {
			log.error("Failed to connect to Finnhub WebSocket server", e);
		}
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		List<String> initialSymbols = List.of("MSFT", "NVDA", "AAPL", "AMZN", "META", "AVGO", "TSLA", "NFLX", "PLTR", "MSTR",
			"BINANCE:BTCUSDT", "BINANCE:ETHUSDT", "BINANCE:XRPUSDT", "BINANCE:BNBUSDT", "BINANCE:SOLUSDT",
			"BINANCE:DOGEUSDT", "BINANCE:PEPEUSDT", "BINANCE:TRXUSDT", "BINANCE:SUIUSDT", "BINANCE:ADAUSDT");

		subscribeToSymbols(session, initialSymbols);
	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		String payload = message.getPayload();
		marketDataService.processFinnhubMessage(payload);
	}

	private void subscribeToSymbols(WebSocketSession session, List<String> symbols) {
		if (session != null && session.isOpen()) {
			symbols.forEach(symbol -> {
				try {
					Map<String, String> subscribeMessage = Map.of("type", "subscribe", "symbol", symbol);
					String messagePayload = objectMapper.writeValueAsString(subscribeMessage);
					session.sendMessage(new TextMessage(messagePayload));
					log.info("Subscribed to symbol: {}", symbol);
				} catch (JsonProcessingException e) {
					log.error("Error creating subscribe message for symbol: {}", symbol, e);
				} catch (IOException e) {
					log.error("Error sending subscribe message for symbol: {}", symbol, e);
				}
			});
		} else {
			log.warn("Cannot subscribe. Finnhub session is not open.");
		}
	}
}
