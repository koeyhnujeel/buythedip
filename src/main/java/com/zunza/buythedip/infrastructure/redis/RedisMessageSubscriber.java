package com.zunza.buythedip.infrastructure.redis;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.zunza.buythedip.constant.ChannelNames;
import com.zunza.buythedip.infrastructure.redis.subhandle.ChatHandler;
import com.zunza.buythedip.infrastructure.redis.subhandle.RedisMessageHandler;
import com.zunza.buythedip.infrastructure.redis.subhandle.SymbolTickerHandler;
import com.zunza.buythedip.infrastructure.redis.subhandle.TopVolumeHandler;
import com.zunza.buythedip.infrastructure.redis.subhandle.TopVolumePriceHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RedisMessageSubscriber {

	private final Map<String, RedisMessageHandler> handleMap;

	public RedisMessageSubscriber(
		ChatHandler chatHandler,
		TopVolumeHandler topVolumeHandler,
		TopVolumePriceHandler topVolumePriceHandler,
		SymbolTickerHandler symbolTickerHandler
	) {
		this.handleMap = Map.of(
			ChannelNames.CHAT_MESSAGE_TOPIC, chatHandler,
			ChannelNames.TOP_VOLUME_TOPIC, topVolumeHandler,
			ChannelNames.TOP_PRICE_TICK_TOPIC, topVolumePriceHandler,
			ChannelNames.SYMBOL_TICKER_TOPIC, symbolTickerHandler
		);
	}

	public void sendMessage(String message, String channel) {
		try {
			handleMap.get(channel).handle(message);
		} catch (Exception e) {
			log.error("Error processing message from Redis Pub/Sub: {}", e.getMessage(), e);
		}
	}
}
