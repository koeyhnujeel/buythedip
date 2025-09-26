package com.zunza.buythedip.watchlist.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.zunza.buythedip.watchlist.dto.WatchlistDetailsResponse;
import com.zunza.buythedip.watchlist.dto.WatchlistResponse;
import com.zunza.buythedip.watchlist.entity.Watchlist;

@Repository
public interface WatchlistRepository extends JpaRepository<Watchlist, Long> {
	@Query(
		"""
		SELECT new com.zunza.buythedip.watchlist.dto.WatchlistResponse(
		w.id,
		w.name,
		w.isDefault,
		w.isSystem,
		w.sortOrder
		)
		From Watchlist w
		WHERE (:userId IS NULL AND w.isSystem = true)
		OR (:userId IS NOT NULL AND w.user.id = :userId)
		ORDER By w.sortOrder ASC
		"""
	)
	List<WatchlistResponse> findWatchlistsByUserId(@Param("userId") Long userId);

	@Query(
		"""
		SELECT new com.zunza.buythedip.watchlist.dto.WatchlistDetailsResponse(
		wi.id,
		c.id,
		c.name,
		c.symbol,
		m.logo,
		wi.sortOrder
		)
		FROM Watchlist w
		JOIN w.watchlistItems wi
		JOIN wi.crypto c
		JOIN c.metadata m
		WHERE w.id = :watchlistId
		ORDER BY wi.sortOrder ASC
		"""
	)
	List<WatchlistDetailsResponse> findWatchlistDetailsById(@Param("watchlistId") Long watchlistId);
}
