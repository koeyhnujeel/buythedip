package com.zunza.buythedip.watchlist.repository

import com.zunza.buythedip.watchlist.dto.WatchlistDetailsResponse
import com.zunza.buythedip.watchlist.entity.Watchlist
import com.zunza.buythedip.watchlist.repository.querydsl.WatchlistQuerydslRepository
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface WatchlistRepository : JpaRepository<Watchlist, Long>, WatchlistQuerydslRepository {

    @Query(
        """
            SELECT new com.zunza.buythedip.watchlist.dto.WatchlistDetailsResponse(
            c.id,
            c.name,
            c.symbol,
            c.logo,
            wi.sortOrder
            )
            FROM Watchlist w
            JOIN w.watchlistItems wi
            JOIN wi.crypto c
            WHERE w.id = :watchlistId
            ORDER BY wi.sortOrder ASC
        """
    )
    fun findWatchlistDetailsById(@Param("watchlistId") watchlistId: Long): List<WatchlistDetailsResponse>
}
