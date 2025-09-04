package com.zunza.buythedip.auth.oauth2.exception

import org.springframework.security.core.AuthenticationException

class SocialEmailAlreadyRegisteredException() : AuthenticationException(MESSAGE) {
    companion object {
        private const val MESSAGE = "이메일이 기존 계정과 연결되어 있습니다."
    }
}
