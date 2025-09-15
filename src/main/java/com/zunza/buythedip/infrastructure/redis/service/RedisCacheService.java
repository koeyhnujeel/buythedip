package com.zunza.buythedip.infrastructure.redis.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RedisCacheService {
	private final StringRedisTemplate redisTemplate;

	public void set(String key, String value) {
		redisTemplate.opsForValue().set(key, value);
	}
}
