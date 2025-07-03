package com.zunza.buythedip.cryptocurrency.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CryptoInfoDto {
	private String name;
	private String symbol;
	private String logo;
	private String description;
	private List<String> website;
	private List<String> twitter;
	private List<String> explorer;
	private List<String> tagNames;
}
