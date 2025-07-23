package com.zunza.buythedip.community.exception;

import com.zunza.buythedip.common.CustomException;

import jakarta.servlet.http.HttpServletResponse;

public class CommentLikeNotFoundException extends CustomException {

	private static final String MESSAGE = "해당 댓글에 좋아요를 누르지 않았습니다. COMMENT ID: ";

	public CommentLikeNotFoundException(Long commentId) {
		super(MESSAGE + commentId);
	}

	@Override
	public int getStatusCode() {
		return HttpServletResponse.SC_NOT_FOUND;
	}
}
