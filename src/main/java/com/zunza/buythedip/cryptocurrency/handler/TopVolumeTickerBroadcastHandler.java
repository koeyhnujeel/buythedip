package com.zunza.buythedip.cryptocurrency.handler;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.zunza.buythedip.constant.RabbitMQConstants;
import com.zunza.buythedip.infrastructure.messaging.RabbitMQService;
import com.zunza.buythedip.cryptocurrency.dto.binance.TradeDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class TopVolumeTickerBroadcastHandler implements PurposeHandler {

	private final RedisTemplate<String, Object> redisTemplate;
	private final RabbitMQService rabbitMQService;

	private static final String CACHE_TOP_VOLUME_KEY = "cache:topVolume";

	@Override
	public void handle(TradeDto tradeDto) {
		Set<String> topVolume = (Set<String>)redisTemplate.opsForValue().get(CACHE_TOP_VOLUME_KEY);

		Optional.ofNullable(topVolume)
			.filter(tv -> !tv.isEmpty())
			.filter(tv -> tv.contains(tradeDto.getSymbol()))
			.ifPresent(tv -> {
				rabbitMQService.publishMessage(
					RabbitMQConstants.PUBLIC_EXCHANGE,
					RabbitMQConstants.TOP_VOLUME_TICKER_BROADCAST_ROUTING_KEY,
					tradeDto
				);
			});
	}
}
