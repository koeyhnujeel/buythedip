package com.zunza.buythedip.news.dto;

import com.zunza.buythedip.news.entity.News;

import lombok.Builder;
import lombok.Getter;

@Getter
public class NewsDetailResponseDto {
	private Long id;
	private String headline;
	private String summary;
	private String source;
	private String url;
	private Long datetime;

	@Builder
	private NewsDetailResponseDto(Long id, String headline, String summary, String source, String url, Long datetime) {
		this.id = id;
		this.headline = headline;
		this.summary = summary;
		this.source = source;
		this.url = url;
		this.datetime = datetime;
	}

	public static NewsDetailResponseDto from(News news) {
		return NewsDetailResponseDto.builder()
			.id(news.getId())
			.headline(news.getHeadline())
			.summary(news.getSummary())
			.source(news.getSource())
			.url(news.getUrl())
			.datetime(news.getDatetime())
			.build();
	}
}
