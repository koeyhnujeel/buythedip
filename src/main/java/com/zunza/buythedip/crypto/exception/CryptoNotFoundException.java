package com.zunza.buythedip.crypto.exception;

import org.springframework.http.HttpStatus;

import com.zunza.buythedip.common.CustomException;

public class CryptoNotFoundException extends CustomException {
	private static final String MESSAGE = "암호화폐가 존재하지 않습니다.";

	public CryptoNotFoundException() {
		super(MESSAGE);
	}

	@Override
	public int getStatusCode() {
		return HttpStatus.NOT_FOUND.value();
	}
}
