package com.zunza.buythedip.session.manage;

import java.io.IOException;
import java.util.Set;

import org.springframework.data.redis.core.RedisTemplate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractSubscriptionManager {

	protected final RedisTemplate<String, Object> redisTemplate;

	private static final String SUBSCRIBER_COUNT_KEY_PREFIX = "subscribers:count:";
	private static final String SESSION_SUBSCRIPTIONS_KEY_PREFIX = "session:subs:";
	private static final String SUBSCRIPTION_DESTINATION_KEY_PREFIX = "sub:dest:";

	protected AbstractSubscriptionManager(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	public final void handleSubscribe(String sessionId, String subscriptionId, String destination) throws IOException {
		if (destination == null || !canHandle(destination)) {
			return;
		}

		String countKey = getCountKey(destination);
		Long subscriberCount = redisTemplate.opsForValue().increment(countKey);
		log.info("Subscribed to {} | Current subscribers: {}", destination, subscriberCount);

		String sessionKey = getSessionKey(sessionId);
		redisTemplate.opsForSet().add(sessionKey, destination);

		String subDestinationKey = getSubDestinationKey(sessionId, subscriptionId);
		redisTemplate.opsForValue().set(subDestinationKey, destination);

		if (subscriberCount != null && subscriberCount == 1) {
			onFirstSubscriber(destination);
		}
	}

	public void handleUnSubscribe(String sessionId, String subscriptionId) throws IOException {
		String subDestinationKey = getSubDestinationKey(sessionId, subscriptionId);
		String destination = String.valueOf(redisTemplate.opsForValue().get(subDestinationKey));

		if (destination == null || !canHandle(destination)) {
			return;
		}

		String countKey = getCountKey(destination);
		Long subscriberCount = redisTemplate.opsForValue().decrement(countKey);
		log.info("Unsubscribed from {}. Current subscribers: {}", destination, subscriberCount);

		String sessionKey = getSessionKey(sessionId);
		redisTemplate.opsForSet().remove(sessionKey, destination);
		redisTemplate.delete(subDestinationKey);

		if (subscriberCount != null && subscriberCount == 0) {
			onLastSubscriber(destination);
		}
	}

	protected void decrementSubscriberCounts(Set<String> destinations) throws IOException {
		if (destinations == null || destinations.isEmpty()) return;

		destinations.forEach(destination -> {
			String countKey = getCountKey(destination);
			Long subscriberCount = redisTemplate.opsForValue().decrement(countKey);
			log.info("[Disconnected] Unsubscribed from {}. Current subscribers: {}", destination, subscriberCount);
		});

		for (String destination : destinations) {
			String countKey = getCountKey(destination);
			Integer count = (Integer)redisTemplate.opsForValue().get(countKey);
			if (count != null && count == 0) {
				onLastSubscriber(destination);
			}
		}
	}

	protected void cleanupAllSessionData(String sessionId) {
		Set<String> subDestKeys = redisTemplate.keys(SUBSCRIPTION_DESTINATION_KEY_PREFIX + sessionId + ":*");
		if (subDestKeys != null && !subDestKeys.isEmpty()) {
			redisTemplate.delete(subDestKeys);
		}

		redisTemplate.delete(getSessionKey(sessionId));
		log.info("Cleaned up all subscription metadata for session: {}", sessionId);
	}

	public abstract void handleDisconnect(String sessionId) throws IOException;

	protected abstract void onFirstSubscriber(String destination) throws IOException;

	protected abstract void onLastSubscriber(String destination) throws IOException;

	protected abstract boolean canHandle(String destination);

	protected Set<Object> getSubscribedDestinationsForSession(String sessionId) {
		return redisTemplate.opsForSet().members(getSessionKey(sessionId));
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
