package com.zunza.buythedip.cryptocurrency.service.trade;

import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zunza.buythedip.cryptocurrency.dto.ReConnectionEvent;
import com.zunza.buythedip.cryptocurrency.dto.SubDto;
import com.zunza.buythedip.cryptocurrency.dto.binance.StreamDto;
import com.zunza.buythedip.cryptocurrency.entity.Cryptocurrency;
import com.zunza.buythedip.cryptocurrency.handler.BinanceMessageRouter;
import com.zunza.buythedip.cryptocurrency.repository.CryptocurrencyRepository;
import com.zunza.buythedip.cryptocurrency.stream.CustomCloseStatus;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class TradeStreamReceiver extends TextWebSocketHandler {

	private final ApplicationEventPublisher eventPublisher;
	private final CryptocurrencyRepository cryptoCurrencyRepository;
	private final BinanceMessageRouter binanceMessageRouter;
	private final ObjectMapper objectMapper;

	public static final String STREAM_NAME = "TRADE";
	public static final String STREAM_SUFFIX = "usdt@trade";

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		log.info("WebSocket session established successfully: {}", STREAM_NAME);

		List<Cryptocurrency> cryptocurrencies = cryptoCurrencyRepository.findAll();
		List<String> symbols = cryptocurrencies.stream()
			.map(crypto -> crypto.getSymbol().toLowerCase() + STREAM_SUFFIX)
			.toList();


		SubDto subscribe = new SubDto("SUBSCRIBE", symbols, session.getId());
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

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		if (status.equalsCode(CustomCloseStatus.PLANNED_RECONNECTION)) {
			return;
		}

		eventPublisher.publishEvent(new ReConnectionEvent(STREAM_NAME));
	}
}
