package com.zunza.buythedip.crypto.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.zunza.buythedip.crypto.dto.CryptoSuggestResponse;
import com.zunza.buythedip.crypto.entity.Crypto;

@Repository
public interface CryptoRepository extends JpaRepository<Crypto, Long> {
	@Query(
		"""
		SELECT c
		FROM Crypto c
		WHERE c.symbol IN :symbols
		"""
	)
	List<Crypto> findBySymbols(@Param("symbols") List<String> symbols);

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
	List<CryptoSuggestResponse> findByKeyword(@Param("keyword") String keyword);
}
