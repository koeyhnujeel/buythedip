package com.zunza.buythedip.watchlist.repository

import com.zunza.buythedip.watchlist.entity.WatchlistItem
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface WatchlistItemRepository : JpaRepository<WatchlistItem, Long> {
}
