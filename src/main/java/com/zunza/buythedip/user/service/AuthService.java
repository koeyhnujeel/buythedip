package com.zunza.buythedip.user.service;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zunza.buythedip.infrastructure.event.event.UserRegisteredEvent;
import com.zunza.buythedip.user.dto.EmailAvailableResponse;
import com.zunza.buythedip.user.dto.NicknameAvailableResponse;
import com.zunza.buythedip.user.dto.SignupRequest;
import com.zunza.buythedip.user.entity.User;
import com.zunza.buythedip.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final ApplicationEventPublisher eventPublisher;

	public EmailAvailableResponse isEmailAvailable(String email) {
		if (userRepository.existsByEmail(email)) {
			return EmailAvailableResponse.createFrom(false);
		} else {
			return EmailAvailableResponse.createFrom(true);
		}
	}

	public NicknameAvailableResponse isNicknameAvailable(String nickname) {
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
}
