package com.zunza.buythedip.user.exception;

import org.springframework.http.HttpStatus;

import com.zunza.buythedip.common.CustomException;

public class DuplicateEmailException extends CustomException {
	private static final String MESSAGE = "이미 사용 중인 이메일 입니다.";

	public DuplicateEmailException() {
		super(MESSAGE);
	}

	@Override
	public int getStatusCode() {
		return HttpStatus.CONFLICT.value();
	}
}
