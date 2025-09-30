package com.zunza.buythedip.crypto.scheduler;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;

import com.zunza.buythedip.crypto.repository.CryptoRepository;
import com.zunza.buythedip.external.binance.client.BinanceClient;
import com.zunza.buythedip.infrastructure.redis.constant.RedisKey;
import com.zunza.buythedip.infrastructure.redis.service.RedisReactiveCacheService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@Component
@RequiredArgsConstructor
public class CryptoScheduler {
	private final BinanceClient binanceClient;
	private final CryptoRepository cryptoRepository;
	private final RedisReactiveCacheService reactiveCacheService;

	private static final String SYMBOL_SUFFIX = "USDT";

	@Scheduled(cron = "3 0 0 * * *", zone = "UTC")
	@SchedulerLock(name = "cacheDailyOpenPrice_lock")
	@EventListener(ApplicationReadyEvent.class)
	public void cacheDailyOpenPrice() {
		long startTime = System.currentTimeMillis();
		log.info("open price 캐싱 작업을 시작합니다.");

		Mono<List<String>> symbolsMono = Mono.fromCallable(() ->
			cryptoRepository.findAll().stream()
				.map(crypto -> crypto.getSymbol() + SYMBOL_SUFFIX)
				.toList()
		);

		symbolsMono
			.subscribeOn(Schedulers.boundedElastic())
			.flatMapMany(Flux::fromIterable)
			.flatMap(this::cacheSymbolOpenPrice)
			.doFinally(signalType -> {
				long endTime = System.currentTimeMillis();
				log.info("open price 캐싱 작업이 최종적으로 완료되었습니다. (총 소요 시간: {}ms) [종료 타입: {}]",
					endTime - startTime, signalType);
			})
			.subscribe();
	}

	private Mono<Void> cacheSymbolOpenPrice(String symbol) {
		return binanceClient.getKline(symbol, "1d", 1)
			.flatMap(klines -> {
				BigDecimal openPrice = klines.get(0).getOpen();
				String key = RedisKey.OPEN_PRICE_KEY_PREFIX.getValue() + symbol;

				return reactiveCacheService.set(key, openPrice.toPlainString())
					.doOnSuccess(result -> {
							if (result) log.debug("[캐싱 성공] [symbol]: {} | [open price]: {}", symbol, openPrice);
							else log.warn("[캐싱 실패] [symbol]: {}", symbol);
						});
			})
			.onErrorResume(error -> {
					log.warn("[API 요청 실패] [symbol]: {} | [에러]: {}", symbol, error.getMessage());
					return Mono.empty();
				}
			)
			.then();
	}
}
