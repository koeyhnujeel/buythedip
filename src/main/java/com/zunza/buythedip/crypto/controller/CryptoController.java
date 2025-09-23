package com.zunza.buythedip.crypto.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zunza.buythedip.crypto.dto.ChartResponse;
import com.zunza.buythedip.crypto.dto.CryptoDetailsResponse;
import com.zunza.buythedip.crypto.dto.CryptoSuggestResponse;
import com.zunza.buythedip.crypto.service.CryptoService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/cryptos")
@RequiredArgsConstructor
public class CryptoController {
	private final CryptoService cryptoService;

	@GetMapping("/suggest")
	public ResponseEntity<List<CryptoSuggestResponse>> suggestCrypto(
		@RequestParam String keyword
	) {
		return ResponseEntity.ok(cryptoService.suggestCrypto(keyword));
	}

	@GetMapping("/{cryptoId}")
	public ResponseEntity<CryptoDetailsResponse> getCryptoDetails(
		@PathVariable Long cryptoId
	) {
		return ResponseEntity.ok(cryptoService.getCryptoDetails(cryptoId));
	}

	@GetMapping("/chart")
	public ResponseEntity<List<ChartResponse>> getCryptoChart(
		@RequestParam String symbol,
		@RequestParam(defaultValue = "15m") String interval
	) {
		return ResponseEntity.ok(cryptoService.getCryptoChart(symbol, interval));
	}
}
