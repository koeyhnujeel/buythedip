package com.zunza.buythedip.session.manage;

import java.io.IOException;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("chatSubscriptionManager")
public class ChatSubscriptionManager extends AbstractSubscriptionManager {

	protected ChatSubscriptionManager(RedisTemplate<String, Object> redisTemplate) {
		super(redisTemplate);
	}

	@Override
	public void handleDisconnect(String sessionId, String destination) {
		Long subscriberCount = decrementSubscriberCounts(destination);
		redisTemplate.opsForSet().remove(getSessionKey(sessionId), destination);
		log.info("[Disconnected] Unsubscribed from {} | Current subscribers: {}", destination, subscriberCount);
	}

	@Override
	public void onLastSubscriber(String destination, Long subscriberCount) {
	}
}
