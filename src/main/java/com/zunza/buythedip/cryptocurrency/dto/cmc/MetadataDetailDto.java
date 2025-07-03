package com.zunza.buythedip.cryptocurrency.dto.cmc;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class MetadataDetailDto {
	private String name;
	private String symbol;
	private String description;
	private UrlsDto urls;
	private String logo;

	@JsonProperty("tag-names")
	private List<String> tagNames;

}
