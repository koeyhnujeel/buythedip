package com.zunza.buythedip.session;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

import com.zunza.buythedip.session.manage.AbstractSubscriptionManager;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class StompSessionEventListener {

	private final AbstractSubscriptionManager tradeSubscriptionManger;
	private final AbstractSubscriptionManager klineSubscriptionManger;
	private final AbstractSubscriptionManager chatSubscriptionManager;

	public StompSessionEventListener(
		@Qualifier("tradeSubscriptionManager")AbstractSubscriptionManager tradeSubscriptionManger,
		@Qualifier("klineSubscriptionManager")AbstractSubscriptionManager klineSubscriptionManger,
		@Qualifier("chatSubscriptionManager")AbstractSubscriptionManager chatSubscriptionManger
	) {
		this.tradeSubscriptionManger = tradeSubscriptionManger;
		this.klineSubscriptionManger = klineSubscriptionManger;
		this.chatSubscriptionManager = chatSubscriptionManger;
	}

	@EventListener
	public void handleSessionSubscribe(SessionSubscribeEvent event) throws IOException {
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());

		String sessionId = accessor.getSessionId();
		String subscriptionId = accessor.getSubscriptionId();
		String destination = accessor.getDestination();

		handleSubscribe(sessionId, subscriptionId, destination);
	}

	@EventListener
	public void handleSessionUnSubscribe(SessionUnsubscribeEvent event) throws IOException {
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
		Object endpointPath = accessor.getSessionAttributes().get("endpointPath");
		log.info("[UnSubscription] | [{}]", String.valueOf(endpointPath));
		log.info("[UnSubscription] | [{}]", String.valueOf(accessor.getDestination()));

		String sessionId = accessor.getSessionId();
		String subscriptionId = accessor.getSubscriptionId();

		tradeSubscriptionManger.handleUnSubscribe(sessionId, subscriptionId);
		klineSubscriptionManger.handleUnSubscribe(sessionId, subscriptionId);
	}

	@EventListener
	public void handleDisConnect(SessionDisconnectEvent event) throws IOException {
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
		Object endpointPath = accessor.getSessionAttributes().get("endpointPath");
		log.info("[DisConnect] | [{}]", String.valueOf(endpointPath));
		log.info("[DisConnect] | [{}]", String.valueOf(accessor.getDestination()));

		String sessionId = accessor.getSessionId();

		chatSubscriptionManager.handleDisconnect(sessionId);
		tradeSubscriptionManger.handleDisconnect(sessionId);
		klineSubscriptionManger.handleDisconnect(sessionId);
	}

	private void handleSubscribe(String sessionId, String subscriptionId, String destination) throws IOException {
		klineSubscriptionManger.handleSubscribe(sessionId, subscriptionId, destination);
		tradeSubscriptionManger.handleSubscribe(sessionId, subscriptionId, destination);
		chatSubscriptionManager.handleSubscribe(sessionId, subscriptionId, destination);
	}
}
