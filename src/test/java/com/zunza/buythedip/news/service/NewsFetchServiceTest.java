package com.zunza.buythedip.news.service;

import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.zunza.buythedip.constant.RabbitMQConstants;
import com.zunza.buythedip.infrastructure.messaging.RabbitMQService;
import com.zunza.buythedip.news.dto.NewsDto;
import com.zunza.buythedip.news.entity.MinId;
import com.zunza.buythedip.news.repository.MinIdRepository;

import io.finnhub.api.apis.DefaultApi;
import io.finnhub.api.models.MarketNews;

@ExtendWith(MockitoExtension.class)
class NewsFetchServiceTest {

	@Mock
	private RabbitMQService rabbitMQService;

	@Mock
	private DefaultApi finnHubClient;

	@Mock
	private MinIdRepository minIdRepository;

	@InjectMocks
	private NewsFetchService newsFetchService;

	@Test
	void 뉴스를_수집하고_큐에_전송한_뒤_가장_최신_뉴스_id를_minId에_저장한다() {
		// given
		for (Topic topic : Topic.values()) {
			MinId minId = new MinId(topic);
			minId.updateLatestId(100L);

			when(minIdRepository.findByTopic(topic))
				.thenReturn(Optional.of(minId));

			when(minIdRepository.save(any()))
				.thenReturn(new MinId(topic));

			List<MarketNews> mockNewsList = createMockNewsList(101L, 102L, 103L);
			when(finnHubClient.marketNews(topic.getValue(), 100L))
				.thenReturn(mockNewsList);
		}

		// when
		newsFetchService.fetchNews();

		// then
		for (Topic topic : Topic.values()) {
			verify(minIdRepository).findByTopic(topic);
			verify(finnHubClient).marketNews(eq(topic.getValue()), eq(100L));

			verify(rabbitMQService, times(2)).publishMessage(
				eq(RabbitMQConstants.PUBLIC_EXCHANGE),
				eq(RabbitMQConstants.NEWS_TRANSLATION_ROUTING_KEY),
				argThat((List<NewsDto> newsDtoList) ->
					newsDtoList.size() == 3 &&
						newsDtoList.get(0).getHeadLine().equals("sample headline101") &&
						newsDtoList.get(1).getHeadLine().equals("sample headline102") &&
						newsDtoList.get(2).getHeadLine().equals("sample headline103")
				)
			);

			verify(minIdRepository).save(argThat((MinId saved) ->
				saved.getTopic().equals(topic) &&
					saved.getLatestId() == 103L
			));
		}
	}

	private List<MarketNews> createMockNewsList(Long... ids) {
		return Arrays.stream(ids)
			.map(id -> {
				MarketNews news = mock(MarketNews.class);
				when(news.getId()).thenReturn(id);
				when(news.getHeadline()).thenReturn("sample headline" + id);
				return news;
			})
			.toList();
	}
}
