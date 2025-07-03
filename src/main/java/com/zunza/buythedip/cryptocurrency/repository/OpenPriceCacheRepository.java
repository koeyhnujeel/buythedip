package com.zunza.buythedip.cryptocurrency.repository;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class OpenPriceCacheRepository {

	private final RedisTemplate<String, Object> redisTemplate;

	public void save(String key, Object o) {
		redisTemplate.opsForValue()
			.set(key, o);
	}
}
