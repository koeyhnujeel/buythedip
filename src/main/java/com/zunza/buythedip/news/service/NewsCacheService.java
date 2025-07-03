package com.zunza.buythedip.news.service;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zunza.buythedip.news.dto.NewsListResponseDto;
import com.zunza.buythedip.news.entity.News;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class NewsCacheService {

	private final ObjectMapper objectMapper;
	private final RedisTemplate<String, Object> redisTemplate;

	private static final String NEWS_FEED_KEY = "news:feed";
	private static final String NEWS_DATA_KEY_PREFIX = "news:data:";
	private static final int MAX_CACHED_NEWS = 150;

	public void cacheNewsList(List<News> newsList) {
		List<NewsListResponseDto> newsListResponseDto = newsList.stream()
			.map(NewsListResponseDto::from)
			.toList();

		addToCache(newsListResponseDto);
		trimOldNews();
	}

	public List<NewsListResponseDto> getCachedNewsList(Long cursor, int size) {
		Set<Object> newsIds;

		if (cursor == null) {
			newsIds = redisTemplate.opsForZSet().reverseRange(NEWS_FEED_KEY, 0, size - 1);
		} else {
			newsIds = redisTemplate.opsForZSet().reverseRangeByScore(NEWS_FEED_KEY, 0, cursor - 1, 0, size);
		}

		if (newsIds == null || newsIds.isEmpty()) {
			log.info("News List Cache Miss!");
			return Collections.emptyList();
		}

		log.info("News List Cache Hit!");
		return newsIds.stream()
			.map(id -> getNewsData(id.toString()))
			.toList();
	}

	public void cacheMissedNews(List<NewsListResponseDto> newsListResponseDto) {
		log.info("try cache missed news!");
		addToCache(newsListResponseDto);
	}

	private void addToCache(List<NewsListResponseDto> newsListResponseDto) {
		newsListResponseDto.forEach(dto -> {
			redisTemplate.opsForZSet().add(NEWS_FEED_KEY, dto.getId(), dto.getDatetime());
			redisTemplate.opsForValue().set(NEWS_DATA_KEY_PREFIX + dto.getId(), dto);
		});
	}

	private void trimOldNews() {
		Long currentSize = redisTemplate.opsForZSet().size(NEWS_FEED_KEY);

		if (MAX_CACHED_NEWS >= currentSize) {
			return;
		}

		long removeCount = currentSize - MAX_CACHED_NEWS;

		Set<Object> oldNewsIds = redisTemplate.opsForZSet().range(NEWS_FEED_KEY, 0, removeCount - 1);
		List<String> keysToRemove = oldNewsIds.stream()
			.map(id -> NEWS_DATA_KEY_PREFIX + id)
			.toList();

		redisTemplate.delete(keysToRemove);
		redisTemplate.opsForZSet().removeRange(NEWS_FEED_KEY, 0, removeCount - 1);
	}

	private NewsListResponseDto getNewsData(String newsId) {
		Object dto = redisTemplate.opsForValue().get(NEWS_DATA_KEY_PREFIX + newsId);
		return objectMapper.convertValue(dto, NewsListResponseDto.class);
	}
}
