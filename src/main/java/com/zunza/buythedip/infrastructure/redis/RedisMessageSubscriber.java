package com.zunza.buythedip.infrastructure.redis;

import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zunza.buythedip.chat.dto.ChatMessageDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisMessageSubscriber {

	private final ObjectMapper objectMapper;
	private final SimpMessageSendingOperations messagingTemplate;
	private static final String DESTINATION = "/topic/chat/room/public";

	public void sendMessage(String publishMessage) {
		try {
			ChatMessageDto chatMessageDto = objectMapper.readValue(publishMessage, ChatMessageDto.class);
			messagingTemplate.convertAndSend(DESTINATION, chatMessageDto);

		} catch (Exception e) {
			log.error("Error processing message from Redis Pub/Sub: {}", e.getMessage(), e);
		}
	}
}
