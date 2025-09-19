package com.zunza.buythedip.crypto.exception;

import org.springframework.http.HttpStatus;

import com.zunza.buythedip.common.CustomException;

public class CryptoMetadataNotFoundException extends CustomException {
	private static final String MESSAGE = "암호화폐에 대한 상세정보가 존재하지 않습니다.";

	public CryptoMetadataNotFoundException() {
		super(MESSAGE);
	}

	@Override
	public int getStatusCode() {
		return HttpStatus.NOT_FOUND.value();
	}
}
