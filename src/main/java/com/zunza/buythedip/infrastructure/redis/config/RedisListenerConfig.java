package com.zunza.buythedip.infrastructure.redis.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

import com.zunza.buythedip.infrastructure.redis.constant.Channels;
import com.zunza.buythedip.infrastructure.redis.pubsub.RedisMessageSubscriber;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class RedisListenerConfig {
	private final RedisMessageSubscriber subscriber;
	private final RedisConnectionFactory redisConnectionFactory;

	@Bean
	public ChannelTopic tickerChannelTopic() {
		return new ChannelTopic(Channels.TICKER_CHANNEL.getTopic());
	}

	@Bean
	public ChannelTopic chartChannelTopic() {
		return new ChannelTopic(Channels.CHART_CHANNEL.getTopic());
	}

	@Bean
	public MessageListenerAdapter listenerAdapter() {
		return new MessageListenerAdapter(subscriber, "sendMessage");
	}

	@Bean
	public RedisMessageListenerContainer container() {
		RedisMessageListenerContainer container = new RedisMessageListenerContainer();
		container.setConnectionFactory(redisConnectionFactory);
		container.addMessageListener(listenerAdapter(), tickerChannelTopic());
		container.addMessageListener(listenerAdapter(), chartChannelTopic());
		return container;
	}
}
