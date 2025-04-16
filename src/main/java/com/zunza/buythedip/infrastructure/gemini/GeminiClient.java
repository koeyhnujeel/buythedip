package com.zunza.buythedip.infrastructure.gemini;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.zunza.buythedip.infrastructure.gemini.dto.ContentDto;
import com.zunza.buythedip.infrastructure.gemini.dto.GeminiRequestDto;
import com.zunza.buythedip.infrastructure.gemini.dto.GeminiResponseDto;
import com.zunza.buythedip.infrastructure.gemini.dto.PartDto;

import reactor.core.publisher.Mono;

@Component
public class GeminiClient {

	private final String uri;
	private final String apiKey;
	private final WebClient webClient;

	public GeminiClient(
		WebClient.Builder webClientBuilder,
		@Value("${gemini.baseUrl}") String baseUrl,
		@Value("${gemini.uri}") String uri,
		@Value("${gemini.secret}") String apiKey
	) {
		this.uri = uri;
		this.apiKey = apiKey;
		this.webClient = webClientBuilder
			.baseUrl(baseUrl)
			.build();
	}

	public Mono<GeminiResponseDto> translateHeadlineAndSummary(String headline, String summary) {
		String body = "headline: " + headline + "\n" + "summary: " + summary + "\n";
		String prompt = """
			1. Do not translate company names and person names into Korean
			2. Please translate the headline and summary into Korean.
			3. Do not include anything other than the headline and summary.
			4. Please respond with only the translated content.
			5. Please format the response as shown below:
			   headline: translated content
			
			   summary: translated content
			""";

		PartDto partDto = PartDto.from(body + prompt);
		ContentDto contentDto = ContentDto.from(List.of(partDto));
		GeminiRequestDto geminiRequestDto = GeminiRequestDto.from(List.of(contentDto));

		return webClient.post()
			.uri(uriBuilder -> uriBuilder
				.path(uri)
				.queryParam("key", apiKey)
				.build()
			)
			.body(BodyInserters.fromValue(geminiRequestDto))
			.retrieve()
			.bodyToMono(GeminiResponseDto.class);
	}
}
