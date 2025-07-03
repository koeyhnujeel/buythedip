package com.zunza.buythedip.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.genai.Client;

@Configuration
public class GenaiClientConfig {

	@Value("${gemini.secret}")
	private String apiKey;

	@Bean
	public Client genaiClient() {
		return new Client.Builder()
			.apiKey(apiKey)
			.build();
	}
}
