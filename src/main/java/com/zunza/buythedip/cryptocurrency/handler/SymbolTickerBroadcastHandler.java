package com.zunza.buythedip.cryptocurrency.handler;

import java.util.Optional;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.zunza.buythedip.constant.RabbitMQConstants;
import com.zunza.buythedip.infrastructure.messaging.RabbitMQService;
import com.zunza.buythedip.cryptocurrency.dto.binance.TradeDto;
import com.zunza.buythedip.infrastructure.redis.subhandle.Destination;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class SymbolTickerBroadcastHandler implements PurposeHandler {

	private final RabbitMQService rabbitMQService;
	private final RedisTemplate<String, Object> redisTemplate;

	private static final String SUBSCRIBER_COUNT_KEY_PREFIX = "subscribers:count:";

	@Override
	public void handle(TradeDto tradeDto) {
		String baseSymbol = extractBaseCurrency(tradeDto.getSymbol());
		String countKey = getCountKey(baseSymbol);
		Integer count = (Integer)redisTemplate.opsForValue().get(countKey);

		Optional.ofNullable(count)
			.filter(c -> c > 0)
			.ifPresent(c -> {
				rabbitMQService.publishMessage(
					RabbitMQConstants.PUBLIC_EXCHANGE,
					RabbitMQConstants.SYMBOL_TICKER_BROADCAST_ROUTING_KEY,
					tradeDto
				);
			});
	}

	private String extractBaseCurrency(String symbol) {
		return symbol.replace("USDT", "");
	}

	private String getCountKey(String baseSymbol) {
		return SUBSCRIBER_COUNT_KEY_PREFIX + Destination.SYMBOL_TICKER_DESTINATION_PREFIX.getDestination() + baseSymbol;
	}
}
