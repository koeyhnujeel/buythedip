package com.zunza.buythedip.cryptocurrency.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CryptoDataWithLogoDto {
	private Long id;
	private String name;
	private String symbol;
	private String logo;
}
