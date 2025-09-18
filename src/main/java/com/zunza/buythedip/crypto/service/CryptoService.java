package com.zunza.buythedip.crypto.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.zunza.buythedip.crypto.dto.TickerResponse;
import com.zunza.buythedip.external.binance.dto.TickerData;
import com.zunza.buythedip.infrastructure.redis.constant.Channels;
import com.zunza.buythedip.infrastructure.redis.constant.RedisKey;
import com.zunza.buythedip.infrastructure.redis.pubsub.RedisMessagePublisher;
import com.zunza.buythedip.infrastructure.redis.service.RedisCacheService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CryptoService {
	private final RedisCacheService redisCacheService;
	private final RedisMessagePublisher redisMessagePublisher;

	public void publishTicker(TickerData data) {
		BigDecimal openPrice = getOpenPrice(data.getSymbol());
		BigDecimal currentPrice = data.getClosePrice();
		BigDecimal changePrice = currentPrice.subtract(openPrice);
		BigDecimal changeRate = getChangeRate(openPrice, currentPrice);

		int scale = getScale(data.getSymbol());

		TickerResponse tickerResponse = TickerResponse.createOf(
			data.getSymbol().replace("USDT", ""),
			currentPrice.setScale(scale, RoundingMode.HALF_UP),
			changePrice.setScale(scale, RoundingMode.HALF_UP),
			changeRate.setScale(2, RoundingMode.HALF_UP)
		);

		redisMessagePublisher.publishMessage(
			Channels.TICKER_CHANNEL.getTopic(),
			tickerResponse
		);
	}

	private BigDecimal getChangeRate(BigDecimal openPrice, BigDecimal currentPrice) {
		if (openPrice.compareTo(BigDecimal.ZERO) == 0) {
			return BigDecimal.ZERO;
		}

		return currentPrice.subtract(openPrice)
			.divide(openPrice, 10, RoundingMode.HALF_UP)
			.multiply(BigDecimal.valueOf(100));
	}

	private int getScale(String symbol) {
		String tickSize = redisCacheService.get(RedisKey.TICK_SIZE_KEY_PREFIX.getValue() + symbol);
		BigDecimal tickSizeDecimal = new BigDecimal(tickSize).stripTrailingZeros();
		return Math.max(0, tickSizeDecimal.scale());
	}

	private BigDecimal getOpenPrice(String Symbol) {
		return Optional.ofNullable(
				redisCacheService.get(RedisKey.OPEN_PRICE_KEY_PREFIX.getValue() + Symbol)
			)
			.map(BigDecimal::new)
			.orElse(BigDecimal.ZERO);
	}
}
