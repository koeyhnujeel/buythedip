package com.zunza.buythedip.cryptocurrency.service.broadcast;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.zunza.buythedip.constant.ChannelNames;
import com.zunza.buythedip.constant.RabbitMQConstants;
import com.zunza.buythedip.infrastructure.redis.RedisMessagePublisher;
import com.zunza.buythedip.cryptocurrency.dto.SymbolTickerDto;
import com.zunza.buythedip.cryptocurrency.dto.binance.TradeDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SingleSymbolTickerService {

	private final RedisMessagePublisher redisMessagePublisher;
	private final RedisTemplate<String, Object> redisTemplate;


	private static final String OPEN_PRICE_KEY_SUFFIX = ":OPENPRICE";


	@RabbitListener(queues = RabbitMQConstants.SYMBOL_TICKER_BROADCAST_QUEUE)
	public void publishTickerForSymbol(TradeDto tradeDto) {
		try {
			String symbol = tradeDto.getSymbol();

			double currentPrice = tradeDto.getPrice();
			double changePrice = getChangePrice(symbol, currentPrice);
			double changeRate = getChangeRate(symbol, currentPrice);
			String baseSymbol = extractBaseCurrency(symbol);

			SymbolTickerDto symbolTickerDto = SymbolTickerDto.of(baseSymbol, currentPrice, changePrice, changeRate);

			redisMessagePublisher.publishMessage(ChannelNames.SYMBOL_TICKER_TOPIC, symbolTickerDto);

		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

	private double getOpenPrice(String symbol) {
		return (double)redisTemplate.opsForValue().get(symbol + OPEN_PRICE_KEY_SUFFIX);
	}

	private double getChangePrice(String symbol, double currentPrice) {
		double openPrice = getOpenPrice(symbol);
		return currentPrice - openPrice;
	}

	private double getChangeRate(String symbol, double currentPrice) {
		double openPrice = getOpenPrice(symbol);
		return ((currentPrice - openPrice) / openPrice) * 100;
	}

	private String extractBaseCurrency(String symbol) {
		return symbol.replace("USDT", "");
	}
}
