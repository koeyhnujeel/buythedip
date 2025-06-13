package com.zunza.buythedip.chat.service;

import org.springframework.stereotype.Service;

import com.zunza.buythedip.chat.dto.ChatMessageDto;
import com.zunza.buythedip.infrastructure.redis.RedisMessagePublisher;
import com.zunza.buythedip.infrastructure.redis.RedisStreamService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatService {

	private final RedisMessagePublisher redisMessagePublisher;
	private final RedisStreamService redisStreamService;

	public void sendMessage(String accountId, ChatMessageDto chatMessageDto) {
		redisMessagePublisher.publishMessage(chatMessageDto);
		redisStreamService.addToStream(accountId, chatMessageDto);
	}
}
