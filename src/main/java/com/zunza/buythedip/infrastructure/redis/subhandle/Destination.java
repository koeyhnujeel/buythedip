package com.zunza.buythedip.infrastructure.redis.subhandle;

import lombok.Getter;

public enum Destination {
	CHAT_DESTINATION("/topic/chat/room/public"),
	TOP_VOLUME_DESTINATION("/topic/crypto/trade/top/volume"),
	TOP_VOLUME_TIKER_DESTINATION("/topic/crypto/trade/top/ticker"),
	SYMBOL_TICKER_DESTINATION_PREFIX("/topic/crypto/trade/ticker/"),
	SYMBOL_KLINE_DESTINATION_PREFIX("/topic/crypto/kline/");

	@Getter
	private String destination;

	Destination(String destination) {
		this.destination = destination;
	}
}
