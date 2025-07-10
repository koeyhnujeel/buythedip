package com.zunza.buythedip.cryptocurrency.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.zunza.buythedip.cryptocurrency.dto.CryptoInfoDto;
import com.zunza.buythedip.cryptocurrency.dto.InitKlineDto;
import com.zunza.buythedip.cryptocurrency.dto.RankingDto;
import com.zunza.buythedip.cryptocurrency.service.CryptocurrencyService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CryptocurrencyController {

	private final CryptocurrencyService cryptocurrencyService;

	@GetMapping("/api/cryptocurrencies/{cryptocurrencyId}/info")
	public ResponseEntity<CryptoInfoDto> getCryptoInfo(
		@PathVariable Long cryptocurrencyId
	) {
		return ResponseEntity.ok(cryptocurrencyService.getInfo(cryptocurrencyId));
	}

	@GetMapping("/api/cryptocurrencies/volume/top")
	public ResponseEntity<List<RankingDto>> getTopVolumeRanking() {
		return ResponseEntity.ok(cryptocurrencyService.getTopVolume());
	}

	@MessageMapping("/crypto/kline")
	public void getHistoricalCandles(
		InitKlineDto initKlineDto
	) {
		cryptocurrencyService.getHistoricalCandles(initKlineDto.getSymbol(), initKlineDto.getInterval());
	}
}
