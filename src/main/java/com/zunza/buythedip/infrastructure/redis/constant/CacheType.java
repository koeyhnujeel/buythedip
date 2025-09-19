package com.zunza.buythedip.infrastructure.redis.constant;

import java.time.Duration;

import lombok.Getter;

@Getter
public enum CacheType {
	CRYPTO_DETAILS("CRYPTO:DETAILS", Duration.ofMinutes(10));

	private String cacheName;
	private Duration ttl;

	CacheType(String cacheName, Duration ttl) {
		this.cacheName = cacheName;
		this.ttl = ttl;
	}
}
