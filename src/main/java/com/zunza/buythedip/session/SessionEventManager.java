package com.zunza.buythedip.session;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisConnectionUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

import com.zunza.buythedip.session.manage.AbstractSubscriptionManager;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SessionEventManager {

	private final Map<String, AbstractSubscriptionManager> managerMap;
	private final RedisTemplate<String, Object> redisTemplate;

	private static final String SUBSCRIBER_COUNT_KEY_PREFIX = "subscribers:count:";
	private static final String SESSION_SUBSCRIPTIONS_KEY_PREFIX = "session:subs:";
	private static final String SUBSCRIPTION_DESTINATION_KEY_PREFIX = "sub:dest:";

	private static final String TRADE_DESTINATION_PREFIX = "/topic/crypto/trade";
	private static final String KLINE_DESTINATION_PREFIX = "/topic/crypto/kline";

	private static final String TRADE_MANAGER_KEY = "trade";
	private static final String KLINE_MANAGER_KEY = "kline";
	private static final String CHAT_MANAGER_KEY = "chat";

	public SessionEventManager(
		@Qualifier("tradeSubscriptionManager") AbstractSubscriptionManager tradeSubscriptionManger,
		@Qualifier("klineSubscriptionManager") AbstractSubscriptionManager klineSubscriptionManger,
		@Qualifier("chatSubscriptionManager") AbstractSubscriptionManager chatSubscriptionManger,
		RedisTemplate<String, Object> redisTemplate
	) {
		this.redisTemplate = redisTemplate;
		this.managerMap = Map.of(
			TRADE_MANAGER_KEY, tradeSubscriptionManger,
			KLINE_MANAGER_KEY, klineSubscriptionManger,
			CHAT_MANAGER_KEY, chatSubscriptionManger
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
			String key = getKeyForManagerMap(String.valueOf(destination));

			if (KLINE_MANAGER_KEY.equals(key)) {
				try {
					Long subscriberCount = decrementSubscriberCounts(destination);
					redisTemplate.opsForSet().remove(getSessionKey(sessionId), destination);
					log.info("[Disconnected] Unsubscribed from {} | Current subscribers: {}", destination, subscriberCount);
					managerMap.get(key).onLastSubscriber(destination, subscriberCount);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			} else {
				Long subscriberCount = decrementSubscriberCounts(destination);
				redisTemplate.opsForSet().remove(getSessionKey(sessionId), destination);
				log.info("[Disconnected] Unsubscribed from {} | Current subscribers: {}", destination, subscriberCount);
			}
		});

		String pattern = SUBSCRIPTION_DESTINATION_KEY_PREFIX + sessionId + ":*";
		deleteSubDestinations(pattern);
	}

	private String getKeyForManagerMap(String destination) {
		if (destination.startsWith(TRADE_DESTINATION_PREFIX)) {
			return TRADE_MANAGER_KEY;
		} else if (destination.startsWith(KLINE_DESTINATION_PREFIX)) {
			return KLINE_MANAGER_KEY;
		} else {
			return CHAT_MANAGER_KEY;
		}
	}

	private String getSubDestinationKey(String sessionId, String subscriptionId) {
		return SUBSCRIPTION_DESTINATION_KEY_PREFIX + sessionId + ":" + subscriptionId;
	}

	private String getDestinationBySubDestinationKey(String key) {
		return String.valueOf(redisTemplate.opsForValue().get(key));
	}

	private String getSessionKey(String sessionId) {
		return SESSION_SUBSCRIPTIONS_KEY_PREFIX + sessionId;
	}

	private Set<Object> getSubscribedDestinationForSession(String sessionId) {
		return redisTemplate.opsForSet().members(getSessionKey(sessionId));
	}

	private String getCountKey(String destination) {
		return SUBSCRIBER_COUNT_KEY_PREFIX + destination;
	}

	private Long decrementSubscriberCounts(String destination) {
		String countKey = getCountKey(destination);
		return redisTemplate.opsForValue().decrement(countKey);
	}

	private void deleteSubDestinations(String pattern) {
		ScanOptions options = ScanOptions.scanOptions()
			.match(pattern)
			.count(50)
			.build();

		RedisConnection connection = RedisConnectionUtils.getConnection(redisTemplate.getConnectionFactory());
		RedisSerializer<String> serializer = redisTemplate.getStringSerializer();

		Set<String> keysToDelete = new HashSet<>();

		try (Cursor<byte[]> cursor = connection.scan(options)) {
			while (cursor.hasNext()) {
				String key = serializer.deserialize(cursor.next());
				keysToDelete.add(key);
			}
		} catch (Exception e) {
			throw new RuntimeException("Error during SCAN operation", e);
		} finally {
			RedisConnectionUtils.releaseConnection(connection, redisTemplate.getConnectionFactory());
		}

		if (!keysToDelete.isEmpty()) {
			redisTemplate.delete(keysToDelete);
		}
	}
}
