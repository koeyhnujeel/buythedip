package com.zunza.buythedip.chat.service;

import org.springframework.stereotype.Service;

import com.zunza.buythedip.chat.dto.ChatMessageDto;
import com.zunza.buythedip.infrastructure.redis.RedisMessagePublisher;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatService {

	private final RedisMessagePublisher redisMessagePublisher;

	public void sendMessage(ChatMessageDto chatMessageDto) {
		redisMessagePublisher.publishMessage(chatMessageDto);
	}
}
