package com.zunza.buythedip.watchlist.controller

import com.zunza.buythedip.watchlist.dto.WatchlistResponse
import com.zunza.buythedip.watchlist.service.WatchlistService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class WatchlistController(
    private val watchlistService: WatchlistService
) {
    @GetMapping("/api/watchlists")
    fun getWatchlists(
        @AuthenticationPrincipal userId: Long?
    ): ResponseEntity<List<WatchlistResponse>> {
        return ResponseEntity.ok(watchlistService.getWatchlist(userId))
    }
}
