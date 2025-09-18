package com.zunza.buythedip.crypto.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zunza.buythedip.crypto.dto.CryptoSuggestResponse;
import com.zunza.buythedip.crypto.service.CryptoService;

import lombok.RequiredArgsConstructor;

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
}
