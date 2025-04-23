package com.zunza.buythedip.user.exception;

import com.zunza.buythedip.common.CustomException;

import jakarta.servlet.http.HttpServletResponse;

public class DuplicateAccountIdException extends CustomException {

	private static final String MESSAGE = "이미 사용 중인 아이디 입니다.";

	public DuplicateAccountIdException() {
		super(MESSAGE);
	}

	@Override
	public int getStatusCode() {
		return HttpServletResponse.SC_CONFLICT;
	}
}
