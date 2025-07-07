package com.zunza.buythedip.infrastructure.redis.subhandle;

import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zunza.buythedip.cryptocurrency.dto.RealtimePriceDto;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TopVolumePriceHandler implements RedisMessageHandler {

	private final ObjectMapper objectMapper;
	private final SimpMessageSendingOperations messagingTemplate;

	@Override
	public void handle(String message) throws JsonProcessingException {
		RealtimePriceDto realtimePriceDto = objectMapper.readValue(message, RealtimePriceDto.class);
		messagingTemplate.convertAndSend(Destination.TOP_VOLUME_TIKER_DESTINATION.getDestination(), realtimePriceDto);
	}
}
