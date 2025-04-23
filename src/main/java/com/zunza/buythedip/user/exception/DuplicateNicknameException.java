package com.zunza.buythedip.user.exception;

import com.zunza.buythedip.common.CustomException;

import jakarta.servlet.http.HttpServletResponse;

public class DuplicateNicknameException extends CustomException {

	private static final String MESSAGE = "이미 사용 중인 닉네임 입니다.";

	public DuplicateNicknameException() {
		super(MESSAGE);
	}

	@Override
	public int getStatusCode() {
		return HttpServletResponse.SC_CONFLICT;
	}
}
