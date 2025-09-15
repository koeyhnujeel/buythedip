package com.zunza.buythedip.auth.oauth2.dto;

import com.zunza.buythedip.user.constant.OAuth2Provider;

public interface OAuth2Response {
	OAuth2Provider getProvider();
	String getProviderId();
	String getEmail();
}
