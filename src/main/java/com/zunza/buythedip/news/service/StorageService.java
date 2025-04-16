package com.zunza.buythedip.news.service;

import java.util.List;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.zunza.buythedip.constant.RabbitMQConstants;
import com.zunza.buythedip.news.dto.NewsDto;
import com.zunza.buythedip.news.entity.News;
import com.zunza.buythedip.news.repository.NewsRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StorageService {

	private final NewsRepository newsRepository;

	@RabbitListener(queues = RabbitMQConstants.NEWS_STORAGE_QUEUE)
	public void saveNews(List<NewsDto> translatedNewsDtoList) {
		List<News> newsList = translatedNewsDtoList.stream()
			.map(News::from)
			.toList();
		newsRepository.saveAll(newsList);
	}
}
