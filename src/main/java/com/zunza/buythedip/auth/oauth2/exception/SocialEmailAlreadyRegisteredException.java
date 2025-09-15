package com.zunza.buythedip.auth.oauth2.exception;

import org.springframework.security.core.AuthenticationException;

import com.zunza.buythedip.common.CustomException;

public class SocialEmailAlreadyRegisteredException extends AuthenticationException {
	private static final String MESSAGE = "이메일이 기존 계정과 연결되어 있습니다.";

	public SocialEmailAlreadyRegisteredException() {
		super(MESSAGE);
	}
}
