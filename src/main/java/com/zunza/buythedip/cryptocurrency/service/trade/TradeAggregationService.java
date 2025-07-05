package com.zunza.buythedip.cryptocurrency.service.trade;


import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.zunza.buythedip.constant.RabbitMQConstants;
import com.zunza.buythedip.cryptocurrency.dto.binance.TradeDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class TradeAggregationService {

	private final RedisTemplate<String, Object> redisTemplate;

	private static final long BUCKET_TTL_MINUTE = 31L;
	private static final String MINUTE_BUCKET_KEY_PREFIX = "tv:";

	@RabbitListener(queues = RabbitMQConstants.TRADE_AGGREGATION_QUEUE)
	public void aggregateVolumeLast30Minutes(TradeDto tradeDto) {
		try {
			String bucketKey = generateCurrentMinuteBucketKey(tradeDto.getTradeTime());
			double volume = tradeDto.getPrice() * tradeDto.getQuantity();

			redisTemplate.opsForZSet().incrementScore(bucketKey, tradeDto.getSymbol(), volume);
			redisTemplate.expire(bucketKey, Duration.ofMinutes(BUCKET_TTL_MINUTE));

		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

	private String generateCurrentMinuteBucketKey(Long tradeTime) {
		Instant tradeInstant = Instant.ofEpochMilli(tradeTime);
		LocalDateTime tradeDateTime = LocalDateTime.ofInstant(tradeInstant, ZoneId.of("UTC"));

		return  MINUTE_BUCKET_KEY_PREFIX + tradeDateTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
	}
}
