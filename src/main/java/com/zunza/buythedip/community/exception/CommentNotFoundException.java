package com.zunza.buythedip.community.exception;

import com.zunza.buythedip.common.CustomException;

import jakarta.servlet.http.HttpServletResponse;

public class CommentNotFoundException extends CustomException {

	private static final String MESSAGE = "존재하지 않는 댓글 입니다. COMMENT ID: ";

	public CommentNotFoundException(Long commentId) {
		super(MESSAGE + commentId);
	}

	@Override
	public int getStatusCode() {
		return HttpServletResponse.SC_NOT_FOUND;
	}
}
