package com.zunza.buythedip.cryptocurrency.scheduler;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.zunza.buythedip.cryptocurrency.service.broadcast.TopSymbolsTickerService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UpdateVolumeScheduler {

	private final RedisTemplate<String, Object> redisTemplate;
	private final TopSymbolsTickerService topSymbolsTickerService;

	private static final String MINUTE_BUCKET_KEY_PREFIX = "tv:";
	private static final String AGGREGATED_VOLUME_KEY = MINUTE_BUCKET_KEY_PREFIX + "30m_aggregated";
	private static final String CACHE_TOP_VOLUME_KEY = "cache:topVolume";

	private static final int AGGREGATION_WINDOW_MINUTES = 30;
	private static final int TOP_N = 50;

	@Scheduled(fixedRate = 10000)
	public void updateVolumeData() {
		aggregateVolumeData();
		Set<ZSetOperations.TypedTuple<Object>> topNVolumeSet = getTopNVolumeSet(TOP_N);

		if (topNVolumeSet == null || topNVolumeSet.isEmpty()) {
			log.info("No Volume Data");
			return;
		}

		topSymbolsTickerService.publishTopVolumeForSymbols(topNVolumeSet);
		cacheTopVolume(topNVolumeSet);
	}

	private void aggregateVolumeData() {
		List<String> keys = generateKeysForLastNMinutes(AGGREGATION_WINDOW_MINUTES);

		if (keys == null || keys.isEmpty()) {
			return;
		}

		redisTemplate.opsForZSet().unionAndStore(keys.get(0), keys.subList(1, keys.size()), AGGREGATED_VOLUME_KEY);
	}

	private List<String> generateKeysForLastNMinutes(int minutes) {
		ZonedDateTime utc = ZonedDateTime.now(ZoneId.of("UTC"));
		return IntStream.range(0, minutes)
			.mapToObj(i -> utc.minusMinutes(i).format(DateTimeFormatter.ofPattern("yyyyMMddHHmm")))
			.map(time -> MINUTE_BUCKET_KEY_PREFIX + time)
			.toList();
	}

	private Set<ZSetOperations.TypedTuple<Object>> getTopNVolumeSet(int n) {
		return redisTemplate.opsForZSet().reverseRangeWithScores(AGGREGATED_VOLUME_KEY, 0, n - 1);
	}

	private void cacheTopVolume(Set<ZSetOperations.TypedTuple<Object>> topNVolumeSet) {
		Set<String> set = topNVolumeSet.stream()
			.map(tuple -> tuple.getValue().toString())
			.collect(Collectors.toSet());

		redisTemplate.opsForValue().set(CACHE_TOP_VOLUME_KEY, set);
	}
}
