package com.zunza.buythedip.cryptocurrency.service.trade;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zunza.buythedip.cryptocurrency.dto.SubDto;
import com.zunza.buythedip.cryptocurrency.dto.binance.StreamDto;
import com.zunza.buythedip.cryptocurrency.entity.Cryptocurrency;
import com.zunza.buythedip.cryptocurrency.handler.BinanceMessageRouter;
import com.zunza.buythedip.cryptocurrency.repository.CryptocurrencyRepository;
import com.zunza.buythedip.cryptocurrency.service.AbstractBinanceStreamReceiver;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TradeStreamReceiver extends AbstractBinanceStreamReceiver {

	private final CryptocurrencyRepository cryptoCurrencyRepository;
	private final BinanceMessageRouter binanceMessageRouter;

	public static final String URL = "wss://data-stream.binance.vision/stream";
	public static final String STREAM_SUFFIX = "usdt@trade";

	public TradeStreamReceiver(
		WebSocketClient webSocketClient,
		ObjectMapper objectMapper,
		CryptocurrencyRepository cryptoCurrencyRepository,
		BinanceMessageRouter binanceMessageRouter
	) {
		super(webSocketClient, objectMapper, URL);
		this.cryptoCurrencyRepository = cryptoCurrencyRepository;
		this.binanceMessageRouter = binanceMessageRouter;
	}

	@Override
	public void connect() {
		super.connect();
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
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
}
