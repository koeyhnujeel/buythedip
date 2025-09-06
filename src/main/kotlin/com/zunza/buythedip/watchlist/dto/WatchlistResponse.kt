package com.zunza.buythedip.watchlist.dto

data class WatchlistResponse(
    val id: Long = 0,
    val name: String = "",
    val isDefault: Boolean = true,
    val isSystem: Boolean = true,
    val sortOrder: Int = 0
)
