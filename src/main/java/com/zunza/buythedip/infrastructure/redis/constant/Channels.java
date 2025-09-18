package com.zunza.buythedip.infrastructure.redis.constant;

import com.zunza.buythedip.crypto.dto.TickerResponse;

import lombok.Getter;

@Getter
public enum Channels {
	TICKER_CHANNEL("ticker", "/topic/ticker/", TickerResponse.class);

	private String topic;
	private String destinationPrefix;
	private Class<?> type;

	Channels(String topic, String destinationPrefix, Class<?> type) {
		this.topic = topic;
		this.destinationPrefix = destinationPrefix;
		this.type = type;
	}
}
