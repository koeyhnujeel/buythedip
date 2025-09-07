package com.zunza.buythedip.watchlist.dto

data class WatchlistDetailsResponse(
    val id: Long = 0,
    val name: String = "",
    val symbol: String = "",
    val logo: String = "",
    val sortOrder: Int = 0
)
