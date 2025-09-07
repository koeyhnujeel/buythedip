package com.zunza.buythedip.watchlist.controller

import com.zunza.buythedip.common.ApiResponse
import com.zunza.buythedip.watchlist.dto.WatchlistCreateRequest
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
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/watchlists")
class WatchlistController(
    private val watchlistService: WatchlistService
) {
    @GetMapping
    fun getWatchlists(
        @AuthenticationPrincipal userId: Long?
    ): ResponseEntity<List<WatchlistResponse>> {
        return ResponseEntity.ok(watchlistService.getWatchlist(userId))
    }

    @GetMapping("/{watchlistId}")
    fun getWatchlistDetails(
        @PathVariable watchlistId: Long
    ): ResponseEntity<List<WatchlistDetailsResponse>> {
        return ResponseEntity.ok(watchlistService.getWatchlistDetails(watchlistId))
    }

    @PostMapping
    fun createWatchlist(
        @AuthenticationPrincipal userId: Long,
        @RequestBody watchlistCreateRequest: WatchlistCreateRequest
    ): ResponseEntity<ApiResponse<Unit>> {
        watchlistService.createWatchlist(userId, watchlistCreateRequest)
        return ResponseEntity.status(HttpStatus.CREATED.value()).build()
    }

//    @PostMapping("/api/watchlists/{watchlistId}/items")
//    fun addItem(
//        @PathVariable watchlistId: Long,
//        @RequestBody addWatchlistItemRequest: AddWatchlistItemRequest
//    ) {
//
//    }
}
