package com.zunza.buythedip.news.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;

import com.google.genai.errors.ApiException;
import com.rabbitmq.client.Channel;
import com.zunza.buythedip.constant.RabbitMQConstants;
import com.zunza.buythedip.infrastructure.gemini.service.GenAiService;
import com.zunza.buythedip.infrastructure.messaging.RabbitMQService;
import com.zunza.buythedip.news.dto.NewsDto;


@ExtendWith(MockitoExtension.class)
class NewsTranslationServiceTest {

	@Mock
	private RabbitMQService rabbitMQService;

	@Mock
	private GenAiService genAiService;

	@Mock
	private Channel channel;

	@InjectMocks
	private NewsTranslationService newsTranslationService;

	@Test
	void 뉴스_리스트가_들어오면_헤드라인과_요약을_번역하고_큐에_전송한다() throws IOException {
		// given
		NewsDto newsDto = NewsDto.builder()
			.headline("Original Headline")
			.summary("Original Summary")
			.build();
		List<NewsDto> newsDtoList = List.of(newsDto);

		Message message = MessageBuilder.withBody(new byte[0])
			.setHeader("x-retry-count", null)
			.build();

		String response = "headline: 번역된 헤드라인\n\nsummary: 번역된 요약";

		when(genAiService.generate(anyString()))
			.thenReturn(response);

		// when
		newsTranslationService.translateNews(newsDtoList, message, channel, 123L);

		// then
		ArgumentCaptor<List<NewsDto>> captor = ArgumentCaptor.forClass(List.class);
		verify(channel).basicAck(123L, false);
		verify(rabbitMQService, only()).publishMessage(
			eq(RabbitMQConstants.PUBLIC_EXCHANGE),
			eq(RabbitMQConstants.NEWS_STORAGE_ROUTING_KEY),
			captor.capture()
		);

		List<NewsDto> value = captor.getValue();
		assertEquals("번역된 헤드라인", value.get(0).getHeadline());
		assertEquals("번역된 요약", value.get(0).getSummary());
	}

	@Test
	void API_할당량_한도를_초과하면_큐에_넣지않고_ack로_메시지를_완전_소비처리한다() throws IOException {
		// given
		NewsDto newsDto = NewsDto.builder()
			.headline("Original Headline")
			.summary("Original Summary")
			.build();
		List<NewsDto> newsDtoList = List.of(newsDto);

		Message message = MessageBuilder.withBody(new byte[0])
			.setHeader("x-retry-count", 0)
			.build();

		ApiException mockApiException = mock(ApiException.class);
		when(mockApiException.status())
			.thenReturn("RESOURCE_EXHAUSTED");

		when(genAiService.generate(anyString()))
			.thenThrow(mockApiException);

		//when
		newsTranslationService.translateNews(newsDtoList, message, channel, 123L);

		//then
		verify(channel, only()).basicAck(123L, false);
		verify(rabbitMQService, never()).publishMessage(
			anyString(),
			anyString(),
			any()
		);
	}

	@Test
	void 일반_예외_발생_시_수동으로_재시도_처리를_한다() throws IOException {
		// given
		NewsDto newsDto = NewsDto.builder()
			.headline("Original Headline")
			.summary("Original Summary")
			.build();
		List<NewsDto> newsDtoList = List.of(newsDto);

		Message message = MessageBuilder.withBody(new byte[0])
			.setHeader("x-retry-count", 0)
			.build();

		when(genAiService.generate(anyString()))
			.thenThrow(new RuntimeException());

		//when
		newsTranslationService.translateNews(newsDtoList, message, channel, 123L);

		//then
		verify(rabbitMQService, only()).publishMessage(
			eq(RabbitMQConstants.PUBLIC_EXCHANGE),
			eq(RabbitMQConstants.NEWS_TRANSLATION_ROUTING_KEY + ".retry"),
			eq(message)
		);
		verify(channel, only()).basicAck(123L, false);

		assertEquals(1, message.getMessageProperties().getHeaders().get("x-retry-count"));
	}

	@Test
	void 재시도_횟수를_초과하면_ack로_메시지지를_완전_소비해버린다() throws IOException {
		// given
		NewsDto newsDto = NewsDto.builder()
			.headline("Original Headline")
			.summary("Original Summary")
			.build();
		List<NewsDto> newsDtoList = List.of(newsDto);

		Message message = MessageBuilder.withBody(new byte[0])
			.setHeader("x-retry-count", 3)
			.build();

		when(genAiService.generate(anyString()))
			.thenThrow(new RuntimeException());

		//when
		newsTranslationService.translateNews(newsDtoList, message, channel, 123L);

		//then
		verify(rabbitMQService, never()).publishMessage(
			anyString(),
			anyString(),
			any()
		);
		verify(channel, only()).basicAck(123L, false);
	}
}
