package com.zunza.buythedip.security.exception;

import com.zunza.buythedip.common.CustomException;

public class StompAuthorizationHeaderException extends CustomException {

	private static final String MESSAGE = "Stomp 인증 헤더가 필요합니다.";

	public StompAuthorizationHeaderException() {
		super(MESSAGE);
	}

	@Override
	public int getStatusCode() {
		return 401;
	}
}
