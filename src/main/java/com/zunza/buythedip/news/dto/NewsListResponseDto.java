package com.zunza.buythedip.news.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NewsListResponseDto {
	private Long id;
	private String headline;
	private Long datetime;
}
