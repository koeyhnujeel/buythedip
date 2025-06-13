package com.zunza.buythedip.market.service;

import java.util.List;

import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zunza.buythedip.market.dto.FinnhubTradeDto;
import com.zunza.buythedip.market.dto.PriceUpdateDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MarketDataService {

	private final SimpMessageSendingOperations messagingTemplate;
	private final ObjectMapper objectMapper;

	private static final String PRICE_DESTINATION = "/topic/prices";
	private static final String CRYPTO_PREFIX = "BINANCE:";

	public void processFinnhubMessage(String payload) {
		try {
			FinnhubTradeDto finnhubTrade = objectMapper.readValue(payload, FinnhubTradeDto.class);

			if (!finnhubTrade.getType().equals("trade")) {
				return;
			}

			List<PriceUpdateDto> priceUpdates = finnhubTrade.getData();

			for (PriceUpdateDto update : priceUpdates) {
				if (update.getSymbol().startsWith(CRYPTO_PREFIX)) {
					String parsed = parseCryptoSymbol(update.getSymbol());
					update.modifySymbol(parsed);
				}
				messagingTemplate.convertAndSend(PRICE_DESTINATION, update);
			}

		} catch (JsonProcessingException e) {
			log.error("Error parsing message from Finnhub: {}", payload, e);
		}
	}

	public String parseCryptoSymbol(String symbol) {
		return symbol.split(CRYPTO_PREFIX)[1].replace("USDT", "");
	}
}
