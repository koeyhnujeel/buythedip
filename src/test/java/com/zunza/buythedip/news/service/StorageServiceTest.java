package com.zunza.buythedip.news.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.zunza.buythedip.news.dto.NewsDto;
import com.zunza.buythedip.news.entity.News;
import com.zunza.buythedip.news.repository.NewsRepository;

@ExtendWith(MockitoExtension.class)
class StorageServiceTest {

	@Mock
	private NewsRepository newsRepository;

	@InjectMocks
	private StorageService storageService;

	@Test
	void 번역된_뉴스를_저장한다() {
		// given
		NewsDto newsDto1 = NewsDto.builder()
			.headline("헤드라인1")
			.summary("요약1")
			.url("url1")
			.source("source1")
			.datetime(1232312L)
			.build();

		NewsDto newsDto2 = NewsDto.builder()
			.headline("헤드라인2")
			.summary("요약2")
			.url("url2")
			.source("source2")
			.datetime(1232310L)
			.build();

		List<NewsDto> translatedNewsDtoList = List.of(newsDto1, newsDto2);

		// when
		storageService.saveNews(translatedNewsDtoList);

		// then
		ArgumentCaptor<List<News>> captor = ArgumentCaptor.forClass(List.class);
		verify(newsRepository, times(1)).saveAll(captor.capture());

		List<News> savedNewsList = captor.getValue();

		assertEquals(2, savedNewsList.size());
		assertEquals("헤드라인1", savedNewsList.get(0).getHeadline());
		assertEquals("요약1", savedNewsList.get(0).getSummary());
		assertEquals("url1", savedNewsList.get(0).getUrl());
		assertEquals("source1", savedNewsList.get(0).getSource());
		assertEquals(1232312L, savedNewsList.get(0).getDatetime());

		assertEquals( "헤드라인2", savedNewsList.get(1).getHeadline());
		assertEquals("요약2", savedNewsList.get(1).getSummary());
		assertEquals("url2", savedNewsList.get(1).getUrl());
		assertEquals("source2", savedNewsList.get(1).getSource());
		assertEquals(1232310L, savedNewsList.get(1).getDatetime());
	}
}
