package com.zunza.buythedip.watchlist.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WatchlistResponse {
	private Long id;
	private String name;
	private boolean isDefault;
	private boolean isSystem;
	private int sortOrder;
}
