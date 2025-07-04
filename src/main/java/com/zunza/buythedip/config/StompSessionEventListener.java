package com.zunza.buythedip.config;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

import com.zunza.buythedip.infrastructure.redis.RedisSubscriptionManager;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class StompSessionEventListener {

	private final RedisSubscriptionManager redisSubscriptionManager;

	@EventListener
	public void handleSessionSubscribe(SessionSubscribeEvent event) {
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
		String sessionId = accessor.getSessionId();
		String subscriptionId = accessor.getSubscriptionId();
		String destination = accessor.getDestination();

		redisSubscriptionManager.handleSubscribe(sessionId, subscriptionId, destination);
	}

	@EventListener
	public void handleSessionUnSubscribe(SessionUnsubscribeEvent event) {
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
		String sessionId = accessor.getSessionId();
		String subscriptionId = accessor.getSubscriptionId();

		redisSubscriptionManager.handleUnSubscribe(sessionId, subscriptionId);
	}

	@EventListener
	public void handleDisConnect(SessionDisconnectEvent event) {
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
		String sessionId = accessor.getSessionId();

		redisSubscriptionManager.handleDisconnect(sessionId);
	}
}
