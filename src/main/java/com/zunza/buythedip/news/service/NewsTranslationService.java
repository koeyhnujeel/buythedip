package com.zunza.buythedip.news.service;


import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import com.google.genai.errors.ApiException;
import com.rabbitmq.client.Channel;
import com.zunza.buythedip.constant.RabbitMQConstants;
import com.zunza.buythedip.infrastructure.gemini.service.GenAiService;
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
	private final GenAiService genAiService;

	private static final int MAX_RETRY_COUNT = 3;

	@RabbitListener(queues = RabbitMQConstants.NEWS_TRANSLATION_QUEUE, ackMode = "MANUAL")
	public void translateNews(
		List<NewsDto> list,
		Message message,
		Channel channel,
		@Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag
	) throws IOException {
		try {
			List<NewsDto> translatedNewsDtoList = list.stream()
				.map(dto -> {
					String headline = dto.getHeadline();
					String summary = dto.getSummary();
					String response = genAiService.generate(setPrompt(headline, summary));

					Map<String, String> parsed = TranslatedContentParser.parse(response);
					dto.modifyHeadLine(parsed.get("headline"));
					dto.modifySummary(parsed.get("summary"));
					return dto;
				})
				.toList();

			rabbitMQService.publishMessage(
				RabbitMQConstants.PUBLIC_EXCHANGE,
				RabbitMQConstants.NEWS_STORAGE_ROUTING_KEY,
				translatedNewsDtoList
			);

			channel.basicAck(deliveryTag, false);

		} catch (Exception e) {
			if (isLimitExceeded(e)) {
				log.warn("API Quota Limit Exceeded");
				channel.basicAck(deliveryTag, false);
				return;
			}

			int currentRetryCount = getRetryCount(message);
			log.info("current retry count: {}", currentRetryCount);
			if (currentRetryCount >= MAX_RETRY_COUNT) {
				log.info("retry count exceeded");
				channel.basicAck(deliveryTag, false);
				return;
			}

			publishMessageToRetryQueue(message);
			channel.basicAck(deliveryTag, false);
		}
	}

	private String setPrompt(String headline, String summary) {
		String body = "headline: " + headline + "\n" + "summary: " + summary + "\n";
		String prompt = """
			Translate only the headline and the summary paragraph (lede) of an English-language news article into Korean. Follow the rules below carefully:
			1.	Do not translate company names or person names (e.g., “Trump”, “Apple”); keep them in English.
			2.	Recognize that the content is a news article and use a tone appropriate for Korean news reporting.
			3.	Translate only the headline and the first summary (lede) paragraph.
			4.	Do not include anything other than the translation.
			5.	Format the output exactly like this:
			   headline: translated content
			
			   summary: translated content
			""";

		return body + prompt;
	}

	private boolean isLimitExceeded(Exception e) {
		return e instanceof ApiException && ((ApiException)e).status().equals("RESOURCE_EXHAUSTED");
	}

	private int getRetryCount(Message message) {
		Integer retryCount = (Integer)message.getMessageProperties().getHeaders().get("x-retry-count");
		return retryCount == null ? 0 : retryCount;
	}

	private void incrementRetryCount(Message message) {
		message.getMessageProperties().getHeaders().put("x-retry-count", getRetryCount(message) + 1);
	}

	private void publishMessageToRetryQueue(Message message) {
		incrementRetryCount(message);

		rabbitMQService.publishMessage(
			RabbitMQConstants.PUBLIC_EXCHANGE,
			RabbitMQConstants.NEWS_TRANSLATION_ROUTING_KEY + ".retry",
			message
		);
	}
}
