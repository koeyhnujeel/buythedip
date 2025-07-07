package com.zunza.buythedip.infrastructure.redis.subhandle;

import java.util.List;

import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TopVolumeHandler implements RedisMessageHandler {

	private final ObjectMapper objectMapper;
	private final SimpMessageSendingOperations messagingTemplate;

	@Override
	public void handle(String message) throws JsonProcessingException {
		List list = objectMapper.readValue(message, List.class);
		messagingTemplate.convertAndSend(Destination.TOP_VOLUME_DESTINATION.getDestination(), list);
	}
}
