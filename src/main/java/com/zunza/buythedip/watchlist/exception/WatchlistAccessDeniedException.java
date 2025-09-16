package com.zunza.buythedip.watchlist.exception;

import org.springframework.http.HttpStatus;

import com.zunza.buythedip.common.CustomException;

public class WatchlistAccessDeniedException extends CustomException {
	private static final String MESSAGE = "권한이 없습니다.";

	public WatchlistAccessDeniedException() {
		super(MESSAGE);
	}

	@Override
	public int getStatusCode() {
		return HttpStatus.FORBIDDEN.value();
	}
}
