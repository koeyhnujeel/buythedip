package com.zunza.buythedip.user.constant;

import lombok.Getter;

public enum UserRole {
	USER("ROLE_USER"),
	ADMIN("ROLE_ADMIN");

	@Getter
	private String value;

	UserRole(String value) {
		this.value = value;
	}
}
