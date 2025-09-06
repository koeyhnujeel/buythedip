package com.zunza.buythedip.crypto.repository

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
}
