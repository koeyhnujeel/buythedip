package com.zunza.buythedip.watchlist.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zunza.buythedip.common.ApiResponse;
import com.zunza.buythedip.watchlist.dto.WatchlistCreateRequest;
import com.zunza.buythedip.watchlist.dto.WatchlistDetailsResponse;
import com.zunza.buythedip.watchlist.dto.WatchlistItemAddRequest;
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

	@GetMapping("/{watchlistId}")
	public ResponseEntity<List<WatchlistDetailsResponse>> getWatchlistDetails(
		@PathVariable Long watchlistId
	) {
		return ResponseEntity.ok(watchlistService.getWatchlistDetails(watchlistId));
	}

	@PostMapping
	public ResponseEntity<ApiResponse<Void>> createWatchlist(
		@AuthenticationPrincipal Long userId,
		@RequestBody WatchlistCreateRequest watchlistCreateRequest
	) {
		watchlistService.createWatchlist(userId, watchlistCreateRequest);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@DeleteMapping("/{watchlistId}")
	public ResponseEntity<ApiResponse<Void>> deleteWatchlist(
		@AuthenticationPrincipal Long userId,
		@PathVariable Long watchlistId
	) {
		watchlistService.deleteWatchlist(userId, watchlistId);
		return ResponseEntity.noContent().build();
	}

	@PostMapping("/{watchlistId}/items")
	public ResponseEntity<ApiResponse<Void>> addItem(
		@PathVariable Long watchlistId,
		@RequestBody WatchlistItemAddRequest watchlistItemAddRequest
	) {
		watchlistService.addWatchlistItem(watchlistId, watchlistItemAddRequest);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
}
