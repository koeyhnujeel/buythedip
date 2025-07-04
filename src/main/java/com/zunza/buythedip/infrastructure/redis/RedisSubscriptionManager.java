package com.zunza.buythedip.infrastructure.redis;

import java.util.Set;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisSubscriptionManager {

	private final RedisTemplate<String, Object> redisTemplate;

	private static final String SUBSCRIBER_COUNT_KEY_PREFIX = "subscribers:count:";
	private static final String SESSION_SUBSCRIPTIONS_KEY_PREFIX = "session:subs:";
	private static final String SUBSCRIPTION_DESTINATION_KEY_PREFIX = "sub:dest:";

	public void handleSubscribe(String sessionId, String subscriptionId, String destination) {
		if (sessionId == null || subscriptionId == null || destination == null) {
			return;
		}

		String countKey = getCountKey(destination);
		Long subscriberCount = redisTemplate.opsForValue().increment(countKey);
		log.info("Subscribed to {}. Current subscribers: {}", destination, subscriberCount);

		String sessionKey = getSessionKey(sessionId);
		redisTemplate.opsForSet().add(sessionKey, destination);

		String subDestinationKey = getSubDestinationKey(sessionId, subscriptionId);
		redisTemplate.opsForValue().set(subDestinationKey, destination);
	}

	public void handleUnSubscribe(String sessionId, String subscriptionId) {
		if (sessionId == null || subscriptionId == null) {
			return;
		}

		String subDestinationKey = getSubDestinationKey(sessionId, subscriptionId);
		String destination = redisTemplate.opsForValue().get(subDestinationKey).toString();

		if (destination == null) {
			return;
		}

		String countKey = getCountKey(destination);
		Long subscriberCount = redisTemplate.opsForValue().decrement(countKey);
		log.info("Unsubscribed from {}. Current subscribers: {}", destination, subscriberCount);

		String sessionKey = getSessionKey(sessionId);
		redisTemplate.opsForSet().remove(sessionKey, destination);
		redisTemplate.delete(subDestinationKey);
	}

	public void handleDisconnect(String sessionId) {
		if (sessionId == null) {
			return;
		}

		String sessionKey = getSessionKey(sessionId);
		Set<Object> destinations = redisTemplate.opsForSet().members(sessionKey);

		if (destinations == null || destinations.isEmpty()) {
			return;
		}

		destinations.forEach(destination -> {
			String destinationStr = String.valueOf(destination);
			String countKey = getCountKey(destinationStr);
			Long subscriberCount = redisTemplate.opsForValue().decrement(countKey);
			log.info("[Disconnected] Unsubscribed from {}. Current subscribers: {}", destination, subscriberCount);
		});

		redisTemplate.delete(sessionKey);
		log.info("Cleaned up subscriptions for disconnected session: {}", sessionId);
	}

	private String getCountKey(String destination) {
		return SUBSCRIBER_COUNT_KEY_PREFIX + destination;
	}

	private String getSessionKey(String sessionId) {
		return SESSION_SUBSCRIPTIONS_KEY_PREFIX + sessionId;
	}

	private String getSubDestinationKey(String sessionId, String subscriptionId) {
		return SUBSCRIPTION_DESTINATION_KEY_PREFIX + sessionId + ":" + subscriptionId;
	}
}
