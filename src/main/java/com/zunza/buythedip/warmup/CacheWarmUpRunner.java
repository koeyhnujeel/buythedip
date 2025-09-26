package com.zunza.buythedip.warmup;

import java.util.concurrent.TimeUnit;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.zunza.buythedip.crypto.repository.CryptoRepository;
import com.zunza.buythedip.infrastructure.redis.constant.RedisKey;
import com.zunza.buythedip.infrastructure.redis.service.RedisCacheService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class CacheWarmUpRunner implements ApplicationRunner {
	private static final String SYMBOL_SUFFIX = "USDT";
	private static final String DONE_KEY = "TICK:SIZE:WARMUP:DONE::";
	private static final String LOCK_KEY = "LOCK:TICK:SIZE:WARMUP";

	private final RedissonClient redissonClient;
	private final CryptoRepository cryptoRepository;
	private final RedisCacheService redisCacheService;

	@Override
	public void run(ApplicationArguments args) {
		if (redisCacheService.hasKey(DONE_KEY)) {
			log.info("이미 웜업이 완료됐습니다.");
			return;
		}

		RLock lock = redissonClient.getLock(LOCK_KEY);
		boolean isLocked = false;

		try {
			isLocked = lock.tryLock(15, 30, TimeUnit.SECONDS);
			if (!isLocked) {
				log.info("분산 락을 획득하지 못했습니다.");
				return;
			}

			long startTime = System.currentTimeMillis();
			log.info("tick size 캐싱 작업 시작");

			cryptoRepository.findAllWithMetadata()
				.forEach(crypto ->
					redisCacheService.set(
						RedisKey.TICK_SIZE_KEY_PREFIX.getValue() + crypto.getSymbol() + SYMBOL_SUFFIX,
						crypto.getMetadata().getTickSize().toString()
					)
				);

			redisCacheService.set(DONE_KEY, "1");
			long endTime = System.currentTimeMillis();
			log.info("tick size 캐싱 작업이 완료되었습니다. (총 소요 시간: {}ms)]", endTime - startTime);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			log.warn("warmup interrupted", e);
		} finally {
			if (isLocked && lock.isHeldByCurrentThread()) {
				lock.unlock();
			}
		}
	}
}
