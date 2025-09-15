package com.zunza.buythedip.user.exception;

import org.springframework.http.HttpStatus;

import com.zunza.buythedip.common.CustomException;

public class UserNotFoundException extends CustomException {
	private final static String MESSAGE = "사용자를 찾을 수 없습니다";

	public UserNotFoundException() {
		super(MESSAGE);
	}

	@Override
	public int getStatusCode() {
		return HttpStatus.NOT_FOUND.value();
	}
}
