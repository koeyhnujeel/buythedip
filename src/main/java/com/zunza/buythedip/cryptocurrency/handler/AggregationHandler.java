package com.zunza.buythedip.cryptocurrency.handler;

import org.springframework.stereotype.Component;

import com.zunza.buythedip.constant.RabbitMQConstants;
import com.zunza.buythedip.infrastructure.messaging.RabbitMQService;
import com.zunza.buythedip.cryptocurrency.dto.binance.TradeDto;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AggregationHandler implements PurposeHandler {

	private final RabbitMQService rabbitMQService;

	@Override
	public void handle(TradeDto tradeDto) {
		rabbitMQService.publishMessage(
			RabbitMQConstants.PUBLIC_EXCHANGE,
			RabbitMQConstants.TRADE_AGGREGATION_ROUTING_KEY,
			tradeDto
		);
	}
}
