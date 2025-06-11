package com.zunza.buythedip.config;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.zunza.buythedip.security.JwtTokenProvider;
import com.zunza.buythedip.security.exception.StompAuthenticationException;
import com.zunza.buythedip.security.exception.StompAuthorizationHeaderException;
import com.zunza.buythedip.security.exception.StompInvalidTokenException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class StompHandler implements ChannelInterceptor {

	private final JwtTokenProvider jwtTokenProvider;
	private static final String BEARER_PREFIX = "Bearer ";

	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

		if (accessor == null) {
			return message;
		}

		StompCommand command = accessor.getCommand();

		if (StompCommand.CONNECT.equals(command)) {
			return handleConnect(accessor, message);
		}

		if (StompCommand.SEND.equals(command) || StompCommand.SUBSCRIBE.equals(command)) {
			if (accessor.getUser() == null) {
				throw new StompAuthenticationException();
			}
		}

		return message;
	}

	private Message<?> handleConnect(StompHeaderAccessor accessor, Message<?> message) {
		String authorization = accessor.getFirstNativeHeader("Authorization");

		if (!StringUtils.hasText(authorization)) {
			throw new StompAuthorizationHeaderException();
		}

		if (!authorization.startsWith(BEARER_PREFIX)) {
			throw new StompInvalidTokenException();
		}

		String jwt = authorization.substring(BEARER_PREFIX.length());
		jwtTokenProvider.validateToken(jwt);

		Authentication authentication = jwtTokenProvider.getAuthentication(jwt);
		accessor.setUser(authentication);

		return message;
	}
}
