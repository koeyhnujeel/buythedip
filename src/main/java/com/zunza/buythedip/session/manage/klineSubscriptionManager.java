package com.zunza.buythedip.session.manage;

import java.io.IOException;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.zunza.buythedip.cryptocurrency.service.kline.KlineStreamManager;
import com.zunza.buythedip.infrastructure.redis.subhandle.Destination;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("klineSubscriptionManager")
public class klineSubscriptionManager extends AbstractSubscriptionManager {

	private final KlineStreamManager klineStreamManager;

	public klineSubscriptionManager(
		RedisTemplate<String, Object> redisTemplate,
		KlineStreamManager klineStreamManager
	) {
		super(redisTemplate);
		this.klineStreamManager = klineStreamManager;
	}

	@Override
	public void handleSubscribe(String sessionId, String subscriptionId, String destination) throws IOException {
		String countKey = getCountKey(destination);
		Long subscriberCount = redisTemplate.opsForValue().increment(countKey);
		log.info("Subscribed to {} | Current subscribers: {}", destination, subscriberCount);

		String sessionKey = getSessionKey(sessionId);
		redisTemplate.opsForSet().add(sessionKey, destination);

		String subDestinationKey = getSubDestinationKey(sessionId, subscriptionId);
		redisTemplate.opsForValue().set(subDestinationKey, destination);

		onFirstSubscriber(destination, subscriberCount);
	}

	@Override
	public void handleUnSubscribe(String sessionId, String subscriptionId) throws IOException {
		String subDestinationKey = getSubDestinationKey(sessionId, subscriptionId);
		String destination = String.valueOf(redisTemplate.opsForValue().get(subDestinationKey));

		String countKey = getCountKey(destination);
		Long subscriberCount = redisTemplate.opsForValue().decrement(countKey);
		log.info("Unsubscribed from {} | Current subscribers: {}", destination, subscriberCount);

		String sessionKey = getSessionKey(sessionId);
		redisTemplate.opsForSet().remove(sessionKey, destination);
		redisTemplate.delete(subDestinationKey);

		onLastSubscriber(destination, subscriberCount);
	}

	private void onFirstSubscriber(String destination, Long subscriberCount) throws IOException {
		if (subscriberCount != null && subscriberCount == 1) {
			SymbolInterval symbolInterval = extractSymbolAndInterval(destination);
			klineStreamManager.subKlineForSymbol(symbolInterval.symbol, symbolInterval.interval);
		}
	}

	@Override
	public void onLastSubscriber(String destination, Long subscriberCount) throws IOException {
		if (subscriberCount != null && subscriberCount == 0) {
			SymbolInterval symbolInterval = extractSymbolAndInterval(destination);
			klineStreamManager.unSubKlineForSymbol(symbolInterval.symbol, symbolInterval.interval);
		}
	}

	private SymbolInterval extractSymbolAndInterval(String destination) {
		String substring = destination.substring(Destination.SYMBOL_KLINE_DESTINATION_PREFIX.getDestination().length());
		String[] split = substring.split("/");
		return new SymbolInterval(split[0].toLowerCase(), split[1]);
	}

	@AllArgsConstructor
	public static class SymbolInterval {
		private final String symbol;
		private final String interval;
	}
}
