package com.zunza.buythedip.auth.oauth2.exception

import org.springframework.security.core.AuthenticationException

class DuplicateEmailWithDifferentProviderException() : AuthenticationException(MESSAGE) {
    companion object {
        private const val MESSAGE = "이미 가입된 이메일입니다."
    }
}
