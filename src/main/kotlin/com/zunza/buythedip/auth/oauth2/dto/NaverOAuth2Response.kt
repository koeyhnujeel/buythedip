package com.zunza.buythedip.auth.oauth2.dto

import com.zunza.buythedip.user.constant.OAuth2Provider

class NaverOAuth2Response(
    private val attribute: Map<String, Any>
) : OAuth2Response {

    private val response = attribute["response"] as Map<String, Any>

    override fun getProvider(): OAuth2Provider {
        return OAuth2Provider.NAVER
    }

    override fun getProviderId(): String {
        return response["id"].toString()
    }

    override fun getEmail(): String {
        return response["email"].toString()
    }
}
