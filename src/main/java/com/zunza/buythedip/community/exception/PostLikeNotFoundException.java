package com.zunza.buythedip.community.exception;

import com.zunza.buythedip.common.CustomException;

import jakarta.servlet.http.HttpServletResponse;

public class PostLikeNotFoundException extends CustomException {

	private static final String MESSAGE = "해당 게시글에 좋아요를 누르지 않았습니다. POST ID: ";

	public PostLikeNotFoundException(Long postId) {
		super(MESSAGE + postId);
	}

	@Override
	public int getStatusCode() {
		return HttpServletResponse.SC_NOT_FOUND;
	}
}
