package com.zunza.buythedip.cryptocurrency.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.zunza.buythedip.constant.CacheNames;
import com.zunza.buythedip.cryptocurrency.dto.CryptoInfoDto;
import com.zunza.buythedip.cryptocurrency.dto.CryptoDataWithLogoDto;
import com.zunza.buythedip.cryptocurrency.entity.Cryptocurrency;

import io.lettuce.core.dynamic.annotation.Param;

@Repository
public interface CryptocurrencyRepository extends JpaRepository<Cryptocurrency, Long> {


	@Query("""
		SELECT new com.zunza.buythedip.cryptocurrency.dto.CryptoDataWithLogoDto(
		c.id,
		c.name,
		c.symbol,
		m.logo
		)
		FROM Cryptocurrency c
		JOIN c.metadata m
		""")
	@Cacheable(value = CacheNames.CRYPTO_DATA_WITH_LOGO)
	List<CryptoDataWithLogoDto> findAllWithLogo();

	@Query("""
		SELECT new com.zunza.buythedip.cryptocurrency.dto.CryptoInfoDto(
		c.name,
		c.symbol,
		m.logo,
		m.description,
		m.website,
		m.twitter,
		m.explorer,
		m.tagNames
		)
		FROM Cryptocurrency c
		JOIN c.metadata m
		WHERE c.id = :cryptocurrencyId
		""")
	@Cacheable(value = CacheNames.CRYPTO_INFO, key = "#cryptocurrencyId")
	Optional<CryptoInfoDto> findByIdWithMetadata(@Param("cryptocurrencyId") Long cryptocurrencyId);
}
