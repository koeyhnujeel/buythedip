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

	public String get(String key) {
		return redisTemplate.opsForValue().get((key));
	}

	public boolean delete(String key) {
		return redisTemplate.delete(key);
	}

	public boolean hasKey(String key) {
		return redisTemplate.hasKey(key);
	}
}
