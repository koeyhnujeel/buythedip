package com.zunza.buythedip.infrastructure.redis.service

import org.springframework.data.redis.core.ReactiveStringRedisTemplate
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class ReactiveCacheService(
    private val redisTemplate: ReactiveStringRedisTemplate
) {
    fun set(key: String, value: String): Mono<Boolean> {
        return redisTemplate.opsForValue().set(key, value)
    }
}
