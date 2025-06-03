package com.zunza.buythedip.infrastructure.redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

import com.zunza.buythedip.chat.dto.ChatMessageDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RedisMessagePublisher {

	private final RedisTemplate<String, Object> redisTemplate;
	private final ChannelTopic channelTopic;

	public void publishMessage(ChatMessageDto chatMessageDto) {
		redisTemplate.convertAndSend(channelTopic.getTopic(), chatMessageDto);
	}
}
