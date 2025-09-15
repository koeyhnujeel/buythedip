package com.zunza.buythedip.auth.oauth2.exception;

import org.springframework.security.core.AuthenticationException;

public class DuplicateEmailWithDifferentProviderException extends AuthenticationException {
	private static final String MESSAGE = "이미 가입된 이메일입니다.";

	public DuplicateEmailWithDifferentProviderException() {
		super(MESSAGE);
	}
}
