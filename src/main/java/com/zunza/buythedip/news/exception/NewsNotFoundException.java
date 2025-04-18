package com.zunza.buythedip.news.exception;

import com.zunza.buythedip.common.CustomException;

import jakarta.servlet.http.HttpServletResponse;

public class NewsNotFoundException extends CustomException {

	private static final String MESSAGE = "해당 뉴스를 찾을 수 없습니다. ID: ";

	public NewsNotFoundException(Long newsId) {
		super(MESSAGE + newsId);
	}

	@Override
	public int getStatusCode() {
		return HttpServletResponse.SC_NOT_FOUND;
	}
}
