package com.zunza.buythedip.cryptocurrency.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class InitKlineDto {
	private String symbol;
	private String interval;
}
