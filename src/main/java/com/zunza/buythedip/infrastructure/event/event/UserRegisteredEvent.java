package com.zunza.buythedip.infrastructure.event.event;

import com.zunza.buythedip.user.entity.User;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserRegisteredEvent {
	private User user;

	public static UserRegisteredEvent createFrom(User user) {
		return UserRegisteredEvent.builder()
			.user(user)
			.build();
	}
}
