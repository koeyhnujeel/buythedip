package com.zunza.buythedip.crypto.controller

import com.zunza.buythedip.crypto.dto.CryptoSuggestResponse
import com.zunza.buythedip.crypto.service.CryptoService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/cryptos")
class CryptoController(
    private val cryptoService: CryptoService
) {
    /**
     * 검색 개선 하기
     */
    @GetMapping("/suggest")
    fun suggestCrypto(
        @RequestParam keyword: String
    ): ResponseEntity<List<CryptoSuggestResponse>> {
        return ResponseEntity.ok(cryptoService.suggestCrypto(keyword))
    }
}
