package com.zunza.buythedip.cryptocurrency.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zunza.buythedip.cryptocurrency.entity.CryptocurrencyMetadata;

@Repository
public interface CryptocurrencyMetadataRepository extends JpaRepository<CryptocurrencyMetadata, Long> {
}
