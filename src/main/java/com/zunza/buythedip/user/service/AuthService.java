package com.zunza.buythedip.user.service;

import org.springframework.stereotype.Service;

import com.zunza.buythedip.user.dto.SignupRequestDto;
import com.zunza.buythedip.user.entity.DuplicateCheckType;
import com.zunza.buythedip.user.entity.User;
import com.zunza.buythedip.user.exception.DuplicateAccountIdException;
import com.zunza.buythedip.user.exception.DuplicateNicknameException;
import com.zunza.buythedip.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final UserRepository userRepository;

	public void signup(SignupRequestDto signupRequestDto) {
		if (userRepository.existsByAccountId(signupRequestDto.getAccountId())) {
			throw new DuplicateAccountIdException();
		}

		if (userRepository.existsByNickname(signupRequestDto.getNickname())) {
			throw new DuplicateNicknameException();
		}

		userRepository.save(User.from(signupRequestDto));
	}

	public void validateDuplicateField(DuplicateCheckType type, String value) {
		switch (type) {
			case ACCOUNT_ID -> {
				if (userRepository.existsByAccountId(value)) {
					throw new DuplicateAccountIdException();
				}
			}

			case NICKNAME -> {
				if (userRepository.existsByNickname(value)) {
					throw new DuplicateNicknameException();
				}
			}

			default -> throw new IllegalArgumentException("지원하지 않는 필드 타입입니다.");
		}
	}
}
