package com.zunza.buythedip.security.exception;

import com.zunza.buythedip.common.CustomException;

public class StompInvalidTokenException extends CustomException {

	private static final String MESSAGE = "인증 헤더의 값은 'Bearer [토큰]' 형식이어야 합니다.";

	public StompInvalidTokenException() {
		super(MESSAGE);
	}

	@Override
	public int getStatusCode() {
		return 401;
	}
}
