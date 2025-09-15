package com.zunza.buythedip.infrastructure.event.listener;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import com.zunza.buythedip.infrastructure.event.event.UserRegisteredEvent;
import com.zunza.buythedip.watchlist.service.WatchlistService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserEventListener {
	private final WatchlistService watchlistService;

	// @Async("taskExecutor")
	@TransactionalEventListener
	public void handleUserRegistered(UserRegisteredEvent event) {
		watchlistService.createDefaultWatchlist(event.getUser());
	}
}
