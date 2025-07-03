package com.zunza.buythedip.cryptocurrency.service.broadcast;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zunza.buythedip.cryptocurrency.dto.binance.StreamDto;
import com.zunza.buythedip.cryptocurrency.entity.Cryptocurrency;
import com.zunza.buythedip.cryptocurrency.handler.BinanceMessageRouter;
import com.zunza.buythedip.cryptocurrency.repository.CryptocurrencyRepository;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class TradeStreamReceiver extends TextWebSocketHandler {

	private final CryptocurrencyRepository cryptoCurrencyRepository;
	private final BinanceMessageRouter binanceMessageRouter;
	private final WebSocketClient webSocketClient;
	private final ObjectMapper objectMapper;

	public static final String URL = "wss://data-stream.binance.vision/stream";
	public static final String STREAM_SUFFIX = "usdt@trade";

	@PostConstruct
	public void connect() {
		try {
			log.info("start connecting...");
			webSocketClient.execute(this, URL).get();
			log.info("Binance WebSocket Streams Connected");
		} catch (Exception e) {
			log.error("Binance WebSocket Streams Connect failed");
			log.error(e.getMessage());
		}
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		List<Cryptocurrency> cryptocurrencies = cryptoCurrencyRepository.findAll();
		List<String> symbols = cryptocurrencies.stream()
			.map(crypto -> crypto.getSymbol().toLowerCase() + STREAM_SUFFIX)
			.toList();

		SubDto subscribe = new SubDto("SUBSCRIBE", symbols);
		String value = objectMapper.writeValueAsString(subscribe);

		session.sendMessage(new TextMessage(value));
	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws JsonProcessingException {
		String payload = message.getPayload();
		StreamDto streamDto = objectMapper.readValue(payload, StreamDto.class);

		if (streamDto.getTradeDto() == null) {
			return;
		}

		binanceMessageRouter.route(streamDto.getTradeDto());
	}

	@Getter
	public static class SubDto {
		private String method;
		private List<String> params;

		public SubDto() {
		}

		public SubDto(String method, List<String> params) {
			this.method = method;
			this.params = params;
		}
	}
}
