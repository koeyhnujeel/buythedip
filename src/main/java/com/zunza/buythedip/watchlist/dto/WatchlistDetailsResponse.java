package com.zunza.buythedip.watchlist.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WatchlistDetailsResponse {
	private Long watchlistItemId;
	private Long cryptoId;
	private String name;
	private String symbol;
	private String logo;
	private int sortOrder;
}
