package com.zunza.buythedip.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

import com.zunza.buythedip.constant.ChannelNames;
import com.zunza.buythedip.infrastructure.redis.RedisMessageSubscriber;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class RedisListenerConfig {

	private final RedisConnectionFactory redisConnectionFactory;
	private final RedisMessageSubscriber subscriber;

	@Bean
	public ChannelTopic chatMessageTopic() {
		return new ChannelTopic(ChannelNames.CHAT_MESSAGE_TOPIC);
	}

	@Bean
	public ChannelTopic topPriceTickTopic() {
		return new ChannelTopic(ChannelNames.TOP_PRICE_TICK_TOPIC);
	}

	@Bean
	public ChannelTopic topVolumeTopic() {
		return new ChannelTopic(ChannelNames.TOP_VOLUME_TOPIC);
	}

	@Bean
	public ChannelTopic symbolTickerTopic() {
		return new ChannelTopic(ChannelNames.SYMBOL_TICKER_TOPIC);
	}

	@Bean
	public MessageListenerAdapter listenerAdapter() {
		return new MessageListenerAdapter(subscriber, "sendMessage");
	}

	@Bean
	public RedisMessageListenerContainer container() {
		RedisMessageListenerContainer listenerContainer = new RedisMessageListenerContainer();
		listenerContainer.setConnectionFactory(redisConnectionFactory);
		listenerContainer.addMessageListener(listenerAdapter(), chatMessageTopic());
		listenerContainer.addMessageListener(listenerAdapter(), topPriceTickTopic());
		listenerContainer.addMessageListener(listenerAdapter(), topVolumeTopic());
		listenerContainer.addMessageListener(listenerAdapter(), symbolTickerTopic());
		return listenerContainer;
	}
}
