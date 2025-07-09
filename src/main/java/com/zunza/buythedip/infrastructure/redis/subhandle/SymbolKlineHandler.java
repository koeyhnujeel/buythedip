package com.zunza.buythedip.infrastructure.redis.subhandle;

import java.io.IOException;
import java.util.Map;

import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zunza.buythedip.cryptocurrency.dto.SymbolTickerDto;
import com.zunza.buythedip.cryptocurrency.dto.binance.KlineDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class SymbolKlineHandler implements RedisMessageHandler {

	private final ObjectMapper objectMapper;
	private final SimpMessageSendingOperations messagingTemplate;

	@Override
	public void handle(String message) throws IOException {
		KlineDto klineDto = objectMapper.readValue(message, KlineDto.class);
		String destination = Destination.SYMBOL_KLINE_DESTINATION_PREFIX.getDestination() + klineDto.getSymbol() + "/" + klineDto.getInterval();
		messagingTemplate.convertAndSend(destination, klineDto);
		log.info("Published Kline data for Symbol: {} | Destination: {}", klineDto.getSymbol(), destination);
	}
}
