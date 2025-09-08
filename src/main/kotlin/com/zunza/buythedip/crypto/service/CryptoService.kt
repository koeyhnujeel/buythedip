package com.zunza.buythedip.crypto.service

import com.zunza.buythedip.crypto.dto.CryptoSuggestResponse
import com.zunza.buythedip.crypto.repository.CryptoRepository
import org.springframework.stereotype.Service

@Service
class CryptoService(
    private val cryptoRepository: CryptoRepository
) {
    fun suggestCrypto(keyword: String): List<CryptoSuggestResponse> {
        return cryptoRepository.findByKeyword(keyword)
    }
}
