package com.zunza.buythedip.crypto.repository

import com.zunza.buythedip.crypto.entity.CryptoMetadata
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CryptoMetadataRepository : JpaRepository<CryptoMetadata, Long> {
}
