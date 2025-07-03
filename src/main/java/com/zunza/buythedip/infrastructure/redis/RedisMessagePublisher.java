package com.zunza.buythedip.infrastructure.redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RedisMessagePublisher {

	private final RedisTemplate<String, Object> redisTemplate;

	public void publishMessage(String topic, Object message) {
		redisTemplate.convertAndSend(topic, message);
	}
}
