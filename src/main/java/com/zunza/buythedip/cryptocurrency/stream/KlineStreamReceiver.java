package com.zunza.buythedip.cryptocurrency.service.kline;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zunza.buythedip.constant.ChannelNames;
import com.zunza.buythedip.cryptocurrency.dto.ReConnectionEvent;
import com.zunza.buythedip.cryptocurrency.dto.binance.KlineDto;
import com.zunza.buythedip.cryptocurrency.stream.CustomCloseStatus;
import com.zunza.buythedip.infrastructure.redis.RedisMessagePublisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class KlineStreamReceiver extends TextWebSocketHandler {

	private final ApplicationEventPublisher eventPublisher;
	private final RedisMessagePublisher redisMessagePublisher;
	private final KlineStreamManager klineStreamManager;
	private final ObjectMapper objectMapper;

	private static final String STREAM_NAME = "KLINE";

	@Override
	public void afterConnectionEstablished(WebSocketSession session) {
		log.info("WebSocket session established successfully: {}", STREAM_NAME);
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

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		if (status.equalsCode(CustomCloseStatus.PLANNED_RECONNECTION)) {
			return;
		}

		eventPublisher.publishEvent(new ReConnectionEvent(STREAM_NAME));
	}
}
