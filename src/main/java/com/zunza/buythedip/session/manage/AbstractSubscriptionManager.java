package com.zunza.buythedip.session.manage;

import java.io.IOException;

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

	public void handleSubscribe(String sessionId, String subscriptionId, String destination) throws IOException {
		String countKey = getCountKey(destination);
		Long subscriberCount = redisTemplate.opsForValue().increment(countKey);
		log.info("Subscribed to {} | Current subscribers: {}", destination, subscriberCount);

		String sessionKey = getSessionKey(sessionId);
		redisTemplate.opsForSet().add(sessionKey, destination);

		String subDestinationKey = getSubDestinationKey(sessionId, subscriptionId);
		redisTemplate.opsForValue().set(subDestinationKey, destination);
	}

	public void handleUnSubscribe(String sessionId, String subscriptionId) throws IOException {
		String subDestinationKey = getSubDestinationKey(sessionId, subscriptionId);
		String destination = String.valueOf(redisTemplate.opsForValue().get(subDestinationKey));

		String countKey = getCountKey(destination);
		Long subscriberCount = redisTemplate.opsForValue().decrement(countKey);
		log.info("Unsubscribed from {} | Current subscribers: {}", destination, subscriberCount);

		String sessionKey = getSessionKey(sessionId);
		redisTemplate.opsForSet().remove(sessionKey, destination);
		redisTemplate.delete(subDestinationKey);
	}

	public abstract void onLastSubscriber(String destination, Long subscriberCount) throws IOException;

	protected String getCountKey(String destination) {
		return SUBSCRIBER_COUNT_KEY_PREFIX + destination;
	}

	protected String getSessionKey(String sessionId) {
		return SESSION_SUBSCRIPTIONS_KEY_PREFIX + sessionId;
	}

	protected String getSubDestinationKey(String sessionId, String subscriptionId) {
		return SUBSCRIPTION_DESTINATION_KEY_PREFIX + sessionId + ":" + subscriptionId;
	}
}
