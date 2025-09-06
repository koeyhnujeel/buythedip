package com.zunza.buythedip.watchlist.repository.querydsl

import com.zunza.buythedip.watchlist.dto.WatchlistResponse

interface WatchlistQuerydslRepository {
    fun findWatchlist(userId: Long?): List<WatchlistResponse>
}
