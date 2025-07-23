package com.zunza.buythedip.community.exception;

import com.zunza.buythedip.common.CustomException;

import jakarta.servlet.http.HttpServletResponse;

public class PostNotFoundException extends CustomException {

	private static final String MESSAGE = "존재하지 않는 게시글 입니다. POST ID: ";

	public PostNotFoundException(Long postId) {
		super(MESSAGE + postId);
	}

	@Override
	public int getStatusCode() {
		return HttpServletResponse.SC_NOT_FOUND;
	}
}
