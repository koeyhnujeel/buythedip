package com.zunza.buythedip.watchlist.dto;

import lombok.Getter;

@Getter
public class WatchlistCreateRequest {
	private String name;
	private int sortOrder;
}
