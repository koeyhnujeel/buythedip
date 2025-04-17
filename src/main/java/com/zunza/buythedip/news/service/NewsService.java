package com.zunza.buythedip.news.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.zunza.buythedip.news.dto.NewsListResponseDto;
import com.zunza.buythedip.news.repository.NewsRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NewsService {

	private final NewsRepository newsRepository;

	public List<NewsListResponseDto> getNewsList(Long cursor, int size) {
		return newsRepository.findByCursor(cursor, size);
	}
}
