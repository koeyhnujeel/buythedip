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

import com.zunza.buythedip.constant.RabbitMQConstants;
import com.zunza.buythedip.infrastructure.gemini.GeminiClient;
import com.zunza.buythedip.infrastructure.gemini.dto.CandidateDto;
import com.zunza.buythedip.infrastructure.gemini.dto.ContentDto;
import com.zunza.buythedip.infrastructure.gemini.dto.GeminiResponseDto;
import com.zunza.buythedip.infrastructure.gemini.dto.PartDto;
import com.zunza.buythedip.infrastructure.messaging.RabbitMQService;
import com.zunza.buythedip.news.dto.NewsDto;

import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
class NewsTranslationServiceTest {

	@Mock
	private RabbitMQService rabbitMQService;

	@Mock
	private GeminiClient geminiClient;

	@InjectMocks
	private NewsTranslationService newsTranslationService;

	@Test
	void 뉴스_리스트가_들어오면_헤드라인과_요약을_번역하고_큐에_전송한다() {
		// given
		NewsDto newsDto = NewsDto.builder()
			.headline("Original Headline")
			.summary("Original Summary")
			.build();

		String text = "headline: 번역된 헤드라인\n\nsummary: 번역된 요약";

		PartDto partDto = PartDto.from(text);
		ContentDto contentDto = ContentDto.from(List.of(partDto));
		CandidateDto candidateDto = CandidateDto.from(contentDto);
		GeminiResponseDto geminiResponseDto = GeminiResponseDto.from(List.of(candidateDto));

		when(geminiClient.translateHeadlineAndSummary(newsDto.getHeadline(), newsDto.getSummary()))
			.thenReturn(Mono.just(geminiResponseDto));

		List<NewsDto> newsDtoList = List.of(newsDto);

		// when
		newsTranslationService.translateNews(newsDtoList);

		// then
		ArgumentCaptor<List<NewsDto>> captor = ArgumentCaptor.forClass(List.class);
		verify(rabbitMQService).publishMessage(
			eq(RabbitMQConstants.PUBLIC_EXCHANGE),
			eq(RabbitMQConstants.NEWS_STORAGE_ROUTING_KEY),
			captor.capture()
		);

		List<NewsDto> translatedNewsDtoList = captor.getValue();
		NewsDto translatedNewsDto = translatedNewsDtoList.get(0);

		assertEquals(translatedNewsDto.getHeadline(), "번역된 헤드라인");
		assertEquals(translatedNewsDto.getSummary(), "번역된 요약");
	}
}
