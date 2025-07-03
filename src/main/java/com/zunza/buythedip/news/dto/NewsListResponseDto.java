package com.zunza.buythedip.news.dto;

import com.zunza.buythedip.news.entity.News;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NewsListResponseDto {
	private Long id;
	private String headline;
	private Long datetime;

	@Builder
	public NewsListResponseDto(Long id, String headline, Long datetime) {
		this.id = id;
		this.headline = headline;
		this.datetime = datetime;
	}

	public static NewsListResponseDto from(News news) {
		return NewsListResponseDto.builder()
			.id(news.getId())
			.headline(news.getHeadline())
			.datetime(news.getDatetime())
			.build();
	}
}
