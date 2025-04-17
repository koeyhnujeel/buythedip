package com.zunza.buythedip.news.repository;

import java.util.List;

import com.zunza.buythedip.news.dto.NewsListResponseDto;

public interface QuerydslNewsRepository {
	List<NewsListResponseDto> findByCursor(Long cursor, int size);
}
