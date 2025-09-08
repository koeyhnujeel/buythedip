package com.zunza.buythedip.crypto.repository

import com.zunza.buythedip.crypto.dto.CryptoSuggestResponse
import com.zunza.buythedip.crypto.entity.Crypto
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface CryptoRepository : JpaRepository<Crypto, Long> {

    @Query(
        """
        SELECT c
        FROM Crypto c
        WHERE c.symbol IN :symbols
        """
    )
    fun findBySymbols(@Param("symbols") symbols: List<String>): List<Crypto>

    @Query(
        """
            SELECT new com.zunza.buythedip.crypto.dto.CryptoSuggestResponse(
            c.id,
            c.name,
            c.symbol,
            c.logo
            )
            FROM Crypto c
            WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR LOWER(c.symbol) LIKE LOWER(CONCAT('%', :keyword, '%'))
        """
    )
    fun findByKeyword(@Param("keyword") keyword: String): List<CryptoSuggestResponse>
}
