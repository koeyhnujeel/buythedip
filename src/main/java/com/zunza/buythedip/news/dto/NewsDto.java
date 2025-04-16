package com.zunza.buythedip.news.dto;

import io.finnhub.api.models.MarketNews;
import lombok.Builder;
import lombok.Getter;

@Getter
public class NewsDto {
	private String headLine;
	private String source;
	private String summary;
	private String url;
	private Long dateTime;

	@Builder
	private NewsDto(String headLine, String source, String summary, String url, Long dateTime) {
		this.headLine = headLine;
		this.source = source;
		this.summary = summary;
		this.url = url;
		this.dateTime = dateTime;
	}

	public static NewsDto from(MarketNews marketNews) {
		return NewsDto.builder()
			.headLine(marketNews.getHeadline())
			.source(marketNews.getSource())
			.summary(marketNews.getSummary())
			.url(marketNews.getUrl())
			.dateTime(marketNews.getDatetime())
			.build();
	}

	public void modifyHeadLine(String translatedHeadLine) {
		this.headLine = translatedHeadLine;
	}

	public void modifySummary(String translatedSummary) {
		this.summary = translatedSummary;
	}
}
