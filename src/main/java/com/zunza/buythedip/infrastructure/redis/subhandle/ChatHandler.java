package com.zunza.buythedip.infrastructure.redis.subhandle;

import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zunza.buythedip.chat.dto.ChatMessageDto;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ChatHandler implements RedisMessageHandler {

	private final ObjectMapper objectMapper;
	private final SimpMessageSendingOperations messagingTemplate;

	private static final String CHAT_DESTINATION = "/topic/chat/room/public";

	@Override
	public void handle(String message) throws JsonProcessingException {
		ChatMessageDto chatMessageDto = objectMapper.readValue(message, ChatMessageDto.class);
		messagingTemplate.convertAndSend(CHAT_DESTINATION, chatMessageDto);
	}
}
