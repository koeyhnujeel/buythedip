package com.zunza.buythedip.news.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zunza.buythedip.news.dto.NewsDetailResponseDto;
import com.zunza.buythedip.news.dto.NewsListResponseDto;
import com.zunza.buythedip.news.service.NewsCacheService;
import com.zunza.buythedip.news.service.NewsService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class NewsController {

	private final NewsService newsService;
	private final NewsCacheService newsCacheService;

	@GetMapping("/api/news")
	public ResponseEntity<List<NewsListResponseDto>> getNewsList(
		@RequestParam(required = false) Long cursor,
		@RequestParam(defaultValue = "20") int size
	) {
		List<NewsListResponseDto> cachedNewsList = newsCacheService.getCachedNewsList(cursor, size);

		if (cachedNewsList == null || cachedNewsList.isEmpty()) {
			List<NewsListResponseDto> newsList = newsService.getNewsList(cursor, size);
			newsCacheService.cacheMissedNews(newsList);
			return ResponseEntity.ok(newsList);
		}

		return ResponseEntity.ok(cachedNewsList);
		// return ResponseEntity.ok(newsService.getNewsList(cursor, size));
	}

	@GetMapping("/api/news/{newsId}")
	public ResponseEntity<NewsDetailResponseDto> getNews(
		@PathVariable Long newsId
	) {
		return ResponseEntity.ok(newsService.getNews(newsId));
	}
}
