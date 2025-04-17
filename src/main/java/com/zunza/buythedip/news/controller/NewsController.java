package com.zunza.buythedip.news.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zunza.buythedip.news.dto.NewsListResponseDto;
import com.zunza.buythedip.news.service.NewsService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class NewsController {

	private final NewsService newsService;

	@GetMapping("/api/news")
	public ResponseEntity<List<NewsListResponseDto>> getNewsList(
		@RequestParam(required = false) Long cursor,
		@RequestParam(defaultValue = "20") int size
	) {
		return ResponseEntity.ok(newsService.getNewsList(cursor, size));
	}
}
