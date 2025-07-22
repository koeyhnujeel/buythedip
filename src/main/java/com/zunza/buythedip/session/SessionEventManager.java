package com.zunza.buythedip.session;

import com.zunza.buythedip.session.manage.AbstractSubscriptionManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Component
public class SessionEventManager {

	private final Map<String, AbstractSubscriptionManager> managerMap;
	private final RedisTemplate<String, Object> redisTemplate;

	public SessionEventManager(
		@Qualifier("tradeSubscriptionManager") AbstractSubscriptionManager tradeSubscriptionManger,
		@Qualifier("klineSubscriptionManager") AbstractSubscriptionManager klineSubscriptionManger,
		@Qualifier("chatSubscriptionManager") AbstractSubscriptionManager chatSubscriptionManger,
		RedisTemplate<String, Object> redisTemplate
	) {
		this.redisTemplate = redisTemplate;
		this.managerMap = Map.of(
			SubscriptionType.TRADE.getManagerKey(), tradeSubscriptionManger,
			SubscriptionType.KLINE.getManagerKey(), klineSubscriptionManger,
			SubscriptionType.CHAT.getManagerKey(), chatSubscriptionManger
		);
	}

	public void subscribe(String sessionId, String subscriptionId, String destination) throws IOException {
		if (destination == null) return;

		String key = getKeyForManagerMap(destination);
		managerMap.get(key).handleSubscribe(sessionId, subscriptionId, destination);
	}

	public void unSubscribe(String sessionId, String subscriptionId) throws IOException {
		String subDestinationKey = getSubDestinationKey(sessionId, subscriptionId);
		String destination = getDestinationBySubDestinationKey(subDestinationKey);

		if (destination == null) return;

		String key = getKeyForManagerMap(destination);
		managerMap.get(key).handleUnSubscribe(sessionId, subscriptionId);
	}

	public void disConnect(String sessionId) {
		Set<Object> destinations = getSubscribedDestinationForSession(sessionId);
		destinations.forEach(d -> {
			String destination = String.valueOf(d);
			String key = getKeyForManagerMap(destination);
			try {
				managerMap.get(key).handleDisconnect(sessionId, destination);
			} catch (IOException e) {
				log.error("Error during disconnect for destination {}: {}", destination, e.getMessage(), e);
			}
		});

		String pattern = RedisKeyPrefix.SUBSCRIPTION_DESTINATION.getKey(sessionId, "*");
		deleteSubDestinations(pattern);
	}

	private String getKeyForManagerMap(String destination) {
		return SubscriptionType.from(destination).getManagerKey();
	}

	private String getSubDestinationKey(String sessionId, String subscriptionId) {
		return RedisKeyPrefix.SUBSCRIPTION_DESTINATION.getKey(sessionId, subscriptionId);
	}

	private String getDestinationBySubDestinationKey(String key) {
		return String.valueOf(redisTemplate.opsForValue().get(key));
	}

	private String getSessionKey(String sessionId) {
		return RedisKeyPrefix.SESSION_SUBSCRIPTIONS.getKey(sessionId);
	}

	private Set<Object> getSubscribedDestinationForSession(String sessionId) {
		return redisTemplate.opsForSet().members(getSessionKey(sessionId));
	}

	private void deleteSubDestinations(String pattern) {
		ScanOptions options = KeyScanOptions.scanOptions()
			.match(pattern)
			.count(50)
			.build();

		Set<String> keys = redisTemplate.execute((RedisConnection connection) -> {
			RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
			Set<String> results = new HashSet<>();

			try (Cursor<byte[]> cursor = connection.keyCommands().scan(options)) {
				while (cursor.hasNext()) {
					String key = serializer.deserialize(cursor.next());
					Optional.ofNullable(key).ifPresent(results::add);
				}
			}
			return results;
		});

		if (!keys.isEmpty()) {
			redisTemplate.delete(keys);
		}
	}
}
