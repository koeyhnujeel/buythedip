package com.zunza.buythedip.news.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.zunza.buythedip.news.dto.NewsDetailResponseDto;
import com.zunza.buythedip.news.entity.News;
import com.zunza.buythedip.news.exception.NewsNotFoundException;
import com.zunza.buythedip.news.repository.NewsRepository;

@ExtendWith(MockitoExtension.class)
class NewsServiceTest {

	@Mock
	private NewsRepository newsRepository;

	@InjectMocks
	private NewsService newsService;

	@Test
	void 뉴스_단건_조회_성공() {
		// given
		Long newsId = 1L;
		News news = News.builder()
			.headline("헤드라인")
			.summary("요약")
			.source("source")
			.url("url")
			.datetime(23213L)
			.build();

		when(newsRepository.findById(newsId)).thenReturn(Optional.of(news));

		// when
		NewsDetailResponseDto newsDetailResponseDto = newsService.getNews(newsId);

		// then
		assertEquals("헤드라인", newsDetailResponseDto.getHeadline());
		assertEquals("요약", newsDetailResponseDto.getSummary());
		assertEquals("source", newsDetailResponseDto.getSource());
		assertEquals("url", newsDetailResponseDto.getUrl());
		assertEquals(23213L, newsDetailResponseDto.getDatetime());
	}

	@Test
	void 뉴스_단건_조회_실패_시_예외를_던진다() {
		// given
		Long newsId = 2L;

		when(newsRepository.findById(newsId)).thenReturn(Optional.empty());

		// when - then
		assertThrows(NewsNotFoundException.class, () ->
			newsService.getNews(newsId));
	}
}
