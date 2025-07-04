package com.zunza.buythedip;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class BuythedipApplication {

	public static void main(String[] args) {
		SpringApplication.run(BuythedipApplication.class, args);
	}

}
