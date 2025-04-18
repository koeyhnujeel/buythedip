package com.zunza.buythedip.news.service;

import static org.junit.jupiter.api.Assertions.*;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.zunza.buythedip.news.dto.NewsDetailResponseDto;
import com.zunza.buythedip.news.entity.News;
import com.zunza.buythedip.news.exception.NewsNotFoundException;
import com.zunza.buythedip.news.repository.NewsRepository;

@Testcontainers
@SpringBootTest
class NewsServiceTest {

	@Container
	static MySQLContainer<?> mysqlContainer = new MySQLContainer<>("mysql:8.0")
		.withDatabaseName("testdb")
		.withUsername("testuser")
		.withPassword("testpass");

	@DynamicPropertySource
	static void overrideProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", mysqlContainer::getJdbcUrl);
		registry.add("spring.datasource.username", mysqlContainer::getUsername);
		registry.add("spring.datasource.password", mysqlContainer::getPassword);
		registry.add("spring.datasource.driver-class-name", mysqlContainer::getDriverClassName);
	}

	@Autowired
	private NewsService newsService;

	@Autowired
	private NewsRepository newsRepository;

	@Autowired
	private DataSource dataSource;

	@Test
	void 뉴스_단건_조회_성공() {
		// given
		News news = News.builder()
			.headline("헤드라인")
			.summary("요약")
			.source("source")
			.url("url")
			.datetime(23213L)
			.build();
		News saved = newsRepository.save(news);

		// when
		NewsDetailResponseDto newsDetailResponseDto = newsService.getNews(saved.getId());

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

		// when - then
		assertThrows(NewsNotFoundException.class, () ->
			newsService.getNews(newsId));
	}
}
