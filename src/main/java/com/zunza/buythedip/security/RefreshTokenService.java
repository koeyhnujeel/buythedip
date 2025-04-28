package com.zunza.buythedip.security;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

	private static final String REFRESH_TOKEN_PREFIX = "RT:";
	private static final long REFRESH_TOKEN_EXPIRE_TIME = 7 * 24 * 60 * 60 * 1000L;

	private final RedisTemplate<String, Object> redisTemplate;

	public void saveRefreshToken(Long userId, String refreshToken) {
		String refreshTokenKey = getRefreshTokenKey(userId);
		redisTemplate.opsForValue().set(refreshTokenKey, refreshToken, REFRESH_TOKEN_EXPIRE_TIME, TimeUnit.MILLISECONDS);
	}

	public String getRefreshToken(Long userId) {
		String refreshTokenKey = getRefreshTokenKey(userId);
		Object refreshToken = redisTemplate.opsForValue().get(refreshTokenKey);
		return refreshToken != null ? refreshToken.toString() : null;
	}

	public void deleteRefreshToken(Long userId) {
		String refreshTokenKey = getRefreshTokenKey(userId);
		redisTemplate.delete(refreshTokenKey);
	}

	public boolean validateRefreshToken(Long userId, String refreshToken) {
		String storedRefreshToken = getRefreshToken(userId);
		return refreshToken.equals(storedRefreshToken);
	}

	private String getRefreshTokenKey(Long userId) {
		return REFRESH_TOKEN_PREFIX + userId;
	}
}
