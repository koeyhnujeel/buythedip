package com.zunza.buythedip.cryptocurrency.service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import com.zunza.buythedip.cryptocurrency.client.BinanceClient;
import com.zunza.buythedip.cryptocurrency.dto.CryptoDataWithLogoDto;
import com.zunza.buythedip.cryptocurrency.dto.CryptoInfoDto;
import com.zunza.buythedip.cryptocurrency.dto.RankingDto;
import com.zunza.buythedip.cryptocurrency.dto.binance.KlineDto;
import com.zunza.buythedip.cryptocurrency.exception.CryptoCurrencyNotFoundException;
import com.zunza.buythedip.cryptocurrency.repository.CryptocurrencyRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CryptocurrencyService {

	private final CryptocurrencyRepository cryptocurrencyRepository;
	private final SimpMessageSendingOperations messagingTemplate;
	private final RedisTemplate<String, Object> redisTemplate;
	private final BinanceClient binanceClient;

	private static final String MINUTE_BUCKET_KEY_PREFIX = "tv:";
	private static final String AGGREGATED_VOLUME_KEY = MINUTE_BUCKET_KEY_PREFIX + "30m_aggregated";
	private static final int TOP_N = 50;

	public CryptoInfoDto getInfo(Long cryptocurrencyId) {
		return cryptocurrencyRepository.findByIdWithMetadata(cryptocurrencyId)
			.orElseThrow(() -> new CryptoCurrencyNotFoundException(cryptocurrencyId));
	}

	public List<RankingDto> getTopVolume() {
		Set<ZSetOperations.TypedTuple<Object>> zset = redisTemplate.opsForZSet()
			.reverseRangeWithScores(AGGREGATED_VOLUME_KEY, 0, TOP_N - 1);

		Map<String, CryptoDataWithLogoDto> cryptoMap = cryptocurrencyRepository.findAllWithLogo()
			.stream()
			.collect(Collectors.toMap(CryptoDataWithLogoDto::getSymbol, c -> c));

		return zset.stream()
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
	}

	public void getHistoricalCandles(String symbol, String interval) {
		Mono<List<KlineDto>> klines = binanceClient.getKlines(symbol + "USDT", interval);

		klines.subscribe(candles ->
			messagingTemplate.convertAndSend(
				"/queue/crypto/kline/" + symbol + "/" + interval,
				candles)
			);
	}
}
