package com.zunza.buythedip.watchlist.dto;

import lombok.Getter;

@Getter
public class WatchlistItemAddRequest {
	private Long cryptoId;
	private int sortOrder;
}
