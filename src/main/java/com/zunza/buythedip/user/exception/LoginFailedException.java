package com.zunza.buythedip.user.exception;

import org.springframework.http.HttpStatus;

import com.zunza.buythedip.common.CustomException;

public class LoginFailedException extends CustomException {
	private static final String MESSAGE = "이메일 또는 비밀번호를 확인해 주세요";

	public LoginFailedException() {
		super(MESSAGE);
	}

	@Override
	public int getStatusCode() {
		return HttpStatus.UNAUTHORIZED.value();
	}
}
