package com.zunza.buythedip.infrastructure.redis.constant;

import lombok.Getter;

@Getter
public enum RedisKey {
	OPEN_PRICE_KEY_PREFIX("OPEN:PRICE:"),
	TICK_SIZE_KEY_PREFIX("TICK:SIZE:");

	private String value;

	RedisKey(String value) {
		this.value = value;
	}
}
