package com.zunza.buythedip.news.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.assertj.MockMvcTester.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.stream.IntStream;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.zunza.buythedip.news.entity.News;
import com.zunza.buythedip.news.repository.NewsRepository;

@Testcontainers
@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
class NewsControllerTest {

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
	private MockMvc mockMvc;

	@Autowired
	private NewsRepository newsRepository;

	@Autowired
	private DataSource dataSource;

	@BeforeEach
	void setUp() throws SQLException {
		truncateTable();
		List<News> newsList = IntStream.range(1, 11)
			.mapToObj(i ->
				{
					return News.builder()
						.headline("헤드라인" + i)
						.summary("요약" + i)
						.url("url" + i)
						.source("source" + i)
						.datetime((long)i)
						.build();
				}
			)
			.toList();
		newsRepository.saveAll(newsList);
	}

	private void truncateTable() throws SQLException {
		try (Connection connection = dataSource.getConnection();
			 Statement statement = connection.createStatement()) {
			statement.execute("TRUNCATE TABLE news");
		}
	}

	@Test
	void 뉴스_조회_기본_커서() throws Exception {
		// when - then
		mockMvc.perform(get("/api/news"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$[0].id").value(10))
			.andExpect(jsonPath("$[0].headline").value("헤드라인10"))
			.andExpect(jsonPath("$[0].datetime").value(10))
			.andExpect(jsonPath("$[4].id").value(6))
			.andExpect(jsonPath("$[4].headline").value("헤드라인6"))
			.andExpect(jsonPath("$[4].datetime").value(6))
			.andExpect(jsonPath("$[9].id").value(1))
			.andExpect(jsonPath("$[9].headline").value("헤드라인1"))
			.andExpect(jsonPath("$[9].datetime").value(1));
	}

	@Test
	void 뉴스_조회_커서_지정() throws Exception {
		// when -then
		mockMvc.perform(get("/api/news?cursor=6"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$[0].id").value(5))
			.andExpect(jsonPath("$[0].headline").value("헤드라인5"))
			.andExpect(jsonPath("$[0].datetime").value(5))
			.andExpect(jsonPath("$[1].id").value(4))
			.andExpect(jsonPath("$[1].headline").value("헤드라인4"))
			.andExpect(jsonPath("$[1].datetime").value(4))
			.andExpect(jsonPath("$[4].id").value(1))
			.andExpect(jsonPath("$[4].headline").value("헤드라인1"))
			.andExpect(jsonPath("$[4].datetime").value(1));
	}
}
