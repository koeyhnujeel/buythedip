package com.zunza.buythedip.infrastructure.redis.pubsub;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisMessagePublisher {
	private final ObjectMapper objectMapper;
	private final StringRedisTemplate redisTemplate;

	public void publishMessage(String topic, Object message) {
		try {
			redisTemplate.convertAndSend(topic, objectMapper.writeValueAsString(message));
		} catch (JsonProcessingException e) {
			log.warn(e.getMessage());
		}
	}
}
