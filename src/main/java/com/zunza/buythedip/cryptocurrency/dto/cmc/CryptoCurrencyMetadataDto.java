package com.zunza.buythedip.cryptocurrency.dto.cmc;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CryptoCurrencyMetadataDto {
	private Map<String, List<MetadataDetailDto>> data;
}
