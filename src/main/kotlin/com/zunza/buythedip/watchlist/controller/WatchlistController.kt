package com.zunza.buythedip.watchlist.controller

import com.zunza.buythedip.common.ApiResponse
import com.zunza.buythedip.watchlist.dto.CreateWatchlistRequest
import com.zunza.buythedip.watchlist.dto.WatchlistDetailsResponse
import com.zunza.buythedip.watchlist.dto.WatchlistResponse
import com.zunza.buythedip.watchlist.service.WatchlistService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
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

    @GetMapping("/api/watchlists/{watchlistId}")
    fun getWatchlistDetails(
        @PathVariable watchlistId: Long
    ): ResponseEntity<List<WatchlistDetailsResponse>> {
        return ResponseEntity.ok(watchlistService.getWatchlistDetails(watchlistId))
    }

    @PostMapping("/api/watchlists")
    fun createWatchlist(
        @AuthenticationPrincipal userId: Long,
        @RequestBody createWatchlistRequest: CreateWatchlistRequest
    ): ResponseEntity<ApiResponse<Unit>> {
        watchlistService.createWatchlist(userId, createWatchlistRequest)
        return ResponseEntity.status(HttpStatus.CREATED.value()).build()
    }
}
