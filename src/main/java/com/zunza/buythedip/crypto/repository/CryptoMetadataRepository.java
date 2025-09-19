package com.zunza.buythedip.crypto.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zunza.buythedip.crypto.entity.CryptoMetadata;

@Repository
public interface CryptoMetadataRepository extends JpaRepository<CryptoMetadata, Long> {
	Optional<CryptoMetadata> findByCryptoId(Long cryptoId);
}
