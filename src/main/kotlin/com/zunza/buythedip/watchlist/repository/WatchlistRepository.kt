package com.zunza.buythedip.watchlist.repository

import com.zunza.buythedip.watchlist.entity.Watchlist
import com.zunza.buythedip.watchlist.repository.querydsl.WatchlistQuerydslRepository
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface WatchlistRepository : JpaRepository<Watchlist, Long>, WatchlistQuerydslRepository {

}
