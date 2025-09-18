package com.zunza.buythedip.infrastructure.redis.service;

import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class RedisReactiveCacheService {
	private final ReactiveStringRedisTemplate redisTemplate;

	public Mono<Boolean> set(String key, String value) {
		return redisTemplate.opsForValue().set(key, value);
	}
}
