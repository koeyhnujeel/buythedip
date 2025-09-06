package com.zunza.buythedip.watchlist.controller

import com.zunza.buythedip.watchlist.service.WatchlistService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class WatchlistController(
    private val watchlistService: WatchlistService
) {
//    @GetMapping("/api/watchlists/default")
//    fun get()
}
