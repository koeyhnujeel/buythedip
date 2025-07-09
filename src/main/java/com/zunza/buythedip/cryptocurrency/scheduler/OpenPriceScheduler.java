package com.zunza.buythedip.cryptocurrency.scheduler;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.zunza.buythedip.cryptocurrency.client.BinanceClient;
import com.zunza.buythedip.cryptocurrency.entity.Cryptocurrency;
import com.zunza.buythedip.cryptocurrency.repository.CryptocurrencyRepository;
import com.zunza.buythedip.cryptocurrency.repository.OpenPriceCacheRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpenPriceScheduler {

	private final CryptocurrencyRepository cryptoCurrencyRepository;
	private final OpenPriceCacheRepository openPriceCacheRepository;
	private final BinanceClient binanceClient;

	private static final String SYMBOL_SUFFIX = "USDT";
	private static final String KEY_SUFFIX_OPEN_PRICE = ":OPENPRICE";

	@Scheduled(cron = "10 0 0 * * *", zone = "UTC")
	public void fetchAndCacheAllSymbolsOpenPrice() {
		List<Cryptocurrency> cryptocurrencies = cryptoCurrencyRepository.findAll();

		Flux.fromIterable(cryptocurrencies)
			.delayElements(Duration.ofMillis(100))
			.flatMap(cryptocurrency -> binanceClient.getDailyOpenPrice(cryptocurrency.getSymbol() + SYMBOL_SUFFIX)
				.doOnNext(openPrice -> {
					openPriceCacheRepository.save(cryptocurrency.getSymbol() + SYMBOL_SUFFIX + KEY_SUFFIX_OPEN_PRICE, openPrice);
					log.info("Successfully cached symbol: {} / open price: {}", cryptocurrency.getSymbol() + SYMBOL_SUFFIX, openPrice);
				})
			)
			.subscribe();
	}
}
