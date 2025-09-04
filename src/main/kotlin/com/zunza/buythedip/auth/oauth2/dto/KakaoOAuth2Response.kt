package com.zunza.buythedip.auth.oauth2.dto

import com.zunza.buythedip.user.constant.OAuth2Provider

class KakaoOAuth2Response(
    private val attribute: Map<String, Any>
) : OAuth2Response {

    override fun getProvider(): OAuth2Provider {
        return OAuth2Provider.KAKAO
    }

    override fun getProviderId(): String {
        return attribute["id"].toString()
    }

    override fun getEmail(): String {
        val kakaoAccount: Map<String, Any> = attribute["kakao_account"] as Map<String, Any>
        return kakaoAccount["email"].toString()
    }
}
