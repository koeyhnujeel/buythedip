package com.zunza.buythedip.user.exception;

import org.springframework.http.HttpStatus;

import com.zunza.buythedip.common.CustomException;

public class DuplicateNicknameException extends CustomException {
	private static final String MESSAGE = "이미 사용 중인 닉네임 입니다.";

	public DuplicateNicknameException() {
		super(MESSAGE);
	}

	@Override
	public int getStatusCode() {
		return HttpStatus.CONFLICT.value();
	}
}
