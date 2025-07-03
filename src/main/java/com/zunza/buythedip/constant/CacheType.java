package com.zunza.buythedip.constant;

import java.time.Duration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CacheType {
	NEWS_DETAIL(CacheNames.NEWS_DETAIL, Duration.ofMinutes(10)),
	CRYPTO_DATA_WITH_LOGO(CacheNames.CRYPTO_DATA_WITH_LOGO, Duration.ofMinutes(60)),
	CRYPTO_INFO(CacheNames.CRYPTO_INFO, Duration.ofMinutes(10));

	private final String cacheName;
	private final Duration expiredAfterWrite;
}
