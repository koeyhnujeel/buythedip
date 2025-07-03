package com.zunza.buythedip.cryptocurrency.dto.cmc;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class UrlsDto {
	private List<String> website;
	private List<String> twitter;
	private List<String> explorer;
}
