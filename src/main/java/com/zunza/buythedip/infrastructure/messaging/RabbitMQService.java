package com.zunza.buythedip.infrastructure.messaging;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RabbitMQService {

	private final RabbitTemplate rabbitTemplate;

	public void publishMessage(String exchange, String routingKey, Object message) {
		rabbitTemplate.convertAndSend(exchange, routingKey, message);
	}
}
