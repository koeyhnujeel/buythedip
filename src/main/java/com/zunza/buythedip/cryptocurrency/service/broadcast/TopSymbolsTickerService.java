package com.zunza.buythedip.cryptocurrency.service.broadcast;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import com.zunza.buythedip.constant.ChannelNames;
import com.zunza.buythedip.constant.RabbitMQConstants;
import com.zunza.buythedip.infrastructure.redis.RedisMessagePublisher;
import com.zunza.buythedip.cryptocurrency.dto.CryptoDataWithLogoDto;
import com.zunza.buythedip.cryptocurrency.dto.RankingDto;
import com.zunza.buythedip.cryptocurrency.dto.RealtimePriceDto;
import com.zunza.buythedip.cryptocurrency.dto.binance.TradeDto;
import com.zunza.buythedip.cryptocurrency.repository.CryptocurrencyRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class TopSymbolsTickerService {

	private final CryptocurrencyRepository cryptocurrencyRepository;
	private final RedisMessagePublisher redisMessagePublisher;
	private final RedisTemplate<String, Object> redisTemplate;

	private static final String OPEN_PRICE_KEY_SUFFIX = ":OPENPRICE";

	public void publishTopVolumeForSymbols(Set<ZSetOperations.TypedTuple<Object>> TopNVolumeSet) {
		try {
			Map<String, CryptoDataWithLogoDto> cryptoMap = cryptocurrencyRepository.findAllWithLogo()
				.stream()
				.collect(Collectors.toMap(CryptoDataWithLogoDto::getSymbol, c -> c));

			List<RankingDto> res = TopNVolumeSet.stream()
				.map(tuple -> {
					String symbol = tuple.getValue().toString().replace("USDT", "");
					CryptoDataWithLogoDto crypto = cryptoMap.get(symbol);

					return RankingDto.of(
						crypto.getId(),
						crypto.getName(),
						crypto.getSymbol(),
						crypto.getLogo(),
						Double.parseDouble(tuple.getScore().toString()));
				})
				.toList();

			redisMessagePublisher.publishMessage(ChannelNames.TOP_VOLUME_TOPIC, res);
			log.info("Successfully published updated TopVolume Ranking");

		} catch (Exception e) {
			log.error("Failed to serialize ranking data to JSON.", e);
			log.error(e.getMessage());
		}
	}

	@RabbitListener(queues = RabbitMQConstants.TOP_VOLUME_TICKER_BROADCAST_QUEUE)
	public void publishTopTickerForSymbol(TradeDto tradeDto) {
		try {
			String symbol = tradeDto.getSymbol();

			RealtimePriceDto realtimePriceDto = RealtimePriceDto.of(
				extractBaseCurrency(symbol),
				tradeDto.getPrice(),
				getChangeRate(symbol, tradeDto.getPrice())
			);

			redisMessagePublisher.publishMessage(ChannelNames.TOP_PRICE_TICK_TOPIC, realtimePriceDto);

		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

	private String extractBaseCurrency(String symbol) {
		return symbol.replace("USDT", "");
	}

	private double getOpenPrice(String symbol) {
		String openPriceStr = redisTemplate.opsForValue().get(symbol + OPEN_PRICE_KEY_SUFFIX).toString();
		return Double.parseDouble(openPriceStr);
	}

	private double getChangeRate(String symbol, double currentPrice) {
		double openPrice = getOpenPrice(symbol);
		return ((currentPrice - openPrice) / openPrice) * 100;
	}
}
