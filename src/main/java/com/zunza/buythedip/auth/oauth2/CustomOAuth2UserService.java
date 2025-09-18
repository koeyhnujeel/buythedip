package com.zunza.buythedip.auth.oauth2;

import java.util.UUID;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zunza.buythedip.auth.oauth2.exception.DuplicateEmailWithDifferentProviderException;
import com.zunza.buythedip.auth.oauth2.exception.SocialEmailAlreadyRegisteredException;
import com.zunza.buythedip.auth.oauth2.dto.GoogleOAuth2Response;
import com.zunza.buythedip.auth.oauth2.dto.KakaoOAuth2Response;
import com.zunza.buythedip.auth.oauth2.dto.NaverOAuth2Response;
import com.zunza.buythedip.auth.oauth2.dto.OAuth2Response;
import com.zunza.buythedip.infrastructure.event.event.UserRegisteredEvent;
import com.zunza.buythedip.user.constant.UserType;
import com.zunza.buythedip.user.entity.User;
import com.zunza.buythedip.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
	private final UserRepository userRepository;
	private final ApplicationEventPublisher eventPublisher;

	@Override
	@Transactional
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2User oAuth2User = super.loadUser(userRequest);
		String provider = userRequest.getClientRegistration().getRegistrationId();
		OAuth2Response oAuth2Response = getOAuth2Response(oAuth2User, provider);

		return userRepository.findByEmail(oAuth2Response.getEmail())
			.map(user -> {
				validateExistingUserForOAuth2Login(user, oAuth2Response);
				return new CustomOAuth2User(user);
			})
			.orElseGet(() -> {
				User user = User.createSocialUser(oAuth2Response, createRandomNickname());
				User savedUser = userRepository.save(user);

				eventPublisher.publishEvent(UserRegisteredEvent.createFrom(savedUser));
				return new CustomOAuth2User(user);
			});
	}

	private OAuth2Response getOAuth2Response(
		OAuth2User oAuth2User,
		String provider
	) {
		return switch (provider) {
			case "google" -> new GoogleOAuth2Response(oAuth2User.getAttributes());
			case "naver" -> new NaverOAuth2Response(oAuth2User.getAttributes());
			case "kakao" -> new KakaoOAuth2Response(oAuth2User.getAttributes());
			default -> throw new IllegalArgumentException("지원하지 않는 Provider 입니다.");
		};
	}

	private void validateExistingUserForOAuth2Login(
		User user,
		OAuth2Response oAuth2Response
	) {
		if (user.getUserType() == UserType.NORMAL) {
			throw new SocialEmailAlreadyRegisteredException();
		}

		if (user.getOAuth2Provider() != oAuth2Response.getProvider()) {
			throw new DuplicateEmailWithDifferentProviderException();
		}
	}

	private String createRandomNickname() {
		return "user" + UUID.randomUUID().toString().substring(0, 8);
	}
}
