package com.zunza.buythedip.warmup;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.zunza.buythedip.crypto.repository.CryptoRepository;
import com.zunza.buythedip.infrastructure.redis.constant.RedisKey;
import com.zunza.buythedip.infrastructure.redis.service.RedisCacheService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * TODO: 로컬 캐시 적용하기
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CacheWarmUpRunner implements ApplicationRunner {
	private static final String SYMBOL_SUFFIX = "USDT";

	private final CryptoRepository cryptoRepository;
	private final RedisCacheService redisCacheService;

	@Override
	public void run(ApplicationArguments args) {
		long startTime = System.currentTimeMillis();
		log.info("tick size 캐싱 작업 시작");

		cryptoRepository.findAllWithMetadata()
			.forEach(crypto ->
				redisCacheService.set(
					RedisKey.TICK_SIZE_KEY_PREFIX.getValue() + crypto.getSymbol() + SYMBOL_SUFFIX,
					crypto.getMetadata().getTickSize().toString()
				)
			);

		long endTime = System.currentTimeMillis();
		log.info("tick size 캐싱 작업이 완료되었습니다. (총 소요 시간: {}ms)]",
			endTime - startTime);
	}
}
