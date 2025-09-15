package com.zunza.buythedip.auth.oauth2;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.Charset;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.zunza.buythedip.auth.jwt.JwtProvider;
import com.zunza.buythedip.infrastructure.redis.service.RedisCacheService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomOAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	private final JwtProvider jwtProvider;
	private final RedisCacheService redisCacheService;

	@Override
	public void onAuthenticationSuccess(
		HttpServletRequest request,
		HttpServletResponse response,
		Authentication authentication
	) throws IOException, ServletException {
		CustomOAuth2User oAuth2User = (CustomOAuth2User)authentication.getPrincipal();
		Long userId = oAuth2User.getUserId();
		String nickname = oAuth2User.getName();

		String accessToken = jwtProvider.generateAccessToken(userId, oAuth2User.getAuthorities());
		String refreshToken = jwtProvider.generateRefreshToken(userId);
		redisCacheService.set(userId.toString(), refreshToken);

		String encodedNickname = URLEncoder.encode(nickname, Charset.defaultCharset());
		String redirectionUrl = "http://localhost:5173/login?token=" + accessToken + "&nickname=" + encodedNickname;

		response.sendRedirect(redirectionUrl);
	}
}
