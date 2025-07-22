package com.zunza.buythedip.session.manage;

import java.io.IOException;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.zunza.buythedip.constant.ChannelNames;
import com.zunza.buythedip.cryptocurrency.dto.KlineControlMessage;
import com.zunza.buythedip.cryptocurrency.service.RedisLeaderElection;
import com.zunza.buythedip.cryptocurrency.stream.KlineStreamManager;
import com.zunza.buythedip.infrastructure.redis.RedisMessagePublisher;
import com.zunza.buythedip.infrastructure.redis.subhandle.Destination;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("klineSubscriptionManager")
public class klineSubscriptionManager extends AbstractSubscriptionManager {

	private final KlineStreamManager klineStreamManager;
	private final RedisLeaderElection redisLeaderElection;
	private final RedisMessagePublisher redisMessagePublisher;

	public klineSubscriptionManager(
		RedisTemplate<String, Object> redisTemplate,
		KlineStreamManager klineStreamManager,
		RedisLeaderElection redisLeaderElection,
		RedisMessagePublisher redisMessagePublisher
	) {
		super(redisTemplate);
		this.klineStreamManager = klineStreamManager;
		this.redisLeaderElection = redisLeaderElection;
		this.redisMessagePublisher = redisMessagePublisher;
	}

	@Override
	public void handleDisconnect(String sessionId, String destination) throws IOException {
		Long subscriberCount = decrementSubscriberCounts(destination);
		redisTemplate.opsForSet().remove(getSessionKey(sessionId), destination);
		log.info("[Disconnected] Unsubscribed from {} | Current subscribers: {}", destination, subscriberCount);
		onLastSubscriber(destination, subscriberCount);
	}

	@Override
	public void onFirstSubscriber(String destination, Long subscriberCount) throws IOException {
		if (subscriberCount == null || subscriberCount != 1) {
			return;
		}

		SymbolInterval symbolInterval = extractSymbolAndInterval(destination);

		if (redisLeaderElection.isLeader()) {
			klineStreamManager.subKlineForSymbol(symbolInterval.symbol, symbolInterval.interval);
		} else {
			KlineControlMessage klineControlMessage = new KlineControlMessage(
				KlineControlMessage.ActionType.SUBSCRIBE,
				symbolInterval.symbol,
				symbolInterval.interval);

			redisMessagePublisher.publishMessage(ChannelNames.KLINE_CONTROL_TOPIC, klineControlMessage);
		}
	}

	@Override
	public void onLastSubscriber(String destination, Long subscriberCount) throws IOException {
		if (subscriberCount == null || subscriberCount != 0) {
			return;
		}

		SymbolInterval symbolInterval = extractSymbolAndInterval(destination);

		if (redisLeaderElection.isLeader()) {
			klineStreamManager.unSubKlineForSymbol(symbolInterval.symbol, symbolInterval.interval);
		} else {
			KlineControlMessage klineControlMessage = new KlineControlMessage(
				KlineControlMessage.ActionType.UNSUBSCRIBE,
				symbolInterval.symbol,
				symbolInterval.interval);

			redisMessagePublisher.publishMessage(ChannelNames.KLINE_CONTROL_TOPIC, klineControlMessage);
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
