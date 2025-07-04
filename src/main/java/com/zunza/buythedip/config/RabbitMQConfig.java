package com.zunza.buythedip.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Declarable;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.zunza.buythedip.constant.RabbitMQConstants;

@Configuration
public class RabbitMQConfig {

	private final String[] queueNames = {
		RabbitMQConstants.NEWS_TRANSLATION_QUEUE,
		RabbitMQConstants.NEWS_STORAGE_QUEUE,
		RabbitMQConstants.TRADE_AGGREGATION_QUEUE,
		RabbitMQConstants.TOP_VOLUME_TICKER_BROADCAST_QUEUE,
		RabbitMQConstants.SYMBOL_TICKER_BROADCAST_QUEUE
	};

	@Bean
	public Declarables rabbitMQDeclarables() {
		List<Declarable> declarables = new ArrayList<>();

		DirectExchange exchange = new DirectExchange(RabbitMQConstants.PUBLIC_EXCHANGE, true, false);
		declarables.add(exchange);

		for (String queueName : queueNames) {
			Queue mainQueue = QueueBuilder.durable(queueName).build();
			declarables.add(mainQueue);
			declarables.add(BindingBuilder.bind(mainQueue).to(exchange).with(RabbitMQConstants.getRoutingKey(queueName)));

			Queue retryQueue = QueueBuilder.durable(queueName + ".retry")
				.withArgument("x-dead-letter-exchange", "")
				.withArgument("x-dead-letter-routing-key", queueName)
				.withArgument("x-message-ttl", 5000)
				.build();
			declarables.add(retryQueue);
		}

		return new Declarables(declarables);
	}

	@Bean
	public MessageConverter jsonMessageConverter() {
		return new Jackson2JsonMessageConverter();
	}
}
