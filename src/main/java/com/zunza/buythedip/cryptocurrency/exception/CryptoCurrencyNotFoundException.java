package com.zunza.buythedip.cryptocurrency.exception;

import com.zunza.buythedip.common.CustomException;

import jakarta.servlet.http.HttpServletResponse;

public class CryptoCurrencyNotFoundException extends CustomException {

	private static final String SYMBOL_MESSAGE = "해당 암호화폐를 찾을 수 없습니다. symbol: ";
	private static final String ID_MESSAGE = "해당 암호화폐를 찾을 수 없습니다. ID: ";

	public CryptoCurrencyNotFoundException(String symbol) {
		super(SYMBOL_MESSAGE + symbol);
	}

	public CryptoCurrencyNotFoundException(Long id) {
		super(ID_MESSAGE + id);
	}

	@Override
	public int getStatusCode() {
		return HttpServletResponse.SC_NOT_FOUND;
	}
}
