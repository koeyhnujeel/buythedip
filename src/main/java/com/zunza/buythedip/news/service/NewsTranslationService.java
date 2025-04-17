package com.zunza.buythedip.news.service;

import java.util.List;
import java.util.Map;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.zunza.buythedip.constant.RabbitMQConstants;
import com.zunza.buythedip.infrastructure.gemini.GeminiClient;
import com.zunza.buythedip.infrastructure.gemini.dto.GeminiResponseDto;
import com.zunza.buythedip.infrastructure.messaging.RabbitMQService;
import com.zunza.buythedip.news.dto.NewsDto;
import com.zunza.buythedip.news.util.TranslatedContentParser;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class NewsTranslationService {

	private final RabbitMQService rabbitMQService;
	private final GeminiClient geminiClient;

	@RabbitListener(queues = RabbitMQConstants.NEWS_TRANSLATION_QUEUE)
	public void translateNews(List<NewsDto> newsDtoList) {
		List<NewsDto> translatedNewsDtoList = newsDtoList.stream()
			.map(newsDto -> {
				String headLine = newsDto.getHeadline();
				String summary = newsDto.getSummary();
				GeminiResponseDto geminiResponseDto = geminiClient.translateHeadlineAndSummary(headLine, summary).block();

				String text = geminiResponseDto.getResult();
				Map<String, String> parsed = TranslatedContentParser.parse(text);

				newsDto.modifyHeadLine(parsed.get("headline"));
				newsDto.modifySummary(parsed.get("summary"));
				return newsDto;
			})
			.toList();

		rabbitMQService.publishMessage(
			RabbitMQConstants.PUBLIC_EXCHANGE,
			RabbitMQConstants.NEWS_STORAGE_ROUTING_KEY,
			translatedNewsDtoList
		);
	}
}
