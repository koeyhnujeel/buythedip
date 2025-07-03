package com.zunza.buythedip.cryptocurrency.dto.binance;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExchangeInfoDto {
	private List<SymbolDto> symbols;
}
