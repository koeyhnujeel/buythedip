package com.zunza.buythedip.auth.oauth2.dto

import com.zunza.buythedip.user.constant.OAuth2Provider

class GoogleOAuth2Response(
    private val attribute: Map<String, Any>
) : OAuth2Response {

    override fun getProvider(): OAuth2Provider {
        return OAuth2Provider.GOOGLE
    }

    override fun getProviderId(): String {
        return attribute["sub"].toString()
    }

    override fun getEmail(): String {
        return attribute["email"].toString()
    }
}
