package com.zunza.buythedip.cryptocurrency.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class RankingDto {
	private Long id;
	private String name;
	private String symbol;
	private String logo;
	private double volume;

	@Builder
	private RankingDto(Long id, String name, String symbol, String logo, double volume) {
		this.id = id;
		this.name = name;
		this.symbol = symbol;
		this.logo = logo;
		this.volume = volume;
	}

	public static RankingDto of(Long id, String name, String symbol, String logo, double volume) {
		return RankingDto.builder()
			.id(id)
			.name(name)
			.symbol(symbol)
			.logo(logo)
			.volume(volume)
			.build();
	}
}
