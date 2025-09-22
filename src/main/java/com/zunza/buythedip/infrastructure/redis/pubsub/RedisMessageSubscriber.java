package com.zunza.buythedip.infrastructure.redis.pubsub;

import java.util.Arrays;
import java.util.Optional;

import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zunza.buythedip.crypto.dto.ChartResponse;
import com.zunza.buythedip.crypto.dto.TickerResponse;
import com.zunza.buythedip.infrastructure.redis.constant.Channels;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisMessageSubscriber {
	private final ObjectMapper objectMapper;
	private final SimpMessageSendingOperations messagingTemplate;

	public void sendMessage(String message, String channel) {
		Optional<Channels> channelOptional = getChannel(channel);

		if (channelOptional.isEmpty()) {
			log.warn("지원하지 않는 채널입니다: {}", channel);
			return;
		}

		Channels channelEnum = channelOptional.get();

		try {
			Object payload = objectMapper.readValue(message, channelEnum.getType());
			String destination = getDestination(channelEnum, payload);

			if (destination.isEmpty()) {
				log.warn("목적지를 찾을 수 없습니다. Channel: {}, Payload: {}", channel, payload);
				return;
			}

			messagingTemplate.convertAndSend(destination, payload);

		} catch (JsonProcessingException e) {
			log.error("역직렬화 실패 Channel: {}, Message: {}", channel, message, e);
		}
	}

	private Optional<Channels> getChannel(String channel) {
		return Arrays.stream(Channels.values())
			.filter(c -> c.getTopic().equals(channel))
			.findFirst();
	}

	private String getDestination(Channels channel, Object payload) {
		return switch (payload) {
			case TickerResponse tickerResponse ->
				channel.getDestinationPrefix() + tickerResponse.getSymbol();
			case ChartResponse chartResponse ->
				channel.getDestinationPrefix() + chartResponse.getSymbol() + "/" + chartResponse.getInterval();
			default -> "";
		};
	}
}
