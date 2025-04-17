package com.zunza.buythedip.news.dto;

import io.finnhub.api.models.MarketNews;
import lombok.Builder;
import lombok.Getter;

@Getter
public class NewsDto {
	private String headline;
	private String source;
	private String summary;
	private String url;
	private Long datetime;

	@Builder
	private NewsDto(String headline, String source, String summary, String url, Long datetime) {
		this.headline = headline;
		this.source = source;
		this.summary = summary;
		this.url = url;
		this.datetime = datetime;
	}

	public static NewsDto from(MarketNews marketNews) {
		return NewsDto.builder()
			.headline(marketNews.getHeadline())
			.source(marketNews.getSource())
			.summary(marketNews.getSummary())
			.url(marketNews.getUrl())
			.datetime(marketNews.getDatetime())
			.build();
	}

	public void modifyHeadLine(String translatedHeadLine) {
		this.headline = translatedHeadLine;
	}

	public void modifySummary(String translatedSummary) {
		this.summary = translatedSummary;
	}
}
