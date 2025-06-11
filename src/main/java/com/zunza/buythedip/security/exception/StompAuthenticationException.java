package com.zunza.buythedip.security.exception;

import com.zunza.buythedip.common.CustomException;

public class StompAuthenticationException extends CustomException {

	private static final String MESSAGE = "인증이 필요한 작업입니다.";

	public StompAuthenticationException() {
		super(MESSAGE);
	}

	@Override
	public int getStatusCode() {
		return 401;
	}
}
