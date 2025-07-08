package com.zunza.buythedip.cryptocurrency.service.kline;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zunza.buythedip.constant.ChannelNames;
import com.zunza.buythedip.cryptocurrency.dto.binance.KlineDto;
import com.zunza.buythedip.cryptocurrency.service.AbstractBinanceStreamReceiver;
import com.zunza.buythedip.infrastructure.redis.RedisMessagePublisher;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class KlineStreamReceiver extends AbstractBinanceStreamReceiver {

	private final RedisMessagePublisher redisMessagePublisher;
	private final KlineStreamManager klineStreamManager;
	private final ObjectMapper objectMapper;

	public static final String URL = "wss://data-stream.binance.vision/ws";

	public KlineStreamReceiver(
		RedisMessagePublisher redisMessagePublisher,
		KlineStreamManager klineStreamManager,
		WebSocketClient webSocketClient,
		ObjectMapper objectMapper
	) {
		super(webSocketClient, objectMapper, URL);
		this.klineStreamManager = klineStreamManager;
		this.objectMapper = objectMapper;
		this.redisMessagePublisher = redisMessagePublisher;
	}

	@Override
	public void connect() {
		super.connect();
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		klineStreamManager.registerKlineSession(session);
	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		JsonNode jsonNode = objectMapper.readTree(message.getPayload());

		if (jsonNode.get("s") == null) {
			return;
		}

		KlineDto klineDto = KlineDto.createKlineDtoForUpdate(jsonNode);
		redisMessagePublisher.publishMessage(ChannelNames.SYMBOL_KLINE_TOPIC, klineDto);
	}
}
