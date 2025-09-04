package com.zunza.buythedip.auth.oauth2.dto

import com.zunza.buythedip.user.constant.OAuth2Provider

interface OAuth2Response {
    fun getProvider(): OAuth2Provider;
    fun getProviderId(): String;
    fun getEmail(): String
}
