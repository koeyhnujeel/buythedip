package com.zunza.buythedip.config;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.zunza.buythedip.security.CustomUserDetails;
import com.zunza.buythedip.security.JwtTokenProvider;
import com.zunza.buythedip.security.exception.StompAuthenticationException;
import com.zunza.buythedip.security.exception.StompAuthorizationHeaderException;
import com.zunza.buythedip.security.exception.StompInvalidTokenException;
import com.zunza.buythedip.user.entity.User;
import com.zunza.buythedip.user.exception.UserNotFoundException;
import com.zunza.buythedip.user.repository.UserRepository;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class StompHandler implements ChannelInterceptor {

	private final JwtTokenProvider jwtTokenProvider;
	private static final String BEARER_PREFIX = "Bearer ";
	private final UserRepository userRepository;

	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

		if (accessor == null) {
			return message;
		}

		StompCommand command = accessor.getCommand();
		String endpointPath = (String)accessor.getSessionAttributes().get("endpointPath");

		if (isChatEndpoint(endpointPath) && StompCommand.CONNECT.equals(command)) {
			return handleConnect(accessor, message);
		}

		if (isChatEndpoint(endpointPath) && (StompCommand.SEND.equals(command) || StompCommand.SUBSCRIBE.equals(command))) {
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

		Authentication authentication = createAuthentication(jwt);
		accessor.setUser(authentication);

		return message;
	}

	private Authentication createAuthentication(String jwt) {
		Claims claims = jwtTokenProvider.parseClaims(jwt);
		Long userId = claims.get("userId", Long.class);

		User user = userRepository.findById(userId)
			.orElseThrow(() -> new UserNotFoundException(userId));

		UserDetails details = new CustomUserDetails(user);
		return new UsernamePasswordAuthenticationToken(
			details,
			null,
			details.getAuthorities());
	}

	private boolean isChatEndpoint(String endpoint) {
		return endpoint != null && endpoint.startsWith("/ws/chat");
	}
}
