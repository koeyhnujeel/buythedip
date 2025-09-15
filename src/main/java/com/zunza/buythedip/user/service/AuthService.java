package com.zunza.buythedip.user.service;

import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zunza.buythedip.auth.jwt.JwtProvider;
import com.zunza.buythedip.auth.user.CustomUserDetails;
import com.zunza.buythedip.infrastructure.event.event.UserRegisteredEvent;
import com.zunza.buythedip.infrastructure.redis.service.RedisCacheService;
import com.zunza.buythedip.user.dto.EmailAvailableResponse;
import com.zunza.buythedip.user.dto.LoginRequest;
import com.zunza.buythedip.user.dto.NicknameAvailableResponse;
import com.zunza.buythedip.user.dto.SignupRequest;
import com.zunza.buythedip.user.entity.User;
import com.zunza.buythedip.user.exception.LoginFailedException;
import com.zunza.buythedip.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
	private static final Pattern EMAIL_REGEX = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");
	private static final Pattern NICKNAME_REGEX = Pattern.compile("^(?=.*[가-힣a-zA-Z])[가-힣a-zA-Z0-9]{2,12}$");

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final ApplicationEventPublisher eventPublisher;

	private final JwtProvider jwtProvider;
	private final RedisCacheService redisCacheService;
	private final AuthenticationManager authenticationManager;

	public EmailAvailableResponse isEmailAvailable(String email) {
		validateEmailFormat(email);
		if (userRepository.existsByEmail(email)) {
			return EmailAvailableResponse.createFrom(false);
		} else {
			return EmailAvailableResponse.createFrom(true);
		}
	}

	public NicknameAvailableResponse isNicknameAvailable(String nickname) {
		validateNicknameFormat(nickname);
		if (userRepository.existsByNickname(nickname)) {
			return NicknameAvailableResponse.createFrom(false);
		} else {
			return NicknameAvailableResponse.createFrom(true);
		}
	}

	@Transactional
	public void signup(SignupRequest signupRequest) {
		String encodedPassword = passwordEncoder.encode(signupRequest.getPassword());
		User savedUser = userRepository.save(User.createNormalUser(
			signupRequest.getEmail(),
			encodedPassword,
			signupRequest.getNickname()
		));

		eventPublisher.publishEvent(UserRegisteredEvent.createFrom(savedUser));
	}

	public Map<String, String> login(LoginRequest loginRequest) {
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
			loginRequest.getEmail(),
			loginRequest.getPassword());

		Authentication authentication = authenticateUser(authenticationToken);
		CustomUserDetails userDetails = (CustomUserDetails)authentication.getPrincipal();

		String accessToken = jwtProvider.generateAccessToken(userDetails.getUserId(), userDetails.getAuthorities());
		String refreshToken = jwtProvider.generateRefreshToken(userDetails.getUserId());
		redisCacheService.set(userDetails.getUserId().toString(), refreshToken);

		return Map.of(
			"nickname", userDetails.getNickname(),
			"accessToken", accessToken,
			"refreshToken", refreshToken
		);
	}

	private void validateEmailFormat(String email) {
		if (!EMAIL_REGEX.matcher(email).matches()) {
			throw new IllegalArgumentException("잘못된 이메일 형식입니다.");
		}
	}

	private void validateNicknameFormat(String email) {
		if (!NICKNAME_REGEX.matcher(email).matches()) {
			throw new IllegalArgumentException("잘못된 닉네임 형식입니다.");
		}
	}

	private Authentication authenticateUser(
		UsernamePasswordAuthenticationToken authenticationToken
	) {
		try {
			return authenticationManager.authenticate(authenticationToken);
		} catch (AuthenticationException e) {
			throw new LoginFailedException();
		}
	}
}
