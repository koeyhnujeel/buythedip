package com.zunza.buythedip.infrastructure.event.event;

import com.zunza.buythedip.user.entity.User;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserRegisteredEvent {
	private User user;

	@Builder
	private UserRegisteredEvent(User user) {
		this.user = user;
	}

	public static UserRegisteredEvent createFrom(User user) {
		return UserRegisteredEvent.builder()
			.user(user)
			.build();
	}
}
