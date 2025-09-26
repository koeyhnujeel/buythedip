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
		SELECT c
		FROM Crypto c
		JOIN FETCH c.metadata
		"""
	)
	List<Crypto> findAllWithMetadata();

	// @Query(
	// 	"""
	// 	SELECT new com.zunza.buythedip.crypto.dto.CryptoSuggestResponse(
	// 		c.id,
	// 		c.name,
	// 		c.symbol,
	// 		m.logo
	// 	)
	// 	FROM Crypto c
	// 	JOIN c.metadata m
	// 	WHERE c.name LIKE CONCAT('%', :keyword, '%')
	// 		OR c.symbol LIKE CONCAT('%', :keyword, '%')
	// 	ORDER BY m.marketCapRank asc
	// 	"""
	// )
	@Query(
		value = """
        SELECT
            c.id,
            c.name,
            c.symbol,
            m.logo
        FROM crypto as c
        JOIN crypto_metadata as m on c.id = m.crypto_id
        WHERE MATCH(c.name, c.symbol) AGAINST(?1 IN BOOLEAN MODE)
        ORDER BY m.market_cap_rank asc
        """,
		nativeQuery = true
	)
	List<CryptoSuggestResponse> findByKeyword(String keyword);
}
