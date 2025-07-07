package com.zunza.buythedip.infrastructure.redis.subhandle;

import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zunza.buythedip.cryptocurrency.dto.SymbolTickerDto;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SymbolTickerHandler implements RedisMessageHandler {

	private final ObjectMapper objectMapper;
	private final SimpMessageSendingOperations messagingTemplate;

	@Override
	public void handle(String message) throws JsonProcessingException {
		SymbolTickerDto symbolTickerDto = objectMapper.readValue(message, SymbolTickerDto.class);
		String destination = Destination.SYMBOL_TICKER_DESTINATION_PREFIX.getDestination() + symbolTickerDto.getSymbol();
		messagingTemplate.convertAndSend(destination, symbolTickerDto);
	}
}
