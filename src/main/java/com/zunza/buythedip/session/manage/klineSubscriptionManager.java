package com.zunza.buythedip.session.manage;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

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
	public void handleDisconnect(String sessionId) throws IOException {
		Set<Object> destinations = getSubscribedDestinationsForSession(sessionId);
		if (destinations == null || destinations.isEmpty()) {
			return;
		}

		Set<String> klineDestinations = destinations.stream()
			.filter(des -> des != null && des.toString().startsWith(Destination.SYMBOL_KLINE_DESTINATION_PREFIX.getDestination()))
			.map(String::valueOf)
			.collect(Collectors.toSet());

		decrementSubscriberCounts(klineDestinations);
		log.info("[Kline] Cleaned up kline subscriptions for disconnected session: {}", sessionId);

		cleanupAllSessionData(sessionId);
	}

	@Override
	protected void onFirstSubscriber(String destination) throws IOException {
		SymbolInterval symbolInterval = extractSymbolAndInterval(destination);
		klineStreamManager.subKlineForSymbol(symbolInterval.symbol, symbolInterval.interval);
	}

	@Override
	protected void onLastSubscriber(String destination) throws IOException {
		SymbolInterval symbolInterval = extractSymbolAndInterval(destination);
		klineStreamManager.unSubKlineForSymbol(symbolInterval.symbol, symbolInterval.interval);
	}

	@Override
	protected boolean canHandle(String destination) {
		return destination != null && destination.startsWith(Destination.SYMBOL_KLINE_DESTINATION_PREFIX.getDestination());
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
