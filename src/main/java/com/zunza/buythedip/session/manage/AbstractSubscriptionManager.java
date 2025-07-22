package com.zunza.buythedip.session.manage;

import java.io.IOException;

import org.springframework.data.redis.core.RedisTemplate;

import com.zunza.buythedip.session.RedisKeyPrefix;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractSubscriptionManager {

	protected final RedisTemplate<String, Object> redisTemplate;

	protected AbstractSubscriptionManager(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	public final void handleSubscribe(String sessionId, String subscriptionId, String destination) throws IOException {
		String countKey = getCountKey(destination);
		Long subscriberCount = redisTemplate.opsForValue().increment(countKey);
		log.info("Subscribed to {} | Current subscribers: {}", destination, subscriberCount);

		String sessionKey = getSessionKey(sessionId);
		redisTemplate.opsForSet().add(sessionKey, destination);

		String subDestinationKey = getSubDestinationKey(sessionId, subscriptionId);
		redisTemplate.opsForValue().set(subDestinationKey, destination);

		onFirstSubscriber(destination, subscriberCount);
	}

	public final void handleUnSubscribe(String sessionId, String subscriptionId) throws IOException {
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

	public abstract void handleDisconnect(String sessionId, String destination) throws IOException;

	public void onFirstSubscriber(String destination, Long subscriberCount) throws IOException {
	}

	public void onLastSubscriber(String destination, Long subscriberCount) throws IOException {
	}

	protected String getCountKey(String destination) {
		return RedisKeyPrefix.SUBSCRIBER_COUNT.getKey(destination);
	}

	protected String getSessionKey(String sessionId) {
		return RedisKeyPrefix.SESSION_SUBSCRIPTIONS.getKey(sessionId);
	}

	protected String getSubDestinationKey(String sessionId, String subscriptionId) {
		return RedisKeyPrefix.SUBSCRIPTION_DESTINATION.getKey(sessionId, subscriptionId);
	}

    protected Long decrementSubscriberCounts(String destination) {
        String countKey = getCountKey(destination);
        return redisTemplate.opsForValue().decrement(countKey);
    }
}
