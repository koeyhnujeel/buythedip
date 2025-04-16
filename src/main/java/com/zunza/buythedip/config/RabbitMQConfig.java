package com.zunza.buythedip.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Declarable;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.zunza.buythedip.constant.RabbitMQConstants;

@Configuration
public class RabbitMQConfig {

	private final String[] queueNames = {
		RabbitMQConstants.NEWS_TRANSLATION_QUEUE,
		RabbitMQConstants.NEWS_STORAGE_QUEUE
	};

	@Bean
	public Declarables rabbitMQDeclarables() {
		List<Declarable> declarables = new ArrayList<>();

		DirectExchange exchange = new DirectExchange(RabbitMQConstants.PUBLIC_EXCHANGE, true, false);
		declarables.add(exchange);

		for (String queueName : queueNames) {
			Queue queue = new Queue(queueName, true);
			declarables.add(queue);

			Binding binding = BindingBuilder.bind(queue).to(exchange).with(RabbitMQConstants.getRoutingKey(queueName));
			declarables.add(binding);
		}

		return new Declarables(declarables);
	}

	@Bean
	public MessageConverter jsonMessageConverter() {
		return new Jackson2JsonMessageConverter();
	}
}
