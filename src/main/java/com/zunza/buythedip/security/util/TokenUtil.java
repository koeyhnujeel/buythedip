package com.zunza.buythedip.security.util;

import org.springframework.util.StringUtils;

public class TokenUtil {

	public static final String TOKEN_PREFIX = "Bearer ";

	public static String resolveToken(String authorizationHeader) {
		if (StringUtils.hasText(authorizationHeader) && authorizationHeader.startsWith(TOKEN_PREFIX)) {
			return authorizationHeader.substring(7);
		}

		return null;
	}
}
