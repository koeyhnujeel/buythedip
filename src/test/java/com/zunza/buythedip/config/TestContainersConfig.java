package com.zunza.buythedip.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.MySQLContainer;

import com.redis.testcontainers.RedisContainer;

@TestConfiguration(proxyBeanMethods = false)
public class TestContainersConfig {

	@Bean
	@ServiceConnection
	public MySQLContainer<?> mySQLContainer() {
		return new MySQLContainer<>("mysql:8.0")
			.withDatabaseName("testdb")
			.withUsername("test")
			.withPassword("test")
			.withReuse(true);
	}

	@Bean
	public RedisContainer redisContainer() {
		RedisContainer container = new RedisContainer("redis:7-alpine")
			.withReuse(true);
		container.start();

		System.setProperty("spring.data.redis.host", container.getHost());
		System.setProperty("spring.data.redis.port", container.getMappedPort(6379).toString());
		return container;
	}
}
