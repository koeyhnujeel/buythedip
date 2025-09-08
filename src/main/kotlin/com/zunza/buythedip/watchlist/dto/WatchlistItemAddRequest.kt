package com.zunza.buythedip.watchlist.dto

data class WatchlistItemAddRequest(
    val cryptoId: Long,
    val sortOrder: Int
)
