package com.zunza.buythedip.user.repository

import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class RefreshJwtRepository(
    @Value("\${jwt.refresh-token-expire-time}")
    private val ttl: Long,
    private val stringRedisTemplate: StringRedisTemplate
) {
    companion object {
        private const val REFRESH_TOKEN_KEY_PREFIX = "RT:"
    }

    fun save(userId: Long, refreshJwt: String) {
        stringRedisTemplate
            .opsForValue()
            .set(
                REFRESH_TOKEN_KEY_PREFIX + userId,
                refreshJwt,
                ttl,
                TimeUnit.MILLISECONDS
            )
    }

    fun delete(userId: Long) {
        stringRedisTemplate.delete(REFRESH_TOKEN_KEY_PREFIX + userId)
    }
}
