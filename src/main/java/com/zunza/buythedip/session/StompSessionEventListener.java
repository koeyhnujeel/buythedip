package com.zunza.buythedip.session;

import java.io.IOException;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class StompSessionEventListener {

	private final SessionEventManager sessionEventManager;

	@EventListener
	public void handleSessionSubscribe(SessionSubscribeEvent event) throws IOException {
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
		String sessionId = accessor.getSessionId();
		String subscriptionId = accessor.getSubscriptionId();
		String destination = accessor.getDestination();

		sessionEventManager.subscribe(sessionId, subscriptionId, destination);
	}

	@EventListener
	public void handleSessionUnSubscribe(SessionUnsubscribeEvent event) throws IOException {
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
		String sessionId = accessor.getSessionId();
		String subscriptionId = accessor.getSubscriptionId();

		sessionEventManager.unSubscribe(sessionId, subscriptionId);
	}

	@EventListener
	public void handleDisConnect(SessionDisconnectEvent event) throws IOException {
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
		String sessionId = accessor.getSessionId();

		sessionEventManager.disConnect(sessionId);
	}
}
