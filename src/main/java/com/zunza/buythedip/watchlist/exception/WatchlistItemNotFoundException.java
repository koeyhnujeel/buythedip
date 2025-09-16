package com.zunza.buythedip.watchlist.exception;

import org.springframework.http.HttpStatus;

import com.zunza.buythedip.common.CustomException;

public class WatchlistItemNotFoundException extends CustomException {
	private static final String MESSAGE = "왓치리스트 종목이 존재하지 않습니다.";

	public WatchlistItemNotFoundException() {
		super(MESSAGE);
	}

	@Override
	public int getStatusCode() {
		return HttpStatus.NOT_FOUND.value();
	}
}
