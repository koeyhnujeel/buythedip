package com.zunza.buythedip.news.entity;

import com.zunza.buythedip.news.dto.NewsDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "news", indexes = {
	@Index(name = "idx_news_datetime_id_headline", columnList = "datetime DESC, id DESC, headline")
})
public class News {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String headline;

	@Column(columnDefinition = "TEXT", nullable = false)
	private String summary;

	@Column(nullable = false)
	private String source;

	@Column(nullable = false)
	private String url;

	@Column(nullable = false)
	private Long datetime;

	@Builder
	private News(String headline, String summary, String source, String url, Long datetime) {
		this.headline = headline;
		this.summary = summary;
		this.source = source;
		this.url = url;
		this.datetime = datetime;
	}

	public static News from(NewsDto translatedNewsDto) {
		return News.builder()
			.headline(translatedNewsDto.getHeadline())
			.summary(translatedNewsDto.getSummary())
			.source(translatedNewsDto.getSource())
			.url(translatedNewsDto.getUrl())
			.datetime(translatedNewsDto.getDatetime())
			.build();
	}
}
