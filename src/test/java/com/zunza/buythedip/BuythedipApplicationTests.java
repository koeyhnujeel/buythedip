package com.zunza.buythedip;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.zunza.buythedip.config.TestContainersConfig;

@Testcontainers
@SpringBootTest
@Import(TestContainersConfig.class)
class BuythedipApplicationTests {

	@Test
	void contextLoads() {
	}

}
