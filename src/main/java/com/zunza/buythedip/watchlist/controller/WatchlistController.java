package com.zunza.buythedip.watchlist.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zunza.buythedip.watchlist.dto.WatchlistResponse;
import com.zunza.buythedip.watchlist.service.WatchlistService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/watchlists")
@RequiredArgsConstructor
public class WatchlistController {
	private final WatchlistService watchlistService;

	@GetMapping
	public ResponseEntity<List<WatchlistResponse>> getWatchlists(
		@AuthenticationPrincipal Long userId
	) {
		return ResponseEntity.ok(watchlistService.getWatchlist(userId));
	}
}
